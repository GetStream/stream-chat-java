package io.getstream.chat.java.services.framework;

import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

@Log
public class StreamServiceGenerator {
  public static @NotNull <S> S createService(@NotNull Class<S> serviceClass) {
    return DefaultServiceFactory.getInstance().create(serviceClass);
  }
}
