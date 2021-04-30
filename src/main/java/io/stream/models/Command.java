package io.stream.models;

import org.jetbrains.annotations.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Command {
  public Command() {}

  @NotNull
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("description")
  private String description;

  @NotNull
  @JsonProperty("args")
  private String args;

  @NotNull
  @JsonProperty("set")
  private String setValue;
}
