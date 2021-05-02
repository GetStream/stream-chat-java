package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class Command {
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

  public Command() {}
}
