package io.getstream.chat.java.services.framework;

import org.jetbrains.annotations.NotNull;

public interface ServiceFactory {
  @NotNull
  <TService> TService create(Class<TService> svcClass);
}
