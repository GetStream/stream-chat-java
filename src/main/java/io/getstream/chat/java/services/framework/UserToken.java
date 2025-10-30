package io.getstream.chat.java.services.framework;

/**
 * Immutable wrapper for a user authentication token.
 * <p>
 * This class encapsulates a user token string that is injected into HTTP requests
 * for per-user authentication in multi-tenant scenarios. The token is stored as a
 * request tag and retrieved by interceptors for adding authorization headers.
 * </p>
 * <p>
 * Package-private to prevent direct instantiation outside the framework.
 * </p>
 */
final class UserToken {
  private final String value;

  /**
   * Constructs a new UserToken with the specified value.
   *
   * @param value the token string value
   */
  UserToken(String value) {
    this.value = value;
  }

  /**
   * Returns the token string value.
   *
   * @return the token string
   */
  String value() {
    return value;
  }
}
