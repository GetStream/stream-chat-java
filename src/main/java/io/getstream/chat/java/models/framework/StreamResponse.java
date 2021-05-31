package io.getstream.chat.java.models.framework;

import org.jetbrains.annotations.NotNull;

public interface StreamResponse {

  String getDuration();

  void setDuration(@NotNull String duration);
}
