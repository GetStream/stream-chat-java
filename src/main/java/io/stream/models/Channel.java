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
import io.stream.exceptions.StreamException;
import io.stream.models.Channel.ChannelGetRequestData.ChannelGetRequest;
import io.stream.models.Channel.ChannelListRequestData.ChannelListRequest;
import io.stream.models.Channel.ChannelUpdateRequestData.ChannelUpdateRequest;
import io.stream.models.ChannelConfig.BlocklistBehavior;
import io.stream.models.ChannelConfig.ChannelConfigWithCommands;
import io.stream.models.Message.MessageRequest;
import io.stream.models.User.UserRequest;
import io.stream.models.framework.StreamResponse;
import io.stream.services.ChannelService;
import io.stream.services.framework.StreamServiceGenerator;
import io.stream.services.framework.StreamServiceHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Channel {
  public Channel() {
    additionalFields = new HashMap<>();
  }

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
  private ChannelConfigWithCommands config;

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

  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return this.additionalFields;
  }

  @JsonAnySetter
  public void setAdditionalField(String name, Object value) {
    this.additionalFields.put(name, value);
  }

  @Data
  public static class ChannelGetRequestData {
    public ChannelGetRequestData() {}

    @Nullable
    @JsonProperty("connection_id")
    private String connectionId;

    private ChannelRequest data;

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

    private ChannelGetRequestData(ChannelGetRequest builder) {
      this.connectionId = builder.connectionId;
      this.data = builder.data;
      this.watch = builder.watch;
      this.state = builder.state;
      this.presence = builder.presence;
      this.messages = builder.messages;
      this.members = builder.members;
      this.watchers = builder.watchers;
    }

    /**
     * Creates builder to build {@link ChannelGetRequestData}.
     *
     * @return created builder
     */
    public static ChannelGetRequest builder(@NotNull String type, @NotNull String id) {
      return new ChannelGetRequest(type, id);
    }

    /** Builder to build {@link ChannelGetRequestData}. */
    public static final class ChannelGetRequest {
      private String channelId;
      private String channelType;
      private String connectionId;
      private ChannelRequest data;
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
      public ChannelGetRequest withData(@NotNull ChannelRequest data) {
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

      /**
       * Executes the request
       *
       * @return the channel get response
       * @throws StreamException when IO problem occurs or the stream API return an error
       */
      @NotNull
      public ChannelGetResponse request() throws StreamException {
        if (this.channelId != null) {
          return new StreamServiceHandler()
              .handle(
                  StreamServiceGenerator.createService(ChannelService.class)
                      .getOrCreateWithId(
                          this.channelType, this.channelId, new ChannelGetRequestData(this)));
        }
        return new StreamServiceHandler()
            .handle(
                StreamServiceGenerator.createService(ChannelService.class)
                    .getOrCreateWithoutId(this.channelType, new ChannelGetRequestData(this)));
      }
    }
  }

  @Data
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
    private UserRequest user;

    @Nullable
    @JsonProperty("connection_id")
    private String connectionId;

    private ChannelListRequestData(ChannelListRequest builder) {
      this.filterConditions = builder.filterConditions;
      this.sort = builder.sort;
      this.watch = builder.watch;
      this.state = builder.state;
      this.presence = builder.presence;
      this.messageLimit = builder.messageLimit;
      this.memberLimit = builder.memberLimit;
      this.limit = builder.limit;
      this.offset = builder.offset;
      this.userId = builder.userId;
      this.user = builder.user;
      this.connectionId = builder.connectionId;
    }

    /**
     * Creates builder to build {@link ChannelListRequestData}.
     *
     * @return created builder
     */
    public static ChannelListRequest builder() {
      return new ChannelListRequest();
    }

    /** Builder to build {@link ChannelListRequestData}. */
    public static final class ChannelListRequest {
      private Map<String, Object> filterConditions = Collections.emptyMap();
      private List<Sort> sort = Collections.emptyList();
      private Boolean watch;
      private Boolean state;
      private Boolean presence;
      private Integer messageLimit;
      private Integer memberLimit;
      private Integer limit;
      private Integer offset;
      private String userId;
      private UserRequest user;
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
      public ChannelListRequest withUser(@NotNull UserRequest user) {
        this.user = user;
        return this;
      }

      @NotNull
      public ChannelListRequest withConnectionId(@NotNull String connectionId) {
        this.connectionId = connectionId;
        return this;
      }

      /**
       * Executes the request
       *
       * @return the channel list response
       * @throws StreamException when IO problem occurs or the stream API return an error
       */
      @NotNull
      public ChannelListResponse request() throws StreamException {
        return new StreamServiceHandler()
            .handle(
                StreamServiceGenerator.createService(ChannelService.class)
                    .list(new ChannelListRequestData(this)));
      }
    }
  }

  @Data
  public static class ChannelUpdateRequestData {
    public ChannelUpdateRequestData() {}

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
    private MessageRequest message;

    @Nullable
    @JsonProperty("skip_push")
    private Boolean skipPush;

    @Nullable
    @JsonProperty("data")
    private ChannelRequest data;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequest user;

    private ChannelUpdateRequestData(ChannelUpdateRequest builder) {
      this.addMembers = builder.addMembers;
      this.removeMembers = builder.removeMembers;
      this.addModerators = builder.addModerators;
      this.demoteModerators = builder.demoteModerators;
      this.invites = builder.invites;
      this.cooldown = builder.cooldown;
      this.acceptInvite = builder.acceptInvite;
      this.rejectInvite = builder.rejectInvite;
      this.message = builder.message;
      this.skipPush = builder.skipPush;
      this.data = builder.data;
      this.userId = builder.userId;
      this.user = builder.user;
    }

    /**
     * Creates builder to build {@link ChannelUpdateRequestData}.
     *
     * @return created builder
     */
    public static ChannelUpdateRequest builder(@NotNull String type, @NotNull String id) {
      return new ChannelUpdateRequest(type, id);
    }

    /** Builder to build {@link ChannelUpdateRequestData}. */
    public static final class ChannelUpdateRequest {
      private String channelId;
      private String channelType;
      private List<String> addMembers = Collections.emptyList();
      private List<String> removeMembers = Collections.emptyList();
      private List<String> addModerators = Collections.emptyList();
      private List<String> demoteModerators = Collections.emptyList();
      private List<String> invites = Collections.emptyList();
      private Integer cooldown;
      private Boolean acceptInvite;
      private Boolean rejectInvite;
      private MessageRequest message;
      private Boolean skipPush;
      private ChannelRequest data;
      private String userId;
      private UserRequest user;

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
      public ChannelUpdateRequest withMessage(@NotNull MessageRequest message) {
        this.message = message;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withSkipPush(@NotNull Boolean skipPush) {
        this.skipPush = skipPush;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withData(@NotNull ChannelRequest data) {
        this.data = data;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public ChannelUpdateRequest withUser(@NotNull UserRequest user) {
        this.user = user;
        return this;
      }

      /**
       * Executes the request
       *
       * @return the channel update response
       * @throws StreamException when IO problem occurs or the stream API return an error
       */
      @NotNull
      public ChannelUpdateResponse request() throws StreamException {
        return new StreamServiceHandler()
            .handle(
                StreamServiceGenerator.createService(ChannelService.class)
                    .update(this.channelType, this.channelId, new ChannelUpdateRequestData(this)));
      }
    }
  }

  @Data
  public static class ChannelRequest {
    public ChannelRequest() {
      additionalFields = new HashMap<>();
    }

    @Nullable
    @JsonProperty("created_by")
    private UserRequest createdBy;

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
    private List<ChannelMemberRequest> members;

    @Nullable
    @JsonProperty("config_overrides")
    private ConfigOverridesRequest configOverrides;

    private ChannelRequest(Builder builder) {
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

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
      this.additionalFields.put(name, value);
    }

    /**
     * Creates builder to build {@link ChannelRequest}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link ChannelRequest}. */
    public static final class Builder {
      private UserRequest createdBy;
      private Map<String, Object> additionalFields = Collections.emptyMap();
      private String team;
      private Boolean autoTranslationEnabled;
      private String autoTranslationLanguage;
      private Boolean frozen;
      private List<ChannelMemberRequest> members = Collections.emptyList();
      private ConfigOverridesRequest configOverrides;

      private Builder() {}

      @NotNull
      public Builder withCreatedBy(@NotNull UserRequest createdBy) {
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
      public Builder withMembers(@NotNull List<ChannelMemberRequest> members) {
        this.members = members;
        return this;
      }

      @NotNull
      public Builder withConfigOverrides(@NotNull ConfigOverridesRequest configOverrides) {
        this.configOverrides = configOverrides;
        return this;
      }

      @NotNull
      public ChannelRequest build() {
        return new ChannelRequest(this);
      }
    }
  }

  @Data
  public static class ChannelMemberRequest {

    public ChannelMemberRequest() {}

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequest user;

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

    private ChannelMemberRequest(Builder builder) {
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
     * Creates builder to build {@link ChannelMemberRequest}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link ChannelMemberRequest}. */
    public static final class Builder {
      private String userId;
      private UserRequest user;
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
      public Builder withUser(@NotNull UserRequest user) {
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
      public ChannelMemberRequest build() {
        return new ChannelMemberRequest(this);
      }
    }
  }

  public static class ConfigOverridesRequest {

    public ConfigOverridesRequest() {}

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

  @Data
  public static class ChannelDeleteRequest {
    private String channelId;
    private String channelType;

    private ChannelDeleteRequest(String channelType, String channelId) {
      this.channelType = channelType;
      this.channelId = channelId;
    }

    /**
     * Executes the request
     *
     * @return the channel in a channel delete response
     * @throws StreamException when IO problem occurs or the stream API return an error
     */
    @NotNull
    public ChannelDeleteResponse request() throws StreamException {
      return new StreamServiceHandler()
          .handle(
              StreamServiceGenerator.createService(ChannelService.class)
                  .delete(this.channelType, this.channelId));
    }
  }

  @Data
  public static class ChannelTruncateRequest {
    private String channelId;
    private String channelType;

    private ChannelTruncateRequest(String channelType, String channelId) {
      this.channelType = channelType;
      this.channelId = channelId;
    }

    /**
     * Executes the request
     *
     * @return the channel in a channel truncate response
     * @throws StreamException when IO problem occurs or the stream API return an error
     */
    @NotNull
    public ChannelTruncateResponse request() throws StreamException {
      return new StreamServiceHandler()
          .handle(
              StreamServiceGenerator.createService(ChannelService.class)
                  .truncate(this.channelType, this.channelId));
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelGetResponse extends StreamResponse {
    public ChannelGetResponse() {}

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
  }

  @Data
  public static class ChannelRead {
    public ChannelRead() {}

    @Nullable
    @JsonProperty("user")
    private User user;

    @Nullable
    @JsonProperty("last_read")
    private Date lastRead;

    @Nullable
    @JsonProperty("unread_messages")
    private Integer unreadMessages;
  }

  @Data
  public static class ChannelMember {
    public ChannelMember() {}

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
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelUpdateResponse extends StreamResponse {
    public ChannelUpdateResponse() {}

    @Nullable
    @JsonProperty("channel")
    private Channel channel;

    @Nullable
    @JsonProperty("message")
    private Message message;

    @Nullable
    @JsonProperty("members")
    private List<ChannelMember> members;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelDeleteResponse extends StreamResponse {
    public ChannelDeleteResponse() {}

    @Nullable
    @JsonProperty("channel")
    private Channel channel;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelTruncateResponse extends StreamResponse {
    public ChannelTruncateResponse() {}

    @Nullable
    @JsonProperty("channel")
    private Channel channel;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelListResponse extends StreamResponse {
    public ChannelListResponse() {}

    @Nullable
    @JsonProperty("channels")
    private List<ChannelGetResponse> channels;
  }

  /**
   * Creates a get or create request
   *
   * @return the created request
   */
  public static ChannelGetRequest getOrCreate(@NotNull String type, @NotNull String id) {
    return ChannelGetRequestData.builder(type, id);
  }

  /**
   * Creates an update request
   *
   * @return the created request
   */
  public static ChannelUpdateRequest update(@NotNull String type, @NotNull String id) {
    return ChannelUpdateRequestData.builder(type, id);
  }

  /**
   * Creates a delete request
   *
   * @return the created request
   */
  public static ChannelDeleteRequest delete(@NotNull String type, @NotNull String id) {
    return new ChannelDeleteRequest(type, id);
  }

  /**
   * Creates a list request
   *
   * @return the created request
   */
  public static ChannelListRequest list() {
    return new ChannelListRequest();
  }

  /**
   * Creates a truncate request
   *
   * @return the created request
   */
  public static ChannelTruncateRequest truncate(@NotNull String type, @NotNull String id) {
    return new ChannelTruncateRequest(type, id);
  }
}
