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
import io.stream.models.Channel.ChannelExportRequestData.ChannelExportRequest;
import io.stream.models.Channel.ChannelGetRequestData.ChannelGetRequest;
import io.stream.models.Channel.ChannelHideRequestData.ChannelHideRequest;
import io.stream.models.Channel.ChannelListRequestData.ChannelListRequest;
import io.stream.models.Channel.ChannelMarkAllReadRequestData.ChannelMarkAllReadRequest;
import io.stream.models.Channel.ChannelMarkReadRequestData.ChannelMarkReadRequest;
import io.stream.models.Channel.ChannelMuteRequestData.ChannelMuteRequest;
import io.stream.models.Channel.ChannelQueryMembersRequestData.ChannelQueryMembersRequest;
import io.stream.models.Channel.ChannelShowRequestData.ChannelShowRequest;
import io.stream.models.Channel.ChannelUnMuteRequestData.ChannelUnMuteRequest;
import io.stream.models.Channel.ChannelUpdateRequestData.ChannelUpdateRequest;
import io.stream.models.ChannelType.BlocklistBehavior;
import io.stream.models.ChannelType.ChannelTypeWithCommands;
import io.stream.models.Message.MessageRequestObject;
import io.stream.models.User.ChannelMute;
import io.stream.models.User.OwnUser;
import io.stream.models.User.UserRequestObject;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.ChannelService;
import io.stream.services.framework.StreamServiceGenerator;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import retrofit2.Call;

@Data
public class Channel {
  @NotNull
  @JsonProperty("id")
  private String id;

  @NotNull
  @JsonProperty("type")
  private String type;

  @NotNull
  @JsonProperty("cid")
  private String cId;

  @NotNull
  @JsonProperty("team")
  private String team;

  @NotNull
  @JsonProperty("config")
  private ChannelTypeWithCommands config;

  @Nullable
  @JsonProperty("created_by")
  private User createdBy;

  @NotNull
  @JsonProperty("frozen")
  private Boolean frozen;

  @NotNull
  @JsonProperty("member_count")
  private Integer memberCount;

  @Nullable
  @JsonProperty("members")
  private List<ChannelMember> members;

  @Nullable
  @JsonProperty("messages")
  private List<Message> messages;

  @Nullable
  @JsonProperty("read")
  private List<ChannelRead> read;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

  @NotNull
  @JsonProperty("deleted_at")
  private Date deletedAt;

  @NotNull
  @JsonProperty("last_message_at")
  private Date lastMessageAt;

  @NotNull @JsonIgnore private Map<String, Object> additionalFields;

  public Channel() {
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

  @Data
  public static class ChannelRead {
    @Nullable
    @JsonProperty("user")
    private User user;

    @Nullable
    @JsonProperty("last_read")
    private Date lastRead;

    @Nullable
    @JsonProperty("unread_messages")
    private Integer unreadMessages;

    public ChannelRead() {}
  }

  @Data
  public static class ChannelMember {
    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private User user;

    @Nullable
    @JsonProperty("is_moderator")
    private Boolean isModerator;

    @Nullable
    @JsonProperty("invited")
    private Boolean invited;

    @Nullable
    @JsonProperty("invite_accepted_at")
    private Date inviteAcceptedAt;

    @Nullable
    @JsonProperty("invite_rejected_at")
    private Date inviteRejectedAt;

    @Nullable
    @JsonProperty("role")
    private String role;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;

    @Nullable
    @JsonProperty("banned")
    private Boolean banned;

    @Nullable
    @JsonProperty("ban_expires")
    private String banExpires;

    @Nullable
    @JsonProperty("shadow_banned")
    private Boolean shadowBanned;

    public ChannelMember() {}
  }

  @Builder
  public static class ChannelRequestObject {
    @Nullable
    @JsonProperty("created_by")
    private UserRequestObject createdBy;

    @Singular @Nullable @JsonIgnore private Map<String, Object> additionalFields;

    @Nullable
    @JsonProperty("team")
    private String team;

    @Nullable
    @JsonProperty("auto_translation_enabled")
    private Boolean autoTranslationEnabled;

    @Nullable
    @JsonProperty("auto_translation_language")
    private String autoTranslationLanguage;

    @Nullable
    @JsonProperty("frozen")
    private Boolean frozen;

    @Singular
    @Nullable
    @JsonProperty("members")
    private List<ChannelMemberRequestObject> members;

    @Nullable
    @JsonProperty("config_overrides")
    private ConfigOverridesRequestObject configOverrides;
  }

  @Builder
  public static class ChannelMemberRequestObject {
    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    @Nullable
    @JsonProperty("is_moderator")
    private Boolean isModerator;

    @Nullable
    @JsonProperty("invited")
    private Boolean invited;

    @Nullable
    @JsonProperty("invite_accepted_at")
    private Date inviteAcceptedAt;

    @Nullable
    @JsonProperty("invite_rejected_at")
    private Date inviteRejectedAt;

    @Nullable
    @JsonProperty("role")
    private String role;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;

    @Nullable
    @JsonProperty("banned")
    private Boolean banned;

    @Nullable
    @JsonProperty("ban_expires")
    private String banExpires;

    @Nullable
    @JsonProperty("shadow_banned")
    private Boolean shadowBanned;
  }

  @Builder
  public static class ConfigOverridesRequestObject {
    @Nullable
    @JsonProperty("typing_events")
    private Boolean typingEvents;

    @Nullable
    @JsonProperty("reactions")
    private Boolean reactions;

    @Nullable
    @JsonProperty("replies")
    private Boolean replies;

    @Nullable
    @JsonProperty("uploads")
    private Boolean uploads;

    @Nullable
    @JsonProperty("url_enrichment")
    private Boolean urlEnrichment;

    @Nullable
    @JsonProperty("max_message_length")
    private Integer maxMessageLength;

    @Nullable
    @JsonProperty("blocklist")
    private String blocklist;

    @Nullable
    @JsonProperty("blocklist_behavior")
    private BlocklistBehavior blocklistBehavior;
  }

  @Builder
  public static class ChannelExportRequestObject {
    @NotNull
    @JsonProperty("type")
    private String type;

    @NotNull
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("messages_since")
    private Date messagesSince;

    @Nullable
    @JsonProperty("messages_until")
    private Date messagesUntil;
  }

  @Builder(
      builderClassName = "ChannelGetRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelGetRequestData {
    @Nullable
    @JsonProperty("connection_id")
    private String connectionId;

    @Nullable
    @JsonProperty("data")
    private ChannelRequestObject data;

    @Nullable
    @JsonProperty("watch")
    private Boolean watch;

    @Nullable
    @JsonProperty("state")
    private Boolean state;

    @Nullable
    @JsonProperty("presence")
    private Boolean presence;

    @Nullable
    @JsonProperty("messages")
    private MessagePaginationParameters messages;

    @Nullable
    @JsonProperty("members")
    private PaginationParameters members;

    @Nullable
    @JsonProperty("watchers")
    private PaginationParameters watchers;

    public static class ChannelGetRequest extends StreamRequest<ChannelGetResponse> {
      private String channelId;
      private String channelType;

      private ChannelGetRequest(String channelType, String channelId) {
        this.channelId = channelId;
        this.channelType = channelType;
      }

      @Override
      protected Call<ChannelGetResponse> generateCall() {
        if (this.channelId != null) {
          return StreamServiceGenerator.createService(ChannelService.class)
              .getOrCreateWithId(this.channelType, this.channelId, this.internalBuild());
        }
        return StreamServiceGenerator.createService(ChannelService.class)
            .getOrCreateWithoutId(this.channelType, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelUpdateRequestData {
    // Singular is required because cannot be empty
    @Singular
    @NotNull
    @JsonProperty("add_members")
    private List<String> addMembers;

    // Singular is required because cannot be empty
    @Singular
    @NotNull
    @JsonProperty("remove_members")
    private List<String> removeMembers;

    // Singular is required because cannot be empty
    @Singular
    @NotNull
    @JsonProperty("add_moderators")
    private List<String> addModerators;

    // Singular is required because cannot be empty
    @Singular
    @NotNull
    @JsonProperty("demote_moderators")
    private List<String> demoteModerators;

    // Singular is required because cannot be empty
    @Singular
    @NotNull
    @JsonProperty("invites")
    private List<String> invites;

    @Nullable
    @JsonProperty("cooldown")
    private Integer cooldown;

    @Nullable
    @JsonProperty("accept_invite")
    private Boolean acceptInvite;

    @Nullable
    @JsonProperty("reject_invite")
    private Boolean rejectInvite;

    @Nullable
    @JsonProperty("message")
    private MessageRequestObject message;

    @Nullable
    @JsonProperty("skip_push")
    private Boolean skipPush;

    @Nullable
    @JsonProperty("data")
    private ChannelRequestObject data;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelUpdateRequest extends StreamRequest<ChannelUpdateResponse> {
      private String channelId;
      private String channelType;

      private ChannelUpdateRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelId = channelId;
        this.channelType = channelType;
      }

      @Override
      protected Call<ChannelUpdateResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .update(this.channelType, this.channelId, this.internalBuild());
      }
    }
  }

  public static class ChannelDeleteRequest extends StreamRequest<ChannelDeleteResponse> {
    private String channelId;
    private String channelType;

    private ChannelDeleteRequest(String channelType, String channelId) {
      this.channelType = channelType;
      this.channelId = channelId;
    }

    @Override
    protected Call<ChannelDeleteResponse> generateCall() {
      return StreamServiceGenerator.createService(ChannelService.class)
          .delete(this.channelType, this.channelId);
    }
  }

  @Builder(
      builderClassName = "ChannelListRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelListRequestData {
    @Singular
    @Nullable
    @JsonProperty("filter_conditions")
    private Map<String, Object> filterConditions;

    @Singular
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

    @Nullable
    @JsonProperty("watch")
    private Boolean watch;

    @Nullable
    @JsonProperty("state")
    private Boolean state;

    @Nullable
    @JsonProperty("presence")
    private Boolean presence;

    @Nullable
    @JsonProperty("message_limit")
    private Integer messageLimit;

    @Nullable
    @JsonProperty("member_limit")
    private Integer memberLimit;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    @Nullable
    @JsonProperty("connection_id")
    private String connectionId;

    public static class ChannelListRequest extends StreamRequest<ChannelListResponse> {
      @Override
      protected Call<ChannelListResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .list(this.internalBuild());
      }
    }
  }

  public static class ChannelTruncateRequest extends StreamRequest<ChannelTruncateResponse> {
    private String channelId;
    private String channelType;

    private ChannelTruncateRequest(String channelType, String channelId) {
      this.channelType = channelType;
      this.channelId = channelId;
    }

    @Override
    protected Call<ChannelTruncateResponse> generateCall() {
      return StreamServiceGenerator.createService(ChannelService.class)
          .truncate(this.channelType, this.channelId);
    }
  }

  @Builder(
      builderClassName = "ChannelQueryMembersRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelQueryMembersRequestData {

    @Nullable
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("members")
    private ChannelMember members;

    // Singular is required because cannot be empty
    @Singular
    @Nullable
    @JsonProperty("filter_conditions")
    private Map<String, Object> filterConditions;

    @Singular
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("user_id_gte")
    private String userIdGte;

    @Nullable
    @JsonProperty("user_id_gt")
    private String userIdGt;

    @Nullable
    @JsonProperty("user_id_lte")
    private String userIdLte;

    @Nullable
    @JsonProperty("user_id_lt")
    private String userIdLt;

    @Nullable
    @JsonProperty("created_at_after_or_equal")
    private Date createdAtAfterOrEqual;

    @Nullable
    @JsonProperty("created_at_after")
    private Date createdAtAfter;

    @Nullable
    @JsonProperty("created_at_before_or_equal")
    private Date createdAtBeforeOrEqual;

    @Nullable
    @JsonProperty("created_at_before")
    private Date createdAtBefore;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelQueryMembersRequest
        extends StreamRequest<ChannelQueryMembersResponse> {
      @Override
      protected Call<ChannelQueryMembersResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .queryMembers(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelExportRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelExportRequestData {
    @Singular
    @NotNull
    @JsonProperty("channels")
    private List<ChannelExportRequestObject> channels;

    public static class ChannelExportRequest extends StreamRequest<ChannelExportResponse> {
      @Override
      protected Call<ChannelExportResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .export(this.internalBuild());
      }
    }
  }

  public static class ChannelExportStatusRequest
      extends StreamRequest<ChannelExportStatusResponse> {
    private String id;

    private ChannelExportStatusRequest(String id) {
      this.id = id;
    }

    @Override
    protected Call<ChannelExportStatusResponse> generateCall() {
      return StreamServiceGenerator.createService(ChannelService.class).exportStatus(id);
    }
  }

  @Builder(
      builderClassName = "ChannelHideRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelHideRequestData {
    @Nullable
    @JsonProperty("clear_history")
    private String clearHistory;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelHideRequest extends StreamRequest<StreamResponseObject> {
      private String channelId;
      private String channelType;

      private ChannelHideRequest(String channelType, String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .hide(this.channelType, this.channelId, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelMarkAllReadRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelMarkAllReadRequestData {
    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelMarkAllReadRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .markAllRead(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelMarkReadRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelMarkReadRequestData {
    @Nullable
    @JsonProperty("message_id")
    private String messageId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelMarkReadRequest extends StreamRequest<ChannelMarkReadResponse> {
      private String channelId;
      private String channelType;

      private ChannelMarkReadRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @Override
      protected Call<ChannelMarkReadResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .markRead(channelType, channelId, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelMuteRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelMuteRequestData {
    @Nullable
    @JsonProperty("channel_cid")
    private String channelCid;

    @Nullable
    @JsonProperty("channel_cids")
    private List<String> channelCids;

    @Nullable
    @JsonProperty("expiration")
    private Integer expiration;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelMuteRequest extends StreamRequest<ChannelMuteResponse> {

      @Override
      protected Call<ChannelMuteResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .mute(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelShowRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelShowRequestData {

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelShowRequest extends StreamRequest<StreamResponseObject> {
      private String channelId;
      private String channelType;

      private ChannelShowRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .show(this.channelType, this.channelId, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelUnMuteRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelUnMuteRequestData {
    @Nullable
    @JsonProperty("channel_cid")
    private String channelCid;

    @Nullable
    @JsonProperty("channel_cids")
    private List<String> channelCids;

    @Nullable
    @JsonProperty("expiration")
    private Integer expiration;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelUnMuteRequest extends StreamRequest<ChannelUnMuteResponse> {
      @Override
      protected Call<ChannelUnMuteResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .unmute(this.internalBuild());
      }
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelGetResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channel")
    private Channel channel;

    @Nullable
    @JsonProperty("messages")
    private List<Message> messages;

    @Nullable
    @JsonProperty("pinned_messages")
    private List<Message> pinnedMessages;

    @Nullable
    @JsonProperty("watcher_count")
    private Integer watcher_count;

    @Nullable
    @JsonProperty("watchers")
    private List<User> watchers;

    @Nullable
    @JsonProperty("read")
    private List<ChannelRead> read;

    @Nullable
    @JsonProperty("members")
    private List<ChannelMember> members;

    @Nullable
    @JsonProperty("membership")
    private ChannelMember membership;

    @Nullable
    @JsonProperty("hidden")
    private Boolean hidden;

    @Nullable
    @JsonProperty("hide_messages_before")
    private Date hideMessagesBefore;

    public ChannelGetResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelUpdateResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channel")
    private Channel channel;

    @Nullable
    @JsonProperty("message")
    private Message message;

    @Nullable
    @JsonProperty("members")
    private List<ChannelMember> members;

    public ChannelUpdateResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelDeleteResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channel")
    private Channel channel;

    public ChannelDeleteResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelListResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channels")
    private List<ChannelGetResponse> channels;

    public ChannelListResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelTruncateResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channel")
    private Channel channel;

    public ChannelTruncateResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelQueryMembersResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("members")
    private List<ChannelMember> members;

    public ChannelQueryMembersResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelExportResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("task_id")
    private String taskId;

    public ChannelExportResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelExportStatusResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("task_id")
    private String taskId;

    @NotNull
    @JsonProperty("status")
    private Status status;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @NotNull
    @JsonProperty("updated_at")
    private Date updatedAt;

    @NotNull
    @JsonProperty("duration")
    private String duration;

    @Nullable
    @JsonProperty("result")
    private ExportChannelsResult result;

    @Nullable
    @JsonProperty("error")
    private ErrorResult error;

    public ChannelExportStatusResponse() {}

    public enum Status {
      @JsonProperty("waiting")
      WAITING,
      @JsonProperty("pending")
      PENDING,
      @JsonProperty("running")
      RUNNING,
      @JsonProperty("completed")
      COMPLETED,
      @JsonProperty("failed")
      FAILED
    }

    public static class ExportChannelsResult {
      @NotNull
      @JsonProperty("url")
      private String url;

      @NotNull
      @JsonProperty("path")
      private String path;

      @NotNull
      @JsonProperty("s3_bucket_name")
      private String s3BucketName;

      public ExportChannelsResult() {}
    }

    public static class ErrorResult {
      @Nullable
      @JsonProperty("type")
      private String type;

      @Nullable
      @JsonProperty("description")
      private String description;

      @Nullable
      @JsonProperty("stacktrace")
      private String stacktrace;

      @Nullable
      @JsonProperty("version")
      private String version;

      public ErrorResult() {}
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelMarkReadResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("event")
    private Event event;

    public ChannelMarkReadResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelMuteResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channel_mute")
    private ChannelMute channelMute;

    @Nullable
    @JsonProperty("channel_mutes")
    private List<ChannelMute> channelMutes;

    @Nullable
    @JsonProperty("own_user")
    private OwnUser ownUser;

    public ChannelMuteResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelUnMuteResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channel_mute")
    private ChannelMute channelMute;

    @Nullable
    @JsonProperty("channel_mutes")
    private List<ChannelMute> channelMutes;

    @Nullable
    @JsonProperty("own_user")
    private OwnUser ownUser;

    public ChannelUnMuteResponse() {}
  }

  /**
   * Creates a get or create request
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static ChannelGetRequest getOrCreate(@NotNull String type, @NotNull String id) {
    return new ChannelGetRequest(type, id);
  }

  /**
   * Creates an update request
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static ChannelUpdateRequest update(@NotNull String type, @NotNull String id) {
    return new ChannelUpdateRequest(type, id);
  }

  /**
   * Creates a delete request
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static ChannelDeleteRequest delete(@NotNull String type, @NotNull String id) {
    return new ChannelDeleteRequest(type, id);
  }

  /**
   * Creates a list request
   *
   * @return the created request
   */
  @NotNull
  public static ChannelListRequest list() {
    return new ChannelListRequest();
  }

  /**
   * Creates a truncate request
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static ChannelTruncateRequest truncate(@NotNull String type, @NotNull String id) {
    return new ChannelTruncateRequest(type, id);
  }

  /**
   * Creates a query members request
   *
   * @return the created request
   */
  @NotNull
  public static ChannelQueryMembersRequest queryMembers() {
    return new ChannelQueryMembersRequest();
  }

  /**
   * Creates an export request
   *
   * @return the created request
   */
  @NotNull
  public static ChannelExportRequest export() {
    return new ChannelExportRequest();
  }

  /**
   * Creates an export status request
   *
   * @param taskId the id of the export task
   * @return the created request
   */
  @NotNull
  public static ChannelExportStatusRequest exportStatus(String taskId) {
    return new ChannelExportStatusRequest(taskId);
  }

  /**
   * Creates a hide request
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static ChannelHideRequest hide(@NotNull String type, @NotNull String id) {
    return new ChannelHideRequest(type, id);
  }

  /**
   * Creates a mark all read request
   *
   * @return the created request
   */
  @NotNull
  public static ChannelMarkAllReadRequest markAllRead() {
    return new ChannelMarkAllReadRequest();
  }

  /**
   * Creates a mark read request
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static ChannelMarkReadRequest markRead(@NotNull String type, @NotNull String id) {
    return new ChannelMarkReadRequest(type, id);
  }

  /**
   * Creates a mute request
   *
   * @return the created request
   */
  @NotNull
  public static ChannelMuteRequest mute() {
    return new ChannelMuteRequest();
  }

  /**
   * Creates a show request
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static ChannelShowRequest show(@NotNull String type, @NotNull String id) {
    return new ChannelShowRequest(type, id);
  }

  /**
   * Creates an unmute request
   *
   * @return the created request
   */
  @NotNull
  public static ChannelUnMuteRequest unmute() {
    return new ChannelUnMuteRequest();
  }
}
