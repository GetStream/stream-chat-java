package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.ChannelType.Threshold;
import io.getstream.chat.java.models.Flag.FlagCreateRequestData.FlagCreateRequest;
import io.getstream.chat.java.models.Flag.FlagDeleteRequestData.FlagDeleteRequest;
import io.getstream.chat.java.models.Flag.FlagMessageQueryRequestData.FlagMessageQueryRequest;
import io.getstream.chat.java.models.Flag.QueryFlagReportsRequestData.QueryFlagReportsRequest;
import io.getstream.chat.java.models.Flag.ReviewFlagReportRequestData.ReviewFlagReportRequest;
import io.getstream.chat.java.models.Message.Moderation;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.FlagService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Flag {
  @NotNull
  @JsonProperty("created_by_automod")
  private Boolean createdByAutomod;

  @Nullable
  @JsonProperty("user")
  private User user;

  @Nullable
  @JsonProperty("target_message_id")
  private String targetMessageId;

  @Nullable
  @JsonProperty("target_user")
  private User targetUser;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @Nullable
  @JsonProperty("updatedAt")
  private Date updated_at;

  @Nullable
  @JsonProperty("reviewed_at")
  private Date reviewedAt;

  @Nullable
  @JsonProperty("approved_at")
  private Date approvedAt;

  @Nullable
  @JsonProperty("rejected_at")
  private Date rejectedAt;

  @Data
  @NoArgsConstructor
  public static class MessageFlag {
    @NotNull
    @JsonProperty("created_by_automod")
    private Boolean createdByAutomod;

    @NotNull
    @JsonProperty("moderation_result")
    private MessageModerationResult moderationResult;

    @Nullable
    @JsonProperty("user")
    private User user;

    @Nullable
    @JsonProperty("message")
    private Message message;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updatedAt")
    private Date updated_at;

    @Nullable
    @JsonProperty("reviewed_at")
    private Date reviewedAt;

    @Nullable
    @JsonProperty("reviewed_by")
    private User reviewedBy;

    @Nullable
    @JsonProperty("approved_at")
    private Date approvedAt;

    @Nullable
    @JsonProperty("rejected_at")
    private Date rejectedAt;
  }

  @Data
  @NoArgsConstructor
  public static class FlagReport {
    @NotNull
    @JsonProperty("id")
    private String Id;

    @Nullable
    @JsonProperty("message")
    private Message message;

    @Nullable
    @JsonProperty("flags_count")
    private Integer flagsCount;

    @Nullable
    @JsonProperty("message_user_id")
    private String messageUserId;

    @Nullable
    @JsonProperty("channel_cid")
    private String channelCid;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ExtendedFlagReport extends FlagReport {
    @NotNull
    @JsonProperty("review_result")
    private String reviewResult;

    @Nullable
    @JsonProperty("review_details")
    private Map<String, Object> reviewDetails;
  }

  @Data
  @NoArgsConstructor
  public static class MessageModerationResult {
    @Nullable
    @JsonProperty("message_id")
    private String message_id;

    @Nullable
    @JsonProperty("action")
    private String action;

    @Nullable
    @JsonProperty("moderated_by")
    private String moderatedBy;

    @Nullable
    @JsonProperty("blocked_word")
    private String blockedWord;

    @Nullable
    @JsonProperty("blocklist_name")
    private String blocklistName;

    @Nullable
    @JsonProperty("moderation_thresholds")
    private Thresholds moderationThresholds;

    @Nullable
    @JsonProperty("ai_moderation_response")
    private Moderation aiModerationResponse;

    @Nullable
    @JsonProperty("user_karma")
    private Integer userKarma;

    @Nullable
    @JsonProperty("user_bad_karma")
    private Boolean userBadKarma;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;
  }

  @Data
  @NoArgsConstructor
  public static class Thresholds {
    private Threshold explicit;

    private Threshold spam;

    private Threshold toxic;
  }

  @Builder(
      builderClassName = "FlagCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class FlagCreateRequestData {
    @Nullable
    @JsonProperty("target_message_id")
    private String targetMessageId;

    @Nullable
    @JsonProperty("target_user_id")
    private String targetUserId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class FlagCreateRequest extends StreamRequest<FlagCreateResponse> {
      @Override
      protected Call<FlagCreateResponse> generateCall(Client client) {
        return client.create(FlagService.class).create(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "FlagDeleteRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class FlagDeleteRequestData {
    @Nullable
    @JsonProperty("target_message_id")
    private String targetMessageId;

    @Nullable
    @JsonProperty("target_user_id")
    private String targetUserId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class FlagDeleteRequest extends StreamRequest<FlagDeleteResponse> {
      @Override
      protected Call<FlagDeleteResponse> generateCall(Client client) {
        return client.create(FlagService.class).delete(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "FlagMessageQueryRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class FlagMessageQueryRequestData {
    @Nullable
    @JsonProperty("filter_conditions")
    @Singular
    private Map<String, Object> filterConditions;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class FlagMessageQueryRequest extends StreamRequest<FlagMessageQueryResponse> {
      @Override
      protected Call<FlagMessageQueryResponse> generateCall(Client client) {
        return client.create(FlagService.class).messageQuery(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "QueryFlagReportsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class QueryFlagReportsRequestData {
    @Nullable
    @JsonProperty("filter_conditions")
    @Singular
    private Map<String, Object> filterConditions;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class QueryFlagReportsRequest extends StreamRequest<QueryFlagReportsResponse> {
      @Override
      protected Call<QueryFlagReportsResponse> generateCall(Client client) {
        return client.create(FlagService.class).queryFlagReports(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ReviewFlagReportRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ReviewFlagReportRequestData {
    @NotNull
    @JsonProperty("review_result")
    private String reviewResult;

    @Nullable
    @JsonProperty("review_details")
    private Map<String, Object> reviewDetails;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    public static class ReviewFlagReportRequest extends StreamRequest<ReviewFlagReportResponse> {
      @NotNull private String id;

      public ReviewFlagReportRequest(@NotNull String id) {
        this.id = id;
      }

      @Override
      protected Call<ReviewFlagReportResponse> generateCall(Client client) {
        return client.create(FlagService.class).reviewFlagReport(id, this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class FlagCreateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("flag")
    private Flag flag;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class FlagDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("flag")
    private Flag flag;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class FlagMessageQueryResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("flags")
    private List<MessageFlag> flags;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryFlagReportsResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("flag_reports")
    private List<FlagReport> flagReports;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ReviewFlagReportResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("flag_report")
    private ExtendedFlagReport flagReport;
  }

  /**
   * Creates a create request
   *
   * @return the created request
   */
  @NotNull
  public static FlagCreateRequest create() {
    return new FlagCreateRequest();
  }

  /**
   * Creates a delete request
   *
   * @return the created request
   */
  @NotNull
  public static FlagDeleteRequest delete() {
    return new FlagDeleteRequest();
  }

  /**
   * Creates a query messages request
   *
   * @return the created request
   */
  @NotNull
  public static FlagMessageQueryRequest queryMessages() {
    return new FlagMessageQueryRequest();
  }

  /**
   * Creates a query flag report request
   *
   * @return the created request
   */
  @NotNull
  public static QueryFlagReportsRequest queryFlagReports() {
    return new QueryFlagReportsRequest();
  }

  /**
   * Creates a review flag report request
   *
   * @param id the flag report id
   * @return the created request
   */
  @NotNull
  public static ReviewFlagReportRequest reviewFlagReport(String id) {
    return new ReviewFlagReportRequest(id);
  }
}
