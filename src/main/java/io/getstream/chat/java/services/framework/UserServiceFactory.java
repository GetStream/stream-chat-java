package io.getstream.chat.java.services.framework;

/**
 * Factory interface for creating service instances with user-specific authentication.
 *
 * <p>Implementations of this interface are responsible for creating Retrofit service proxies that
 * inject the provided {@link UserToken} into API requests. This enables per-user authentication
 * without requiring separate HTTP client instances.
 *
 * <p>Package-private to control instantiation within the framework.
 *
 * @see UserToken
 * @see UserTokenCallRewriter
 */
interface UserServiceFactory {

  /**
   * Creates a service instance that injects the specified user token into all requests.
   *
   * @param svcClass the service interface class to create
   * @param userToken the user token to inject into requests
   * @param <TService> the service interface type
   * @return a proxy instance of the service with token injection capabilities
   */
  <TService> TService create(Class<TService> svcClass, UserToken userToken);
}
