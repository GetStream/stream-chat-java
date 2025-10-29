package io.getstream.chat.java.services.framework;

public final class UserToken {
  private final String value;

  public UserToken(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  public boolean isBlank() {
    return value == null || value.isBlank();
  }
}
