package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.Reaction.ReactionSendRequestData.ReactionSendRequest;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.framework.RequestObjectBuilder;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.ReactionService;
import io.getstream.chat.java.services.framework.ServiceFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
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

  @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return this.additionalFields;
  }

  @JsonAnySetter
  public void setAdditionalField(String name, Object value) {
    this.additionalFields.put(name, value);
  }

  @Builder
  @Setter
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

    @Nullable
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

    @Nullable
    public static ReactionRequestObject buildFrom(@Nullable Reaction reaction) {
      return RequestObjectBuilder.build(ReactionRequestObject.class, reaction);
    }
  }

  @Builder(
      builderClassName = "ReactionSendRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ReactionSendRequestData {
    @Nullable
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
      protected Call<ReactionSendResponse> generateCall(ServiceFactory serviceFactory) {
        return serviceFactory.create(ReactionService.class).send(messageId, this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class ReactionDeleteRequest extends StreamRequest<ReactionDeleteResponse> {
    @NotNull private String messageId;

    @NotNull private String type;

    @Nullable private String userId;

    public ReactionDeleteRequest userId(@NotNull String userId) {
      this.userId = userId;
      return this;
    }

    @Override
    protected Call<ReactionDeleteResponse> generateCall(ServiceFactory serviceFactory) {
      return serviceFactory.create(ReactionService.class).delete(messageId, type, userId);
    }
  }

  @RequiredArgsConstructor
  public static class ReactionListRequest extends StreamRequest<ReactionListResponse> {
    @NotNull private String messageId;

    @Nullable private Integer limit;

    @Nullable private Integer offset;

    @NotNull
    public ReactionListRequest limit(@NotNull Integer limit) {
      this.limit = limit;
      return this;
    }

    @NotNull
    public ReactionListRequest offset(@NotNull Integer offset) {
      this.offset = offset;
      return this;
    }

    @Override
    protected Call<ReactionListResponse> generateCall(ServiceFactory serviceFactory) {
      return serviceFactory.create(ReactionService.class).list(messageId);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ReactionSendResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;

    @NotNull
    @JsonProperty("reaction")
    private Reaction reaction;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ReactionDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;

    @NotNull
    @JsonProperty("reaction")
    private Reaction reaction;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ReactionListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("reactions")
    private List<Reaction> reactions;
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
   * @param type the reaction type
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
