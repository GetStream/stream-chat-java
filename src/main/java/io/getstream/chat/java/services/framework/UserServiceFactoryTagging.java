package io.getstream.chat.java.services.framework;

import okhttp3.Request;
import retrofit2.Retrofit;

/**
 * User-aware service factory that tags OkHttp requests with user tokens.
 *
 * <p>This implementation wraps the OkHttp call factory to automatically attach a {@link UserToken}
 * as a request tag. The token can then be retrieved by interceptors for authentication purposes.
 *
 * <p><b>Mechanism:</b> Creates a new Retrofit instance with a custom call factory that tags each
 * request before delegating to the underlying call factory.
 */
final class UserServiceFactoryTagging implements UserServiceFactory {

  private final Retrofit retrofit;

  /**
   * Constructs a new tagging-based user service factory.
   *
   * @param retrofit the base Retrofit instance to derive user-specific instances from
   */
  UserServiceFactoryTagging(Retrofit retrofit) {
    this.retrofit = retrofit;
  }

  /**
   * Creates a user-aware service instance that automatically tags requests with the user token.
   *
   * <p>The returned service wraps each OkHttp request with a {@link UserToken} tag that can be
   * retrieved by interceptors using {@code request.tag(UserToken.class)}.
   *
   * @param svcClass the Retrofit service interface class
   * @param userToken the user token to attach to all requests from this service
   * @param <TService> the service type
   * @return a service instance that tags requests with the user token
   */
  @SuppressWarnings("unchecked")
  public final <TService> TService create(Class<TService> svcClass, UserToken userToken) {
    Retrofit taggedRetrofit =
        retrofit
            .newBuilder()
            .callFactory(
                request -> {
                  Request taggedRequest =
                      request.newBuilder().tag(UserToken.class, userToken).build();
                  return retrofit.callFactory().newCall(taggedRequest);
                })
            .build();

    return taggedRetrofit.create(svcClass);
  }
}
