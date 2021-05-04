package io.stream.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Reaction.ReactionSendRequestData.ReactionSendRequest;
import io.stream.models.User.UserRequestObject;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.ReactionService;
import io.stream.services.framework.StreamServiceGenerator;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import retrofit2.Call;

@Data
public class Reaction {
  @NotNull
  @JsonProperty("message_id")
  private String messageId;

  @NotNull
  @JsonProperty("user_id")
  private String userId;

  @NotNull
  @JsonProperty("type")
  private String type;

  @Nullable
  @JsonProperty("score")
  private Integer score;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

  @NotNull @JsonIgnore private Map<String, Object> additionalFields;

  public Reaction() {
    additionalFields = new HashMap<>();
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return this.additionalFields;
  }

  @JsonAnySetter
  public void setAdditionalField(String name, Object value) {
    this.additionalFields.put(name, value);
  }

  @Builder
  public static class ReactionRequestObject {
    @Nullable
    @JsonProperty("message_id")
    private String messageId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    @NotNull
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("score")
    private Integer score;

    @Singular @Nullable @JsonIgnore private Map<String, Object> additionalFields;

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
      this.additionalFields.put(name, value);
    }
  }

  @Builder(
      builderClassName = "ReactionSendRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ReactionSendRequestData {
    @NotNull
    @JsonProperty("reaction")
    private ReactionRequestObject reaction;

    @Nullable
    @JsonProperty("enforce_unique")
    private Boolean enforceUnique;

    @Nullable
    @JsonProperty("skip_push")
    private Boolean skipPush;

    public static class ReactionSendRequest extends StreamRequest<ReactionSendResponse> {
      @NotNull private String messageId;

      private ReactionSendRequest(@NotNull String messageId) {
        this.messageId = messageId;
      }

      @Override
      protected Call<ReactionSendResponse> generateCall() {
        return StreamServiceGenerator.createService(ReactionService.class)
            .send(messageId, this.internalBuild());
      }
    }
  }

  public static class ReactionDeleteRequest extends StreamRequest<ReactionDeleteResponse> {
    @NotNull private String messageId;

    @NotNull private String type;

    @Nullable private String userId;

    private ReactionDeleteRequest(@NotNull String messageId, @NotNull String type) {
      this.messageId = messageId;
      this.type = type;
    }

    public ReactionDeleteRequest userId(@NotNull String userId) {
      this.userId = userId;
      return this;
    }

    @Override
    protected Call<ReactionDeleteResponse> generateCall() {
      return StreamServiceGenerator.createService(ReactionService.class)
          .delete(messageId, type, userId);
    }
  }

  public static class ReactionListRequest extends StreamRequest<ReactionListResponse> {
    @NotNull private String messageId;

    private ReactionListRequest(@NotNull String messageId) {
      this.messageId = messageId;
    }

    @Override
    protected Call<ReactionListResponse> generateCall() {
      return StreamServiceGenerator.createService(ReactionService.class).list(messageId);
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ReactionSendResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;

    @NotNull
    @JsonProperty("reaction")
    private Reaction reaction;

    public ReactionSendResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ReactionDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;

    @NotNull
    @JsonProperty("reaction")
    private Reaction reaction;

    public ReactionDeleteResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ReactionListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("reactions")
    private List<Reaction> reactions;

    public ReactionListResponse() {}
  }

  /**
   * Creates a send request
   *
   * @param messageId the message id
   * @return the created request
   */
  @NotNull
  public static ReactionSendRequest send(@NotNull String messageId) {
    return new ReactionSendRequest(messageId);
  }

  /**
   * Creates a delete request
   *
   * @param messageId the message id
   * @param messageId the reaction type
   * @return the created request
   */
  @NotNull
  public static ReactionDeleteRequest delete(@NotNull String messageId, @NotNull String type) {
    return new ReactionDeleteRequest(messageId, type);
  }

  /**
   * Creates a list request
   *
   * @param messageId the message id
   * @return the created request
   */
  @NotNull
  public static ReactionListRequest list(@NotNull String messageId) {
    return new ReactionListRequest(messageId);
  }
}
