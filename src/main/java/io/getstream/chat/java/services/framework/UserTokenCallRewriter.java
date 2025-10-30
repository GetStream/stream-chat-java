package io.getstream.chat.java.services.framework;

import io.getstream.chat.java.services.framework.internal.TokenInjectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import okhttp3.Call;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;

/**
 * Dynamic proxy that intercepts Retrofit service calls and injects {@link UserToken} into requests
 * for per-user authentication.
 *
 * <p>This class uses Java reflection to modify Retrofit's internal {@code Call} objects, injecting
 * a {@link UserToken} as a request tag. The token is then retrieved by OkHttp interceptors to add
 * authentication headers.
 *
 * @param <TService> the service interface type being proxied
 * @see UserToken
 * @see UserServiceFactory
 */
class UserTokenCallRewriter<TService> implements InvocationHandler {
  /**
   * Cached reference to Retrofit's internal rawCall field. Uses double-checked locking for
   * thread-safe lazy initialization.
   */
  private static volatile Field rawCallField;

  private final Call.Factory callFactory;
  private final TService delegate;
  private final UserToken token;

  /**
   * Constructs a new call rewriter that injects the specified token.
   *
   * @param callFactory the OkHttp call factory for creating modified calls
   * @param delegate the original service implementation to proxy
   * @param token the user token to inject into requests
   */
  UserTokenCallRewriter(
      @NotNull Call.Factory callFactory, @NotNull TService delegate, @NotNull UserToken token) {
    this.callFactory = callFactory;
    this.delegate = delegate;
    this.token = token;
  }

  /**
   * Intercepts service method invocations to inject the user token.
   *
   * <p>This method ensures that all service methods return {@code retrofit2.Call<?>} objects. If a
   * method returns a different type, a {@link TokenInjectionException} is thrown.
   *
   * @param proxy the proxy instance
   * @param method the method being invoked
   * @param args the method arguments
   * @return the modified Call with token injection
   * @throws Throwable if the underlying method throws an exception
   * @throws TokenInjectionException if the method doesn't return retrofit2.Call<?>
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object result = method.invoke(delegate, args);

    // If the result is a Retrofit Call, inject the user token
    if (result instanceof retrofit2.Call<?>) {
      return injectTokenIntoCall((retrofit2.Call<?>) result);
    }

    // All service methods must return Call<?> for token injection
    throw new TokenInjectionException(
        "Method "
            + method.getName()
            + " on "
            + delegate.getClass().getName()
            + " did not return retrofit2.Call<?>. User token injection requires all service methods to return Call<?>.");
  }

  /**
   * Injects the user token into a Retrofit call by modifying its internal OkHttp call.
   *
   * <p>The token is added as a request tag of type {@link UserToken}, which can be retrieved by
   * OkHttp interceptors for authentication purposes.
   *
   * @param originalCall the original Retrofit call
   * @return a cloned call with the user token injected
   * @throws TokenInjectionException if reflection fails or Retrofit's structure has changed
   */
  private retrofit2.Call<?> injectTokenIntoCall(retrofit2.Call<?> originalCall)
      throws TokenInjectionException {
    retrofit2.Call<?> clonedCall = originalCall.clone();

    try {
      // Cache field lookup for performance (double-checked locking)
      if (rawCallField == null) {
        synchronized (UserTokenCallRewriter.class) {
          if (rawCallField == null) {
            rawCallField = clonedCall.getClass().getDeclaredField("rawCall");
            rawCallField.setAccessible(true);
          }
        }
      }

      // Create new request with token tag
      Request newRequest = originalCall.request().newBuilder().tag(UserToken.class, token).build();

      // Create new OkHttp call with modified request
      okhttp3.Call newOkHttpCall = callFactory.newCall(newRequest);

      // Inject the new call into the cloned Retrofit call
      rawCallField.set(clonedCall, newOkHttpCall);

      return clonedCall;
    } catch (NoSuchFieldException e) {
      // If Retrofit's internal structure changes, provide clear error message
      throw new TokenInjectionException(
          "Retrofit internal structure changed. Field 'rawCall' not found in "
              + clonedCall.getClass().getName()
              + ". Update client implementation.",
          e);
    } catch (IllegalAccessException e) {
      throw new TokenInjectionException("Failed to inject token into call", e);
    }
  }
}
