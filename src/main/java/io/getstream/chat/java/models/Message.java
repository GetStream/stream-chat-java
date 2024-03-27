package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.*;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Flag.FlagCreateRequestData.FlagCreateRequest;
import io.getstream.chat.java.models.Flag.FlagDeleteRequestData.FlagDeleteRequest;
import io.getstream.chat.java.models.Flag.FlagMessageQueryRequestData.FlagMessageQueryRequest;
import io.getstream.chat.java.models.Message.MessageCommitRequestData.MessageCommitRequest;
import io.getstream.chat.java.models.Message.MessagePartialUpdateRequestData.MessagePartialUpdateRequest;
import io.getstream.chat.java.models.Message.MessageRunCommandActionRequestData.MessageRunCommandActionRequest;
import io.getstream.chat.java.models.Message.MessageSearchRequestData.MessageSearchRequest;
import io.getstream.chat.java.models.Message.MessageSendRequestData.MessageSendRequest;
import io.getstream.chat.java.models.Message.MessageTranslateRequestData.MessageTranslateRequest;
import io.getstream.chat.java.models.Message.MessageUnblockRequestData.MessageUnblockRequest;
import io.getstream.chat.java.models.Message.MessageUpdateRequestData.MessageUpdateRequest;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.framework.*;
import io.getstream.chat.java.services.MessageService;
import io.getstream.chat.java.services.framework.Client;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Message {
  public static Class<? extends FileHandler> fileHandlerClass = DefaultFileHandler.class;

  @NotNull
  @JsonProperty("id")
  private String id;

  @Nullable
  @JsonProperty("text")
  private String text;

  @Nullable
  @JsonProperty("mml")
  private String mml;

  @Nullable
  @JsonProperty("command")
  private String command;

  @Nullable
  @JsonProperty("html")
  private String html;

  @Nullable
  @JsonProperty("type")
  private MessageType type;

  @Nullable
  @JsonProperty("silent")
  private Boolean silent;

  @Nullable
  @JsonProperty("user")
  private User user;

  @Nullable
  @JsonProperty("attachments")
  private List<Attachment> attachments;

  @Nullable
  @JsonProperty("latest_reactions")
  private List<Reaction> latestReactions;

  @Nullable
  @JsonProperty("own_reactions")
  private List<Reaction> ownReactions;

  @Nullable
  @JsonProperty("reaction_counts")
  private Map<String, Integer> reactionCounts;

  @Nullable
  @JsonProperty("reaction_scores")
  private Map<String, Integer> reactionScores;

  @Nullable
  @JsonProperty("parent_id")
  private String parentId;

  @Nullable
  @JsonProperty("show_in_channel")
  private Boolean showInChannel;

  @Nullable
  @JsonProperty("reply_count")
  private Integer replyCount;

  @Nullable
  @JsonProperty("deleted_reply_count")
  private Integer deletedReplyCount;

  @Nullable
  @JsonProperty("quoted_message_id")
  private String quotedMessageId;

  @Nullable
  @JsonProperty("quoted_message")
  private Message quoted_message;

  @Nullable
  @JsonProperty("thread_participants")
  private List<User> threadParticipants;

  @NotNull
  @JsonProperty("cid")
  private String cid;

  @Nullable
  @JsonProperty("mentioned_users")
  private List<User> mentionedUsers;

  @Nullable
  @JsonProperty("created_at")
  private Date createdAt;

  @Nullable
  @JsonProperty("updated_at")
  private Date updatedAt;

  @Nullable
  @JsonProperty("deleted_at")
  private Date deletedAt;

  @Nullable
  @JsonProperty("shadowed")
  private Boolean shadowed;

  @Nullable
  @JsonProperty("image_labels")
  private Map<String, List<String>> imageLabels;

  @Nullable
  @JsonProperty("i18n")
  private Map<String, String> i18n;

  private Boolean before_message_send_failed;

  @Nullable
  @JsonProperty("pinned")
  private Boolean pinned;

  @Nullable
  @JsonProperty("pin_expires")
  private Date pinExpires;

  @Nullable
  @JsonProperty("pinned_by")
  private User pinnedBy;

  @Nullable
  @JsonProperty("pinned_at")
  private Date pinnedAt;

  @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return this.additionalFields;
  }

  @JsonAnySetter
  public void setAdditionalField(String name, Object value) {
    this.additionalFields.put(name, value);
  }

  public enum MessageType {
    @JsonProperty("regular")
    REGULAR,
    @JsonProperty("ephemeral")
    EPHEMERAL,
    @JsonProperty("error")
    ERROR,
    @JsonProperty("reply")
    REPLY,
    @JsonProperty("system")
    SYSTEM,
    @JsonProperty("deleted")
    DELETED,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  @Data
  @NoArgsConstructor
  public static class Attachment {
    @Nullable
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("fallback")
    private String fallback;

    @Nullable
    @JsonProperty("color")
    private String color;

    @Nullable
    @JsonProperty("pretext")
    private String pretext;

    @Nullable
    @JsonProperty("author_name")
    private String authorName;

    @Nullable
    @JsonProperty("author_link")
    private String authorLink;

    @Nullable
    @JsonProperty("author_icon")
    private String authorIcon;

    @Nullable
    @JsonProperty("title")
    private String title;

    @Nullable
    @JsonProperty("title_link")
    private String titleLink;

    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("image_url")
    private String imageURL;

    @Nullable
    @JsonProperty("thumb_url")
    private String thumbURL;

    @Nullable
    @JsonProperty("footer")
    private String footer;

    @Nullable
    @JsonProperty("footer_icon")
    private String footerIcon;

    @Nullable
    @JsonProperty("actions")
    private List<Action> actions;

    @Nullable
    @JsonProperty("fields")
    private List<Field> fields;

    @Nullable
    @JsonProperty("asset_url")
    private String assetURL;

    @Nullable
    @JsonProperty("og_scrape_url")
    private String ogScrapeURL;

    @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
      this.additionalFields.put(name, value);
    }
  }

  @Data
  @NoArgsConstructor
  public static class Action {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("text")
    private String text;

    @NotNull
    @JsonProperty("style")
    private String style;

    @NotNull
    @JsonProperty("type")
    private String type;

    @NotNull
    @JsonProperty("value")
    private String value;
  }

  @Data
  @NoArgsConstructor
  public static class Field {
    @NotNull
    @JsonProperty("type")
    private String type;

    @NotNull
    @JsonProperty("value")
    private String value;

    @NotNull
    @JsonProperty("short")
    private Boolean shortField;
  }

  @Data
  @NoArgsConstructor
  public static class SearchResult {
    @NotNull
    @JsonProperty("message")
    private SearchResultMessage message;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class SearchResultMessage extends Message {
    @NotNull
    @JsonProperty("channel")
    private Channel channel;
  }

  @Data
  @NoArgsConstructor
  public static class ImageSize {
    @Nullable
    @JsonProperty("crop")
    private Crop crop;

    @Nullable
    @JsonProperty("resize")
    private Resize resize;

    @Nullable
    @JsonProperty("height")
    private Integer height;

    @Nullable
    @JsonProperty("width")
    private Integer width;
  }

  public enum Crop {
    @JsonProperty("top")
    TOP,
    @JsonProperty("bottom")
    BOTTOM,
    @JsonProperty("left")
    LEFT,
    @JsonProperty("right")
    RIGHT,
    @JsonProperty("center")
    CENTER,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  public enum Resize {
    @JsonProperty("clip")
    CLIP,
    @JsonProperty("crop")
    CROP,
    @JsonProperty("scale")
    SCALE,
    @JsonProperty("fill")
    FILL,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  @Data
  @NoArgsConstructor
  public static class Moderation {
    @Nullable
    @JsonProperty("toxic")
    private Integer toxic;

    @Nullable
    @JsonProperty("explicit")
    private Integer explicit;

    @Nullable
    @JsonProperty("spam")
    private Integer spam;
  }

  @Builder
  @Setter
  public static class MessageRequestObject {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("type")
    private MessageType type;

    @Nullable
    @JsonProperty("mml")
    private String mml;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    @Nullable
    @JsonProperty("parent_id")
    private String parentId;

    @Singular
    @Nullable
    @JsonProperty("attachments")
    private List<AttachmentRequestObject> attachments;

    @Nullable
    @JsonProperty("show_in_channel")
    private Boolean showInChannel;

    @Nullable
    @JsonProperty("mentioned_users")
    private List<String> mentionedUsers;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("html")
    private String html;

    @Singular
    @Nullable
    @JsonProperty("reaction_scores")
    private Map<String, Integer> reactionScores;

    @Nullable
    @JsonProperty("quoted_message_id")
    private String quotedMessageId;

    @Nullable
    @JsonProperty("cid")
    private String cId;

    @Nullable
    @JsonProperty("silent")
    private Boolean silent;

    @Nullable
    @JsonProperty("pinned")
    private Boolean pinned;

    @Nullable
    @JsonProperty("pin_expires")
    private Date pinExpires;

    @Nullable
    @JsonProperty("pinned_by")
    private UserRequestObject pinnedBy;

    @Nullable
    @JsonProperty("pinned_at")
    private Date pinnedAt;

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
    public static MessageRequestObject buildFrom(@Nullable Message message) {
      return RequestObjectBuilder.build(MessageRequestObject.class, message);
    }
  }

  @Builder
  @Setter
  public static class AttachmentRequestObject {
    @Nullable
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("fallback")
    private String fallback;

    @Nullable
    @JsonProperty("color")
    private String color;

    @Nullable
    @JsonProperty("pretext")
    private String pretext;

    @Nullable
    @JsonProperty("author_name")
    private String authorName;

    @Nullable
    @JsonProperty("author_link")
    private String authorLink;

    @Nullable
    @JsonProperty("author_icon")
    private String authorIcon;

    @Nullable
    @JsonProperty("title")
    private String title;

    @Nullable
    @JsonProperty("title_link")
    private String titleLink;

    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("image_url")
    private String imageURL;

    @Nullable
    @JsonProperty("thumb_url")
    private String thumbURL;

    @Nullable
    @JsonProperty("footer")
    private String footer;

    @Nullable
    @JsonProperty("footer_icon")
    private String footerIcon;

    @Singular
    @Nullable
    @JsonProperty("actions")
    private List<ActionRequestObject> actions;

    @Singular
    @Nullable
    @JsonProperty("fields")
    private List<FieldRequestObject> fields;

    @Nullable
    @JsonProperty("asset_url")
    private String assetURL;

    @Nullable
    @JsonProperty("og_scrape_url")
    private String ogScrapeURL;

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
    public static AttachmentRequestObject buildFrom(@Nullable Attachment attachment) {
      return RequestObjectBuilder.build(AttachmentRequestObject.class, attachment);
    }
  }

  @Builder
  @Setter
  public static class ActionRequestObject {
    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("style")
    private String style;

    @Nullable
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("value")
    private String value;

    @Nullable
    public static ActionRequestObject buildFrom(@Nullable Action action) {
      return RequestObjectBuilder.build(ActionRequestObject.class, action);
    }
  }

  @Builder
  @Setter
  public static class FieldRequestObject {
    @Nullable
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("value")
    private String value;

    @Nullable
    @JsonProperty("short")
    private Boolean shortField;

    @Nullable
    public static FieldRequestObject buildFrom(@Nullable Field field) {
      return RequestObjectBuilder.build(FieldRequestObject.class, field);
    }
  }

  @Builder
  @Setter
  public static class ImageSizeRequestObject {
    @Nullable
    @JsonProperty("crop")
    private Crop crop;

    @Nullable
    @JsonProperty("resize")
    private Resize resize;

    @Nullable
    @JsonProperty("height")
    private Integer height;

    @Nullable
    @JsonProperty("width")
    private Integer width;

    @Nullable
    public static ImageSizeRequestObject buildFrom(@Nullable ImageSize imageSize) {
      return RequestObjectBuilder.build(ImageSizeRequestObject.class, imageSize);
    }
  }

  @Builder
  @Setter
  public static class ModerationRequestObject {
    @Nullable
    @JsonProperty("toxic")
    private Integer toxic;

    @Nullable
    @JsonProperty("explicit")
    private Integer explicit;

    @Nullable
    @JsonProperty("spam")
    private Integer spam;

    @Nullable
    public static ModerationRequestObject buildFrom(@Nullable Moderation moderation) {
      return RequestObjectBuilder.build(ModerationRequestObject.class, moderation);
    }
  }

  @Builder(
      builderClassName = "MessageSendRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class MessageSendRequestData {
    @Nullable
    @JsonProperty("message")
    private MessageRequestObject message;

    @Nullable
    @JsonProperty("skip_push")
    private Boolean skipPush;

    @Nullable
    @JsonProperty("is_pending_message")
    private Boolean isPendingMessage;

    @Nullable
    @JsonProperty("pending")
    private Boolean pending;

    @Nullable
    @JsonProperty("force_moderation")
    private Boolean forceModeration;

    @Nullable
    @JsonProperty("pending_message_metadata")
    private Map<String, Object> pendingMessageMetadata;

    public static class MessageSendRequest extends StreamRequest<MessageSendResponse> {
      @NotNull private String channelId;

      @NotNull private String channelType;

      private MessageSendRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @Override
      protected Call<MessageSendResponse> generateCall(Client client) {
        return client
            .create(MessageService.class)
            .send(this.channelType, this.channelId, this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class MessageGetRequest extends StreamRequest<MessageGetResponse> {
    @NotNull private String id;
    @Nullable private Boolean showDeletedMessages;

    @NotNull
    public MessageGetRequest showDeletedMessages(@NotNull Boolean showDeletedMessages) {
      this.showDeletedMessages = showDeletedMessages;
      return this;
    }

    @Override
    protected Call<MessageGetResponse> generateCall(Client client) {
      return client.create(MessageService.class).get(this.id, this.showDeletedMessages);
    }
  }

  @Builder(
      builderClassName = "MessageUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class MessageUpdateRequestData {
    @Nullable
    @JsonProperty("message")
    private MessageRequestObject message;

    public static class MessageUpdateRequest extends StreamRequest<MessageUpdateResponse> {
      @NotNull private String id;

      private MessageUpdateRequest(@NotNull String id) {
        this.id = id;
      }

      @Override
      protected Call<MessageUpdateResponse> generateCall(Client client) {
        return client.create(MessageService.class).update(this.id, this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class MessageDeleteRequest extends StreamRequest<MessageDeleteResponse> {
    @NotNull private String id;

    @Nullable private Boolean hard;

    @NotNull
    public MessageDeleteRequest hard(@NotNull Boolean hard) {
      this.hard = hard;
      return this;
    }

    @Override
    protected Call<MessageDeleteResponse> generateCall(Client client) {
      return client.create(MessageService.class).delete(this.id, this.hard);
    }
  }

  @Builder(
      builderClassName = "MessageSearchRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class MessageSearchRequestData {
    @Nullable
    @JsonProperty("query")
    private String query;

    @Singular
    @Nullable
    @JsonProperty("filter_conditions")
    private Map<String, Object> filterConditions;

    @Singular
    @Nullable
    @JsonProperty("message_filter_conditions")
    private Map<String, Object> messageFilterConditions;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Singular
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

    private MessageSearchRequestData(
        @Nullable String query,
        @Nullable Map<String, Object> filterConditions,
        @Nullable Map<String, Object> messageFilterConditions,
        @Nullable Integer limit,
        @Nullable Integer offset,
        @Nullable String next,
        @Nullable List<Sort> sorts) {
      if ((query == null || query.isEmpty())
          && (messageFilterConditions == null || messageFilterConditions.isEmpty())) {
        throw new IllegalArgumentException(
            "Must specify one of query and message filter conditions");
      }
      if (query != null
          && !query.isEmpty()
          && messageFilterConditions != null
          && !messageFilterConditions.isEmpty()) {
        throw new IllegalArgumentException(
            "Can only specify one of query and message filter conditions");
      }
      if (offset != null && offset > 0 && next != null && !next.isEmpty()) {
        throw new IllegalArgumentException("Cannot use offset with next value");
      }
      this.query = query;
      this.filterConditions = filterConditions;
      this.messageFilterConditions = messageFilterConditions;
      this.limit = limit;
      this.offset = offset;
      this.next = next;
      this.sorts = sorts;
    }

    public static class MessageSearchRequest extends StreamRequest<MessageSearchResponse> {
      @Override
      protected Call<MessageSearchResponse> generateCall(Client client) {
        return client.create(MessageService.class).search(this.internalBuild());
      }
    }
  }

  public abstract static class FileRequest<TResponse> {
    private FileHandler fileHandler;

    public FileRequest<TResponse> withFileHandler(FileHandler fileHandler) {
      this.fileHandler = fileHandler;
      return this;
    }

    public abstract TResponse request() throws StreamException;

    public void requestAsync(
        @Nullable Consumer<TResponse> onSuccess, @Nullable Consumer<StreamException> onError) {
      try {
        var response = request();
        if (onSuccess == null) {
          return;
        }

        onSuccess.accept(response);
      } catch (StreamException ex) {
        if (onError != null) {
          onError.accept(ex);
        }
      }
    }

    @NotNull
    protected FileHandler getFileHandler() throws StreamException {
      var fh = fileHandler;
      if (fh != null) {
        return fh;
      }

      try {
        return fileHandlerClass.getDeclaredConstructor().newInstance();
      } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
        throw StreamException.build(
            "Your file handler should have a public constructor with no argument");
      } catch (InvocationTargetException e) {
        throw StreamException.build(e);
      }
    }
  }

  // We do not use @RequiredArgsConstructor here for uniformity with MessageUploadImageRequest
  public static class MessageUploadFileRequest extends FileRequest<MessageUploadFileResponse> {
    @NotNull private String channelType;

    @NotNull private String channelId;

    @NotNull private String userId;

    @Nullable private File file;

    @Nullable private String contentType;

    private MessageUploadFileRequest(
        @NotNull String channelType,
        @NotNull String channelId,
        @NotNull String userId,
        @Nullable String contentType) {
      this.channelType = channelType;
      this.channelId = channelId;
      this.userId = userId;
      this.contentType = contentType;
    }

    @NotNull
    public MessageUploadFileRequest file(@NotNull File file) {
      this.file = file;
      return this;
    }

    @NotNull
    public MessageUploadFileResponse request() throws StreamException {
      return getFileHandler().uploadFile(channelType, channelId, userId, file, contentType);
    }
  }

  @RequiredArgsConstructor
  public static class MessageUploadImageRequest extends FileRequest<MessageUploadImageResponse> {
    @Nullable private File file;

    @NotNull private String channelType;

    @NotNull private String channelId;

    @NotNull private String userId;

    @NotNull private String contentType;

    @Nullable private List<ImageSizeRequestObject> uploadSizes;

    @NotNull
    public MessageUploadImageRequest file(@NotNull File file) {
      this.file = file;
      return this;
    }

    @NotNull
    public MessageUploadImageRequest uploadSizes(
        @NotNull List<ImageSizeRequestObject> uploadSizes) {
      this.uploadSizes = uploadSizes;
      return this;
    }

    @NotNull
    public MessageUploadImageResponse request() throws StreamException {
      return getFileHandler()
          .uploadImage(channelType, channelId, userId, file, contentType, uploadSizes);
    }
  }

  @RequiredArgsConstructor
  public static class MessageDeleteFileRequest extends FileRequest<StreamResponseObject> {
    @NotNull private String channelType;

    @NotNull private String channelId;

    @NotNull private String url;

    @NotNull
    public StreamResponseObject request() throws StreamException {
      return getFileHandler().deleteFile(channelType, channelId, url);
    }
  }

  @RequiredArgsConstructor
  public static class MessageDeleteImageRequest extends FileRequest<StreamResponseObject> {
    @NotNull private String channelType;

    @NotNull private String channelId;

    @NotNull private String url;

    @NotNull
    public StreamResponseObject request() throws StreamException {
      return getFileHandler().deleteImage(channelType, channelId, url);
    }
  }

  @RequiredArgsConstructor
  public static class MessageGetManyRequest extends StreamRequest<MessageGetManyResponse> {
    @NotNull private String channelType;

    @NotNull private String channelId;

    @NotNull private List<String> messageIds;

    @Override
    protected Call<MessageGetManyResponse> generateCall(Client client) {
      return client
          .create(MessageService.class)
          .getMany(this.channelType, this.channelId, String.join(",", this.messageIds));
    }
  }

  @RequiredArgsConstructor
  public static class MessageGetRepliesRequest extends StreamRequest<MessageGetRepliesResponse> {
    @NotNull private String parentId;

    @Nullable private String idGte;

    @Nullable private String idGt;

    @Nullable private String idLte;

    @Nullable private String idLt;

    @Nullable private Date createdAtAfterOrEqual;

    @Nullable private Date createdAtAfter;

    @Nullable private Date createdAtBeforeOrEqual;

    @Nullable private Date createdAtBefore;

    @Nullable private Integer limit;

    @Nullable private Integer offset;

    @NotNull
    public MessageGetRepliesRequest idGte(@Nullable String idGte) {
      this.idGte = idGte;
      return this;
    }

    @NotNull
    public MessageGetRepliesRequest idGt(@Nullable String idGt) {
      this.idGt = idGt;
      return this;
    }

    @NotNull
    public MessageGetRepliesRequest idLte(@Nullable String idLte) {
      this.idLte = idLte;
      return this;
    }

    @NotNull
    public MessageGetRepliesRequest idLt(@Nullable String idLt) {
      this.idLt = idLt;
      return this;
    }

    @NotNull
    public MessageGetRepliesRequest createdAtAfterOrEqual(@Nullable Date createdAtAfterOrEqual) {
      this.createdAtAfterOrEqual = createdAtAfterOrEqual;
      return this;
    }

    @NotNull
    public MessageGetRepliesRequest createdAtAfter(@Nullable Date createdAtAfter) {
      this.createdAtAfter = createdAtAfter;
      return this;
    }

    @NotNull
    public MessageGetRepliesRequest createdAtBeforeOrEqual(@Nullable Date createdAtBeforeOrEqual) {
      this.createdAtBeforeOrEqual = createdAtBeforeOrEqual;
      return this;
    }

    @NotNull
    public MessageGetRepliesRequest createdAtBefore(@Nullable Date createdAtBefore) {
      this.createdAtBefore = createdAtBefore;
      return this;
    }

    @NotNull
    public MessageGetRepliesRequest limit(@NotNull Integer limit) {
      this.limit = limit;
      return this;
    }

    @NotNull
    public MessageGetRepliesRequest offset(@NotNull Integer offset) {
      this.offset = offset;
      return this;
    }

    @Override
    protected Call<MessageGetRepliesResponse> generateCall(Client client) {
      return client
          .create(MessageService.class)
          .getReplies(
              parentId,
              idGte,
              idGt,
              idLte,
              idLt,
              createdAtAfterOrEqual,
              createdAtAfter,
              createdAtBeforeOrEqual,
              createdAtBefore);
    }
  }

  @Builder(
      builderClassName = "MessageRunCommandActionRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class MessageRunCommandActionRequestData {
    @Nullable
    @JsonProperty("form_data")
    private Map<String, String> formData;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class MessageRunCommandActionRequest
        extends StreamRequest<MessageRunCommandActionResponse> {
      @NotNull private String messageId;

      public MessageRunCommandActionRequest(@NotNull String messageId) {
        this.messageId = messageId;
      }

      @Override
      protected Call<MessageRunCommandActionResponse> generateCall(Client client) {
        return client
            .create(MessageService.class)
            .runCommandAction(messageId, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "MessageTranslateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class MessageTranslateRequestData {
    @Nullable
    @JsonProperty("language")
    private Language language;

    public static class MessageTranslateRequest extends StreamRequest<MessageTranslateResponse> {
      @NotNull private String messageId;

      public MessageTranslateRequest(@NotNull String messageId) {
        this.messageId = messageId;
      }

      @Override
      protected Call<MessageTranslateResponse> generateCall(Client client) {
        return client.create(MessageService.class).translate(messageId, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "MessageCommitRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class MessageCommitRequestData {
    public static class MessageCommitRequest extends StreamRequest<MessageCommitResponse> {
      @NotNull private String messageId;

      public MessageCommitRequest(@NotNull String messageId) {
        this.messageId = messageId;
      }

      @Override
      protected Call<MessageCommitResponse> generateCall(Client client) {
        return client.create(MessageService.class).commit(messageId);
      }
    }
  }

  @Builder(
      builderClassName = "MessagePartialUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class MessagePartialUpdateRequestData {
    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    @Singular
    @Nullable
    @JsonProperty("set")
    private Map<String, Object> setValues;

    @Singular
    @Nullable
    @JsonProperty("unset")
    private List<String> unsetValues;

    public static class MessagePartialUpdateRequest
        extends StreamRequest<MessagePartialUpdateResponse> {
      @NotNull private String id;

      private MessagePartialUpdateRequest(@NotNull String id) {
        this.id = id;
      }

      @Override
      protected Call<MessagePartialUpdateResponse> generateCall(Client client) {
        return client.create(MessageService.class).partialUpdate(id, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "MessageUnblockRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class MessageUnblockRequestData {
    @NotNull
    @JsonProperty("target_message_id")
    private String targetMessageId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    public static class MessageUnblockRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall(Client client) {
        return client.create(MessageService.class).unblockMessage(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageSendResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageGetResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageCommitResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageSearchResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("results")
    private List<SearchResult> results;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("previous")
    private String previous;
  }

  @Data
  @NoArgsConstructor
  public static class MessageUploadFileResponse implements StreamResponse {
    @NotNull
    @JsonProperty("file")
    private String file;

    @NotNull
    @JsonProperty("duration")
    private String duration;
  }

  @Data
  @NoArgsConstructor
  public static class MessageUploadImageResponse implements StreamResponse {
    @NotNull
    @JsonProperty("file")
    private String file;

    @NotNull
    @JsonProperty("duration")
    private String duration;

    @Nullable
    @JsonProperty("upload_sizes")
    private List<ImageSize> uploadSizes;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageGetManyResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("messages")
    private List<Message> messages;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageGetRepliesResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("messages")
    private List<Message> messages;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageRunCommandActionResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageTranslateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessagePartialUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;
  }

  /**
   * Creates send request
   *
   * @param channelType the channel type
   * @param channelId the channel id
   * @return the created request
   */
  @NotNull
  public static MessageSendRequest send(@NotNull String channelType, @NotNull String channelId) {
    return new MessageSendRequest(channelType, channelId);
  }

  /**
   * Creates a get request
   *
   * @param id the message id
   * @return the created request
   */
  @NotNull
  public static MessageGetRequest get(@NotNull String id) {
    return new MessageGetRequest(id);
  }

  /**
   * Creates an update request
   *
   * @param id the message id
   * @return the created request
   */
  @NotNull
  public static MessageUpdateRequest update(@NotNull String id) {
    return new MessageUpdateRequest(id);
  }

  /**
   * Creates an delete request
   *
   * @param id the message id
   * @return the created request
   */
  @NotNull
  public static MessageDeleteRequest delete(@NotNull String id) {
    return new MessageDeleteRequest(id);
  }

  /**
   * Creates a search request
   *
   * @return the created request
   */
  @NotNull
  public static MessageSearchRequest search() {
    return new MessageSearchRequest();
  }

  /**
   * Creates a file upload request
   *
   * @param channelType the channel type
   * @param channelId the channel id
   * @param userId the id of the user sending this file
   * @param contentType the content type of the file to send
   * @return the created request
   */
  @NotNull
  public static MessageUploadFileRequest uploadFile(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable String contentType) {
    return new MessageUploadFileRequest(channelType, channelId, userId, contentType);
  }

  /**
   * Creates an image upload request
   *
   * @param channelType the channel type
   * @param channelId the channel id
   * @param userId the id of the user sending this image
   * @param contentType the content type of the image to send
   * @return the created request
   */
  @NotNull
  public static MessageUploadImageRequest uploadImage(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @NotNull String contentType) {
    return new MessageUploadImageRequest(channelType, channelId, userId, contentType);
  }

  /**
   * Creates a delete file request
   *
   * @param channelType the channel type
   * @param channelId the channel id
   * @param url the file url
   * @return the created request
   */
  @NotNull
  public static MessageDeleteFileRequest deleteFile(
      @NotNull String channelType, @NotNull String channelId, @NotNull String url) {
    return new MessageDeleteFileRequest(channelType, channelId, url);
  }

  /**
   * Creates a delete image request
   *
   * @param channelType the channel type
   * @param channelId the channel id
   * @param url the image url
   * @return the created request
   */
  @NotNull
  public static MessageDeleteImageRequest deleteImage(
      @NotNull String channelType, @NotNull String channelId, @NotNull String url) {
    return new MessageDeleteImageRequest(channelType, channelId, url);
  }

  /**
   * Creates a get many request
   *
   * @param channelType the channel type
   * @param channelId the channel id
   * @param messageIds the message ids
   * @return the created request
   */
  @NotNull
  public static MessageGetManyRequest getMany(
      @NotNull String channelType, @NotNull String channelId, @NotNull List<String> messageIds) {
    return new MessageGetManyRequest(channelType, channelId, messageIds);
  }

  /**
   * Creates a get replies request
   *
   * @param parentMessageId the parent message id
   * @return the created request
   */
  @NotNull
  public static MessageGetRepliesRequest getReplies(@NotNull String parentMessageId) {
    return new MessageGetRepliesRequest(parentMessageId);
  }

  /**
   * Creates a run command action request
   *
   * @param messageId the message id
   * @return the created request
   */
  @NotNull
  public static MessageRunCommandActionRequest runCommandAction(@NotNull String messageId) {
    return new MessageRunCommandActionRequest(messageId);
  }

  /**
   * Creates a translate request
   *
   * @param messageId the message id
   * @return the created request
   */
  @NotNull
  public static MessageTranslateRequest translate(@NotNull String messageId) {
    return new MessageTranslateRequest(messageId);
  }

  /**
   * Creates a commit message request
   *
   * @param messageId the pending message id to commit
   * @return the created request
   */
  @NotNull
  public static MessageCommitRequest commit(@NotNull String messageId) {
    return new MessageCommitRequest(messageId);
  }

  /**
   * Creates a flag request
   *
   * @param messageId the message id to flag
   * @return the created request
   */
  @NotNull
  public static FlagCreateRequest flag(@NotNull String messageId) {
    return new FlagCreateRequest().targetMessageId(messageId);
  }

  /**
   * Creates a unflag request
   *
   * @param messageId the message id to unflag
   * @return the created request
   */
  @NotNull
  public static FlagDeleteRequest unflag(@NotNull String messageId) {
    return new FlagDeleteRequest().targetMessageId(messageId);
  }

  /**
   * Creates a query flag request
   *
   * @return the created request
   */
  @NotNull
  public static FlagMessageQueryRequest queryFlags() {
    return new FlagMessageQueryRequest();
  }

  /**
   * Creates a partial update request
   *
   * @param id the message id
   * @return the created request
   */
  @NotNull
  public static MessagePartialUpdateRequest partialUpdate(@NotNull String id) {
    return new MessagePartialUpdateRequest(id);
  }

  /**
   * Creates a pin message request without expiration. It invokes message partial update under the
   * hood.
   *
   * @param id the message id
   * @param userId id of the user who pins the message
   * @return the created request
   */
  @NotNull
  public static MessagePartialUpdateRequest pinMessage(@NotNull String id, @NotNull String userId) {
    return new MessagePartialUpdateRequest(id).setValue("pinned", true).userId(userId);
  }

  /**
   * Creates a pin message request with expiration. It invokes message partial update under the
   * hood.
   *
   * @param id the message id
   * @param userId id of the user who pins the message
   * @param expiration expiration of the pin
   * @return the created request
   */
  @NotNull
  public static MessagePartialUpdateRequest pinMessage(
      @NotNull String id, @NotNull String userId, @NotNull Date expiration) {
    return new MessagePartialUpdateRequest(id)
        .setValue("pinned", true)
        .setValue("pin_expires", expiration)
        .userId(userId);
  }

  /**
   * Creates an unpin message request. It invokes message partial update under the hood.
   *
   * @param id the message id
   * @param userId id of the user who unpins the message
   * @return the created request
   */
  @NotNull
  public static MessagePartialUpdateRequest unpinMessage(
      @NotNull String id, @NotNull String userId) {
    return new MessagePartialUpdateRequest(id).setValue("pinned", false).userId(userId);
  }

  /**
   * Creates an unblock message request
   *
   * @param messageId the message id to unblock
   * @return the created request
   */
  @NotNull
  public static MessageUnblockRequest unblock(@NotNull String messageId) {
    return new MessageUnblockRequest().targetMessageId(messageId);
  }
}
