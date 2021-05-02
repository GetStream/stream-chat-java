package io.stream.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Channel.ChannelMember;
import io.stream.models.Message.Moderation;
import io.stream.models.User.OwnUser;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Event {
  @Nullable
  @JsonProperty("type")
  private String type;

  @Nullable
  @JsonProperty("connection_id")
  private String connectionId;

  @Nullable
  @JsonProperty("cid")
  private String cid;

  @Nullable
  @JsonProperty("channel_id")
  private String channelId;

  @Nullable
  @JsonProperty("channel_type")
  private String channelType;

  @Nullable
  @JsonProperty("message")
  private Message message;

  @Nullable
  @JsonProperty("reaction")
  private Reaction reaction;

  @Nullable
  @JsonProperty("channel")
  private Channel channel;

  @Nullable
  @JsonProperty("member")
  private ChannelMember member;

  @Nullable
  @JsonProperty("user")
  private User user;

  @Nullable
  @JsonProperty("user_id")
  private String userId;

  @Nullable
  @JsonProperty("me")
  private OwnUser me;

  @Nullable
  @JsonProperty("watcher_count")
  private Integer watcherCount;

  @Nullable
  @JsonProperty("reason")
  private String reason;

  @Nullable
  @JsonProperty("created_by")
  private User createdBy;

  @Nullable
  @JsonProperty("automoderation")
  private Boolean automoderation;

  @Nullable
  @JsonProperty("automoderation_scores")
  private Moderation automoderationScores;

  @Nullable
  @JsonProperty("parent_id")
  private String parentId;

  @Nullable
  @JsonProperty("team")
  private String team;

  @Nullable
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull @JsonIgnore private Map<String, Object> additionalFields;

  public Event() {
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
}
