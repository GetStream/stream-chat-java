package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Moderation.*;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.*;

public interface ModerationService {
  @GET("api/v2/moderation/config/{key}")
  Call<ConfigGetResponse> getConfig(@NotNull @Path("key") String key);

  @DELETE("api/v2/moderation/config/{key}")
  Call<StreamResponseObject> deleteConfig(@NotNull @Path("key") String key);

  @POST("api/v2/moderation/config")
  Call<UpsertConfigResponse> upsertConfig(@Nullable @Body UpsertConfigRequestData upsertConfig);

  // Phase 1: Core Moderation Actions
  @POST("api/v2/moderation/flag")
  Call<FlagResponse> flag(@NotNull @Body FlagRequestData flagRequestData);

  @POST("api/v2/moderation/mute")
  Call<MuteResponse> mute(@NotNull @Body MuteRequestData muteRequestData);

  @POST("api/v2/moderation/unmute")
  Call<UnmuteResponse> unmute(@NotNull @Body UnmuteRequestData unmuteRequestData);

  @POST("api/v2/moderation/check")
  Call<CheckResponse> check(@NotNull @Body CheckRequestData checkRequestData);

  @POST("api/v2/moderation/custom_check")
  Call<CustomCheckResponse> customCheck(
      @NotNull @Body CustomCheckRequestData customCheckRequestData);

  @POST("api/v2/moderation/review_queue")
  Call<QueryReviewQueueResponse> queryReviewQueue(
      @NotNull @Body QueryReviewQueueRequestData queryReviewQueueRequestData);

  @POST("api/v2/moderation/configs")
  Call<QueryConfigsResponse> queryConfigs(
      @NotNull @Body QueryConfigsRequestData queryConfigsRequestData);

  @POST("api/v2/moderation/submit_action")
  Call<SubmitActionResponse> submitAction(
      @NotNull @Body SubmitActionRequestData submitActionRequestData);

  @GET("api/v2/moderation/user_report")
  Call<GetUserReportResponse> getUserReport(
      @NotNull @Query("user_id") String userId,
      @Nullable @Query("create_user_if_not_exists") Boolean createUserIfNotExists,
      @Nullable @Query("include_user_blocks") Boolean includeUserBlocks,
      @Nullable @Query("include_user_mutes") Boolean includeUserMutes);

  // Phase 2: Additional V2 Endpoints
  @POST("api/v2/moderation/block")
  Call<BlockResponse> block(@NotNull @Body BlockRequestData blockRequestData);

  @POST("api/v2/moderation/ban")
  Call<BanResponse> ban(@NotNull @Body BanRequestData banRequestData);

  @POST("api/v2/moderation/unban")
  Call<UnbanResponse> unban(@NotNull @Body UnbanRequestData unbanRequestData);

  @POST("api/v2/moderation/appeal")
  Call<AppealResponse> appeal(@NotNull @Body AppealRequestData appealRequestData);

  @GET("api/v2/moderation/appeal/{id}")
  Call<GetAppealResponse> getAppeal(@NotNull @Path("id") String id);

  @POST("api/v2/moderation/appeals")
  Call<QueryAppealsResponse> queryAppeals(
      @NotNull @Body QueryAppealsRequestData queryAppealsRequestData);

  @POST("api/v2/moderation/flags")
  Call<QueryModerationFlagsResponse> queryModerationFlags(
      @NotNull @Body QueryModerationFlagsRequestData queryModerationFlagsRequestData);

  @POST("api/v2/moderation/logs")
  Call<QueryModerationLogsResponse> queryModerationLogs(
      @NotNull @Body QueryModerationLogsRequestData queryModerationLogsRequestData);

  @POST("api/v2/moderation/usage_stats")
  Call<QueryUsageStatsResponse> queryUsageStats(
      @NotNull @Body QueryUsageStatsRequestData queryUsageStatsRequestData);

  @POST("api/v2/moderation/analytics")
  Call<GetModerationAnalyticsResponse> getModerationAnalytics(
      @NotNull @Body GetModerationAnalyticsRequestData getModerationAnalyticsRequestData);

  @GET("api/v2/moderation/review_queue/{id}")
  Call<GetReviewQueueItemResponse> getReviewQueueItem(@NotNull @Path("id") String id);

  @GET("api/v2/moderation/moderator_stats")
  Call<ModeratorStatsResponse> getModeratorStats(
      @Nullable @Query("user_id") String userId,
      @Nullable @Query("start_time") String startTime,
      @Nullable @Query("end_time") String endTime);

  @GET("api/v2/moderation/queue_stats")
  Call<QueueStatsResponse> getQueueStats(
      @Nullable @Query("start_time") String startTime, @Nullable @Query("end_time") String endTime);

  @POST("api/v2/moderation/logs/export")
  Call<ExportModerationLogsResponse> exportModerationLogs(
      @NotNull @Body ExportModerationLogsRequestData exportModerationLogsRequestData);

  @POST("api/v2/moderation/bulk_image_moderation")
  Call<BulkImageModerationResponse> bulkImageModeration(
      @NotNull @Body BulkImageModerationRequestData bulkImageModerationRequestData);

  @POST("api/v2/moderation/bulk_submit_action")
  Call<BulkSubmitActionResponse> bulkSubmitAction(
      @NotNull @Body BulkSubmitActionRequestData bulkSubmitActionRequestData);

  // Phase 3: Feeds Moderation & Rules
  @POST("api/v2/moderation/feeds_moderation_template")
  Call<UpsertTemplateResponse> upsertTemplate(
      @NotNull @Body UpsertTemplateRequestData upsertTemplateRequestData);

  @GET("api/v2/moderation/feeds_moderation_template")
  Call<QueryFeedModerationTemplatesResponse> queryFeedModerationTemplates(
      @Nullable @Query("filter") String filter,
      @Nullable @Query("limit") Integer limit,
      @Nullable @Query("offset") Integer offset);

  @DELETE("api/v2/moderation/feeds_moderation_template")
  Call<StreamResponseObject> deleteModerationTemplate(
      @NotNull @Query("name") String name, @Nullable @Query("team") String team);

  @POST("api/v2/moderation/moderation_rule")
  Call<UpsertModerationRuleResponse> upsertModerationRule(
      @NotNull @Body UpsertModerationRuleRequestData upsertModerationRuleRequestData);

  @POST("api/v2/moderation/moderation_rules")
  Call<QueryModerationRulesResponse> queryModerationRules(
      @NotNull @Body QueryModerationRulesRequestData queryModerationRulesRequestData);

  @GET("api/v2/moderation/moderation_rule/{id}")
  Call<GetModerationRuleResponse> getModerationRule(@NotNull @Path("id") String id);

  @DELETE("api/v2/moderation/moderation_rule/{id}")
  Call<StreamResponseObject> deleteModerationRule(@NotNull @Path("id") String id);
}
