package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Command.CommandCreateRequestData.CommandCreateRequest;
import io.stream.models.Command.CommandUpdateRequestData.CommandUpdateRequest;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponse;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.CommandService;
import io.stream.services.framework.StreamServiceGenerator;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
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

  public Command() {}

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

    private CommandCreateRequestData(CommandCreateRequest commandCreateRequest) {
      this.name = commandCreateRequest.name;
      this.description = commandCreateRequest.description;
      this.args = commandCreateRequest.args;
      this.setValue = commandCreateRequest.setValue;
    }

    public static final class CommandCreateRequest extends StreamRequest<CommandCreateResponse> {
      private String name;
      private String description;
      private String args;
      private String setValue;

      private CommandCreateRequest() {}

      @NotNull
      public CommandCreateRequest name(@NotNull String name) {
        this.name = name;
        return this;
      }

      @NotNull
      public CommandCreateRequest description(@NotNull String description) {
        this.description = description;
        return this;
      }

      @NotNull
      public CommandCreateRequest args(@NotNull String args) {
        this.args = args;
        return this;
      }

      @NotNull
      public CommandCreateRequest setValue(@NotNull String setValue) {
        this.setValue = setValue;
        return this;
      }

      @Override
      protected Call<CommandCreateResponse> generateCall() {
        return StreamServiceGenerator.createService(CommandService.class)
            .create(new CommandCreateRequestData(this));
      }
    }
  }

  public static class CommandGetRequest extends StreamRequest<CommandGetResponse> {
    @NotNull private String name;

    private CommandGetRequest(@NotNull String name) {
      this.name = name;
    }

    @Override
    protected Call<CommandGetResponse> generateCall() {
      return StreamServiceGenerator.createService(CommandService.class).get(name);
    }
  }

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

    private CommandUpdateRequestData(CommandUpdateRequest commandUpdateRequest) {
      this.description = commandUpdateRequest.description;
      this.args = commandUpdateRequest.args;
      this.setValue = commandUpdateRequest.setValue;
    }

    public static final class CommandUpdateRequest extends StreamRequest<CommandUpdateResponse> {
      private String name;
      private String description;
      private String args;
      private String setValue;

      private CommandUpdateRequest(@NotNull String name) {
        this.name = name;
      }

      @NotNull
      public CommandUpdateRequest description(@NotNull String description) {
        this.description = description;
        return this;
      }

      @NotNull
      public CommandUpdateRequest args(@NotNull String args) {
        this.args = args;
        return this;
      }

      @NotNull
      public CommandUpdateRequest setValue(@NotNull String setValue) {
        this.setValue = setValue;
        return this;
      }

      @Override
      protected Call<CommandUpdateResponse> generateCall() {
        return StreamServiceGenerator.createService(CommandService.class)
            .update(name, new CommandUpdateRequestData(this));
      }
    }
  }

  public static class CommandDeleteRequest extends StreamRequest<CommandDeleteResponse> {
    @NotNull private String name;

    private CommandDeleteRequest(@NotNull String name) {
      this.name = name;
    }

    @Override
    protected Call<CommandDeleteResponse> generateCall() {
      return StreamServiceGenerator.createService(CommandService.class).delete(name);
    }
  }

  public static class CommandListRequest extends StreamRequest<CommandListResponse> {

    private CommandListRequest() {}

    @Override
    protected Call<CommandListResponse> generateCall() {
      return StreamServiceGenerator.createService(CommandService.class).list();
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class CommandCreateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("command")
    private Command command;

    public CommandCreateResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class CommandGetResponse extends Command implements StreamResponse {
    private RateLimitData rateLimitData;

    public CommandGetResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class CommandUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("command")
    private Command command;

    public CommandUpdateResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class CommandDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("name")
    private String name;

    public CommandDeleteResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class CommandListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("commands")
    private List<Command> commands;

    public CommandListResponse() {}
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
