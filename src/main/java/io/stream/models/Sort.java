package io.stream.models;

import org.jetbrains.annotations.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sort {
  @NotNull
  @JsonProperty("field")
  private String field;

  @NotNull
  @JsonProperty("direction")
  private Integer direction;
}
