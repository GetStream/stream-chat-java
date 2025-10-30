package io.getstream.chat.java.services.framework.internal;

/**
 * Thrown when user token injection into a request fails.
 * This can happen if:
 * - A service method doesn't return retrofit2.Call<?>
 * - Retrofit's internal structure changes and reflection fails
 */
public class TokenInjectionException extends Exception {
  public TokenInjectionException(String message) {
    super(message);
  }
  
  public TokenInjectionException(String message, Throwable cause) {
    super(message, cause);
  }
}

