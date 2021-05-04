package io.stream.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Device.DeviceRequestObject;
import io.stream.models.User.UserBanRequestData.UserBanRequest;
import io.stream.models.User.UserDeactivateRequestData.UserDeactivateRequest;
import io.stream.models.User.UserListRequestData.UserListRequest;
import io.stream.models.User.UserPartialUpdateRequestData.UserPartialUpdateRequest;
import io.stream.models.User.UserQueryBannedRequestData.UserQueryBannedRequest;
import io.stream.models.User.UserReactivateRequestData.UserReactivateRequest;
import io.stream.models.User.UserUpsertRequestData.UserUpsertRequest;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.UserService;
import io.stream.services.framework.StreamServiceGenerator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
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
  }

  @Data
  @NoArgsConstructor
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
  }

  @Data
  @NoArgsConstructor
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
  }

  @Data
  @NoArgsConstructor
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

    @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

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

    @Singular @Nullable @JsonIgnore private Map<String, Object> additionalFields;

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
  public static class UserPartialUpdateRequestObject {
    @NotNull
    @JsonProperty("id")
    private String id;

    @Singular
    @NotNull
    @JsonProperty("set")
    private Map<String, Object> setValues;

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

    @Singular
    @Nullable
    @JsonProperty("devices")
    private List<DeviceRequestObject> devices;

    @Singular
    @Nullable
    @JsonProperty("mutes")
    private List<UserMuteRequestObject> mutes;

    @Singular
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

    @Singular @Nullable @JsonIgnore private Map<String, Object> additionalFields;

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

  @Builder(
      builderClassName = "UserUpsertRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UserUpsertRequestData {
    @NotNull
    @JsonProperty("users")
    private Map<String, UserRequestObject> users;

    public static class UserUpsertRequest extends StreamRequest<UserUpsertResponse> {
      @NotNull
      public UserUpsertRequest user(@NotNull UserRequestObject user) {
        if (user.getId() == null) {
          throw new IllegalArgumentException("user id cannot be null");
        }
        if (this.users == null) {
          this.users = new HashMap<>();
        }
        users.put(user.getId(), user);
        return this;
      }

      @Override
      protected Call<UserUpsertResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class).upsert(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "UserListRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UserListRequestData {
    // Singular is required because cannot be empty
    @Singular
    @NotNull
    @JsonProperty("filter_conditions")
    private Map<String, Object> filterConditions;

    @Singular
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

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

    public static class UserListRequest extends StreamRequest<UserListResponse> {
      @Override
      protected Call<UserListResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class).list(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "UserPartialUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UserPartialUpdateRequestData {
    @Singular
    @NotNull
    @JsonProperty("users")
    private List<UserPartialUpdateRequestObject> users;

    public static class UserPartialUpdateRequest extends StreamRequest<UserPartialUpdateResponse> {
      @Override
      protected Call<UserPartialUpdateResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .partialUpdate(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "UserQueryBannedRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UserQueryBannedRequestData {
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

    public static class UserQueryBannedRequest extends StreamRequest<UserQueryBannedResponse> {
      @Override
      protected Call<UserQueryBannedResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .queryBanned(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "UserBanRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
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

    public static class UserBanRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(UserService.class).ban(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "UserDeactivateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
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

    public static class UserDeactivateRequest extends StreamRequest<UserDeactivateResponse> {

      private UserDeactivateRequest(@NotNull String userId) {
        this.userId = userId;
      }

      @Override
      protected Call<UserDeactivateResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .deactivate(userId, this.internalBuild());
      }
    }
  }

  public static class UserDeleteRequest extends StreamRequest<UserDeleteResponse> {
    @NotNull private String userId;

    @Nullable private Boolean markMessagesDeleted;

    @Nullable private Boolean hardDelete;

    @Nullable private Boolean deleteConversationChannels;

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

  @Builder(
      builderClassName = "UserReactivateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
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

    public static class UserReactivateRequest extends StreamRequest<UserReactivateResponse> {

      private UserReactivateRequest(@NotNull String userId) {
        this.userId = userId;
      }

      @Override
      protected Call<UserReactivateResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .reactivate(userId, this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UserUpsertResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("users")
    private Map<String, User> users;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UserListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("users")
    private List<User> users;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UserPartialUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("users")
    private Map<String, User> users;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UserQueryBannedResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("bans")
    private List<Ban> bans;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UserDeactivateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("user")
    private User user;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UserDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("user")
    private User user;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UserReactivateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("user")
    private User user;
  }

  /**
   * Creates a query request
   *
   * @return the created request
   */
  @NotNull
  public static UserListRequest list() {
    return new UserListRequest();
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
