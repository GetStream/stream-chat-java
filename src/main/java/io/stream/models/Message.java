package io.stream.models;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.stream.models.Message.MessageSearchRequestData.MessageSearchRequest;
import io.stream.models.Message.MessageSendRequestData.MessageSendRequest;
import io.stream.models.Message.MessageUpdateRequestData.MessageUpdateRequest;
import io.stream.models.User.UserRequestObject;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.MessageService;
import io.stream.services.framework.StreamServiceGenerator;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.extern.java.Log;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

@Log
@Data
public class Message {
  @NotNull
  @JsonProperty("id")
  private String id;

  @Nullable
  @JsonProperty("text")
  private String text;

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
  @JsonProperty("parent_id")
  private String parentId;

  @Nullable
  @JsonProperty("show_in_channel")
  private Boolean showInChannel;

  @Nullable
  @JsonProperty("reply_count")
  private Integer replyCount;

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

  @NotNull @JsonIgnore private Map<String, Object> additionalFields;

  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return this.additionalFields;
  }

  @JsonAnySetter
  public void setAdditionalField(String name, Object value) {
    this.additionalFields.put(name, value);
  }

  public Message() {
    additionalFields = new HashMap<>();
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
    DELETED
  }

  @Data
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

    @NotNull @JsonIgnore private Map<String, Object> additionalFields;

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
      this.additionalFields.put(name, value);
    }

    public Attachment() {
      additionalFields = new HashMap<>();
    }
  }

  @Data
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

    public Action() {}
  }

  @Data
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

    public Field() {}
  }

  @Data
  public static class SearchResult {
    @NotNull
    @JsonProperty("message")
    private SearchResultMessage message;

    public SearchResult() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class SearchResultMessage extends Message {
    @NotNull
    @JsonProperty("channel")
    private Channel channel;

    public SearchResultMessage() {}
  }

  @Data
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

    public ImageSize() {}
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
    CENTER
  }

  public enum Resize {
    @JsonProperty("clip")
    CLIP,
    @JsonProperty("crop")
    CROP,
    @JsonProperty("scale")
    SCALE,
    @JsonProperty("fill")
    FILL
  }

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

    public Moderation() {}
  }

  @Builder
  public static class MessageRequestObject {
    @Nullable
    @JsonProperty("text")
    private String text;

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
    private String pinnedBy;

    @Nullable
    @JsonProperty("pinned_at")
    private Date pinnedAt;

    @Singular @Nullable @JsonIgnore private Map<String, Object> additionalFields;
  }

  @Builder
  public static class MessageRequestObjectMessage {
    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("attachments")
    private List<Attachment> attachments;

    @Nullable
    @JsonProperty("user")
    private MessageRequestObjectUser user;

    @Nullable
    @JsonProperty("mentioned_users")
    private List<String> mentionedUsers;

    @Nullable
    @JsonProperty("parent_id")
    private String parentId;

    @Nullable
    @JsonProperty("show_in_channel")
    private Boolean showInChannel;

    @Nullable
    @JsonProperty("silent")
    private Boolean silent;

    @Singular @Nullable @JsonIgnore private Map<String, Object> additionalFields;
  }

  @Builder
  public static class MessageRequestObjectUser {
    @NotNull
    @JsonProperty("id")
    private String id;
  }

  @Builder
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
  }

  @Builder
  public static class ActionRequestObject {
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

  @Builder
  public static class FieldRequestObject {
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

  @Builder
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
  }

  @Builder
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

    public static class MessageSendRequest extends StreamRequest<MessageSendResponse> {
      private String channelId;
      private String channelType;

      private MessageSendRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @Override
      protected Call<MessageSendResponse> generateCall() {
        return StreamServiceGenerator.createService(MessageService.class)
            .send(this.channelType, this.channelId, this.internalBuild());
      }
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
      private String id;

      private MessageUpdateRequest(String id) {
        this.id = id;
      }

      @Override
      protected Call<MessageUpdateResponse> generateCall() {
        return StreamServiceGenerator.createService(MessageService.class)
            .update(this.id, this.internalBuild());
      }
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
    @NotNull
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

    public static class MessageSearchRequest extends StreamRequest<MessageSearchResponse> {
      @Override
      protected Call<MessageSearchResponse> generateCall() {
        return StreamServiceGenerator.createService(MessageService.class)
            .search(this.internalBuild());
      }
    }
  }

  public static class MessageUploadFileRequest extends StreamRequest<MessageUploadFileResponse> {
    private String channelType;
    private String channelId;
    private String userId;
    private File file;
    private String contentType;

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

    @Override
    protected Call<MessageUploadFileResponse> generateCall() {
      try {
        String resolvedContentType = contentType != null ? contentType : "application/octet-stream";
        RequestBody fileRequestBody =
            RequestBody.create(MediaType.parse(resolvedContentType), file);
        MultipartBody.Part multipartFile =
            MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
        UserRequestObject user = UserRequestObject.builder().id(userId).build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectMapper().writeValue(baos, user);
        RequestBody userRequestBody =
            RequestBody.create(MultipartBody.FORM, baos.toString("UTF-8"));
        return StreamServiceGenerator.createService(MessageService.class)
            .uploadFile(channelType, channelId, userRequestBody, multipartFile);
      } catch (IOException e) {
        // This should not happen, can only be a development error
        log.log(
            Level.SEVERE,
            "Seems there is a problem with the conversion of user request object to json",
            e);
        return null;
      }
    }
  }

  public static class MessageUploadImageRequest extends StreamRequest<MessageUploadImageResponse> {
    private File file;
    private String contentType;
    private String channelType;
    private String channelId;
    private String userId;
    private List<ImageSizeRequestObject> uploadSizes;

    private MessageUploadImageRequest(
        @NotNull String channelType,
        @NotNull String channelId,
        @NotNull String userId,
        @NotNull String contentType) {
      this.channelType = channelType;
      this.channelId = channelId;
      this.userId = userId;
      this.contentType = contentType;
    }

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

    @Override
    protected Call<MessageUploadImageResponse> generateCall() {
      try {
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse(contentType), file);
        MultipartBody.Part multipartFile =
            MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
        UserRequestObject user = UserRequestObject.builder().id(userId).build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectMapper().writeValue(baos, user);
        RequestBody userRequestBody =
            RequestBody.create(MultipartBody.FORM, baos.toString("UTF-8"));
        baos = new ByteArrayOutputStream();
        new ObjectMapper().writeValue(baos, uploadSizes);
        RequestBody uploadSizesRequestBody =
            RequestBody.create(MultipartBody.FORM, baos.toString("UTF-8"));
        return StreamServiceGenerator.createService(MessageService.class)
            .uploadImage(
                channelType, channelId, userRequestBody, multipartFile, uploadSizesRequestBody);
      } catch (IOException e) {
        // This should not happen, can only be a development error
        log.log(
            Level.SEVERE,
            "Seems there is a problem with the conversion of user request object or image size"
                + " request object to json",
            e);
        return null;
      }
    }
  }

  public static class MessageDeleteFileRequest extends StreamRequest<StreamResponseObject> {
    private String channelType;
    private String channelId;
    private String url;

    private MessageDeleteFileRequest(
        @NotNull String channelType, @NotNull String channelId, @NotNull String url) {
      this.channelType = channelType;
      this.channelId = channelId;
      this.url = url;
    }

    @Override
    protected Call<StreamResponseObject> generateCall() {
      return StreamServiceGenerator.createService(MessageService.class)
          .deleteFile(channelType, channelId, url);
    }
  }

  public static class MessageDeleteImageRequest extends StreamRequest<StreamResponseObject> {
    private String channelType;
    private String channelId;
    private String url;

    private MessageDeleteImageRequest(
        @NotNull String channelType, @NotNull String channelId, @NotNull String url) {
      this.channelType = channelType;
      this.channelId = channelId;
      this.url = url;
    }

    @Override
    protected Call<StreamResponseObject> generateCall() {
      return StreamServiceGenerator.createService(MessageService.class)
          .deleteImage(channelType, channelId, url);
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class MessageSendResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;

    public MessageSendResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class MessageUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message")
    private Message message;

    public MessageUpdateResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class MessageSearchResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("results")
    private List<SearchResult> results;

    public MessageSearchResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class MessageUploadFileResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("file")
    private String file;

    public MessageUploadFileResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class MessageUploadImageResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("file")
    private String file;

    @Nullable
    @JsonProperty("upload_sizes")
    private List<ImageSize> uploadSizes;

    public MessageUploadImageResponse() {}
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
}
