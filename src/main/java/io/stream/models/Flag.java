package io.stream.models;

import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Flag.FlagCreateRequestData.FlagCreateRequest;
import io.stream.models.Flag.FlagDeleteRequestData.FlagDeleteRequest;
import io.stream.models.User.UserRequestObject;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.FlagService;
import io.stream.services.framework.StreamServiceGenerator;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
}
