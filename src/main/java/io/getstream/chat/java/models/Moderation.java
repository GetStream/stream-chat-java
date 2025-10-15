package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.*;
import io.getstream.chat.java.models.Moderation.UpsertConfigRequestData.UpsertConfigRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.ModerationService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Moderation {

  @Builder(
      builderClassName = "ConfigGetRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class ConfigGetRequestData {
    public static class ConfigGetRequest extends StreamRequest<ConfigGetResponse> {
      @NotNull private String key;

      private ConfigGetRequest(@NotNull String key) {
        this.key = key;
      }

      @Override
      protected Call<ConfigGetResponse> generateCall(Client client) {
        return client.create(ModerationService.class).getConfig(this.key);
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ConfigGetResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("config")
    private Config config;
  }

  @Data
  @NoArgsConstructor
  public static class Config {
    @Nullable
    @JsonProperty("key")
    private String key;

    @Nullable
    @JsonProperty("async")
    private Boolean async;

    @Nullable
    @JsonProperty("block_list_config")
    private BlockListConfig blockListConfig;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;
  }

  @Data
  @NoArgsConstructor
  public static class BlockListConfig {
    @Nullable
    @JsonProperty("async")
    private Boolean async;

    @NotNull
    @JsonProperty("enabled")
    private Boolean enabled;

    @NotNull
    @JsonProperty("rules")
    private List<BlockListRule> rules;
  }

  public enum Action {
    @JsonProperty("flag")
    FLAG,
    @JsonProperty("shadow")
    SHADOW,
    @JsonProperty("remove")
    REMOVE,
    @JsonProperty("bounce")
    BOUNCE,
    @JsonProperty("bounce_flag")
    BOUNCE_FLAG,
    @JsonProperty("bounce_remove")
    BOUNCE_REMOVE,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  @Data
  @NoArgsConstructor
  @Builder
  @AllArgsConstructor
  public static class BlockListRule {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("action")
    private Action action;
  }

  @Builder
  @Getter
  @EqualsAndHashCode
  public static class BlockListConfigRequestObject {
    @Nullable
    @JsonProperty("async")
    private Boolean async;

    @NotNull
    @JsonProperty("rules")
    private List<BlockListRule> rules;
  }

  @Builder(
      builderClassName = "UpsertConfigRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class UpsertConfigRequestData {
    @Nullable
    @JsonProperty("key")
    private String key;

    @Nullable
    @JsonProperty("async")
    private Boolean async;

    @Nullable
    @JsonProperty("block_list_config")
    private BlockListConfigRequestObject blockListConfig;

    public static class UpsertConfigRequest extends StreamRequest<UpsertConfigResponse> {
      @NotNull private String key;

      private UpsertConfigRequest(@NotNull String key) {
        this.key = key;
      }

      @Override
      protected Call<UpsertConfigResponse> generateCall(Client client) {
        return client
            .create(ModerationService.class)
            .upsertConfig(this.key(this.key).internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UpsertConfigResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("config")
    private Config config;
  }

  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  public static class DeleteConfigRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String key;

    @Override
    protected Call<StreamResponseObject> generateCall(Client client) {
      return client.create(ModerationService.class).deleteConfig(this.key);
    }
  }

  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  public static class ConfigGetRequest extends StreamRequest<ConfigGetResponse> {
    @NotNull private String key;

    @Override
    protected Call<ConfigGetResponse> generateCall(Client client) {
      return client.create(ModerationService.class).getConfig(this.key);
    }
  }

  /**
   * Creates an upsert config request
   *
   * @param key the moderation config key
   * @return the created request
   */
  @NotNull
  public static UpsertConfigRequest upsertConfig(@NotNull String key) {
    return new UpsertConfigRequest(key);
  }

  /**
   * Creates a delete config request
   *
   * @param key the moderation config key
   * @return the created request
   */
  @NotNull
  public static DeleteConfigRequest deleteConfig(@NotNull String key) {
    return new DeleteConfigRequest(key);
  }

  /*
   * Creates a get config request
   *
   * @param key the moderation config key
   * @return the created request
   */
  @NotNull
  public static ConfigGetRequest getConfig(@NotNull String key) {
    return new ConfigGetRequest(key);
  }
}
