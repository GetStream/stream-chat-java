package io.stream.models;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
  private String set;

  @Data
  public static class commandResponse {
    public commandResponse() {}

    @Nullable
    private Command command;

  }

  @Data
  public static class commandsResponse {
    public commandsResponse() {}

    @Nullable
    private List<Command> commands;

  }

}
