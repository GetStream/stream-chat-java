package io.getstream.chat.java.services.framework;

final class UserToken {
  private final String value;

  UserToken(String value) {
    this.value = value;
  }

  String value() {
    return value;
  }
}
