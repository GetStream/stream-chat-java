package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.*;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Message.Attachment;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.DraftService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

/** Represents draft message functionality in Stream Chat. */
@Data
public class Draft {
  /** A draft message. */
  @Data
  @NoArgsConstructor
  public static class DraftMessage {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("html")
    private String html;

    @Nullable
    @JsonProperty("mml")
    private String mml;

    @Nullable
    @JsonProperty("parent_id")
    private String parentId;

    @Nullable
    @JsonProperty("show_in_channel")
    private Boolean showInChannel;

    @Nullable
    @JsonProperty("attachments")
    private List<Attachment> attachments;

    @Nullable
    @JsonProperty("mentioned_users")
    private List<User> mentionedUsers;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;

    @Nullable
    @JsonProperty("quoted_message_id")
    private String quotedMessageId;

    @Nullable
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("silent")
    private Boolean silent;

    @Nullable
    @JsonProperty("poll_id")
    private String pollId;
  }

  /** A draft object containing the message and metadata. */
  @Data
  @NoArgsConstructor
  public static class DraftObject {
    @NotNull
    @JsonProperty("channel_cid")
    private String channelCid;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @NotNull
    @JsonProperty("message")
    private DraftMessage message;

    @Nullable
    @JsonProperty("channel")
    private Channel channel;

    @Nullable
    @JsonProperty("parent_id")
    private String parentId;

    @Nullable
    @JsonProperty("parent_message")
    private Message parentMessage;

    @Nullable
    @JsonProperty("quoted_message")
    private Message quotedMessage;
  }

  /** Response for draft creation. */
  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CreateDraftResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("draft")
    private DraftObject draft;
  }

  /** Response for getting a draft. */
  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class GetDraftResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("draft")
    private DraftObject draft;
  }

  /** Response for querying drafts. */
  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryDraftsResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("drafts")
    private List<DraftObject> drafts;

    @Nullable
    @JsonProperty("next")
    private String next;
  }

  /** Request data for creating a draft. */
  @Builder(
      builderClassName = "CreateDraftRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class CreateDraftRequestData {
    @NotNull
    @JsonProperty("message")
    private MessageRequestObject message;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class CreateDraftRequest extends StreamRequest<CreateDraftResponse> {
      @NotNull private String channelType;
      @NotNull private String channelId;

      private CreateDraftRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @Override
      protected Call<CreateDraftResponse> generateCall(Client client) throws StreamException {
        return client
            .create(DraftService.class)
            .createDraft(this.channelType, this.channelId, this.internalBuild());
      }
    }
  }

  /** Request for deleting a draft. */
  @Getter
  @EqualsAndHashCode
  public static class DeleteDraftRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String channelType;
    @NotNull private String channelId;
    @Nullable private String userId;
    @Nullable private String parentId;

    private DeleteDraftRequest(@NotNull String channelType, @NotNull String channelId) {
      this.channelType = channelType;
      this.channelId = channelId;
    }

    @NotNull
    public DeleteDraftRequest userId(@NotNull String userId) {
      this.userId = userId;
      return this;
    }

    @NotNull
    public DeleteDraftRequest parentId(@Nullable String parentId) {
      this.parentId = parentId;
      return this;
    }

    @Override
    protected Call<StreamResponseObject> generateCall(Client client) throws StreamException {
      return client
          .create(DraftService.class)
          .deleteDraft(this.channelType, this.channelId, this.userId, this.parentId);
    }
  }

  /** Request for getting a draft. */
  @Getter
  @EqualsAndHashCode
  public static class GetDraftRequest extends StreamRequest<GetDraftResponse> {
    @NotNull private String channelType;
    @NotNull private String channelId;
    @Nullable private String userId;
    @Nullable private String parentId;

    private GetDraftRequest(@NotNull String channelType, @NotNull String channelId) {
      this.channelType = channelType;
      this.channelId = channelId;
    }

    @NotNull
    public GetDraftRequest userId(@NotNull String userId) {
      this.userId = userId;
      return this;
    }

    @NotNull
    public GetDraftRequest parentId(@Nullable String parentId) {
      this.parentId = parentId;
      return this;
    }

    @Override
    protected Call<GetDraftResponse> generateCall(Client client) throws StreamException {
      return client
          .create(DraftService.class)
          .getDraft(this.channelType, this.channelId, this.userId, this.parentId);
    }
  }

  /** Request data for querying drafts. */
  @Builder(
      builderClassName = "QueryDraftsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class QueryDraftsRequestData {
    @Nullable
    @JsonProperty("filter")
    private Map<String, Object> filter;

    @Singular
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

    @NotNull
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    public static class QueryDraftsRequest extends StreamRequest<QueryDraftsResponse> {
      public QueryDraftsRequest() {}

      @Override
      protected Call<QueryDraftsResponse> generateCall(Client client) throws StreamException {
        return client.create(DraftService.class).queryDrafts(this.internalBuild());
      }
    }
  }

  /**
   * Creates a draft message in a channel
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static CreateDraftRequestData.CreateDraftRequest createDraft(
      @NotNull String type, @NotNull String id) {
    return new CreateDraftRequestData.CreateDraftRequest(type, id);
  }

  /**
   * Deletes a draft message from a channel
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static DeleteDraftRequest deleteDraft(@NotNull String type, @NotNull String id) {
    return new DeleteDraftRequest(type, id);
  }

  /**
   * Gets a draft message from a channel
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static GetDraftRequest getDraft(@NotNull String type, @NotNull String id) {
    return new GetDraftRequest(type, id);
  }

  /**
   * Queries all drafts for a user
   *
   * @return the created request
   */
  @NotNull
  public static QueryDraftsRequestData.QueryDraftsRequest queryDrafts() {
    return new QueryDraftsRequestData.QueryDraftsRequest();
  }
}
