package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Command.CommandCreateRequestData.CommandCreateRequest;
import io.stream.models.Command.CommandUpdateRequestData.CommandUpdateRequest;
import io.stream.models.framework.RateLimit;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponse;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.CommandService;
import io.stream.services.framework.StreamServiceGenerator;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Command {
  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

  @NotNull
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("description")
  private String description;

  @Nullable
  @JsonProperty("args")
  private String args;

  @Nullable
  @JsonProperty("set")
  private String setValue;

  @Builder(
      builderClassName = "CommandCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class CommandCreateRequestData {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("description")
    private String description;

    @Nullable
    @JsonProperty("args")
    private String args;

    @Nullable
    @JsonProperty("set")
    private String setValue;

    public static class CommandCreateRequest extends StreamRequest<CommandCreateResponse> {
      @Override
      protected Call<CommandCreateResponse> generateCall() {
        return StreamServiceGenerator.createService(CommandService.class)
            .create(this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class CommandGetRequest extends StreamRequest<CommandGetResponse> {
    @NotNull private String name;

    @Override
    protected Call<CommandGetResponse> generateCall() {
      return StreamServiceGenerator.createService(CommandService.class).get(name);
    }
  }

  @Builder(
      builderClassName = "CommandUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class CommandUpdateRequestData {
    @NotNull
    @JsonProperty("description")
    private String description;

    @Nullable
    @JsonProperty("args")
    private String args;

    @Nullable
    @JsonProperty("set")
    private String setValue;

    public static class CommandUpdateRequest extends StreamRequest<CommandUpdateResponse> {
      @NotNull private String name;

      private CommandUpdateRequest(@NotNull String name) {
        this.name = name;
      }

      @Override
      protected Call<CommandUpdateResponse> generateCall() {
        return StreamServiceGenerator.createService(CommandService.class)
            .update(name, this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class CommandDeleteRequest extends StreamRequest<CommandDeleteResponse> {
    @NotNull private String name;

    @Override
    protected Call<CommandDeleteResponse> generateCall() {
      return StreamServiceGenerator.createService(CommandService.class).delete(name);
    }
  }

  public static class CommandListRequest extends StreamRequest<CommandListResponse> {
    @Override
    protected Call<CommandListResponse> generateCall() {
      return StreamServiceGenerator.createService(CommandService.class).list();
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CommandCreateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("command")
    private Command command;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CommandGetResponse extends Command implements StreamResponse {
    private RateLimit rateLimit;

    @NotNull
    @JsonProperty("duration")
    private String duration;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CommandUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("command")
    private Command command;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CommandDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("name")
    private String name;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CommandListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("commands")
    private List<Command> commands;
  }

  /**
   * Creates a create request
   *
   * @return the created request
   */
  @NotNull
  public static CommandCreateRequest create() {
    return new CommandCreateRequest();
  }

  /**
   * Creates a get request
   *
   * @param name the command name
   * @return the created request
   */
  @NotNull
  public static CommandGetRequest get(@NotNull String name) {
    return new CommandGetRequest(name);
  }

  /**
   * Creates an update request
   *
   * @param name the command name
   * @return the created request
   */
  @NotNull
  public static CommandUpdateRequest update(@NotNull String name) {
    return new CommandUpdateRequest(name);
  }

  /**
   * Creates a delete request
   *
   * @param name the command name
   * @return the created request
   */
  @NotNull
  public static CommandDeleteRequest delete(@NotNull String name) {
    return new CommandDeleteRequest(name);
  }

  /**
   * Creates a list request
   *
   * @return the created request
   */
  @NotNull
  public static CommandListRequest list() {
    return new CommandListRequest();
  }
}
