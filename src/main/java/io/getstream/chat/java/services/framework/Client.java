package io.getstream.chat.java.services.framework;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;

public interface Client {
  @NotNull
  <TService> TService create(Class<TService> svcClass);

  @NotNull
  String getApiKey();

  @NotNull
  String getApiSecret();

  void setTimeout(@NotNull Duration timeoutDuration);

  static Client getInstance() {
    return DefaultClient.getInstance();
  }
}
