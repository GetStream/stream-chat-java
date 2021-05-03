package io.stream.models;

import java.util.ArrayList;
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
import lombok.Data;
import lombok.EqualsAndHashCode;
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

  public static class ChannelRequestObject {

    @Nullable
    @JsonProperty("created_by")
    private UserRequestObject createdBy;

    @Nullable @JsonIgnore private Map<String, Object> additionalFields;

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

    @Nullable
    @JsonProperty("members")
    private List<ChannelMemberRequestObject> members;

    @Nullable
    @JsonProperty("config_overrides")
    private ConfigOverridesRequestObject configOverrides;

    private ChannelRequestObject(Builder builder) {
      this.createdBy = builder.createdBy;
      this.additionalFields = builder.additionalFields;
      this.team = builder.team;
      this.autoTranslationEnabled = builder.autoTranslationEnabled;
      this.autoTranslationLanguage = builder.autoTranslationLanguage;
      this.frozen = builder.frozen;
      this.members = builder.members;
      this.configOverrides = builder.configOverrides;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    /**
     * Creates builder to build {@link ChannelRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link ChannelRequestObject}. */
    public static final class Builder {
      private UserRequestObject createdBy;
      private Map<String, Object> additionalFields;
      private String team;
      private Boolean autoTranslationEnabled;
      private String autoTranslationLanguage;
      private Boolean frozen;
      private List<ChannelMemberRequestObject> members;
      private ConfigOverridesRequestObject configOverrides;

      private Builder() {}

      @NotNull
      public Builder withCreatedBy(@NotNull UserRequestObject createdBy) {
        this.createdBy = createdBy;
        return this;
      }

      @NotNull
      public Builder withAdditionalFields(@NotNull Map<String, Object> additionalFields) {
        this.additionalFields = additionalFields;
        return this;
      }

      @NotNull
      public Builder withTeam(@NotNull String team) {
        this.team = team;
        return this;
      }

      @NotNull
      public Builder withAutoTranslationEnabled(@NotNull Boolean autoTranslationEnabled) {
        this.autoTranslationEnabled = autoTranslationEnabled;
        return this;
      }

      @NotNull
      public Builder withAutoTranslationLanguage(@NotNull String autoTranslationLanguage) {
        this.autoTranslationLanguage = autoTranslationLanguage;
        return this;
      }

      @NotNull
      public Builder withFrozen(@NotNull Boolean frozen) {
        this.frozen = frozen;
        return this;
      }

      @NotNull
      public Builder withMembers(@NotNull List<ChannelMemberRequestObject> members) {
        this.members = members;
        return this;
      }

      @NotNull
      public Builder withConfigOverrides(@NotNull ConfigOverridesRequestObject configOverrides) {
        this.configOverrides = configOverrides;
        return this;
      }

      @NotNull
      public ChannelRequestObject build() {
        return new ChannelRequestObject(this);
      }
    }
  }

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

    private ChannelMemberRequestObject(Builder builder) {
      this.userId = builder.userId;
      this.user = builder.user;
      this.isModerator = builder.isModerator;
      this.invited = builder.invited;
      this.inviteAcceptedAt = builder.inviteAcceptedAt;
      this.inviteRejectedAt = builder.inviteRejectedAt;
      this.role = builder.role;
      this.createdAt = builder.createdAt;
      this.updatedAt = builder.updatedAt;
      this.banned = builder.banned;
      this.banExpires = builder.banExpires;
      this.shadowBanned = builder.shadowBanned;
    }

    /**
     * Creates builder to build {@link ChannelMemberRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link ChannelMemberRequestObject}. */
    public static final class Builder {
      private String userId;
      private UserRequestObject user;
      private Boolean isModerator;
      private Boolean invited;
      private Date inviteAcceptedAt;
      private Date inviteRejectedAt;
      private String role;
      private Date createdAt;
      private Date updatedAt;
      private Boolean banned;
      private String banExpires;
      private Boolean shadowBanned;

      private Builder() {}

      @NotNull
      public Builder withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public Builder withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @NotNull
      public Builder withIsModerator(@NotNull Boolean isModerator) {
        this.isModerator = isModerator;
        return this;
      }

      @NotNull
      public Builder withInvited(@NotNull Boolean invited) {
        this.invited = invited;
        return this;
      }

      @NotNull
      public Builder withInviteAcceptedAt(@NotNull Date inviteAcceptedAt) {
        this.inviteAcceptedAt = inviteAcceptedAt;
        return this;
      }

      @NotNull
      public Builder withInviteRejectedAt(@NotNull Date inviteRejectedAt) {
        this.inviteRejectedAt = inviteRejectedAt;
        return this;
      }

      @NotNull
      public Builder withRole(@NotNull String role) {
        this.role = role;
        return this;
      }

      @NotNull
      public Builder withCreatedAt(@NotNull Date createdAt) {
        this.createdAt = createdAt;
        return this;
      }

      @NotNull
      public Builder withUpdatedAt(@NotNull Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
      }

      @NotNull
      public Builder withBanned(@NotNull Boolean banned) {
        this.banned = banned;
        return this;
      }

      @NotNull
      public Builder withBanExpires(@NotNull String banExpires) {
        this.banExpires = banExpires;
        return this;
      }

      @NotNull
      public Builder withShadowBanned(@NotNull Boolean shadowBanned) {
        this.shadowBanned = shadowBanned;
        return this;
      }

      @NotNull
      public ChannelMemberRequestObject build() {
        return new ChannelMemberRequestObject(this);
      }
    }
  }

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

    private ConfigOverridesRequestObject(Builder builder) {
      this.typingEvents = builder.typingEvents;
      this.reactions = builder.reactions;
      this.replies = builder.replies;
      this.uploads = builder.uploads;
      this.urlEnrichment = builder.urlEnrichment;
      this.maxMessageLength = builder.maxMessageLength;
      this.blocklist = builder.blocklist;
      this.blocklistBehavior = builder.blocklistBehavior;
    }

    /**
     * Creates builder to build {@link ConfigOverridesRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link ConfigOverridesRequestObject}. */
    public static final class Builder {
      private Boolean typingEvents;
      private Boolean reactions;
      private Boolean replies;
      private Boolean uploads;
      private Boolean urlEnrichment;
      private Integer maxMessageLength;
      private String blocklist;
      private BlocklistBehavior blocklistBehavior;

      private Builder() {}

      @NotNull
      public Builder withTypingEvents(@NotNull Boolean typingEvents) {
        this.typingEvents = typingEvents;
        return this;
      }

      @NotNull
      public Builder withReactions(@NotNull Boolean reactions) {
        this.reactions = reactions;
        return this;
      }

      @NotNull
      public Builder withReplies(@NotNull Boolean replies) {
        this.replies = replies;
        return this;
      }

      @NotNull
      public Builder withUploads(@NotNull Boolean uploads) {
        this.uploads = uploads;
        return this;
      }

      @NotNull
      public Builder withUrlEnrichment(@NotNull Boolean urlEnrichment) {
        this.urlEnrichment = urlEnrichment;
        return this;
      }

      @NotNull
      public Builder withMaxMessageLength(@NotNull Integer maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
        return this;
      }

      @NotNull
      public Builder withBlocklist(@NotNull String blocklist) {
        this.blocklist = blocklist;
        return this;
      }

      @NotNull
      public Builder withBlocklistBehavior(@NotNull BlocklistBehavior blocklistBehavior) {
        this.blocklistBehavior = blocklistBehavior;
        return this;
      }

      @NotNull
      public ConfigOverridesRequestObject build() {
        return new ConfigOverridesRequestObject(this);
      }
    }
  }

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

    private ChannelExportRequestObject(Builder builder) {
      this.type = builder.type;
      this.id = builder.id;
      this.messagesSince = builder.messagesSince;
      this.messagesUntil = builder.messagesUntil;
    }

    /**
     * Creates builder to build {@link ChannelExportRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link ChannelExportRequestObject}. */
    public static final class Builder {
      private String type;
      private String id;
      private Date messagesSince;
      private Date messagesUntil;

      private Builder() {}

      @NotNull
      public Builder withType(@NotNull String type) {
        this.type = type;
        return this;
      }

      @NotNull
      public Builder withId(@NotNull String id) {
        this.id = id;
        return this;
      }

      @NotNull
      public Builder withMessagesSince(@NotNull Date messagesSince) {
        this.messagesSince = messagesSince;
        return this;
      }

      @NotNull
      public Builder withMessagesUntil(@NotNull Date messagesUntil) {
        this.messagesUntil = messagesUntil;
        return this;
      }

      @NotNull
      public ChannelExportRequestObject build() {
        return new ChannelExportRequestObject(this);
      }
    }
  }

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

    private ChannelGetRequestData(ChannelGetRequest channelGetRequest) {
      this.connectionId = channelGetRequest.connectionId;
      this.data = channelGetRequest.data;
      this.watch = channelGetRequest.watch;
      this.state = channelGetRequest.state;
      this.presence = channelGetRequest.presence;
      this.messages = channelGetRequest.messages;
      this.members = channelGetRequest.members;
      this.watchers = channelGetRequest.watchers;
    }

    public static class ChannelGetRequest extends StreamRequest<ChannelGetResponse> {
      private String channelId;
      private String channelType;
      private String connectionId;
      private ChannelRequestObject data;
      private Boolean watch;
      private Boolean state;
      private Boolean presence;
      private MessagePaginationParameters messages;
      private PaginationParameters members;
      private PaginationParameters watchers;

      private ChannelGetRequest(String channelType, String channelId) {
        this.channelId = channelId;
        this.channelType = channelType;
      }

      @NotNull
      public ChannelGetRequest withConnectionId(@NotNull String connectionId) {
        this.connectionId = connectionId;
        return this;
      }

      @NotNull
      public ChannelGetRequest withData(@NotNull ChannelRequestObject data) {
        this.data = data;
        return this;
      }

      @NotNull
      public ChannelGetRequest withWatch(@NotNull Boolean watch) {
        this.watch = watch;
        return this;
      }

      @NotNull
      public ChannelGetRequest withState(@NotNull Boolean state) {
        this.state = state;
        return this;
      }

      @NotNull
      public ChannelGetRequest withPresence(@NotNull Boolean presence) {
        this.presence = presence;
        return this;
      }

      @NotNull
      public ChannelGetRequest withMessages(@NotNull MessagePaginationParameters messages) {
        this.messages = messages;
        return this;
      }

      @NotNull
      public ChannelGetRequest withMembers(@NotNull PaginationParameters members) {
        this.members = members;
        return this;
      }

      @NotNull
      public ChannelGetRequest withWatchers(@NotNull PaginationParameters watchers) {
        this.watchers = watchers;
        return this;
      }

      @Override
      protected Call<ChannelGetResponse> generateCall() {
        if (this.channelId != null) {
          return StreamServiceGenerator.createService(ChannelService.class)
              .getOrCreateWithId(this.channelType, this.channelId, new ChannelGetRequestData(this));
        }
        return StreamServiceGenerator.createService(ChannelService.class)
            .getOrCreateWithoutId(this.channelType, new ChannelGetRequestData(this));
      }
    }
  }

  public static class ChannelUpdateRequestData {

    @NotNull
    @JsonProperty("add_members")
    private List<String> addMembers;

    @NotNull
    @JsonProperty("remove_members")
    private List<String> removeMembers;

    @NotNull
    @JsonProperty("add_moderators")
    private List<String> addModerators;

    @NotNull
    @JsonProperty("demote_moderators")
    private List<String> demoteModerators;

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

    private ChannelUpdateRequestData(ChannelUpdateRequest channelUpdateRequest) {
      this.addMembers = channelUpdateRequest.addMembers;
      this.removeMembers = channelUpdateRequest.removeMembers;
      this.addModerators = channelUpdateRequest.addModerators;
      this.demoteModerators = channelUpdateRequest.demoteModerators;
      this.invites = channelUpdateRequest.invites;
      this.cooldown = channelUpdateRequest.cooldown;
      this.acceptInvite = channelUpdateRequest.acceptInvite;
      this.rejectInvite = channelUpdateRequest.rejectInvite;
      this.message = channelUpdateRequest.message;
      this.skipPush = channelUpdateRequest.skipPush;
      this.data = channelUpdateRequest.data;
      this.userId = channelUpdateRequest.userId;
      this.user = channelUpdateRequest.user;
    }

    public static class ChannelUpdateRequest extends StreamRequest<ChannelUpdateResponse> {
      private String channelId;
      private String channelType;
      private List<String> addMembers;
      private List<String> removeMembers;
      private List<String> addModerators;
      private List<String> demoteModerators;
      private List<String> invites;
      private Integer cooldown;
      private Boolean acceptInvite;
      private Boolean rejectInvite;
      private MessageRequestObject message;
      private Boolean skipPush;
      private ChannelRequestObject data;
      private String userId;
      private UserRequestObject user;

      private ChannelUpdateRequest(String channelType, String channelId) {
        this.channelId = channelId;
        this.channelType = channelType;
      }

      @NotNull
      public ChannelUpdateRequest withAddMembers(@NotNull List<String> addMembers) {
        this.addMembers = addMembers;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withRemoveMembers(@NotNull List<String> removeMembers) {
        this.removeMembers = removeMembers;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withAddModerators(@NotNull List<String> addModerators) {
        this.addModerators = addModerators;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withDemoteModerators(@NotNull List<String> demoteModerators) {
        this.demoteModerators = demoteModerators;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withInvites(@NotNull List<String> invites) {
        this.invites = invites;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withCooldown(@NotNull Integer cooldown) {
        this.cooldown = cooldown;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withAcceptInvite(@NotNull Boolean acceptInvite) {
        this.acceptInvite = acceptInvite;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withRejectInvite(@NotNull Boolean rejectInvite) {
        this.rejectInvite = rejectInvite;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withMessage(@NotNull MessageRequestObject message) {
        this.message = message;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withSkipPush(@NotNull Boolean skipPush) {
        this.skipPush = skipPush;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withData(@NotNull ChannelRequestObject data) {
        this.data = data;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<ChannelUpdateResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .update(this.channelType, this.channelId, new ChannelUpdateRequestData(this));
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

  public static class ChannelListRequestData {
    @Nullable
    @JsonProperty("filter_conditions")
    private Map<String, Object> filterConditions;

    @Nullable
    @JsonProperty("sort")
    private List<Sort> sort;

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

    private ChannelListRequestData(ChannelListRequest channelListRequest) {
      this.filterConditions = channelListRequest.filterConditions;
      this.sort = channelListRequest.sort;
      this.watch = channelListRequest.watch;
      this.state = channelListRequest.state;
      this.presence = channelListRequest.presence;
      this.messageLimit = channelListRequest.messageLimit;
      this.memberLimit = channelListRequest.memberLimit;
      this.limit = channelListRequest.limit;
      this.offset = channelListRequest.offset;
      this.userId = channelListRequest.userId;
      this.user = channelListRequest.user;
      this.connectionId = channelListRequest.connectionId;
    }

    public static class ChannelListRequest extends StreamRequest<ChannelListResponse> {
      private Map<String, Object> filterConditions;
      private List<Sort> sort;
      private Boolean watch;
      private Boolean state;
      private Boolean presence;
      private Integer messageLimit;
      private Integer memberLimit;
      private Integer limit;
      private Integer offset;
      private String userId;
      private UserRequestObject user;
      private String connectionId;

      private ChannelListRequest() {}

      @NotNull
      public ChannelListRequest withFilterConditions(
          @NotNull Map<String, Object> filterConditions) {
        this.filterConditions = filterConditions;
        return this;
      }

      @NotNull
      public ChannelListRequest withSort(@NotNull List<Sort> sort) {
        this.sort = sort;
        return this;
      }

      @NotNull
      public ChannelListRequest withWatch(@NotNull Boolean watch) {
        this.watch = watch;
        return this;
      }

      @NotNull
      public ChannelListRequest withState(@NotNull Boolean state) {
        this.state = state;
        return this;
      }

      @NotNull
      public ChannelListRequest withPresence(@NotNull Boolean presence) {
        this.presence = presence;
        return this;
      }

      @NotNull
      public ChannelListRequest withMessageLimit(@NotNull Integer messageLimit) {
        this.messageLimit = messageLimit;
        return this;
      }

      @NotNull
      public ChannelListRequest withMemberLimit(@NotNull Integer memberLimit) {
        this.memberLimit = memberLimit;
        return this;
      }

      @NotNull
      public ChannelListRequest withLimit(@NotNull Integer limit) {
        this.limit = limit;
        return this;
      }

      @NotNull
      public ChannelListRequest withOffset(@NotNull Integer offset) {
        this.offset = offset;
        return this;
      }

      @NotNull
      public ChannelListRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public ChannelListRequest withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @NotNull
      public ChannelListRequest withConnectionId(@NotNull String connectionId) {
        this.connectionId = connectionId;
        return this;
      }

      @Override
      protected Call<ChannelListResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .list(new ChannelListRequestData(this));
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

    @Nullable
    @JsonProperty("filter_conditions")
    private Map<String, Object> filterConditions;

    @Nullable
    @JsonProperty("sort")
    private List<Sort> sort;

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

    private ChannelQueryMembersRequestData(ChannelQueryMembersRequest channelQueryMembersRequest) {
      this.type = channelQueryMembersRequest.type;
      this.id = channelQueryMembersRequest.id;
      this.members = channelQueryMembersRequest.members;
      this.filterConditions = channelQueryMembersRequest.filterConditions;
      this.sort = channelQueryMembersRequest.sort;
      this.limit = channelQueryMembersRequest.limit;
      this.offset = channelQueryMembersRequest.offset;
      this.userIdGte = channelQueryMembersRequest.userIdGte;
      this.userIdGt = channelQueryMembersRequest.userIdGt;
      this.userIdLte = channelQueryMembersRequest.userIdLte;
      this.userIdLt = channelQueryMembersRequest.userIdLt;
      this.createdAtAfterOrEqual = channelQueryMembersRequest.createdAtAfterOrEqual;
      this.createdAtAfter = channelQueryMembersRequest.createdAtAfter;
      this.createdAtBeforeOrEqual = channelQueryMembersRequest.createdAtBeforeOrEqual;
      this.createdAtBefore = channelQueryMembersRequest.createdAtBefore;
      this.userId = channelQueryMembersRequest.userId;
      this.user = channelQueryMembersRequest.user;
    }

    public static class ChannelQueryMembersRequest
        extends StreamRequest<ChannelQueryMembersResponse> {
      private String type;
      private String id;
      private ChannelMember members;
      private Map<String, Object> filterConditions = Collections.emptyMap();
      private List<Sort> sort;
      private Integer limit;
      private Integer offset;
      private String userIdGte;
      private String userIdGt;
      private String userIdLte;
      private String userIdLt;
      private Date createdAtAfterOrEqual;
      private Date createdAtAfter;
      private Date createdAtBeforeOrEqual;
      private Date createdAtBefore;
      private String userId;
      private UserRequestObject user;

      private ChannelQueryMembersRequest() {}

      @NotNull
      public ChannelQueryMembersRequest withType(@NotNull String type) {
        this.type = type;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withId(@NotNull String id) {
        this.id = id;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withMembers(@NotNull ChannelMember members) {
        this.members = members;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withFilterConditions(
          @NotNull Map<String, Object> filterConditions) {
        this.filterConditions = filterConditions;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withSort(@NotNull List<Sort> sort) {
        this.sort = sort;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withLimit(@NotNull Integer limit) {
        this.limit = limit;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withOffset(@NotNull Integer offset) {
        this.offset = offset;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withUserIdGte(@NotNull String userIdGte) {
        this.userIdGte = userIdGte;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withUserIdGt(@NotNull String userIdGt) {
        this.userIdGt = userIdGt;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withUserIdLte(@NotNull String userIdLte) {
        this.userIdLte = userIdLte;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withUserIdLt(@NotNull String userIdLt) {
        this.userIdLt = userIdLt;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withCreatedAtAfterOrEqual(
          @NotNull Date createdAtAfterOrEqual) {
        this.createdAtAfterOrEqual = createdAtAfterOrEqual;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withCreatedAtAfter(@NotNull Date createdAtAfter) {
        this.createdAtAfter = createdAtAfter;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withCreatedAtBeforeOrEqual(
          @NotNull Date createdAtBeforeOrEqual) {
        this.createdAtBeforeOrEqual = createdAtBeforeOrEqual;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withCreatedAtBefore(@NotNull Date createdAtBefore) {
        this.createdAtBefore = createdAtBefore;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public ChannelQueryMembersRequest withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<ChannelQueryMembersResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .queryMembers(new ChannelQueryMembersRequestData(this));
      }
    }
  }

  public static class ChannelExportRequestData {
    @NotNull
    @JsonProperty("channels")
    private List<ChannelExportRequestObject> channels;

    private ChannelExportRequestData(ChannelExportRequest channelExportRequest) {
      this.channels = channelExportRequest.channels;
    }

    public static final class ChannelExportRequest extends StreamRequest<ChannelExportResponse> {
      private List<ChannelExportRequestObject> channels = new ArrayList<>();;

      private ChannelExportRequest() {}

      @NotNull
      public ChannelExportRequest withChannels(@NotNull List<ChannelExportRequestObject> channels) {
        this.channels = channels;
        return this;
      }

      @NotNull
      public ChannelExportRequest addChannel(@NotNull ChannelExportRequestObject channel) {
        this.channels.add(channel);
        return this;
      }

      @Override
      protected Call<ChannelExportResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .export(new ChannelExportRequestData(this));
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

    private ChannelHideRequestData(ChannelHideRequest channelHideRequestData) {
      this.clearHistory = channelHideRequestData.clearHistory;
      this.userId = channelHideRequestData.userId;
      this.user = channelHideRequestData.user;
    }

    public static final class ChannelHideRequest extends StreamRequest<StreamResponseObject> {
      private String channelId;
      private String channelType;
      private String clearHistory;
      private String userId;
      private UserRequestObject user;

      private ChannelHideRequest(String channelType, String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @NotNull
      public ChannelHideRequest withClearHistory(@NotNull String clearHistory) {
        this.clearHistory = clearHistory;
        return this;
      }

      @NotNull
      public ChannelHideRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public ChannelHideRequest withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .hide(this.channelType, this.channelId, new ChannelHideRequestData(this));
      }
    }
  }

  public static class ChannelMarkAllReadRequestData {
    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    private ChannelMarkAllReadRequestData(ChannelMarkAllReadRequest channelMarkAllReadRequest) {
      this.userId = channelMarkAllReadRequest.userId;
      this.user = channelMarkAllReadRequest.user;
    }

    public static final class ChannelMarkAllReadRequest
        extends StreamRequest<StreamResponseObject> {
      private String userId;
      private UserRequestObject user;

      private ChannelMarkAllReadRequest() {}

      @NotNull
      public ChannelMarkAllReadRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public ChannelMarkAllReadRequest withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .markAllRead(new ChannelMarkAllReadRequestData(this));
      }
    }
  }

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

    private ChannelMarkReadRequestData(ChannelMarkReadRequest channelMarkReadRequest) {
      this.messageId = channelMarkReadRequest.messageId;
      this.userId = channelMarkReadRequest.userId;
      this.user = channelMarkReadRequest.user;
    }

    public static final class ChannelMarkReadRequest
        extends StreamRequest<ChannelMarkReadResponse> {
      private String channelId;
      private String channelType;
      private String messageId;
      private String userId;
      private UserRequestObject user;

      private ChannelMarkReadRequest(String channelType, String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @NotNull
      public ChannelMarkReadRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public ChannelMarkReadRequest withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<ChannelMarkReadResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .markRead(channelType, channelId, new ChannelMarkReadRequestData(this));
      }
    }
  }

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

    private ChannelMuteRequestData(ChannelMuteRequest channelMuteRequest) {
      this.channelCid = channelMuteRequest.channelCid;
      this.channelCids = channelMuteRequest.channelCids;
      this.expiration = channelMuteRequest.expiration;
      this.userId = channelMuteRequest.userId;
      this.user = channelMuteRequest.user;
    }

    public static final class ChannelMuteRequest extends StreamRequest<ChannelMuteResponse> {
      private String channelCid;
      private List<String> channelCids;
      private Integer expiration;
      private String userId;
      private UserRequestObject user;

      private ChannelMuteRequest() {}

      @NotNull
      public ChannelMuteRequest withChannelCid(@NotNull String channelCid) {
        this.channelCid = channelCid;
        return this;
      }

      @NotNull
      public ChannelMuteRequest withChannelCids(@NotNull List<String> channelCids) {
        this.channelCids = channelCids;
        return this;
      }

      @NotNull
      public ChannelMuteRequest withExpiration(@NotNull Integer expiration) {
        this.expiration = expiration;
        return this;
      }

      @NotNull
      public ChannelMuteRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public ChannelMuteRequest withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<ChannelMuteResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .mute(new ChannelMuteRequestData(this));
      }
    }
  }

  public static class ChannelShowRequestData {

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    private ChannelShowRequestData(ChannelShowRequest channelShowRequestData) {
      this.userId = channelShowRequestData.userId;
      this.user = channelShowRequestData.user;
    }

    public static final class ChannelShowRequest extends StreamRequest<StreamResponseObject> {
      private String channelId;
      private String channelType;
      private String userId;
      private UserRequestObject user;

      private ChannelShowRequest(String channelType, String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @NotNull
      public ChannelShowRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public ChannelShowRequest withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .show(this.channelType, this.channelId, new ChannelShowRequestData(this));
      }
    }
  }

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

    private ChannelUnMuteRequestData(ChannelUnMuteRequest channelUnMuteRequest) {
      this.channelCid = channelUnMuteRequest.channelCid;
      this.channelCids = channelUnMuteRequest.channelCids;
      this.expiration = channelUnMuteRequest.expiration;
      this.userId = channelUnMuteRequest.userId;
      this.user = channelUnMuteRequest.user;
    }

    public static final class ChannelUnMuteRequest extends StreamRequest<ChannelUnMuteResponse> {
      private String channelCid;
      private List<String> channelCids;
      private Integer expiration;
      private String userId;
      private UserRequestObject user;

      private ChannelUnMuteRequest() {}

      @NotNull
      public ChannelUnMuteRequest withChannelCid(@NotNull String channelCid) {
        this.channelCid = channelCid;
        return this;
      }

      @NotNull
      public ChannelUnMuteRequest withChannelCids(@NotNull List<String> channelCids) {
        this.channelCids = channelCids;
        return this;
      }

      @NotNull
      public ChannelUnMuteRequest withExpiration(@NotNull Integer expiration) {
        this.expiration = expiration;
        return this;
      }

      @NotNull
      public ChannelUnMuteRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public ChannelUnMuteRequest withUser(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<ChannelUnMuteResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelService.class)
            .unmute(new ChannelUnMuteRequestData(this));
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
