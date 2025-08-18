package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.*;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel.AssignRoleRequestData.AssignRoleRequest;
import io.getstream.chat.java.models.Channel.ChannelExportRequestData.ChannelExportRequest;
import io.getstream.chat.java.models.Channel.ChannelGetRequestData.ChannelGetRequest;
import io.getstream.chat.java.models.Channel.ChannelHideRequestData.ChannelHideRequest;
import io.getstream.chat.java.models.Channel.ChannelListRequestData.ChannelListRequest;
import io.getstream.chat.java.models.Channel.ChannelMarkAllReadRequestData.ChannelMarkAllReadRequest;
import io.getstream.chat.java.models.Channel.ChannelMarkReadRequestData.ChannelMarkReadRequest;
import io.getstream.chat.java.models.Channel.ChannelMemberPartialUpdateRequestData.ChannelMemberPartialUpdateRequest;
import io.getstream.chat.java.models.Channel.ChannelMuteRequestData.ChannelMuteRequest;
import io.getstream.chat.java.models.Channel.ChannelPartialUpdateRequestData.ChannelPartialUpdateRequest;
import io.getstream.chat.java.models.Channel.ChannelQueryMembersRequestData.ChannelQueryMembersRequest;
import io.getstream.chat.java.models.Channel.ChannelShowRequestData.ChannelShowRequest;
import io.getstream.chat.java.models.Channel.ChannelTruncateRequestData.ChannelTruncateRequest;
import io.getstream.chat.java.models.Channel.ChannelUnMuteRequestData.ChannelUnMuteRequest;
import io.getstream.chat.java.models.Channel.ChannelUpdateRequestData.ChannelUpdateRequest;
import io.getstream.chat.java.models.ChannelType.BlocklistBehavior;
import io.getstream.chat.java.models.ChannelType.ChannelTypeWithCommands;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.User.ChannelMute;
import io.getstream.chat.java.models.User.OwnUser;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.framework.RequestObjectBuilder;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.ChannelService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
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
  @JsonProperty("messages_count")
  private Integer messagesCount;

  @Nullable
  @JsonProperty("read")
  private List<ChannelRead> read;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

  @Nullable
  @JsonProperty("deleted_at")
  private Date deletedAt;

  @Nullable
  @JsonProperty("last_message_at")
  private Date lastMessageAt;

  @Nullable
  @JsonProperty("truncated_by_id")
  private String truncatedById;

  @Nullable
  @JsonProperty("truncated_by")
  private User truncatedBy;

  @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return this.additionalFields;
  }

  @JsonAnySetter
  public void setAdditionalField(String name, Object value) {
    this.additionalFields.put(name, value);
  }

  @Data
  @NoArgsConstructor
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
  }

  @Data
  @NoArgsConstructor
  public static class ChannelMember {
    public enum InviteStatus {
      @JsonProperty("pending")
      PENDING,
      @JsonProperty("member")
      MEMBER,
      @JsonProperty("rejected")
      REJECTED,
      @JsonProperty("accepted")
      ACCEPTED,
      @JsonEnumDefaultValue
      UNKNOWN
    }

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
    @JsonProperty("channel_role")
    private String channelRole;

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

    @Nullable
    @JsonProperty("notifications_muted")
    private Boolean notificationsMuted;

    @Nullable
    @JsonProperty("status")
    private InviteStatus status;

    @Nullable
    @JsonProperty("archived_at")
    private Date archivedAt;

    @Nullable
    @JsonProperty("pinned_at")
    private Date pinnedAt;

    @Singular @Nullable @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

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
  public static class RoleAssignment {
    @Nullable
    @JsonProperty("channel_role")
    private String channelRole;

    @Nullable
    @JsonProperty("user_id")
    private String userId;
  }

  @Builder
  @Setter
  public static class ChannelRequestObject {
    @Nullable
    @JsonProperty("created_by")
    private UserRequestObject createdBy;

    @Nullable
    @JsonProperty("team")
    private String team;

    @Nullable
    @JsonProperty("auto_translation_enabled")
    private Boolean autoTranslationEnabled;

    @Nullable
    @JsonProperty("auto_translation_language")
    private Language autoTranslationLanguage;

    @Nullable
    @JsonProperty("frozen")
    private Boolean frozen;

    @Singular
    @Nullable
    @JsonProperty("members")
    private List<ChannelMemberRequestObject> members;

    @Singular
    @Nullable
    @JsonProperty("invites")
    private List<ChannelMemberRequestObject> invites;

    @Nullable
    @JsonProperty("config_overrides")
    private ConfigOverridesRequestObject configOverrides;

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
    public static ChannelRequestObject buildFrom(@Nullable Channel channel) {
      return RequestObjectBuilder.build(ChannelRequestObject.class, channel);
    }

    private ChannelRequestObject(
        @Nullable UserRequestObject createdBy,
        @Nullable String team,
        @Nullable Boolean autoTranslationEnabled,
        @Nullable Language autoTranslationLanguage,
        @Nullable Boolean frozen,
        @Nullable List<ChannelMemberRequestObject> members,
        @Nullable List<ChannelMemberRequestObject> invites,
        @Nullable ConfigOverridesRequestObject configOverrides,
        Map<String, Object> additionalFields) {
      this.createdBy = createdBy;
      this.team = team;
      this.autoTranslationEnabled = autoTranslationEnabled;
      this.autoTranslationLanguage = autoTranslationLanguage;
      this.frozen = frozen;
      this.members = members;
      this.invites = invites;
      this.configOverrides = configOverrides;
      this.additionalFields = new HashMap<String, Object>(additionalFields);
    }
  }

  @Builder
  @Setter
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
    @JsonProperty("channel_role")
    private String channelRole;

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

    @Nullable
    public static ChannelMemberRequestObject buildFrom(@Nullable ChannelMember channelMember) {
      return RequestObjectBuilder.build(ChannelMemberRequestObject.class, channelMember);
    }
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

    @Nullable
    @JsonProperty("commands")
    private List<String> Commands;

    @Nullable
    @JsonProperty("user_message_reminders")
    private Boolean userMessageReminders;

    @Nullable
    @JsonProperty("shared_locations")
    private Boolean sharedLocations;

    @Nullable
    @JsonProperty("count_messages")
    private Boolean countMessages;
  }

  @Builder
  public static class ChannelExportRequestObject {
    @Nullable
    @JsonProperty("type")
    private String type;

    @Nullable
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
      @NotNull private String channelType;

      @Nullable private String channelId;

      private ChannelGetRequest(@NotNull String channelType, @Nullable String channelId) {
        this.channelId = channelId;
        this.channelType = channelType;
      }

      @Override
      protected Call<ChannelGetResponse> generateCall(Client client) {
        if (this.channelId != null) {
          return client
              .create(ChannelService.class)
              .getOrCreateWithId(this.channelType, this.channelId, this.internalBuild());
        }
        return client
            .create(ChannelService.class)
            .getOrCreateWithoutId(this.channelType, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelUpdateRequestData {
    // Singular is required because cannot be null
    @Singular
    @Nullable
    @JsonProperty("add_members")
    private List<String> addMembers;

    // Singular is required because cannot be null
    @Singular
    @Nullable
    @JsonProperty("remove_members")
    private List<String> removeMembers;

    // Singular is required because cannot be null
    @Singular
    @Nullable
    @JsonProperty("add_moderators")
    private List<String> addModerators;

    // Singular is required because cannot be null
    @Singular
    @Nullable
    @JsonProperty("demote_moderators")
    private List<String> demoteModerators;

    // Singular is required because cannot be null
    @Singular
    @Nullable
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
    @JsonProperty("hide_history")
    private Boolean hideHistory;

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
      @NotNull private String channelType;

      @NotNull private String channelId;

      private ChannelUpdateRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelId = channelId;
        this.channelType = channelType;
      }

      @Override
      protected Call<ChannelUpdateResponse> generateCall(Client client) {
        return client
            .create(ChannelService.class)
            .update(this.channelType, this.channelId, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "AssignRoleRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class AssignRoleRequestData {
    @Singular
    @Nullable
    @JsonProperty("assign_roles")
    private List<RoleAssignment> assignRoles;

    @Nullable
    @JsonProperty("message")
    private MessageRequestObject message;

    public static class AssignRoleRequest extends StreamRequest<ChannelUpdateResponse> {
      @NotNull private String channelType;

      @NotNull private String channelId;

      private AssignRoleRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelId = channelId;
        this.channelType = channelType;
      }

      @Override
      protected Call<ChannelUpdateResponse> generateCall(Client client) {
        return client
            .create(ChannelService.class)
            .assignRoles(this.channelType, this.channelId, this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class ChannelDeleteRequest extends StreamRequest<ChannelDeleteResponse> {
    @NotNull private String channelType;

    @NotNull private String channelId;

    @Override
    protected Call<ChannelDeleteResponse> generateCall(Client client) {
      return client.create(ChannelService.class).delete(this.channelType, this.channelId);
    }
  }

  @RequiredArgsConstructor
  public static class ChannelDeleteManyRequest extends StreamRequest<ChannelDeleteManyResponse> {
    @JsonProperty("cids")
    @NotNull
    private List<String> cids;

    @JsonProperty("hard_delete")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean hardDelete;

    public ChannelDeleteManyRequest setDeleteStrategy(DeleteStrategy strategy) {
      hardDelete = strategy == DeleteStrategy.HARD;
      return this;
    }

    @Override
    protected Call<ChannelDeleteManyResponse> generateCall(Client svcFactory)
        throws StreamException {
      return svcFactory.create(ChannelService.class).deleteMany(this);
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
      protected Call<ChannelListResponse> generateCall(Client client) {
        return client.create(ChannelService.class).list(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelTruncateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelTruncateRequestData {
    @Nullable
    @JsonProperty("hard_delete")
    private boolean hardDelete;

    @Nullable
    @JsonProperty("skip_push")
    private boolean skipPush;

    @Nullable
    @JsonProperty("truncated_at")
    private Date truncatedAt;

    @Nullable
    @JsonProperty("message")
    private MessageRequestObject message;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelTruncateRequest extends StreamRequest<ChannelTruncateResponse> {
      @NotNull private String channelType;

      @NotNull private String channelId;

      private ChannelTruncateRequest(@NotNull String channelType, @Nullable String channelId) {
        this.channelId = channelId;
        this.channelType = channelType;
      }

      @Override
      protected Call<ChannelTruncateResponse> generateCall(Client client) {
        return client
            .create(ChannelService.class)
            .truncate(this.channelType, this.channelId, this.internalBuild());
      }
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
    private ChannelMemberRequestObject members;

    // Singular is required because cannot be null
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
      protected Call<ChannelQueryMembersResponse> generateCall(Client client) {
        return client.create(ChannelService.class).queryMembers(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelExportRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelExportRequestData {
    @Singular
    @Nullable
    @JsonProperty("channels")
    private List<ChannelExportRequestObject> channels;

    @Nullable
    @JsonProperty("version")
    private String version;

    @Nullable
    @JsonProperty("clear_deleted_message_text")
    private Boolean clearDeletedMessageText;

    @Nullable
    @JsonProperty("include_truncated_messages")
    private Boolean includeTruncatedMessages;

    @Nullable
    @JsonProperty("export_users")
    private Boolean exportUsers;

    public static class ChannelExportRequest extends StreamRequest<ChannelExportResponse> {
      @Override
      protected Call<ChannelExportResponse> generateCall(Client client) {
        return client.create(ChannelService.class).export(this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class ChannelExportStatusRequest
      extends StreamRequest<ChannelExportStatusResponse> {
    @NotNull private String id;

    @Override
    protected Call<ChannelExportStatusResponse> generateCall(Client client) {
      return client.create(ChannelService.class).exportStatus(id);
    }
  }

  @Builder(
      builderClassName = "ChannelHideRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelHideRequestData {
    @Nullable
    @JsonProperty("clear_history")
    private Boolean clearHistory;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelHideRequest extends StreamRequest<StreamResponseObject> {
      @NotNull private String channelType;

      @NotNull private String channelId;

      private ChannelHideRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @Override
      protected Call<StreamResponseObject> generateCall(Client client) {
        return client
            .create(ChannelService.class)
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
      protected Call<StreamResponseObject> generateCall(Client client) {
        return client.create(ChannelService.class).markAllRead(this.internalBuild());
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
      @NotNull private String channelType;

      @NotNull private String channelId;

      private ChannelMarkReadRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @Override
      protected Call<ChannelMarkReadResponse> generateCall(Client client) {
        return client
            .create(ChannelService.class)
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
    private Long expiration;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class ChannelMuteRequest extends StreamRequest<ChannelMuteResponse> {
      @Override
      protected Call<ChannelMuteResponse> generateCall(Client client) {
        return client.create(ChannelService.class).mute(this.internalBuild());
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
      @NotNull private String channelType;

      @NotNull private String channelId;

      private ChannelShowRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @Override
      protected Call<StreamResponseObject> generateCall(Client client) {
        return client
            .create(ChannelService.class)
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
      protected Call<ChannelUnMuteResponse> generateCall(Client client) {
        return client.create(ChannelService.class).unmute(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelPartialUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelPartialUpdateRequestData {
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

    public static class ChannelPartialUpdateRequest
        extends StreamRequest<ChannelPartialUpdateResponse> {
      @NotNull private String channelType;

      @NotNull private String channelId;

      private ChannelPartialUpdateRequest(@NotNull String channelType, @NotNull String channelId) {
        this.channelType = channelType;
        this.channelId = channelId;
      }

      @Override
      protected Call<ChannelPartialUpdateResponse> generateCall(Client client) {
        return client
            .create(ChannelService.class)
            .partialUpdate(channelType, channelId, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ChannelMemberPartialUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ChannelMemberPartialUpdateRequestData {
    @Singular
    @Nullable
    @JsonProperty("set")
    private Map<String, Object> setValues;

    @Singular
    @Nullable
    @JsonProperty("unset")
    private List<String> unsetValues;

    public static class ChannelMemberPartialUpdateRequest
        extends StreamRequest<ChannelMemberResponse> {
      @NotNull private String channelType;

      @NotNull private String channelId;

      @NotNull private String userId;

      private ChannelMemberPartialUpdateRequest(
          @NotNull String channelType, @NotNull String channelId, @NotNull String userId) {
        this.channelType = channelType;
        this.channelId = channelId;
        this.userId = userId;
      }

      @Override
      protected Call<ChannelMemberResponse> generateCall(Client client) {
        return client
            .create(ChannelService.class)
            .updateMemberPartial(channelType, channelId, userId, this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelMemberResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channel_member")
    private ChannelMember member;
  }

  @Data
  @NoArgsConstructor
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
    @JsonProperty("pending_messages")
    private List<Message> pendingMessages;

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

    @Nullable
    @JsonProperty("active_live_locations")
    private List<SharedLocation> activeLiveLocations;
  }

  @Data
  @NoArgsConstructor
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
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelDeleteResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channel")
    private Channel channel;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelDeleteManyResponse extends StreamResponseObject {
    @JsonProperty("task_id")
    @Getter
    private String taskId;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelListResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channels")
    private List<ChannelGetResponse> channels;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelTruncateResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channel")
    private Channel channel;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelQueryMembersResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("members")
    private List<ChannelMember> members;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelExportResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("task_id")
    private String taskId;
  }

  @Data
  @NoArgsConstructor
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
      FAILED,
      @JsonEnumDefaultValue
      UNKNOWN
    }

    @Data
    @NoArgsConstructor
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
    }

    @Data
    @NoArgsConstructor
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
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelMarkReadResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("event")
    private Event event;
  }

  @Data
  @NoArgsConstructor
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
  }

  @Data
  @NoArgsConstructor
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
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelPinResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("channel_mute")
    private ChannelMute channelMute;

    @Nullable
    @JsonProperty("channel_mutes")
    private List<ChannelMute> channelMutes;

    @Nullable
    @JsonProperty("own_user")
    private OwnUser ownUser;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelPartialUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("channel")
    private Channel channel;

    @NotNull
    @JsonProperty("members")
    private List<ChannelMember> members;
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
   * Creates a get or create request
   *
   * @param type the channel type
   * @return the created request
   */
  @NotNull
  public static ChannelGetRequest getOrCreate(@NotNull String type) {
    return new ChannelGetRequest(type, null);
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

  @NotNull
  public static ChannelDeleteManyRequest deleteMany(@NotNull List<String> cids) {
    return new ChannelDeleteManyRequest(cids);
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

  /**
   * Creates a partial update request
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static ChannelPartialUpdateRequest partialUpdate(
      @NotNull String type, @NotNull String id) {
    return new ChannelPartialUpdateRequest(type, id);
  }

  /**
   * Creates an assign role request
   *
   * @param type the channel type
   * @param id the channel id
   * @return the created request
   */
  @NotNull
  public static AssignRoleRequest assignRoles(@NotNull String type, @NotNull String id) {
    return new AssignRoleRequest(type, id);
  }

  /**
   * Creates a update member partial request
   *
   * @param type the channel type
   * @param id the channel id
   * @param userId the user id
   * @return the created request
   */
  @NotNull
  public static ChannelMemberPartialUpdateRequest updateMemberPartial(
      @NotNull String type, @NotNull String id, @NotNull String userId) {
    return new ChannelMemberPartialUpdateRequest(type, id, userId);
  }

  /**
   * Creates a pin channel request
   *
   * @param type the channel type
   * @param id the channel id
   * @param userId the user id
   * @return the created request
   */
  @NotNull
  public static ChannelMemberPartialUpdateRequest pin(
      @NotNull String type, @NotNull String id, @NotNull String userId) {
    return new ChannelMemberPartialUpdateRequest(type, id, userId).setValue("pinned", true);
  }

  /**
   * Creates a unpin channel request
   *
   * @param type the channel type
   * @param id the channel id
   * @param userId the user id
   * @return the created request
   */
  @NotNull
  public static ChannelMemberPartialUpdateRequest unpin(
      @NotNull String type, @NotNull String id, @NotNull String userId) {
    return new ChannelMemberPartialUpdateRequest(type, id, userId).setValue("pinned", false);
  }

  /**
   * Creates a archive channel request
   *
   * @param type the channel type
   * @param id the channel id
   * @param userId the user id
   * @return the created request
   */
  @NotNull
  public static ChannelMemberPartialUpdateRequest archive(
      @NotNull String type, @NotNull String id, @NotNull String userId) {
    return new ChannelMemberPartialUpdateRequest(type, id, userId).setValue("archived", true);
  }

  /**
   * Creates a unarchive channel request
   *
   * @param type the channel type
   * @param id the channel id
   * @param userId the user id
   * @return the created request
   */
  @NotNull
  public static ChannelMemberPartialUpdateRequest unarchive(
      @NotNull String type, @NotNull String id, @NotNull String userId) {
    return new ChannelMemberPartialUpdateRequest(type, id, userId).setValue("archived", false);
  }
}
