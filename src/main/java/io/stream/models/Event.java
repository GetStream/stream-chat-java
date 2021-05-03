package io.stream.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Channel.ChannelMember;
import io.stream.models.Channel.ChannelMemberRequestObject;
import io.stream.models.Channel.ChannelRequestObject;
import io.stream.models.Event.EventSendRequestData.EventSendRequest;
import io.stream.models.Event.EventSendUserCustomRequestData.EventSendUserCustomRequest;
import io.stream.models.Message.MessageRequestObject;
import io.stream.models.Message.Moderation;
import io.stream.models.Message.ModerationRequestObject;
import io.stream.models.Reaction.ReactionRequestObject;
import io.stream.models.User.OwnUser;
import io.stream.models.User.OwnUserRequestObject;
import io.stream.models.User.UserRequestObject;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.EventService;
import io.stream.services.framework.StreamServiceGenerator;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import retrofit2.Call;

@Data
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

  @Builder
  public static class EventRequestObject {
    @NotNull
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
    private MessageRequestObject message;

    @Nullable
    @JsonProperty("reaction")
    private ReactionRequestObject reaction;

    @Nullable
    @JsonProperty("channel")
    private ChannelRequestObject channel;

    @Nullable
    @JsonProperty("member")
    private ChannelMemberRequestObject member;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("me")
    private OwnUserRequestObject me;

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
    private ModerationRequestObject automoderationScores;

    @Nullable
    @JsonProperty("parent_id")
    private String parentId;

    @Nullable
    @JsonProperty("team")
    private String team;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

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

  @Builder
  public static class EventUserCustomRequestObject {
    @NotNull
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

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

  public static class EventSendRequestData {
    @NotNull
    @JsonProperty("event")
    private EventRequestObject event;

    private EventSendRequestData(EventSendRequest eventSendRequest) {
      this.event = eventSendRequest.event;
    }

    public static final class EventSendRequest extends StreamRequest<EventSendResponse> {
      private String channelType;
      private String channelId;
      private EventRequestObject event;

      private EventSendRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @NotNull
      public EventSendRequest event(@NotNull EventRequestObject event) {
        this.event = event;
        return this;
      }

      @Override
      protected Call<EventSendResponse> generateCall() {
        return StreamServiceGenerator.createService(EventService.class)
            .send(channelType, channelId, new EventSendRequestData(this));
      }
    }
  }

  public static class EventSendUserCustomRequestData {
    @NotNull
    @JsonProperty("event")
    private EventUserCustomRequestObject event;

    @NotNull
    @JsonProperty("target_user_id")
    private String targetUserId;

    private EventSendUserCustomRequestData(EventSendUserCustomRequest eventSendUserCustomRequest) {
      this.event = eventSendUserCustomRequest.event;
      this.targetUserId = eventSendUserCustomRequest.targetUserId;
    }

    public static final class EventSendUserCustomRequest
        extends StreamRequest<StreamResponseObject> {
      private EventUserCustomRequestObject event;
      private String targetUserId;

      private EventSendUserCustomRequest(@NotNull String userId) {
        this.targetUserId = userId;
      }

      @NotNull
      public EventSendUserCustomRequest event(@NotNull EventUserCustomRequestObject event) {
        this.event = event;
        return this;
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(EventService.class)
            .sendUserCustom(targetUserId, new EventSendUserCustomRequestData(this));
      }
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class EventSendResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("event")
    private Event event;

    public EventSendResponse() {}
  }

  /**
   * Creates a send request
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static EventSendRequest send(@NotNull String channelType, @NotNull String channelId) {
    return new EventSendRequest(channelType, channelId);
  }

  /**
   * Creates a send request
   *
   * @param userId the user id
   * @return the created request
   */
  @NotNull
  public static EventSendUserCustomRequest sendUserCustom(@NotNull String userId) {
    return new EventSendUserCustomRequest(userId);
  }
}
