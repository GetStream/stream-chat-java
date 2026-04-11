package io.getstream.chat.java.services.framework.internal;

/**
 * Thrown when user token injection into a request fails.
 *
 * <p>This can happen if:
 *
 * <ul>
 *   <li>A service method does not return {@code retrofit2.Call<?>}.
 *   <li>Retrofit internal structure changes and reflection fails.
 * </ul>
 */
public class TokenInjectionException extends ReflectiveOperationException {
  public TokenInjectionException(String message) {
    super(message);
  }

  public TokenInjectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
