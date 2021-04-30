package io.stream.models;

import java.awt.TrayIcon.MessageType;
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
import io.stream.models.User.UserRequestObject;
import lombok.Data;

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

  @Data
  public static class MessageRequestObject {
    public MessageRequestObject() {
      additionalFields = new HashMap<>();
    }

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
  public static class MessageRequestObjectMessage {
    public MessageRequestObjectMessage() {
      additionalFields = new HashMap<>();
    }

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
  public static class MessageRequestObjectUser {
    public MessageRequestObjectUser() {}

    @NotNull
    @JsonProperty("id")
    private String id;
  }

  @Data
  public static class AttachmentRequestObject {
    public AttachmentRequestObject() {
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
  public static class ActionRequestObject {
    public ActionRequestObject() {}

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
  public static class FieldRequestObject {
    public FieldRequestObject() {}

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
}
