package io.getstream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.models.ChannelType.Threshold;
import io.getstream.models.Flag.FlagCreateRequestData.FlagCreateRequest;
import io.getstream.models.Flag.FlagDeleteRequestData.FlagDeleteRequest;
import io.getstream.models.Flag.FlagMessageQueryRequestData.FlagMessageQueryRequest;
import io.getstream.models.Message.Moderation;
import io.getstream.models.User.UserRequestObject;
import io.getstream.models.framework.StreamRequest;
import io.getstream.models.framework.StreamResponseObject;
import io.getstream.services.FlagService;
import io.getstream.services.framework.StreamServiceGenerator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
      protected Call<FlagCreateResponse> generateCall() {
        return StreamServiceGenerator.createService(FlagService.class).create(this.internalBuild());
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
      protected Call<FlagDeleteResponse> generateCall() {
        return StreamServiceGenerator.createService(FlagService.class).delete(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "FlagMessageQueryRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class FlagMessageQueryRequestData {
    @Nullable
    @JsonProperty("filterConditions")
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
      protected Call<FlagMessageQueryResponse> generateCall() {
        return StreamServiceGenerator.createService(FlagService.class)
            .messageQuery(this.internalBuild());
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
}
