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
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class DeleteConfigRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String key;

    @Override
    protected Call<StreamResponseObject> generateCall(Client client) {
      return client.create(ModerationService.class).deleteConfig(this.key);
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class ConfigGetRequest extends StreamRequest<ConfigGetResponse> {
    @NotNull private String key;

    @Override
    protected Call<ConfigGetResponse> generateCall(Client client) {
      return client.create(ModerationService.class).getConfig(this.key);
    }
  }

  @Builder(
      builderClassName = "FlagRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class FlagRequestData {
    @NotNull
    @JsonProperty("entity_type")
    private String entityType;

    @NotNull
    @JsonProperty("entity_id")
    private String entityId;

    @NotNull
    @JsonProperty("reason")
    private String reason;

    @Nullable
    @JsonProperty("entity_creator_id")
    private String entityCreatorId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("moderation_payload")
    private ModerationPayload moderationPayload;

    @Nullable
    @JsonProperty("custom")
    private java.util.Map<String, Object> custom;

    public static class FlagRequest extends StreamRequest<FlagResponse> {
      @Override
      protected Call<FlagResponse> generateCall(Client client) {
        return client.create(ModerationService.class).flag(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class FlagResponse extends StreamResponseObject {}

  @Builder(
      builderClassName = "MuteRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class MuteRequestData {
    @NotNull
    @JsonProperty("target_ids")
    private List<String> targetIds;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("timeout")
    private Integer timeout;

    public static class MuteRequest extends StreamRequest<MuteResponse> {
      @Override
      protected Call<MuteResponse> generateCall(Client client) {
        return client.create(ModerationService.class).mute(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MuteResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("mutes")
    private List<User.Mute> mutes;
  }

  @Builder(
      builderClassName = "UnmuteRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class UnmuteRequestData {
    @NotNull
    @JsonProperty("target_ids")
    private List<String> targetIds;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    public static class UnmuteRequest extends StreamRequest<UnmuteResponse> {
      @Override
      protected Call<UnmuteResponse> generateCall(Client client) {
        return client.create(ModerationService.class).unmute(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UnmuteResponse extends StreamResponseObject {}

  @Builder(
      builderClassName = "CheckRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class CheckRequestData {
    @NotNull
    @JsonProperty("entity_type")
    private String entityType;

    @NotNull
    @JsonProperty("entity_id")
    private String entityId;

    @NotNull
    @JsonProperty("moderation_payload")
    private ModerationPayload moderationPayload;

    @NotNull
    @JsonProperty("config_key")
    private String configKey;

    @Nullable
    @JsonProperty("entity_creator_id")
    private String entityCreatorId;

    @Nullable
    @JsonProperty("options")
    private CheckOptions options;

    public static class CheckRequest extends StreamRequest<CheckResponse> {
      @Override
      protected Call<CheckResponse> generateCall(Client client) {
        return client.create(ModerationService.class).check(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @Builder
  @AllArgsConstructor
  public static class ModerationPayload {
    @Nullable
    @JsonProperty("texts")
    private List<String> texts;

    @Nullable
    @JsonProperty("images")
    private List<String> images;

    @Nullable
    @JsonProperty("videos")
    private List<String> videos;

    @Nullable
    @JsonProperty("custom")
    private java.util.Map<String, Object> custom;
  }

  @Data
  @NoArgsConstructor
  @Builder
  @AllArgsConstructor
  public static class CheckOptions {
    @Nullable
    @JsonProperty("force_sync")
    private Boolean forceSync;

    @Nullable
    @JsonProperty("test_mode")
    private Boolean testMode;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CheckResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("status")
    private String status;

    @Nullable
    @JsonProperty("recommended_action")
    private String recommendedAction;

    @Nullable
    @JsonProperty("flags")
    private List<FlagInfo> flags;
  }

  @Data
  @NoArgsConstructor
  public static class FlagInfo {
    @Nullable
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("value")
    private String value;
  }

  @Builder(
      builderClassName = "CustomCheckRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class CustomCheckRequestData {
    @NotNull
    @JsonProperty("entity_type")
    private String entityType;

    @NotNull
    @JsonProperty("entity_id")
    private String entityId;

    @NotNull
    @JsonProperty("flags")
    private List<FlagInfo> flags;

    @Nullable
    @JsonProperty("entity_creator_id")
    private String entityCreatorId;

    @Nullable
    @JsonProperty("moderation_payload")
    private ModerationPayload moderationPayload;

    public static class CustomCheckRequest extends StreamRequest<CustomCheckResponse> {
      @Override
      protected Call<CustomCheckResponse> generateCall(Client client) {
        return client.create(ModerationService.class).customCheck(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CustomCheckResponse extends StreamResponseObject {}

  @Builder(
      builderClassName = "QueryReviewQueueRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class QueryReviewQueueRequestData {
    @Nullable
    @JsonProperty("filter")
    private java.util.Map<String, Object> filter;

    @Nullable
    @JsonProperty("sort")
    private List<Sort> sort;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    @Nullable
    @JsonProperty("lock_items")
    private Boolean lockItems;

    @Nullable
    @JsonProperty("lock_duration")
    private String lockDuration;

    @Nullable
    @JsonProperty("lock_count")
    private Integer lockCount;

    @Nullable
    @JsonProperty("stats_only")
    private Boolean statsOnly;

    public static class QueryReviewQueueRequest extends StreamRequest<QueryReviewQueueResponse> {
      @Override
      protected Call<QueryReviewQueueResponse> generateCall(Client client) {
        return client.create(ModerationService.class).queryReviewQueue(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryReviewQueueResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("items")
    private List<ReviewQueueItem> items;

    @Nullable
    @JsonProperty("stats")
    private java.util.Map<String, Object> stats;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;
  }

  @Data
  @NoArgsConstructor
  public static class ReviewQueueItem {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("entity_type")
    private String entityType;

    @Nullable
    @JsonProperty("entity_id")
    private String entityId;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;
  }

  @Builder(
      builderClassName = "QueryConfigsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class QueryConfigsRequestData {
    @Nullable
    @JsonProperty("filter")
    private java.util.Map<String, Object> filter;

    @Nullable
    @JsonProperty("sort")
    private List<Sort> sort;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    public static class QueryConfigsRequest extends StreamRequest<QueryConfigsResponse> {
      @Override
      protected Call<QueryConfigsResponse> generateCall(Client client) {
        return client.create(ModerationService.class).queryConfigs(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryConfigsResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("configs")
    private List<Config> configs;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;
  }

  @Builder(
      builderClassName = "SubmitActionRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class SubmitActionRequestData {
    @NotNull
    @JsonProperty("action_type")
    private String actionType;

    @NotNull
    @JsonProperty("item_id")
    private String itemId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("details")
    private java.util.Map<String, Object> details;

    public static class SubmitActionRequest extends StreamRequest<SubmitActionResponse> {
      @Override
      protected Call<SubmitActionResponse> generateCall(Client client) {
        return client.create(ModerationService.class).submitAction(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class SubmitActionResponse extends StreamResponseObject {}

  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class GetUserReportRequest extends StreamRequest<GetUserReportResponse> {
    @NotNull private String userId;
    @Nullable private Boolean createUserIfNotExists;
    @Nullable private Boolean includeUserBlocks;
    @Nullable private Boolean includeUserMutes;

    private GetUserReportRequest(
        @NotNull String userId,
        @Nullable Boolean createUserIfNotExists,
        @Nullable Boolean includeUserBlocks,
        @Nullable Boolean includeUserMutes) {
      this.userId = userId;
      this.createUserIfNotExists = createUserIfNotExists;
      this.includeUserBlocks = includeUserBlocks;
      this.includeUserMutes = includeUserMutes;
    }

    @Override
    protected Call<GetUserReportResponse> generateCall(Client client) {
      return client
          .create(ModerationService.class)
          .getUserReport(
              this.userId,
              this.createUserIfNotExists,
              this.includeUserBlocks,
              this.includeUserMutes);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class GetUserReportResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("user")
    private User user;

    @Nullable
    @JsonProperty("flags")
    private List<Flag> flags;

    @Nullable
    @JsonProperty("bans")
    private List<User.Ban> bans;

    @Nullable
    @JsonProperty("mutes")
    private List<User.Mute> mutes;
  }

  @Builder(
      builderClassName = "BlockRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class BlockRequestData {
    @NotNull
    @JsonProperty("target_ids")
    private List<String> targetIds;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    public static class BlockRequest extends StreamRequest<BlockResponse> {
      @Override
      protected Call<BlockResponse> generateCall(Client client) {
        return client.create(ModerationService.class).block(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class BlockResponse extends StreamResponseObject {}

  @Builder(
      builderClassName = "BanRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class BanRequestData {
    @NotNull
    @JsonProperty("target_ids")
    private List<String> targetIds;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("timeout")
    private Integer timeout;

    @Nullable
    @JsonProperty("reason")
    private String reason;

    @Nullable
    @JsonProperty("shadow")
    private Boolean shadow;

    @Nullable
    @JsonProperty("ip_ban")
    private Boolean ipBan;

    public static class BanRequest extends StreamRequest<BanResponse> {
      @Override
      protected Call<BanResponse> generateCall(Client client) {
        return client.create(ModerationService.class).ban(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class BanResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("bans")
    private List<User.Ban> bans;
  }

  @Builder(
      builderClassName = "UnbanRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class UnbanRequestData {
    @NotNull
    @JsonProperty("target_ids")
    private List<String> targetIds;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("shadow")
    private Boolean shadow;

    public static class UnbanRequest extends StreamRequest<UnbanResponse> {
      @Override
      protected Call<UnbanResponse> generateCall(Client client) {
        return client.create(ModerationService.class).unban(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UnbanResponse extends StreamResponseObject {}

  @Builder(
      builderClassName = "AppealRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class AppealRequestData {
    @NotNull
    @JsonProperty("ban_id")
    private String banId;

    @NotNull
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("reason")
    private String reason;

    public static class AppealRequest extends StreamRequest<AppealResponse> {
      @Override
      protected Call<AppealResponse> generateCall(Client client) {
        return client.create(ModerationService.class).appeal(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class AppealResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("appeal")
    private Appeal appeal;
  }

  @Data
  @NoArgsConstructor
  public static class Appeal {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("ban_id")
    private String banId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("reason")
    private String reason;

    @Nullable
    @JsonProperty("status")
    private String status;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class GetAppealRequest extends StreamRequest<GetAppealResponse> {
    @NotNull private String id;

    @Override
    protected Call<GetAppealResponse> generateCall(Client client) {
      return client.create(ModerationService.class).getAppeal(this.id);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class GetAppealResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("appeal")
    private Appeal appeal;
  }

  @Builder(
      builderClassName = "QueryAppealsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class QueryAppealsRequestData {
    @Nullable
    @JsonProperty("filter")
    private java.util.Map<String, Object> filter;

    @Nullable
    @JsonProperty("sort")
    private List<Sort> sort;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    public static class QueryAppealsRequest extends StreamRequest<QueryAppealsResponse> {
      @Override
      protected Call<QueryAppealsResponse> generateCall(Client client) {
        return client.create(ModerationService.class).queryAppeals(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryAppealsResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("appeals")
    private List<Appeal> appeals;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;
  }

  @Builder(
      builderClassName = "QueryModerationFlagsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class QueryModerationFlagsRequestData {
    @Nullable
    @JsonProperty("filter")
    private java.util.Map<String, Object> filter;

    @Nullable
    @JsonProperty("sort")
    private List<Sort> sort;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    public static class QueryModerationFlagsRequest
        extends StreamRequest<QueryModerationFlagsResponse> {
      @Override
      protected Call<QueryModerationFlagsResponse> generateCall(Client client) {
        return client.create(ModerationService.class).queryModerationFlags(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryModerationFlagsResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("flags")
    private List<Flag> flags;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;
  }

  @Builder(
      builderClassName = "QueryModerationLogsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class QueryModerationLogsRequestData {
    @Nullable
    @JsonProperty("filter")
    private java.util.Map<String, Object> filter;

    @Nullable
    @JsonProperty("sort")
    private List<Sort> sort;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    public static class QueryModerationLogsRequest
        extends StreamRequest<QueryModerationLogsResponse> {
      @Override
      protected Call<QueryModerationLogsResponse> generateCall(Client client) {
        return client.create(ModerationService.class).queryModerationLogs(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryModerationLogsResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("logs")
    private List<ModerationLog> logs;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;
  }

  @Data
  @NoArgsConstructor
  public static class ModerationLog {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("action")
    private String action;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;
  }

  @Builder(
      builderClassName = "QueryUsageStatsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class QueryUsageStatsRequestData {
    @Nullable
    @JsonProperty("filter")
    private java.util.Map<String, Object> filter;

    @Nullable
    @JsonProperty("start_time")
    private String startTime;

    @Nullable
    @JsonProperty("end_time")
    private String endTime;

    public static class QueryUsageStatsRequest extends StreamRequest<QueryUsageStatsResponse> {
      @Override
      protected Call<QueryUsageStatsResponse> generateCall(Client client) {
        return client.create(ModerationService.class).queryUsageStats(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryUsageStatsResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("stats")
    private java.util.Map<String, Object> stats;
  }

  @Builder(
      builderClassName = "GetModerationAnalyticsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class GetModerationAnalyticsRequestData {
    @Nullable
    @JsonProperty("start_time")
    private String startTime;

    @Nullable
    @JsonProperty("end_time")
    private String endTime;

    @Nullable
    @JsonProperty("group_by")
    private List<String> groupBy;

    public static class GetModerationAnalyticsRequest
        extends StreamRequest<GetModerationAnalyticsResponse> {
      @Override
      protected Call<GetModerationAnalyticsResponse> generateCall(Client client) {
        return client.create(ModerationService.class).getModerationAnalytics(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class GetModerationAnalyticsResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("analytics")
    private java.util.Map<String, Object> analytics;
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class GetReviewQueueItemRequest extends StreamRequest<GetReviewQueueItemResponse> {
    @NotNull private String id;

    @Override
    protected Call<GetReviewQueueItemResponse> generateCall(Client client) {
      return client.create(ModerationService.class).getReviewQueueItem(this.id);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class GetReviewQueueItemResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("item")
    private ReviewQueueItem item;
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class GetModeratorStatsRequest extends StreamRequest<ModeratorStatsResponse> {
    @Nullable private String userId;
    @Nullable private String startTime;
    @Nullable private String endTime;

    private GetModeratorStatsRequest(
        @Nullable String userId, @Nullable String startTime, @Nullable String endTime) {
      this.userId = userId;
      this.startTime = startTime;
      this.endTime = endTime;
    }

    @Override
    protected Call<ModeratorStatsResponse> generateCall(Client client) {
      return client
          .create(ModerationService.class)
          .getModeratorStats(this.userId, this.startTime, this.endTime);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ModeratorStatsResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("stats")
    private java.util.Map<String, Object> stats;
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class GetQueueStatsRequest extends StreamRequest<QueueStatsResponse> {
    @Nullable private String startTime;
    @Nullable private String endTime;

    private GetQueueStatsRequest(@Nullable String startTime, @Nullable String endTime) {
      this.startTime = startTime;
      this.endTime = endTime;
    }

    @Override
    protected Call<QueueStatsResponse> generateCall(Client client) {
      return client.create(ModerationService.class).getQueueStats(this.startTime, this.endTime);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueueStatsResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("stats")
    private java.util.Map<String, Object> stats;
  }

  @Builder(
      builderClassName = "ExportModerationLogsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class ExportModerationLogsRequestData {
    @Nullable
    @JsonProperty("filter")
    private java.util.Map<String, Object> filter;

    @Nullable
    @JsonProperty("format")
    private String format;

    public static class ExportModerationLogsRequest
        extends StreamRequest<ExportModerationLogsResponse> {
      @Override
      protected Call<ExportModerationLogsResponse> generateCall(Client client) {
        return client.create(ModerationService.class).exportModerationLogs(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ExportModerationLogsResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("task_id")
    private String taskId;
  }

  @Builder(
      builderClassName = "BulkImageModerationRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class BulkImageModerationRequestData {
    @NotNull
    @JsonProperty("image_urls")
    private List<String> imageUrls;

    @NotNull
    @JsonProperty("config_key")
    private String configKey;

    public static class BulkImageModerationRequest
        extends StreamRequest<BulkImageModerationResponse> {
      @Override
      protected Call<BulkImageModerationResponse> generateCall(Client client) {
        return client.create(ModerationService.class).bulkImageModeration(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class BulkImageModerationResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("results")
    private List<CheckResponse> results;
  }

  @Builder(
      builderClassName = "BulkSubmitActionRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class BulkSubmitActionRequestData {
    @NotNull
    @JsonProperty("actions")
    private List<BulkActionItem> actions;

    public static class BulkSubmitActionRequest extends StreamRequest<BulkSubmitActionResponse> {
      @Override
      protected Call<BulkSubmitActionResponse> generateCall(Client client) {
        return client.create(ModerationService.class).bulkSubmitAction(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @Builder
  @AllArgsConstructor
  public static class BulkActionItem {
    @NotNull
    @JsonProperty("action_type")
    private String actionType;

    @NotNull
    @JsonProperty("item_id")
    private String itemId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("details")
    private java.util.Map<String, Object> details;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class BulkSubmitActionResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("results")
    private List<SubmitActionResponse> results;
  }

  @Builder(
      builderClassName = "UpsertTemplateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class UpsertTemplateRequestData {
    @NotNull
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("team")
    private String team;

    @Nullable
    @JsonProperty("template")
    private java.util.Map<String, Object> template;

    public static class UpsertTemplateRequest extends StreamRequest<UpsertTemplateResponse> {
      @Override
      protected Call<UpsertTemplateResponse> generateCall(Client client) {
        return client.create(ModerationService.class).upsertTemplate(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UpsertTemplateResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("template")
    private FeedModerationTemplate template;
  }

  @Data
  @NoArgsConstructor
  public static class FeedModerationTemplate {
    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("team")
    private String team;

    @Nullable
    @JsonProperty("template")
    private java.util.Map<String, Object> template;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class QueryFeedModerationTemplatesRequest
      extends StreamRequest<QueryFeedModerationTemplatesResponse> {
    @Nullable private String filter;
    @Nullable private Integer limit;
    @Nullable private Integer offset;

    private QueryFeedModerationTemplatesRequest(
        @Nullable String filter, @Nullable Integer limit, @Nullable Integer offset) {
      this.filter = filter;
      this.limit = limit;
      this.offset = offset;
    }

    @Override
    protected Call<QueryFeedModerationTemplatesResponse> generateCall(Client client) {
      return client
          .create(ModerationService.class)
          .queryFeedModerationTemplates(this.filter, this.limit, this.offset);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryFeedModerationTemplatesResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("templates")
    private List<FeedModerationTemplate> templates;
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class DeleteModerationTemplateRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String name;
    @Nullable private String team;

    private DeleteModerationTemplateRequest(@NotNull String name, @Nullable String team) {
      this.name = name;
      this.team = team;
    }

    @Override
    protected Call<StreamResponseObject> generateCall(Client client) {
      return client.create(ModerationService.class).deleteModerationTemplate(this.name, this.team);
    }
  }

  @Builder(
      builderClassName = "UpsertModerationRuleRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class UpsertModerationRuleRequestData {
    @NotNull
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("description")
    private String description;

    @Nullable
    @JsonProperty("enabled")
    private Boolean enabled;

    @Nullable
    @JsonProperty("conditions")
    private java.util.Map<String, Object> conditions;

    @Nullable
    @JsonProperty("actions")
    private List<java.util.Map<String, Object>> actions;

    public static class UpsertModerationRuleRequest
        extends StreamRequest<UpsertModerationRuleResponse> {
      @Override
      protected Call<UpsertModerationRuleResponse> generateCall(Client client) {
        return client.create(ModerationService.class).upsertModerationRule(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UpsertModerationRuleResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("rule")
    private ModerationRule rule;
  }

  @Data
  @NoArgsConstructor
  public static class ModerationRule {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("description")
    private String description;

    @Nullable
    @JsonProperty("enabled")
    private Boolean enabled;

    @Nullable
    @JsonProperty("conditions")
    private java.util.Map<String, Object> conditions;

    @Nullable
    @JsonProperty("actions")
    private List<java.util.Map<String, Object>> actions;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;
  }

  @Builder(
      builderClassName = "QueryModerationRulesRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class QueryModerationRulesRequestData {
    @Nullable
    @JsonProperty("filter")
    private java.util.Map<String, Object> filter;

    @Nullable
    @JsonProperty("sort")
    private List<Sort> sort;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    public static class QueryModerationRulesRequest
        extends StreamRequest<QueryModerationRulesResponse> {
      @Override
      protected Call<QueryModerationRulesResponse> generateCall(Client client) {
        return client.create(ModerationService.class).queryModerationRules(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryModerationRulesResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("rules")
    private List<ModerationRule> rules;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class GetModerationRuleRequest extends StreamRequest<GetModerationRuleResponse> {
    @NotNull private String id;

    @Override
    protected Call<GetModerationRuleResponse> generateCall(Client client) {
      return client.create(ModerationService.class).getModerationRule(this.id);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class GetModerationRuleResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("rule")
    private ModerationRule rule;
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class DeleteModerationRuleRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String id;

    @Override
    protected Call<StreamResponseObject> generateCall(Client client) {
      return client.create(ModerationService.class).deleteModerationRule(this.id);
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

  /**
   * Creates a flag request
   *
   * @return the created request
   */
  @NotNull
  public static FlagRequestData.FlagRequest flag() {
    return new FlagRequestData.FlagRequest();
  }

  /**
   * Creates a mute request
   *
   * @return the created request
   */
  @NotNull
  public static MuteRequestData.MuteRequest mute() {
    return new MuteRequestData.MuteRequest();
  }

  /**
   * Creates an unmute request
   *
   * @return the created request
   */
  @NotNull
  public static UnmuteRequestData.UnmuteRequest unmute() {
    return new UnmuteRequestData.UnmuteRequest();
  }

  /**
   * Creates a check request
   *
   * @return the created request
   */
  @NotNull
  public static CheckRequestData.CheckRequest check() {
    return new CheckRequestData.CheckRequest();
  }

  /**
   * Creates a custom check request
   *
   * @return the created request
   */
  @NotNull
  public static CustomCheckRequestData.CustomCheckRequest customCheck() {
    return new CustomCheckRequestData.CustomCheckRequest();
  }

  /**
   * Creates a query review queue request
   *
   * @return the created request
   */
  @NotNull
  public static QueryReviewQueueRequestData.QueryReviewQueueRequest queryReviewQueue() {
    return new QueryReviewQueueRequestData.QueryReviewQueueRequest();
  }

  /**
   * Creates a query configs request
   *
   * @return the created request
   */
  @NotNull
  public static QueryConfigsRequestData.QueryConfigsRequest queryConfigs() {
    return new QueryConfigsRequestData.QueryConfigsRequest();
  }

  /**
   * Creates a submit action request
   *
   * @return the created request
   */
  @NotNull
  public static SubmitActionRequestData.SubmitActionRequest submitAction() {
    return new SubmitActionRequestData.SubmitActionRequest();
  }

  /**
   * Creates a get user report request
   *
   * @param userId the user ID to get report for
   * @return the created request
   */
  @NotNull
  public static GetUserReportRequest getUserReport(@NotNull String userId) {
    return new GetUserReportRequest(userId, null, null, null);
  }

  /**
   * Creates a get user report request with options
   *
   * @param userId the user ID to get report for
   * @param createUserIfNotExists whether to create user if not exists
   * @param includeUserBlocks whether to include user blocks
   * @param includeUserMutes whether to include user mutes
   * @return the created request
   */
  @NotNull
  public static GetUserReportRequest getUserReport(
      @NotNull String userId,
      @Nullable Boolean createUserIfNotExists,
      @Nullable Boolean includeUserBlocks,
      @Nullable Boolean includeUserMutes) {
    return new GetUserReportRequest(
        userId, createUserIfNotExists, includeUserBlocks, includeUserMutes);
  }

  /**
   * Creates a block request
   *
   * @return the created request
   */
  @NotNull
  public static BlockRequestData.BlockRequest block() {
    return new BlockRequestData.BlockRequest();
  }

  /**
   * Creates a ban request
   *
   * @return the created request
   */
  @NotNull
  public static BanRequestData.BanRequest ban() {
    return new BanRequestData.BanRequest();
  }

  /**
   * Creates an unban request
   *
   * @return the created request
   */
  @NotNull
  public static UnbanRequestData.UnbanRequest unban() {
    return new UnbanRequestData.UnbanRequest();
  }

  /**
   * Creates an appeal request
   *
   * @return the created request
   */
  @NotNull
  public static AppealRequestData.AppealRequest appeal() {
    return new AppealRequestData.AppealRequest();
  }

  /**
   * Creates a get appeal request
   *
   * @param id the appeal ID
   * @return the created request
   */
  @NotNull
  public static GetAppealRequest getAppeal(@NotNull String id) {
    return new GetAppealRequest(id);
  }

  /**
   * Creates a query appeals request
   *
   * @return the created request
   */
  @NotNull
  public static QueryAppealsRequestData.QueryAppealsRequest queryAppeals() {
    return new QueryAppealsRequestData.QueryAppealsRequest();
  }

  /**
   * Creates a query moderation flags request
   *
   * @return the created request
   */
  @NotNull
  public static QueryModerationFlagsRequestData.QueryModerationFlagsRequest queryModerationFlags() {
    return new QueryModerationFlagsRequestData.QueryModerationFlagsRequest();
  }

  /**
   * Creates a query moderation logs request
   *
   * @return the created request
   */
  @NotNull
  public static QueryModerationLogsRequestData.QueryModerationLogsRequest queryModerationLogs() {
    return new QueryModerationLogsRequestData.QueryModerationLogsRequest();
  }

  /**
   * Creates a query usage stats request
   *
   * @return the created request
   */
  @NotNull
  public static QueryUsageStatsRequestData.QueryUsageStatsRequest queryUsageStats() {
    return new QueryUsageStatsRequestData.QueryUsageStatsRequest();
  }

  /**
   * Creates a get moderation analytics request
   *
   * @return the created request
   */
  @NotNull
  public static GetModerationAnalyticsRequestData.GetModerationAnalyticsRequest
      getModerationAnalytics() {
    return new GetModerationAnalyticsRequestData.GetModerationAnalyticsRequest();
  }

  /**
   * Creates a get review queue item request
   *
   * @param id the review queue item ID
   * @return the created request
   */
  @NotNull
  public static GetReviewQueueItemRequest getReviewQueueItem(@NotNull String id) {
    return new GetReviewQueueItemRequest(id);
  }

  /**
   * Creates a get moderator stats request
   *
   * @return the created request
   */
  @NotNull
  public static GetModeratorStatsRequest getModeratorStats() {
    return new GetModeratorStatsRequest(null, null, null);
  }

  /**
   * Creates a get moderator stats request with parameters
   *
   * @param userId the user ID
   * @param startTime the start time
   * @param endTime the end time
   * @return the created request
   */
  @NotNull
  public static GetModeratorStatsRequest getModeratorStats(
      @Nullable String userId, @Nullable String startTime, @Nullable String endTime) {
    return new GetModeratorStatsRequest(userId, startTime, endTime);
  }

  /**
   * Creates a get queue stats request
   *
   * @return the created request
   */
  @NotNull
  public static GetQueueStatsRequest getQueueStats() {
    return new GetQueueStatsRequest(null, null);
  }

  /**
   * Creates a get queue stats request with parameters
   *
   * @param startTime the start time
   * @param endTime the end time
   * @return the created request
   */
  @NotNull
  public static GetQueueStatsRequest getQueueStats(
      @Nullable String startTime, @Nullable String endTime) {
    return new GetQueueStatsRequest(startTime, endTime);
  }

  /**
   * Creates an export moderation logs request
   *
   * @return the created request
   */
  @NotNull
  public static ExportModerationLogsRequestData.ExportModerationLogsRequest exportModerationLogs() {
    return new ExportModerationLogsRequestData.ExportModerationLogsRequest();
  }

  /**
   * Creates a bulk image moderation request
   *
   * @return the created request
   */
  @NotNull
  public static BulkImageModerationRequestData.BulkImageModerationRequest bulkImageModeration() {
    return new BulkImageModerationRequestData.BulkImageModerationRequest();
  }

  /**
   * Creates a bulk submit action request
   *
   * @return the created request
   */
  @NotNull
  public static BulkSubmitActionRequestData.BulkSubmitActionRequest bulkSubmitAction() {
    return new BulkSubmitActionRequestData.BulkSubmitActionRequest();
  }

  /**
   * Creates an upsert template request
   *
   * @return the created request
   */
  @NotNull
  public static UpsertTemplateRequestData.UpsertTemplateRequest upsertTemplate() {
    return new UpsertTemplateRequestData.UpsertTemplateRequest();
  }

  /**
   * Creates a query feed moderation templates request
   *
   * @return the created request
   */
  @NotNull
  public static QueryFeedModerationTemplatesRequest queryFeedModerationTemplates() {
    return new QueryFeedModerationTemplatesRequest(null, null, null);
  }

  /**
   * Creates a query feed moderation templates request with parameters
   *
   * @param filter the filter
   * @param limit the limit
   * @param offset the offset
   * @return the created request
   */
  @NotNull
  public static QueryFeedModerationTemplatesRequest queryFeedModerationTemplates(
      @Nullable String filter, @Nullable Integer limit, @Nullable Integer offset) {
    return new QueryFeedModerationTemplatesRequest(filter, limit, offset);
  }

  /**
   * Creates a delete moderation template request
   *
   * @param name the template name
   * @return the created request
   */
  @NotNull
  public static DeleteModerationTemplateRequest deleteModerationTemplate(@NotNull String name) {
    return new DeleteModerationTemplateRequest(name, null);
  }

  /**
   * Creates a delete moderation template request with team
   *
   * @param name the template name
   * @param team the team name
   * @return the created request
   */
  @NotNull
  public static DeleteModerationTemplateRequest deleteModerationTemplate(
      @NotNull String name, @Nullable String team) {
    return new DeleteModerationTemplateRequest(name, team);
  }

  /**
   * Creates an upsert moderation rule request
   *
   * @return the created request
   */
  @NotNull
  public static UpsertModerationRuleRequestData.UpsertModerationRuleRequest upsertModerationRule() {
    return new UpsertModerationRuleRequestData.UpsertModerationRuleRequest();
  }

  /**
   * Creates a query moderation rules request
   *
   * @return the created request
   */
  @NotNull
  public static QueryModerationRulesRequestData.QueryModerationRulesRequest queryModerationRules() {
    return new QueryModerationRulesRequestData.QueryModerationRulesRequest();
  }

  /**
   * Creates a get moderation rule request
   *
   * @param id the moderation rule ID
   * @return the created request
   */
  @NotNull
  public static GetModerationRuleRequest getModerationRule(@NotNull String id) {
    return new GetModerationRuleRequest(id);
  }

  /**
   * Creates a delete moderation rule request
   *
   * @param id the moderation rule ID
   * @return the created request
   */
  @NotNull
  public static DeleteModerationRuleRequest deleteModerationRule(@NotNull String id) {
    return new DeleteModerationRuleRequest(id);
  }
}
