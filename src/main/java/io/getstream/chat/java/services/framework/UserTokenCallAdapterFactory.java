package io.getstream.chat.java.services.framework;

import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * CallAdapter.Factory that wraps Retrofit calls to inject UserToken into requests.
 * This is Retrofit's official API for customizing call behavior.
 * 
 * Compared to reflection-based approach:
 * + Uses public Retrofit API (future-proof)
 * + Type-safe
 * - Still requires a Call wrapper (can't avoid it in Retrofit's design)
 */
class UserTokenCallAdapterFactory extends CallAdapter.Factory {
  private final UserToken token;
  private final okhttp3.Call.Factory callFactory;
  
  UserTokenCallAdapterFactory(@NotNull UserToken token, @NotNull okhttp3.Call.Factory callFactory) {
    this.token = token;
    this.callFactory = callFactory;
  }
  
  @Override
  public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
    // Only handle Call<T> return types
    if (getRawType(returnType) != Call.class) {
      return null;
    }
    
    if (!(returnType instanceof ParameterizedType)) {
      throw new IllegalStateException(
        "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
    }
    
    final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
    
    return new CallAdapter<Object, Call<?>>() {
      @Override
      public Type responseType() {
        return responseType;
      }
      
      @Override
      public Call<Object> adapt(Call<Object> call) {
        return new UserTokenCall<>(call, token, callFactory);
      }
    };
  }
  
  /**
   * Wrapper that injects UserToken tag into the request before execution.
   */
  private static class UserTokenCall<T> implements Call<T> {
    private final Call<T> delegate;
    private final UserToken token;
    private final okhttp3.Call.Factory callFactory;
    
    UserTokenCall(Call<T> delegate, UserToken token, okhttp3.Call.Factory callFactory) {
      this.delegate = delegate;
      this.token = token;
      this.callFactory = callFactory;
    }
    
    @Override
    public retrofit2.Response<T> execute() throws IOException {
      return delegate.execute();
    }
    
    @Override
    public void enqueue(retrofit2.Callback<T> callback) {
      delegate.enqueue(callback);
    }
    
    @Override
    public boolean isExecuted() {
      return delegate.isExecuted();
    }
    
    @Override
    public void cancel() {
      delegate.cancel();
    }
    
    @Override
    public boolean isCanceled() {
      return delegate.isCanceled();
    }
    
    @Override
    public Call<T> clone() {
      return new UserTokenCall<>(delegate.clone(), token, callFactory);
    }
    
    @Override
    public Request request() {
      // This is where the magic happens - inject the token tag
      return delegate.request().newBuilder()
        .tag(UserToken.class, token)
        .build();
    }
    
    @Override
    public okio.Timeout timeout() {
      return delegate.timeout();
    }
  }
}

