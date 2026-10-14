package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.Message.Attachment;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Message.MessageType;
import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessageQueryRequestData.ScheduledMessageQueryRequest;
import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessageUpdateRequestData.ScheduledMessageUpdateRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.ScheduledMessageService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

/**
 * A message stored to be sent later. Created by {@link Message#send(String, String)} with {@code
 * scheduledAt} set; listed, edited, canceled and sent early with the methods of this class.
 *
 * <p>A scheduled message never carries the ID the message will get when sent: {@link #getId()} is
 * the ID of the scheduled message, and the {@code scheduled_message.sent} event reports the ID of
 * the message that was sent.
 */
@Data
@NoArgsConstructor
public class ScheduledMessage {
  /** Waiting for its send time. Can be edited, canceled or sent now. */
  public static final String STATUS_SCHEDULED = "scheduled";

  /** Being sent. Edit, cancel and send now return 409 in this state. */
  public static final String STATUS_SENDING = "sending";

  /** Sending failed; {@link #getFailureReason()} says why. Can only be deleted. */
  public static final String STATUS_FAILED = "failed";

  /** Canceled. Returned by {@link #delete(String)} for a message that was still scheduled. */
  public static final String STATUS_CANCELED = "canceled";

  /** Sent. Reported by the {@code scheduled_message.sent} event only. */
  public static final String STATUS_SENT = "sent";

  @NotNull
  @JsonProperty("id")
  private String id;

  @NotNull
  @JsonProperty("cid")
  private String cid;

  @Nullable
  @JsonProperty("user")
  private User user;

  @Nullable
  @JsonProperty("message")
  private ScheduledMessageContent message;

  @NotNull
  @JsonProperty("scheduled_at")
  private Date scheduledAt;

  /** One of the {@code STATUS_*} constants. */
  @NotNull
  @JsonProperty("status")
  private String status;

  /**
   * Why sending failed. Set only when the status is {@code failed}. One of: banned, no_permission,
   * channel_frozen, channel_deleted, user_deleted, moderation_blocked, hook_rejected, expired,
   * internal_error, invalid_message, pending_not_supported.
   */
  @Nullable
  @JsonProperty("failure_reason")
  private String failureReason;

  @Nullable
  @JsonProperty("created_at")
  private Date createdAt;

  @Nullable
  @JsonProperty("updated_at")
  private Date updatedAt;

  /** The channel of the scheduled message. Set only in query results. */
  @Nullable
  @JsonProperty("channel")
  private ScheduledMessageChannel channel;

  @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return additionalFields;
  }

  @JsonAnySetter
  public void setAdditionalField(String name, Object value) {
    additionalFields.put(name, value);
  }

  /**
   * The message a scheduled message will send. It has no ID, and {@code mentioned_users} holds the
   * IDs of the mentioned users.
   */
  @Data
  @NoArgsConstructor
  public static class ScheduledMessageContent {
    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("mml")
    private String mml;

    @Nullable
    @JsonProperty("html")
    private String html;

    @Nullable
    @JsonProperty("type")
    private MessageType type;

    @Nullable
    @JsonProperty("attachments")
    private List<Attachment> attachments;

    @Nullable
    @JsonProperty("parent_id")
    private String parentId;

    @Nullable
    @JsonProperty("show_in_channel")
    private Boolean showInChannel;

    @Nullable
    @JsonProperty("quoted_message_id")
    private String quotedMessageId;

    @Nullable
    @JsonProperty("mentioned_users")
    private List<String> mentionedUsers;

    @Nullable
    @JsonProperty("mentioned_roles")
    private List<String> mentionedRoles;

    @Nullable
    @JsonProperty("mentioned_group_ids")
    private List<String> mentionedGroupIds;

    @Nullable
    @JsonProperty("mentioned_channel")
    private Boolean mentionedChannel;

    @Nullable
    @JsonProperty("mentioned_here")
    private Boolean mentionedHere;

    @Nullable
    @JsonProperty("restricted_visibility")
    private List<String> restrictedVisibility;

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
    @JsonProperty("poll_id")
    private String pollId;

    @Nullable
    @JsonProperty("shared_location")
    private SharedLocation sharedLocation;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;

    @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return additionalFields;
    }

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
      additionalFields.put(name, value);
    }
  }

  /** Enough of a channel to list a scheduled message without loading the channel. */
  @Data
  @NoArgsConstructor
  public static class ScheduledMessageChannel {
    @NotNull
    @JsonProperty("cid")
    private String cid;

    @NotNull
    @JsonProperty("type")
    private String type;

    @NotNull
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("image")
    private String image;
  }

  @Builder
  @Getter
  @EqualsAndHashCode
  public static class ScheduledMessagesFilter {
    /** Only scheduled messages of this channel ({@code type:id}). */
    @Nullable
    @JsonProperty("cid")
    private String cid;

    /** Only these statuses. One or more of: scheduled, sending, failed. */
    @Singular("status")
    @Nullable
    @JsonProperty("status")
    private List<String> status;
  }

  @Builder(
      builderClassName = "ScheduledMessageUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ScheduledMessageUpdateRequestData {
    /**
     * Replaces the stored message as a whole. The sender stays the user who scheduled the message.
     */
    @Nullable
    @JsonProperty("message")
    private MessageRequestObject message;

    /**
     * The new send time. Must be at least 1 minute and at most the app's maximum lead time away.
     */
    @Nullable
    @JsonProperty("scheduled_at")
    private Date scheduledAt;

    public static class ScheduledMessageUpdateRequest
        extends StreamRequest<ScheduledMessageUpdateResponse> {
      @NotNull private String id;

      private ScheduledMessageUpdateRequest(@NotNull String id) {
        this.id = id;
      }

      @Override
      protected Call<ScheduledMessageUpdateResponse> generateCall(Client client) {
        return client.create(ScheduledMessageService.class).update(id, this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class ScheduledMessageDeleteRequest
      extends StreamRequest<ScheduledMessageDeleteResponse> {
    @NotNull private String id;

    @Override
    protected Call<ScheduledMessageDeleteResponse> generateCall(Client client) {
      return client.create(ScheduledMessageService.class).delete(id);
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class ScheduledMessageSendNowRequest
      extends StreamRequest<ScheduledMessageSendNowResponse> {
    @NotNull private String id;

    @Override
    protected Call<ScheduledMessageSendNowResponse> generateCall(Client client) {
      return client.create(ScheduledMessageService.class).sendNow(id);
    }
  }

  @Builder(
      builderClassName = "ScheduledMessageQueryRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ScheduledMessageQueryRequestData {
    /** The user whose scheduled messages to list. Required. */
    @NotNull
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("filter")
    private ScheduledMessagesFilter filter;

    /** Only {@code scheduled_at} ascending is supported. */
    @Singular
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

    /** Page size, 1 to 100. Defaults to 20. */
    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    /** Cursor from the previous page. */
    @Nullable
    @JsonProperty("next")
    private String next;

    public static class ScheduledMessageQueryRequest
        extends StreamRequest<ScheduledMessageQueryResponse> {
      @Override
      protected Call<ScheduledMessageQueryResponse> generateCall(Client client) {
        return client.create(ScheduledMessageService.class).query(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ScheduledMessageUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("scheduled_message")
    private ScheduledMessage scheduledMessage;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ScheduledMessageDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("scheduled_message")
    private ScheduledMessage scheduledMessage;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ScheduledMessageSendNowResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("scheduled_message")
    private ScheduledMessage scheduledMessage;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ScheduledMessageQueryResponse extends StreamResponseObject {
    /** Pending messages first, then failed ones, each by scheduled_at. */
    @NotNull
    @JsonProperty("scheduled_messages")
    private List<ScheduledMessage> scheduledMessages;

    /** Cursor for the next page; empty on the last page. */
    @Nullable
    @JsonProperty("next")
    private String next;
  }

  /**
   * Updates a scheduled message: its message, its send time, or both. Returns 409 while the message
   * is being sent.
   *
   * @param id the scheduled message ID
   * @return the created request
   */
  @NotNull
  public static ScheduledMessageUpdateRequest update(@NotNull String id) {
    return new ScheduledMessageUpdateRequest(id);
  }

  /**
   * Cancels a scheduled message so it is never sent, or removes a failed one. Returns 409 while the
   * message is being sent.
   *
   * @param id the scheduled message ID
   * @return the created request
   */
  @NotNull
  public static ScheduledMessageDeleteRequest delete(@NotNull String id) {
    return new ScheduledMessageDeleteRequest(id);
  }

  /**
   * Moves the send time of a scheduled message to now and queues the send. Returns 409 while the
   * message is being sent.
   *
   * @param id the scheduled message ID
   * @return the created request
   */
  @NotNull
  public static ScheduledMessageSendNowRequest sendNow(@NotNull String id) {
    return new ScheduledMessageSendNowRequest(id);
  }

  /**
   * Lists the scheduled messages of one user across channels: pending ones first, then failed ones,
   * each by scheduled_at. {@code userId} is required.
   *
   * @return the created request
   */
  @NotNull
  public static ScheduledMessageQueryRequest query() {
    return new ScheduledMessageQueryRequest();
  }
}
