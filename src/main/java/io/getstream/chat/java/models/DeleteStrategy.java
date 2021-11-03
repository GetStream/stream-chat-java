package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DeleteStrategy {
  @JsonProperty("soft")
  SOFT,
  @JsonProperty("hard")
  HARD
}
