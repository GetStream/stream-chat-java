package io.getstream.chat.java.services.framework;

import retrofit2.Retrofit;

/**
 * A user service factory implementation that wraps Retrofit service calls with user token context.
 * <p>
 * This factory creates dynamic proxies around Retrofit service interfaces, intercepting method calls
 * to wrap any {@link retrofit2.Call} results with {@link UserCall}. This enables automatic user token
 * injection for client-side requests without modifying the service interface definitions.
 * </p>
 * <p>
 * The wrapping process is transparent to callers - they interact with the service interface normally,
 * but each Retrofit Call is automatically enhanced with the provided user token.
 * </p>
 *
 * @see UserServiceFactory
 * @see UserCall
 * @see UserToken
 */
final class UserServiceFactoryCall implements UserServiceFactory {

  private final Retrofit retrofit;

  /**
   * Constructs a new UserServiceFactoryCall with the specified Retrofit instance.
   *
   * @param retrofit the Retrofit instance used to create the underlying service implementations
   */
  public UserServiceFactoryCall(Retrofit retrofit) {
    this.retrofit = retrofit;
  }

  /**
   * Creates a dynamic proxy for the specified service interface that wraps Retrofit Calls with user token context.
   * <p>
   * This method generates a service implementation that intercepts all method calls. When a method returns
   * a {@link retrofit2.Call}, it wraps the call in a {@link UserCall} that carries the provided user token.
   * Non-Call return values are passed through unchanged.
   * </p>
   *
   * @param <TService> the service interface type
   * @param svcClass   the service interface class to create
   * @param userToken  the user token to inject into wrapped calls
   * @return a dynamic proxy implementing the service interface with automatic UserCall wrapping
   */
  @SuppressWarnings("unchecked")
  public final <TService> TService create(Class<TService> svcClass, UserToken userToken) {
    TService delegate = retrofit.create(svcClass);

    return (TService) java.lang.reflect.Proxy.newProxyInstance(
        svcClass.getClassLoader(),
        new Class<?>[] { svcClass },
        (proxy, method, args) -> {
          Object result = method.invoke(delegate, args);

          // If the result is a retrofit2.Call, wrap it with UserCall
          if (result instanceof retrofit2.Call<?>) {
            // Extract the response type from the method's return type
            java.lang.reflect.Type returnType = method.getGenericReturnType();
            java.lang.reflect.Type responseType = Object.class;
            
            if (returnType instanceof java.lang.reflect.ParameterizedType) {
              java.lang.reflect.ParameterizedType paramType = (java.lang.reflect.ParameterizedType) returnType;
              if (paramType.getActualTypeArguments().length > 0) {
                responseType = paramType.getActualTypeArguments()[0];
              }
            }
            
            return new UserCall<>((retrofit2.Call<?>) result, userToken, retrofit, responseType);
          }

          return result;
        }
    );
  }

}
