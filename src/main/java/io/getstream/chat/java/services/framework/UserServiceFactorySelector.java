package io.getstream.chat.java.services.framework;

import java.util.concurrent.atomic.AtomicReference;
import retrofit2.Retrofit;

/**
 * Smart user-aware service factory with automatic fallback mechanism.
 *
 * <p>This implementation attempts to use {@link UserServiceFactoryProxy} (more efficient) and
 * automatically falls back to {@link UserServiceFactoryTagging} if the proxy approach fails.
 */
final class UserServiceFactorySelector implements UserServiceFactory {

  private final UserServiceFactory proxyFactory;
  private final UserServiceFactory taggingFactory;
  private final AtomicReference<UserServiceFactory> activeFactory;

  /**
   * Constructs a new smart factory with fallback capability.
   *
   * @param retrofit the Retrofit instance to create services from
   */
  public UserServiceFactorySelector(Retrofit retrofit) {
    this.proxyFactory = new UserServiceFactoryProxy(retrofit);
    this.taggingFactory = new UserServiceFactoryTagging(retrofit);

    // Verify proxy approach is viable before setting default
    UserServiceFactory defaultFactory = proxyFactory;
    try {
      // Check if we can access the rawCall field that UserTokenCallRewriter needs
      Class<?> retrofitCallClass = Class.forName("retrofit2.OkHttpCall");
      retrofitCallClass.getDeclaredField("rawCall");
      // If we get here, proxy should work
    } catch (Throwable e) {
      // Proxy approach won't work, use tagging as default
      defaultFactory = taggingFactory;
    }

    this.activeFactory = new AtomicReference<>(defaultFactory);
  }

  /**
   * Creates a user-aware service instance with automatic fallback.
   *
   * <p>Attempts to use the proxy implementation first. If it fails (due to reflection issues, API
   * changes, or other errors), automatically switches to the tagging implementation and retries.
   *
   * @param svcClass the Retrofit service interface class
   * @param userToken the user token to inject into all requests from this service
   * @param <TService> the service type
   * @return a service instance that injects the user token
   * @throws RuntimeException if both implementations fail
   */
  @Override
  public <TService> TService create(Class<TService> svcClass, UserToken userToken) {
    UserServiceFactory factory = activeFactory.get();

    try {
      return factory.create(svcClass, userToken);
    } catch (Throwable e) {
      // If we're already using the fallback, propagate the error
      if (factory == taggingFactory) {
        throw new RuntimeException("Failed to create service using fallback implementation", e);
      }

      // Switch to fallback and retry
      activeFactory.compareAndSet(proxyFactory, taggingFactory);

      // Retry with fallback
      try {
        return taggingFactory.create(svcClass, userToken);
      } catch (Throwable fallbackException) {
        throw new RuntimeException(
            "Failed to create service with both implementations", fallbackException);
      }
    }
  }
}
