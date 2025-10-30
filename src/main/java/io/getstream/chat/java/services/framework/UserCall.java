package io.getstream.chat.java.services.framework;

import okhttp3.Request;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Wrapper for Retrofit {@code Call} objects that injects user authentication tokens.
 * <p>
 * This class creates new OkHttp calls using the tagged request to ensure the {@link UserToken}
 * is properly attached and available to interceptors for adding authorization headers.
 * </p>
 *
 * @param <T> the response body type
 * @see UserToken
 */
class UserCall<T> implements retrofit2.Call<T> {
  private final retrofit2.Call<T> delegate;
  private final UserToken token;
  private final Retrofit retrofit;
  private final Type responseType;
  private volatile boolean executed;
  private volatile okhttp3.Call rawCall;

  /**
   * Constructs a new UserCall that wraps the provided call with token injection.
   *
   * @param delegate the underlying Retrofit call (used for request template)
   * @param token the user token to inject
   * @param retrofit the Retrofit instance for creating calls and parsing responses
   * @param responseType the actual response type for proper deserialization
   */
  UserCall(retrofit2.Call<T> delegate, UserToken token, Retrofit retrofit, Type responseType) {
    this.delegate = delegate;
    this.token = token;
    this.retrofit = retrofit;
    this.responseType = responseType;
  }

  /**
   * Creates an OkHttp call with the tagged request.
   */
  private okhttp3.Call createRawCall() {
    return retrofit.callFactory().newCall(request());
  }

  /**
   * Executes the HTTP request synchronously using a new call with the tagged request.
   *
   * @return the response
   * @throws IOException if the request fails
   */
  @Override
  public @NotNull retrofit2.Response<T> execute() throws IOException {
    okhttp3.Call call;
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already executed.");
      executed = true;
      rawCall = createRawCall();
      call = rawCall;
    }
    
    okhttp3.Response rawResponse = call.execute();
    return parseResponse(rawResponse);
  }

  /**
   * Asynchronously sends the request using a new call with the tagged request.
   *
   * @param callback the callback to notify when the response arrives
   */
  @Override
  public void enqueue(@NotNull retrofit2.Callback<T> callback) {
    okhttp3.Call call;
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already executed.");
      executed = true;
      rawCall = createRawCall();
      call = rawCall;
    }

    call.enqueue(new okhttp3.Callback() {
      @Override
      public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response rawResponse) {
        retrofit2.Response<T> response;
        try {
          response = parseResponse(rawResponse);
        } catch (Throwable t) {
          callFailure(t);
          return;
        }
        callSuccess(response);
      }

      @Override
      public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
        callFailure(e);
      }

      private void callSuccess(retrofit2.Response<T> response) {
        try {
          callback.onResponse(UserCall.this, response);
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }

      private void callFailure(Throwable t) {
        try {
          callback.onFailure(UserCall.this, t);
        } catch (Throwable t2) {
          t2.printStackTrace();
        }
      }
    });
  }

  /**
   * Parses the raw OkHttp response into a Retrofit response using Retrofit's converters.
   * Based on Retrofit's OkHttpCall.parseResponse() implementation.
   */
  @SuppressWarnings("unchecked")
  private retrofit2.Response<T> parseResponse(okhttp3.Response rawResponse) throws IOException {
    ResponseBody rawBody = rawResponse.body();
    
    // Remove the body's source (the only stateful object) so we can pass the response along
    rawResponse = rawResponse.newBuilder()
        .body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength()))
        .build();
    
    int code = rawResponse.code();
    
    if (code < 200 || code >= 300) {
      try {
        // Buffer the entire body to avoid future I/O
        ResponseBody bufferedBody = bufferResponseBody(rawBody);
        return retrofit2.Response.error(bufferedBody, rawResponse);
      } finally {
        rawBody.close();
      }
    }
    
    if (code == 204 || code == 205) {
      rawBody.close();
      return retrofit2.Response.success(null, rawResponse);
    }
    
    // Success response - parse body using Retrofit's converter
    try {
      retrofit2.Converter<ResponseBody, T> converter = 
          (retrofit2.Converter<ResponseBody, T>) retrofit.responseBodyConverter(
              responseType, new Annotation[0]);
      
      T body = converter.convert(rawBody);
      return retrofit2.Response.success(body, rawResponse);
    } catch (RuntimeException e) {
      rawBody.close();
      throw e;
    }
  }
  
  /**
   * Buffers the response body to avoid future I/O operations.
   */
  private static ResponseBody bufferResponseBody(ResponseBody body) throws IOException {
    okio.Buffer buffer = new okio.Buffer();
    body.source().readAll(buffer);
    return ResponseBody.create(buffer.readByteArray(), body.contentType());
  }
  
  /**
   * A response body that returns empty content, used to prevent reading stateful sources.
   */
  private static final class NoContentResponseBody extends ResponseBody {
    private final okhttp3.MediaType contentType;
    private final long contentLength;
    
    NoContentResponseBody(okhttp3.MediaType contentType, long contentLength) {
      this.contentType = contentType;
      this.contentLength = contentLength;
    }
    
    @Override
    public okhttp3.MediaType contentType() {
      return contentType;
    }
    
    @Override
    public long contentLength() {
      return contentLength;
    }
    
    @Override
    public okio.BufferedSource source() {
      throw new IllegalStateException("Cannot read raw response body of a converted body.");
    }
  }

  /**
   * Returns true if this call has been executed.
   *
   * @return true if executed, false otherwise
   */
  @Override
  public boolean isExecuted() {
    return executed;
  }

  /**
   * Cancels the request, if possible.
   */
  @Override
  public void cancel() {
    if (rawCall != null) {
      rawCall.cancel();
    }
  }

  /**
   * Returns true if this call has been canceled.
   *
   * @return true if canceled, false otherwise
   */
  @Override
  public boolean isCanceled() {
    return rawCall != null && rawCall.isCanceled();
  }

  /**
   * Creates a new, identical call that can be executed independently.
   * <p>
   * The cloned call will also have the user token injected.
   * </p>
   *
   * @return a new call instance
   */
  @Override
  public @NotNull retrofit2.Call<T> clone() {
    return new UserCall<>(delegate.clone(), token, retrofit, responseType);
  }

  /**
   * Returns the original HTTP request with the user token attached as a typed tag.
   * <p>
   * The token is stored using {@link Request#tag(Class, Object)} and can be retrieved
   * by interceptors using {@code request.tag(UserToken.class)}.
   * </p>
   *
   * @return the request with the user token tag
   */
  @Override
  public @NotNull Request request() {
    Request original = delegate.request();
    return original.newBuilder()
        .tag(UserToken.class, token)
        .build();
  }

  /**
   * Returns the timeout for this call.
   *
   * @return the timeout
   */
  @Override
  public @NotNull okio.Timeout timeout() {
    return rawCall != null ? rawCall.timeout() : okio.Timeout.NONE;
  }
}