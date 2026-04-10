package io.getstream.chat.java.services.framework;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;

public interface Client {
  @NotNull
  <TService> TService create(Class<TService> svcClass);

  default @NotNull <TService> TService create(Class<TService> svcClass, String userToken) {
    return create(svcClass);
  }

  @NotNull
  String getApiKey();

  @NotNull
  String getApiSecret();

  void setTimeout(@NotNull Duration timeoutDuration);

  void setConnectionPool(int maxIdleConnections, @NotNull Duration keepAliveDuration);

  static Client getInstance() {
    return DefaultClient.getInstance();
  }
}
