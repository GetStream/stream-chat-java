package io.getstream.chat.java.services.framework;

import org.jetbrains.annotations.NotNull;

public interface Client {
  @NotNull
  <TService> TService create(Class<TService> svcClass);

  @NotNull
  String getApiKey();

  @NotNull
  String getApiSecret();

  static Client getInstance() {
    return DefaultClient.getInstance();
  }
}
