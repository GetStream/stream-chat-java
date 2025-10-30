package io.getstream.chat.java.services.framework;

import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

class UserCall<T> implements retrofit2.Call<T> {
  private final retrofit2.Call<T> delegate;
  private final UserToken token;

  UserCall(retrofit2.Call<T> delegate, UserToken token) {
    this.delegate = delegate;
    this.token = token;
  }

  @Override
  public @NotNull retrofit2.Response<T> execute() throws IOException {
    return delegate.execute();
  }

  @Override
  public void enqueue(@NotNull retrofit2.Callback<T> callback) {
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
  public @NotNull retrofit2.Call<T> clone() {
    return new UserCall<>(delegate.clone(), token);
  }

  @Override
  public @NotNull Request request() {
    Request original = delegate.request();
    return original.newBuilder()
        .tag(UserToken.class, token)
        .build();
  }

  @Override
  public @NotNull okio.Timeout timeout() {
    return delegate.timeout();
  }
}