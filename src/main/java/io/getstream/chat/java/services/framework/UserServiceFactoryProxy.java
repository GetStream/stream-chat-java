package io.getstream.chat.java.services.framework;

import retrofit2.Retrofit;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * User-aware service factory that uses dynamic proxies to inject user tokens.
 * <p>
 * This implementation wraps Retrofit service interfaces with a dynamic proxy that intercepts
 * method calls and delegates to {@link UserTokenCallRewriter} for token injection.
 * <p>
 * <b>Mechanism:</b> Uses Java reflection {@link java.lang.reflect.Proxy} to wrap the service
 * interface and inject user tokens at method invocation time.
 * <p>
 * <b>Trade-offs:</b>
 * <ul>
 *   <li>Pros: Reuses single Retrofit instance, flexible interception point</li>
 *   <li>Cons: Reflection overhead on every method call, slightly more complex debugging</li>
 * </ul>
 * <p>
 * <b>Thread-safety:</b> Immutable and thread-safe once constructed. The underlying proxy
 * delegation is also thread-safe.
 *
 * @see UserTokenCallRewriter
 */
final class UserServiceFactoryProxy implements UserServiceFactory {

  private final Retrofit retrofit;

  /**
   * Constructs a new proxy-based user service factory.
   *
   * @param retrofit the Retrofit instance to create services from
   */
  public UserServiceFactoryProxy(Retrofit retrofit) {
    this.retrofit = retrofit;
  }

  /**
   * Creates a user-aware service instance using a dynamic proxy.
   * <p>
   * The returned service is a dynamic proxy that intercepts all method calls and delegates
   * to the underlying Retrofit service while injecting the user token.
   *
   * @param svcClass  the Retrofit service interface class
   * @param userToken the user token to inject into all requests from this service
   * @param <TService> the service type
   * @return a proxied service instance that injects the user token
   */
  @SuppressWarnings("unchecked")
  public final <TService> TService create(Class<TService> svcClass, UserToken userToken) {
    return (TService) newProxyInstance(
        svcClass.getClassLoader(),
        new Class<?>[] { svcClass },
        new UserTokenCallRewriter<>(retrofit.callFactory(), retrofit.create(svcClass), userToken)
    );
  }

}
