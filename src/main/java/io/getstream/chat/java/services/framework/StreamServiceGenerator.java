package io.getstream.chat.java.services.framework;

import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

@Log
public class StreamServiceGenerator {
  public static @NotNull <S> S createService(@NotNull Class<S> serviceClass) {
    return DefaultClient.getInstance().create(serviceClass);
  }
}
