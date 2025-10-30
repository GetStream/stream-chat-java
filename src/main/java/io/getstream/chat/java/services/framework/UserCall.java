package io.getstream.chat.java.services.framework;

import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Wrapper for Retrofit {@code Call} objects that injects user authentication tokens.
 * <p>
 * This class delegates all {@code Call} operations to an underlying call while ensuring
 * that the {@link UserToken} is attached to the request as a typed tag. The token can
 * then be retrieved by OkHttp interceptors for adding authorization headers.
 * </p>
 *
 * @param <T> the response body type
 * @see UserToken
 * @see UserTokenCallRewriter
 */
class UserCall<T> implements retrofit2.Call<T> {
  private final retrofit2.Call<T> delegate;
  private final UserToken token;

  /**
   * Constructs a new UserCall that wraps the provided call with token injection.
   *
   * @param delegate the underlying Retrofit call
   * @param token the user token to inject
   */
  UserCall(retrofit2.Call<T> delegate, UserToken token) {
    this.delegate = delegate;
    this.token = token;
  }

  /**
   * Executes the HTTP request synchronously.
   *
   * @return the response
   * @throws IOException if the request fails
   */
  @Override
  public @NotNull retrofit2.Response<T> execute() throws IOException {
    return delegate.execute();
  }

  /**
   * Asynchronously sends the request and notifies the callback of its response.
   *
   * @param callback the callback to notify when the response arrives
   */
  @Override
  public void enqueue(@NotNull retrofit2.Callback<T> callback) {
    delegate.enqueue(callback);
  }

  /**
   * Returns true if this call has been executed.
   *
   * @return true if executed, false otherwise
   */
  @Override
  public boolean isExecuted() {
    return delegate.isExecuted();
  }

  /**
   * Cancels the request, if possible.
   */
  @Override
  public void cancel() {
    delegate.cancel();
  }

  /**
   * Returns true if this call has been canceled.
   *
   * @return true if canceled, false otherwise
   */
  @Override
  public boolean isCanceled() {
    return delegate.isCanceled();
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
    return new UserCall<>(delegate.clone(), token);
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
    return delegate.timeout();
  }
}