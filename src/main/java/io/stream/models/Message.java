package io.stream.models;

import java.util.Collections;
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
import io.stream.models.Message.MessageSendRequestData.MessageSendRequest;
import io.stream.models.Message.MessageUpdateRequestData.MessageUpdateRequest;
import io.stream.models.User.UserRequestObject;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.MessageService;
import io.stream.services.framework.StreamServiceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import retrofit2.Call;

@Data
public class Message {
  public Message() {
    additionalFields = new HashMap<>();
  }

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
    public Attachment() {
      additionalFields = new HashMap<>();
    }

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

    @Nullable @JsonIgnore private Map<String, Object> additionalFields;

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
  public static class Action {
    public Action() {}

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
  public static class Field {
    public Field() {}

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

    @Nullable @JsonIgnore private Map<String, Object> additionalFields;

    private MessageRequestObject(Builder builder) {
      this.text = builder.text;
      this.mml = builder.mml;
      this.user = builder.user;
      this.parentId = builder.parentId;
      this.attachments = builder.attachments;
      this.showInChannel = builder.showInChannel;
      this.mentionedUsers = builder.mentionedUsers;
      this.userId = builder.userId;
      this.html = builder.html;
      this.reactionScores = builder.reactionScores;
      this.quotedMessageId = builder.quotedMessageId;
      this.cId = builder.cId;
      this.silent = builder.silent;
      this.pinned = builder.pinned;
      this.pinExpires = builder.pinExpires;
      this.pinnedBy = builder.pinnedBy;
      this.pinnedAt = builder.pinnedAt;
      this.additionalFields = builder.additionalFields;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    /**
     * Creates builder to build {@link MessageRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link MessageRequestObject}. */
    public static final class Builder {
      private String text;
      private String mml;
      private UserRequestObject user;
      private String parentId;
      private List<AttachmentRequestObject> attachments = Collections.emptyList();
      private Boolean showInChannel;
      private List<String> mentionedUsers = Collections.emptyList();
      private String userId;
      private String html;
      private Map<String, Integer> reactionScores = Collections.emptyMap();
      private String quotedMessageId;
      private String cId;
      private Boolean silent;
      private Boolean pinned;
      private Date pinExpires;
      private String pinnedBy;
      private Date pinnedAt;
      private Map<String, Object> additionalFields = Collections.emptyMap();

      private Builder() {}

      @NotNull
      public Builder withText(@NotNull String text) {
        this.text = text;
        return this;
      }

      @NotNull
      public Builder withMml(@NotNull String mml) {
        this.mml = mml;
        return this;
      }

      @NotNull
      public Builder withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @NotNull
      public Builder withParentId(@NotNull String parentId) {
        this.parentId = parentId;
        return this;
      }

      @NotNull
      public Builder withAttachments(@NotNull List<AttachmentRequestObject> attachments) {
        this.attachments = attachments;
        return this;
      }

      @NotNull
      public Builder withShowInChannel(@NotNull Boolean showInChannel) {
        this.showInChannel = showInChannel;
        return this;
      }

      @NotNull
      public Builder withMentionedUsers(@NotNull List<String> mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
        return this;
      }

      @NotNull
      public Builder withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public Builder withHtml(@NotNull String html) {
        this.html = html;
        return this;
      }

      @NotNull
      public Builder withReactionScores(@NotNull Map<String, Integer> reactionScores) {
        this.reactionScores = reactionScores;
        return this;
      }

      @NotNull
      public Builder withQuotedMessageId(@NotNull String quotedMessageId) {
        this.quotedMessageId = quotedMessageId;
        return this;
      }

      @NotNull
      public Builder withCId(@NotNull String cId) {
        this.cId = cId;
        return this;
      }

      @NotNull
      public Builder withSilent(@NotNull Boolean silent) {
        this.silent = silent;
        return this;
      }

      @NotNull
      public Builder withPinned(@NotNull Boolean pinned) {
        this.pinned = pinned;
        return this;
      }

      @NotNull
      public Builder withPinExpires(@NotNull Date pinExpires) {
        this.pinExpires = pinExpires;
        return this;
      }

      @NotNull
      public Builder withPinnedBy(@NotNull String pinnedBy) {
        this.pinnedBy = pinnedBy;
        return this;
      }

      @NotNull
      public Builder withPinnedAt(@NotNull Date pinnedAt) {
        this.pinnedAt = pinnedAt;
        return this;
      }

      @NotNull
      public Builder withAdditionalFields(@NotNull Map<String, Object> additionalFields) {
        this.additionalFields = additionalFields;
        return this;
      }

      @NotNull
      public MessageRequestObject build() {
        return new MessageRequestObject(this);
      }
    }
  }

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

    @Nullable @JsonIgnore private Map<String, Object> additionalFields;

    private MessageRequestObjectMessage(Builder builder) {
      this.text = builder.text;
      this.attachments = builder.attachments;
      this.user = builder.user;
      this.mentionedUsers = builder.mentionedUsers;
      this.parentId = builder.parentId;
      this.showInChannel = builder.showInChannel;
      this.silent = builder.silent;
      this.additionalFields = builder.additionalFields;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    /**
     * Creates builder to build {@link MessageRequestObjectMessage}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link MessageRequestObjectMessage}. */
    public static final class Builder {
      private String text;
      private List<Attachment> attachments = Collections.emptyList();
      private MessageRequestObjectUser user;
      private List<String> mentionedUsers = Collections.emptyList();
      private String parentId;
      private Boolean showInChannel;
      private Boolean silent;
      private Map<String, Object> additionalFields = Collections.emptyMap();

      private Builder() {}

      @NotNull
      public Builder withText(@NotNull String text) {
        this.text = text;
        return this;
      }

      @NotNull
      public Builder withAttachments(@NotNull List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
      }

      @NotNull
      public Builder withUser(@NotNull MessageRequestObjectUser user) {
        this.user = user;
        return this;
      }

      @NotNull
      public Builder withMentionedUsers(@NotNull List<String> mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
        return this;
      }

      @NotNull
      public Builder withParentId(@NotNull String parentId) {
        this.parentId = parentId;
        return this;
      }

      @NotNull
      public Builder withShowInChannel(@NotNull Boolean showInChannel) {
        this.showInChannel = showInChannel;
        return this;
      }

      @NotNull
      public Builder withSilent(@NotNull Boolean silent) {
        this.silent = silent;
        return this;
      }

      @NotNull
      public Builder withAdditionalFields(@NotNull Map<String, Object> additionalFields) {
        this.additionalFields = additionalFields;
        return this;
      }

      @NotNull
      public MessageRequestObjectMessage build() {
        return new MessageRequestObjectMessage(this);
      }
    }
  }

  public static class MessageRequestObjectUser {
    public MessageRequestObjectUser() {}

    @NotNull
    @JsonProperty("id")
    private String id;

    private MessageRequestObjectUser(Builder builder) {
      this.id = builder.id;
    }

    /**
     * Creates builder to build {@link MessageRequestObjectUser}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link MessageRequestObjectUser}. */
    public static final class Builder {
      private String id;

      private Builder() {}

      @NotNull
      public Builder withId(@NotNull String id) {
        this.id = id;
        return this;
      }

      @NotNull
      public MessageRequestObjectUser build() {
        return new MessageRequestObjectUser(this);
      }
    }
  }

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

    @Nullable
    @JsonProperty("actions")
    private List<ActionRequestObject> actions;

    @Nullable
    @JsonProperty("fields")
    private List<FieldRequestObject> fields;

    @Nullable
    @JsonProperty("asset_url")
    private String assetURL;

    @Nullable
    @JsonProperty("og_scrape_url")
    private String ogScrapeURL;

    @Nullable @JsonIgnore private Map<String, Object> additionalFields;

    private AttachmentRequestObject(Builder builder) {
      this.type = builder.type;
      this.fallback = builder.fallback;
      this.color = builder.color;
      this.pretext = builder.pretext;
      this.authorName = builder.authorName;
      this.authorLink = builder.authorLink;
      this.authorIcon = builder.authorIcon;
      this.title = builder.title;
      this.titleLink = builder.titleLink;
      this.text = builder.text;
      this.imageURL = builder.imageURL;
      this.thumbURL = builder.thumbURL;
      this.footer = builder.footer;
      this.footerIcon = builder.footerIcon;
      this.actions = builder.actions;
      this.fields = builder.fields;
      this.assetURL = builder.assetURL;
      this.ogScrapeURL = builder.ogScrapeURL;
      this.additionalFields = builder.additionalFields;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    /**
     * Creates builder to build {@link AttachmentRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link AttachmentRequestObject}. */
    public static final class Builder {
      private String type;
      private String fallback;
      private String color;
      private String pretext;
      private String authorName;
      private String authorLink;
      private String authorIcon;
      private String title;
      private String titleLink;
      private String text;
      private String imageURL;
      private String thumbURL;
      private String footer;
      private String footerIcon;
      private List<ActionRequestObject> actions = Collections.emptyList();
      private List<FieldRequestObject> fields = Collections.emptyList();
      private String assetURL;
      private String ogScrapeURL;
      private Map<String, Object> additionalFields = Collections.emptyMap();

      private Builder() {}

      @NotNull
      public Builder withType(@NotNull String type) {
        this.type = type;
        return this;
      }

      @NotNull
      public Builder withFallback(@NotNull String fallback) {
        this.fallback = fallback;
        return this;
      }

      @NotNull
      public Builder withColor(@NotNull String color) {
        this.color = color;
        return this;
      }

      @NotNull
      public Builder withPretext(@NotNull String pretext) {
        this.pretext = pretext;
        return this;
      }

      @NotNull
      public Builder withAuthorName(@NotNull String authorName) {
        this.authorName = authorName;
        return this;
      }

      @NotNull
      public Builder withAuthorLink(@NotNull String authorLink) {
        this.authorLink = authorLink;
        return this;
      }

      @NotNull
      public Builder withAuthorIcon(@NotNull String authorIcon) {
        this.authorIcon = authorIcon;
        return this;
      }

      @NotNull
      public Builder withTitle(@NotNull String title) {
        this.title = title;
        return this;
      }

      @NotNull
      public Builder withTitleLink(@NotNull String titleLink) {
        this.titleLink = titleLink;
        return this;
      }

      @NotNull
      public Builder withText(@NotNull String text) {
        this.text = text;
        return this;
      }

      @NotNull
      public Builder withImageURL(@NotNull String imageURL) {
        this.imageURL = imageURL;
        return this;
      }

      @NotNull
      public Builder withThumbURL(@NotNull String thumbURL) {
        this.thumbURL = thumbURL;
        return this;
      }

      @NotNull
      public Builder withFooter(@NotNull String footer) {
        this.footer = footer;
        return this;
      }

      @NotNull
      public Builder withFooterIcon(@NotNull String footerIcon) {
        this.footerIcon = footerIcon;
        return this;
      }

      @NotNull
      public Builder withActions(@NotNull List<ActionRequestObject> actions) {
        this.actions = actions;
        return this;
      }

      @NotNull
      public Builder withFields(@NotNull List<FieldRequestObject> fields) {
        this.fields = fields;
        return this;
      }

      @NotNull
      public Builder withAssetURL(@NotNull String assetURL) {
        this.assetURL = assetURL;
        return this;
      }

      @NotNull
      public Builder withOgScrapeURL(@NotNull String ogScrapeURL) {
        this.ogScrapeURL = ogScrapeURL;
        return this;
      }

      @NotNull
      public Builder withAdditionalFields(@NotNull Map<String, Object> additionalFields) {
        this.additionalFields = additionalFields;
        return this;
      }

      @NotNull
      public AttachmentRequestObject build() {
        return new AttachmentRequestObject(this);
      }
    }
  }

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

    private ActionRequestObject(Builder builder) {
      this.name = builder.name;
      this.text = builder.text;
      this.style = builder.style;
      this.type = builder.type;
      this.value = builder.value;
    }

    /**
     * Creates builder to build {@link ActionRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link ActionRequestObject}. */
    public static final class Builder {
      private String name;
      private String text;
      private String style;
      private String type;
      private String value;

      private Builder() {}

      @NotNull
      public Builder withName(@NotNull String name) {
        this.name = name;
        return this;
      }

      @NotNull
      public Builder withText(@NotNull String text) {
        this.text = text;
        return this;
      }

      @NotNull
      public Builder withStyle(@NotNull String style) {
        this.style = style;
        return this;
      }

      @NotNull
      public Builder withType(@NotNull String type) {
        this.type = type;
        return this;
      }

      @NotNull
      public Builder withValue(@NotNull String value) {
        this.value = value;
        return this;
      }

      @NotNull
      public ActionRequestObject build() {
        return new ActionRequestObject(this);
      }
    }
  }

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

    private FieldRequestObject(Builder builder) {
      this.type = builder.type;
      this.value = builder.value;
      this.shortField = builder.shortField;
    }

    /**
     * Creates builder to build {@link FieldRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link FieldRequestObject}. */
    public static final class Builder {
      private String type;
      private String value;
      private Boolean shortField;

      private Builder() {}

      @NotNull
      public Builder withType(@NotNull String type) {
        this.type = type;
        return this;
      }

      @NotNull
      public Builder withValue(@NotNull String value) {
        this.value = value;
        return this;
      }

      @NotNull
      public Builder withShortField(@NotNull Boolean shortField) {
        this.shortField = shortField;
        return this;
      }

      @NotNull
      public FieldRequestObject build() {
        return new FieldRequestObject(this);
      }
    }
  }

  public static class MessageSendRequestData {
    @Nullable
    @JsonProperty("message")
    private MessageRequestObject message;

    @Nullable
    @JsonProperty("skip_push")
    private Boolean skipPush;

    private MessageSendRequestData(MessageSendRequest builder) {
      this.message = builder.message;
      this.skipPush = builder.skipPush;
    }

    public static final class MessageSendRequest extends StreamRequest<MessageSendResponse> {
      private String channelId;
      private String channelType;
      private MessageRequestObject message;
      private Boolean skipPush;

      private MessageSendRequest(String channelType, String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @NotNull
      public MessageSendRequest withMessage(@NotNull MessageRequestObject message) {
        this.message = message;
        return this;
      }

      @NotNull
      public MessageSendRequest withSkipPush(@NotNull Boolean skipPush) {
        this.skipPush = skipPush;
        return this;
      }

      @Override
      protected Call<MessageSendResponse> generateCall() {
        return StreamServiceGenerator.createService(MessageService.class)
            .send(this.channelType, this.channelId, new MessageSendRequestData(this));
      }
    }
  }

  public static class MessageUpdateRequestData {
    @Nullable
    @JsonProperty("message")
    private MessageRequestObject message;

    private MessageUpdateRequestData(MessageUpdateRequest builder) {
      this.message = builder.message;
    }

    public static final class MessageUpdateRequest extends StreamRequest<MessageUpdateResponse> {
      private String id;
      private MessageRequestObject message;

      private MessageUpdateRequest(String id) {
        this.id = id;
      }

      @NotNull
      public MessageUpdateRequest withMessage(@NotNull MessageRequestObject message) {
        this.message = message;
        return this;
      }

      @NotNull
      public MessageUpdateRequestData build() {
        return new MessageUpdateRequestData(this);
      }

      @Override
      protected Call<MessageUpdateResponse> generateCall() {
        return StreamServiceGenerator.createService(MessageService.class)
            .update(this.id, new MessageUpdateRequestData(this));
      }
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class MessageSendResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("message")
    private Message message;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class MessageUpdateResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("message")
    private Message message;
  }

  /**
   * Creates send request
   *
   * @return the created request
   */
  @NotNull
  public static MessageSendRequest send(@NotNull String channelType, @NotNull String channelId) {
    return new MessageSendRequest(channelType, channelId);
  }

  /**
   * Creates an update request
   *
   * @return the created request
   */
  @NotNull
  public static MessageUpdateRequest update(@NotNull String id) {
    return new MessageUpdateRequest(id);
  }
}
