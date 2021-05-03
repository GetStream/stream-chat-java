package io.stream.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Device.DeviceRequestObject;
import io.stream.models.User.UserBanRequestData.UserBanRequest;
import io.stream.models.User.UserDeactivateRequestData.UserDeactivateRequest;
import io.stream.models.User.UserListRequestData.UserQueryRequest;
import io.stream.models.User.UserPartialUpdateRequestData.UserPartialUpdateRequest;
import io.stream.models.User.UserQueryBannedRequestData.UserQueryBannedRequest;
import io.stream.models.User.UserReactivateRequestData.UserReactivateRequest;
import io.stream.models.User.UserUpsertRequestData.UserUpsertRequest;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.UserService;
import io.stream.services.framework.StreamServiceGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
public class User {
  @NotNull
  @JsonProperty("id")
  private String id;

  @Nullable
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("image")
  private String image;

  @NotNull
  @JsonProperty("role")
  private String role;

  @NotNull
  @JsonProperty("teams")
  private List<String> teams;

  @NotNull
  @JsonProperty("online")
  private Boolean online;

  @NotNull
  @JsonProperty("invisible")
  private Boolean invisible;

  @Nullable
  @JsonProperty("created_at")
  private Date createdAt;

  @Nullable
  @JsonProperty("updated_at")
  private Date updatedAt;

  @Nullable
  @JsonProperty("last_active")
  private Date lastActive;

  @Nullable
  @JsonProperty("deleted_at")
  private Date deletedAt;

  @Nullable
  @JsonProperty("deactivated_at")
  private Date deactivatedAt;

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
  @JsonProperty("language")
  private String language;

  @Nullable
  @JsonProperty("mutes")
  private List<Mute> mutes;

  @Nullable
  @JsonProperty("channel_mutes")
  private List<ChannelMute> channelMutes;

  @NotNull @JsonIgnore private Map<String, Object> additionalFields;

  public User() {
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
  public static class Mute {
    @NotNull
    @JsonProperty("user")
    private User user;

    @NotNull
    @JsonProperty("target")
    private User target;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @NotNull
    @JsonProperty("updated_at")
    private Date updatedAt;

    @Nullable
    @JsonProperty("expires")
    private Date expires;

    public Mute() {}
  }

  @Data
  public static class ChannelMute {
    @NotNull
    @JsonProperty("user")
    private User user;

    @NotNull
    @JsonProperty("channel")
    private Channel channel;

    @Nullable
    @JsonProperty("expires")
    private Date expires;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @NotNull
    @JsonProperty("updated_at")
    private Date updatedAt;

    public ChannelMute() {}
  }

  @Data
  public static class Ban {
    @Nullable
    @JsonProperty("channel")
    private Channel channel;

    @Nullable
    @JsonProperty("user")
    private User user;

    @Nullable
    @JsonProperty("expires")
    private Date expires;

    @Nullable
    @JsonProperty("reason")
    private String reason;

    @Nullable
    @JsonProperty("shadow")
    private Boolean shadow;

    @NotNull
    @JsonProperty("banned_by")
    private User bannedBy;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    public Ban() {}
  }

  @Data
  public static class OwnUser {
    @NotNull
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("role")
    private String role;

    @Nullable
    @JsonProperty("roles")
    private List<String> roles;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @NotNull
    @JsonProperty("updated_at")
    private Date updatedAt;

    @NotNull
    @JsonProperty("last_active")
    private Date lastActive;

    @Nullable
    @JsonProperty("deleted_at")
    private Date deletedAt;

    @Nullable
    @JsonProperty("deactivated_at")
    private Date deactivatedAt;

    @Nullable
    @JsonProperty("banned")
    private Boolean banned;

    @Nullable
    @JsonProperty("online")
    private Boolean online;

    @Nullable
    @JsonProperty("invisible")
    private Boolean invisible;

    @Nullable
    @JsonProperty("devices")
    private List<Device> devices;

    @Nullable
    @JsonProperty("mutes")
    private List<UserMute> mutes;

    @Nullable
    @JsonProperty("channel_mutes")
    private List<ChannelMute> channelMutes;

    @Nullable
    @JsonProperty("unread_count")
    private Integer unreadCount;

    @Nullable
    @JsonProperty("total_unread_count")
    private Integer totalUnreadCount;

    @Nullable
    @JsonProperty("unread_channels")
    private Integer unreadChannels;

    @Nullable
    @JsonProperty("language")
    private String language;

    @Nullable
    @JsonProperty("teams")
    private List<String> teams;

    @Nullable
    @JsonProperty("latest_hidden_channels")
    private List<String> latestHiddenChannels;

    @NotNull @JsonIgnore private Map<String, Object> additionalFields;

    public OwnUser() {
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

  @Data
  public static class UserMute {
    @NotNull
    @JsonProperty("user")
    private User user;

    @NotNull
    @JsonProperty("target")
    private User target;

    @Nullable
    @JsonProperty("expires")
    private Date expires;

    @NotNull
    @JsonProperty("created_at")
    private Date created_at;

    @NotNull
    @JsonProperty("updated_at")
    private Date updated_at;

    public UserMute() {}
  }

  @Builder
  public static class UserRequestObject {
    @NotNull
    @JsonProperty("id")
    @Getter
    private String id;

    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("role")
    private String role;

    @Nullable
    @JsonProperty("banned")
    private Boolean banned;

    @Nullable
    @JsonProperty("ban_expires")
    private String banExpires;

    @Nullable
    @JsonProperty("language")
    private String language;

    @Nullable
    @JsonProperty("teams")
    private List<String> teams;

    @Nullable @JsonIgnore private Map<String, Object> additionalFields;
  }

  @Builder
  public static class UserPartialUpdateRequestObject {
    @NotNull
    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("set")
    private Map<String, Object> setValue;

    @NotNull
    @JsonProperty("unset")
    private List<String> unset;
  }

  @Builder
  public static class OwnUserRequestObject {
    @NotNull
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("role")
    private String role;

    @Nullable
    @JsonProperty("roles")
    private List<String> roles;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;

    @Nullable
    @JsonProperty("last_active")
    private Date lastActive;

    @Nullable
    @JsonProperty("deleted_at")
    private Date deletedAt;

    @Nullable
    @JsonProperty("deactivated_at")
    private Date deactivatedAt;

    @Nullable
    @JsonProperty("banned")
    private Boolean banned;

    @Nullable
    @JsonProperty("online")
    private Boolean online;

    @Nullable
    @JsonProperty("invisible")
    private Boolean invisible;

    @Nullable
    @JsonProperty("devices")
    private List<DeviceRequestObject> devices;

    @Nullable
    @JsonProperty("mutes")
    private List<UserMuteRequestObject> mutes;

    @Nullable
    @JsonProperty("channel_mutes")
    private List<ChannelMuteRequestObject> channelMutes;

    @Nullable
    @JsonProperty("unread_count")
    private Integer unreadCount;

    @Nullable
    @JsonProperty("total_unread_count")
    private Integer totalUnreadCount;

    @Nullable
    @JsonProperty("unread_channels")
    private Integer unreadChannels;

    @Nullable
    @JsonProperty("language")
    private String language;

    @Nullable
    @JsonProperty("teams")
    private List<String> teams;

    @Nullable
    @JsonProperty("latest_hidden_channels")
    private List<String> latestHiddenChannels;

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
  public static class UserMuteRequestObject {
    @NotNull
    @JsonProperty("user")
    private User user;

    @NotNull
    @JsonProperty("target")
    private User target;

    @Nullable
    @JsonProperty("expires")
    private Date expires;

    @Nullable
    @JsonProperty("created_at")
    private Date created_at;

    @Nullable
    @JsonProperty("updated_at")
    private Date updated_at;
  }

  @Builder
  public static class ChannelMuteRequestObject {
    @NotNull
    @JsonProperty("user")
    private User user;

    @NotNull
    @JsonProperty("channel")
    private Channel channel;

    @Nullable
    @JsonProperty("expires")
    private Date expires;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;
  }

  public static class UserUpsertRequestData {
    @NotNull
    @JsonProperty("users")
    private Map<String, UserRequestObject> users;

    private UserUpsertRequestData(UserUpsertRequest userUpsertRequest) {
      this.users = userUpsertRequest.users;
    }

    public static class UserUpsertRequest extends StreamRequest<UserUpsertResponse> {
      private Map<String, UserRequestObject> users = new HashMap<>();

      private UserUpsertRequest() {}

      @NotNull
      public UserUpsertRequest users(@NotNull Map<String, UserRequestObject> users) {
        this.users = users;
        return this;
      }

      @NotNull
      public UserUpsertRequest addUser(@NotNull UserRequestObject user) {
        if (user.getId() == null) {
          throw new IllegalArgumentException("user id cannot be null");
        }
        this.users.put(user.getId(), user);
        return this;
      }

      @Override
      protected Call<UserUpsertResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .upsert(new UserUpsertRequestData(this));
      }
    }
  }

  public static class UserListRequestData {

    @NotNull
    @JsonProperty("filter_conditions")
    private Map<String, Object> filterConditions;

    @Nullable
    @JsonProperty("sort")
    private List<Sort> sort;

    @Nullable
    @JsonProperty("presence")
    private Boolean presence;

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
    private User user;

    @Nullable
    @JsonProperty("connection_id")
    private String connectionId;

    private UserListRequestData(UserQueryRequest userQueryRequest) {
      this.filterConditions = userQueryRequest.filterConditions;
      this.sort = userQueryRequest.sort;
      this.presence = userQueryRequest.presence;
      this.limit = userQueryRequest.limit;
      this.offset = userQueryRequest.offset;
      this.userId = userQueryRequest.userId;
      this.user = userQueryRequest.user;
      this.connectionId = userQueryRequest.connectionId;
    }

    public static class UserQueryRequest extends StreamRequest<UserListResponse> {
      private Map<String, Object> filterConditions = Collections.emptyMap();
      private List<Sort> sort;
      private Boolean presence;
      private Integer limit;
      private Integer offset;
      private String userId;
      private User user;
      private String connectionId;

      private UserQueryRequest() {}

      @NotNull
      public UserQueryRequest filterConditions(@NotNull Map<String, Object> filterConditions) {
        this.filterConditions = filterConditions;
        return this;
      }

      @NotNull
      public UserQueryRequest sort(@NotNull List<Sort> sort) {
        this.sort = sort;
        return this;
      }

      @NotNull
      public UserQueryRequest presence(@NotNull Boolean presence) {
        this.presence = presence;
        return this;
      }

      @NotNull
      public UserQueryRequest limit(@NotNull Integer limit) {
        this.limit = limit;
        return this;
      }

      @NotNull
      public UserQueryRequest offset(@NotNull Integer offset) {
        this.offset = offset;
        return this;
      }

      @NotNull
      public UserQueryRequest userId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public UserQueryRequest user(@NotNull User user) {
        this.user = user;
        return this;
      }

      @NotNull
      public UserQueryRequest connectionId(@NotNull String connectionId) {
        this.connectionId = connectionId;
        return this;
      }

      @Override
      protected Call<UserListResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .list(new UserListRequestData(this));
      }
    }
  }

  public static class UserPartialUpdateRequestData {
    @NotNull
    @JsonProperty("users")
    private List<UserPartialUpdateRequestObject> users;

    private UserPartialUpdateRequestData(UserPartialUpdateRequest userPartialUpdateRequest) {
      this.users = userPartialUpdateRequest.users;
    }

    public static class UserPartialUpdateRequest extends StreamRequest<UserPartialUpdateResponse> {
      private List<UserPartialUpdateRequestObject> users = new ArrayList<>();

      private UserPartialUpdateRequest() {}

      @NotNull
      public UserPartialUpdateRequest users(@NotNull List<UserPartialUpdateRequestObject> users) {
        this.users = users;
        return this;
      }

      @NotNull
      public UserPartialUpdateRequest addUser(@NotNull UserPartialUpdateRequestObject user) {
        this.users.add(user);
        return this;
      }

      @Override
      protected Call<UserPartialUpdateResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .partialUpdate(new UserPartialUpdateRequestData(this));
      }
    }
  }

  public static class UserQueryBannedRequestData {
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

    private UserQueryBannedRequestData(UserQueryBannedRequest userQueryBannedRequest) {
      this.filterConditions = userQueryBannedRequest.filterConditions;
      this.sort = userQueryBannedRequest.sort;
      this.limit = userQueryBannedRequest.limit;
      this.offset = userQueryBannedRequest.offset;
      this.createdAtAfterOrEqual = userQueryBannedRequest.createdAtAfterOrEqual;
      this.createdAtAfter = userQueryBannedRequest.createdAtAfter;
      this.createdAtBeforeOrEqual = userQueryBannedRequest.createdAtBeforeOrEqual;
      this.createdAtBefore = userQueryBannedRequest.createdAtBefore;
      this.userId = userQueryBannedRequest.userId;
      this.user = userQueryBannedRequest.user;
    }

    public static class UserQueryBannedRequest extends StreamRequest<UserQueryBannedResponse> {
      private Map<String, Object> filterConditions = Collections.emptyMap();
      private List<Sort> sort;
      private Integer limit;
      private Integer offset;
      private Date createdAtAfterOrEqual;
      private Date createdAtAfter;
      private Date createdAtBeforeOrEqual;
      private Date createdAtBefore;
      private String userId;
      private UserRequestObject user;

      private UserQueryBannedRequest() {}

      @NotNull
      public UserQueryBannedRequest filterConditions(
          @NotNull Map<String, Object> filterConditions) {
        this.filterConditions = filterConditions;
        return this;
      }

      @NotNull
      public UserQueryBannedRequest sort(@NotNull List<Sort> sort) {
        this.sort = sort;
        return this;
      }

      @NotNull
      public UserQueryBannedRequest limit(@NotNull Integer limit) {
        this.limit = limit;
        return this;
      }

      @NotNull
      public UserQueryBannedRequest offset(@NotNull Integer offset) {
        this.offset = offset;
        return this;
      }

      @NotNull
      public UserQueryBannedRequest createdAtAfterOrEqual(@NotNull Date createdAtAfterOrEqual) {
        this.createdAtAfterOrEqual = createdAtAfterOrEqual;
        return this;
      }

      @NotNull
      public UserQueryBannedRequest createdAtAfter(@NotNull Date createdAtAfter) {
        this.createdAtAfter = createdAtAfter;
        return this;
      }

      @NotNull
      public UserQueryBannedRequest createdAtBeforeOrEqual(@NotNull Date createdAtBeforeOrEqual) {
        this.createdAtBeforeOrEqual = createdAtBeforeOrEqual;
        return this;
      }

      @NotNull
      public UserQueryBannedRequest createdAtBefore(@NotNull Date createdAtBefore) {
        this.createdAtBefore = createdAtBefore;
        return this;
      }

      @NotNull
      public UserQueryBannedRequest userId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public UserQueryBannedRequest user(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<UserQueryBannedResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .queryBanned(new UserQueryBannedRequestData(this));
      }
    }
  }

  public static class UserBanRequestData {
    @NotNull
    @JsonProperty("target_user_id")
    private String targetUserId;

    @Nullable
    @JsonProperty("timeout")
    private Integer timeout;

    @Nullable
    @JsonProperty("reason")
    private String reason;

    @Nullable
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("shadow")
    private Boolean shadow;

    @Nullable
    @JsonProperty("ip_ban")
    private Boolean ipBan;

    @Nullable
    @JsonProperty("banned_by_id")
    private String bannedById;

    @Nullable
    @JsonProperty("banned_by")
    private UserRequestObject bannedBy;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    private UserBanRequestData(UserBanRequest userBanRequest) {
      this.targetUserId = userBanRequest.targetUserId;
      this.timeout = userBanRequest.timeout;
      this.reason = userBanRequest.reason;
      this.type = userBanRequest.type;
      this.id = userBanRequest.id;
      this.shadow = userBanRequest.shadow;
      this.ipBan = userBanRequest.ipBan;
      this.bannedById = userBanRequest.bannedById;
      this.bannedBy = userBanRequest.bannedBy;
      this.userId = userBanRequest.userId;
      this.user = userBanRequest.user;
    }

    public static class UserBanRequest extends StreamRequest<StreamResponseObject> {
      private String targetUserId;
      private Integer timeout;
      private String reason;
      private String type;
      private String id;
      private Boolean shadow;
      private Boolean ipBan;
      private String bannedById;
      private UserRequestObject bannedBy;
      private String userId;
      private UserRequestObject user;

      private UserBanRequest() {}

      @NotNull
      public UserBanRequest targetUserId(@NotNull String targetUserId) {
        this.targetUserId = targetUserId;
        return this;
      }

      @NotNull
      public UserBanRequest timeout(@NotNull Integer timeout) {
        this.timeout = timeout;
        return this;
      }

      @NotNull
      public UserBanRequest reason(@NotNull String reason) {
        this.reason = reason;
        return this;
      }

      @NotNull
      public UserBanRequest type(@NotNull String type) {
        this.type = type;
        return this;
      }

      @NotNull
      public UserBanRequest id(@NotNull String id) {
        this.id = id;
        return this;
      }

      @NotNull
      public UserBanRequest shadow(@NotNull Boolean shadow) {
        this.shadow = shadow;
        return this;
      }

      @NotNull
      public UserBanRequest ipBan(@NotNull Boolean ipBan) {
        this.ipBan = ipBan;
        return this;
      }

      @NotNull
      public UserBanRequest bannedById(@NotNull String bannedById) {
        this.bannedById = bannedById;
        return this;
      }

      @NotNull
      public UserBanRequest bannedBy(@NotNull UserRequestObject bannedBy) {
        this.bannedBy = bannedBy;
        return this;
      }

      @NotNull
      public UserBanRequest userId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public UserBanRequest user(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .ban(new UserBanRequestData(this));
      }
    }
  }

  public static class UserDeactivateRequestData {
    @NotNull
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("mark_messages_deleted")
    private Boolean markMessagesDeleted;

    @Nullable
    @JsonProperty("created_by_id")
    private String createdById;

    private UserDeactivateRequestData(UserDeactivateRequest userDeactivateRequest) {
      this.userId = userDeactivateRequest.userId;
      this.markMessagesDeleted = userDeactivateRequest.markMessagesDeleted;
      this.createdById = userDeactivateRequest.createdById;
    }

    public static class UserDeactivateRequest extends StreamRequest<UserDeactivateResponse> {
      private String userId;
      private Boolean markMessagesDeleted;
      private String createdById;

      private UserDeactivateRequest(@NotNull String userId) {
        this.userId = userId;
      }

      @NotNull
      public UserDeactivateRequest markMessagesDeleted(@NotNull Boolean markMessagesDeleted) {
        this.markMessagesDeleted = markMessagesDeleted;
        return this;
      }

      @NotNull
      public UserDeactivateRequest createdById(@NotNull String createdById) {
        this.createdById = createdById;
        return this;
      }

      @Override
      protected Call<UserDeactivateResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .deactivate(userId, new UserDeactivateRequestData(this));
      }
    }
  }

  public static class UserDeleteRequest extends StreamRequest<UserDeleteResponse> {
    private String userId;
    private Boolean markMessagesDeleted;
    private Boolean hardDelete;
    private Boolean deleteConversationChannels;

    private UserDeleteRequest(@NotNull String userId) {
      this.userId = userId;
    }

    @NotNull
    public UserDeleteRequest markMessagesDeleted(@NotNull Boolean markMessagesDeleted) {
      this.markMessagesDeleted = markMessagesDeleted;
      return this;
    }

    @NotNull
    public UserDeleteRequest hardDelete(@NotNull Boolean hardDelete) {
      this.hardDelete = hardDelete;
      return this;
    }

    @NotNull
    public UserDeleteRequest deleteConversationChannels(
        @NotNull Boolean deleteConversationChannels) {
      this.deleteConversationChannels = deleteConversationChannels;
      return this;
    }

    @Override
    protected Call<UserDeleteResponse> generateCall() {
      return StreamServiceGenerator.createService(UserService.class)
          .delete(userId, markMessagesDeleted, hardDelete, deleteConversationChannels);
    }
  }

  public static class UserReactivateRequestData {
    @NotNull
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("restore_messages")
    private Boolean restoreMessages;

    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("created_by_id")
    private String createdById;

    private UserReactivateRequestData(UserReactivateRequest userReactivateRequest) {
      this.userId = userReactivateRequest.userId;
      this.restoreMessages = userReactivateRequest.restoreMessages;
      this.name = userReactivateRequest.name;
      this.createdById = userReactivateRequest.createdById;
    }

    public static class UserReactivateRequest extends StreamRequest<UserReactivateResponse> {
      private String userId;
      private Boolean restoreMessages;
      private String name;
      private String createdById;

      private UserReactivateRequest(@NotNull String userId) {
        this.userId = userId;
      }

      @NotNull
      public UserReactivateRequest restoreMessages(@NotNull Boolean restoreMessages) {
        this.restoreMessages = restoreMessages;
        return this;
      }

      @NotNull
      public UserReactivateRequest name(@NotNull String name) {
        this.name = name;
        return this;
      }

      @NotNull
      public UserReactivateRequest createdById(@NotNull String createdById) {
        this.createdById = createdById;
        return this;
      }

      @Override
      protected Call<UserReactivateResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .reactivate(userId, new UserReactivateRequestData(this));
      }
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class UserUpsertResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("users")
    private Map<String, User> users;

    public UserUpsertResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class UserListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("users")
    private List<User> users;

    public UserListResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class UserPartialUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("users")
    private Map<String, User> users;

    public UserPartialUpdateResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class UserQueryBannedResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("bans")
    private List<Ban> bans;

    public UserQueryBannedResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class UserDeactivateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("user")
    private User user;

    public UserDeactivateResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class UserDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("user")
    private User user;

    public UserDeleteResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class UserReactivateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("user")
    private User user;

    public UserReactivateResponse() {}
  }

  /**
   * Creates a query request
   *
   * @return the created request
   */
  @NotNull
  public static UserQueryRequest list() {
    return new UserQueryRequest();
  }

  /**
   * Creates an upsert request
   *
   * @return the created request
   */
  @NotNull
  public static UserUpsertRequest upsert() {
    return new UserUpsertRequest();
  }

  /**
   * Creates a partial update request
   *
   * @return the created request
   */
  @NotNull
  public static UserPartialUpdateRequest partialUpdate() {
    return new UserPartialUpdateRequest();
  }

  /**
   * Creates a ban request
   *
   * @return the created request
   */
  @NotNull
  public static UserBanRequest ban() {
    return new UserBanRequest();
  }

  /**
   * Creates a query banned request
   *
   * @return the created request
   */
  @NotNull
  public static UserQueryBannedRequest queryBanned() {
    return new UserQueryBannedRequest();
  }

  /**
   * Creates a deactivate request
   *
   * @param userId the user id to deactivate
   * @return the created request
   */
  @NotNull
  public static UserDeactivateRequest deactivate(@NotNull String userId) {
    return new UserDeactivateRequest(userId);
  }

  /**
   * Creates a delete request
   *
   * @param userId the user id to deactivate
   * @return the created request
   */
  @NotNull
  public static UserDeleteRequest delete(@NotNull String userId) {
    return new UserDeleteRequest(userId);
  }

  /**
   * Creates a reactivate request
   *
   * @param userId the user id to reactivate
   * @return the created request
   */
  @NotNull
  public static UserReactivateRequest reactivate(@NotNull String userId) {
    return new UserReactivateRequest(userId);
  }
}
