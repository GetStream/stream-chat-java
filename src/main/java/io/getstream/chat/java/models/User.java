package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.getstream.chat.java.models.Device.DeviceRequestObject;
import io.getstream.chat.java.models.Flag.FlagCreateRequestData.FlagCreateRequest;
import io.getstream.chat.java.models.Flag.FlagDeleteRequestData.FlagDeleteRequest;
import io.getstream.chat.java.models.User.UserBanRequestData.UserBanRequest;
import io.getstream.chat.java.models.User.UserCreateGuestRequestData.UserCreateGuestRequest;
import io.getstream.chat.java.models.User.UserDeactivateRequestData.UserDeactivateRequest;
import io.getstream.chat.java.models.User.UserListRequestData.UserListRequest;
import io.getstream.chat.java.models.User.UserMuteRequestData.UserMuteRequest;
import io.getstream.chat.java.models.User.UserPartialUpdateRequestData.UserPartialUpdateRequest;
import io.getstream.chat.java.models.User.UserQueryBannedRequestData.UserQueryBannedRequest;
import io.getstream.chat.java.models.User.UserReactivateRequestData.UserReactivateRequest;
import io.getstream.chat.java.models.User.UserUnmuteRequestData.UserUnmuteRequest;
import io.getstream.chat.java.models.User.UserUpsertRequestData.UserUpsertRequest;
import io.getstream.chat.java.models.framework.RequestObjectBuilder;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.UserService;
import io.getstream.chat.java.services.framework.StreamServiceGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.crypto.spec.SecretKeySpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

  @Nullable
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
  @JsonDeserialize(using = LanguageDeserializer.class)
  private Language language;

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
    @JsonDeserialize(using = LanguageDeserializer.class)
    private Language language;

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
  @Setter
  public static class UserRequestObject {
    @Nullable
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
    private Language language;

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

    @Nullable
    public static UserRequestObject buildFrom(@Nullable User user) {
      return RequestObjectBuilder.build(UserRequestObject.class, user);
    }
  }

  @Builder
  public static class UserPartialUpdateRequestObject {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Singular
    @Nullable
    @JsonProperty("set")
    private Map<String, Object> setValues;

    @Singular
    @Nullable
    @JsonProperty("unset")
    private List<String> unsetValues;
  }

  @Builder
  @Setter
  public static class OwnUserRequestObject {
    @Nullable
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
    private Language language;

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

    @Nullable
    public static OwnUserRequestObject buildFrom(@Nullable OwnUser ownUser) {
      return RequestObjectBuilder.build(OwnUserRequestObject.class, ownUser);
    }
  }

  @Builder
  @Setter
  public static class UserMuteRequestObject {
    @Nullable
    @JsonProperty("user")
    private User user;

    @Nullable
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

    @Nullable
    public static UserMuteRequestObject buildFrom(@Nullable UserMute userMute) {
      return RequestObjectBuilder.build(UserMuteRequestObject.class, userMute);
    }
  }

  @Builder
  @Setter
  public static class ChannelMuteRequestObject {
    @Nullable
    @JsonProperty("user")
    private User user;

    @Nullable
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

    @Nullable
    public static ChannelMuteRequestObject buildFrom(@Nullable ChannelMute channelMute) {
      return RequestObjectBuilder.build(ChannelMuteRequestObject.class, channelMute);
    }
  }

  @Builder(
      builderClassName = "UserUpsertRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UserUpsertRequestData {
    @Nullable
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
    @Nullable
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
    @Nullable
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
    @Nullable
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

  @RequiredArgsConstructor
  public static class UserDeleteRequest extends StreamRequest<UserDeleteResponse> {
    @NotNull private String userId;

    @Nullable private Boolean markMessagesDeleted;

    @Nullable private Boolean hardDelete;

    @Nullable private Boolean deleteConversationChannels;

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
    @Nullable
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

  @Builder(
      builderClassName = "UserMuteRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UserMuteRequestData {
    @Nullable
    @JsonProperty("target_id")
    private String singleTargetId;

    @Singular
    @Nullable
    @JsonProperty("target_ids")
    private List<String> targetIds;

    @Nullable
    @JsonProperty("timeout")
    private Integer timeout;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class UserMuteRequest extends StreamRequest<UserMuteResponse> {
      @Override
      protected Call<UserMuteResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class).mute(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "UserUnmuteRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UserUnmuteRequestData {
    @Nullable
    @JsonProperty("target_id")
    private String singleTargetId;

    @Singular
    @Nullable
    @JsonProperty("target_ids")
    private List<String> targetIds;

    @Nullable
    @JsonProperty("timeout")
    private Integer timeout;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class UserUnmuteRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(UserService.class).unmute(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "UserCreateGuestRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UserCreateGuestRequestData {
    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class UserCreateGuestRequest extends StreamRequest<UserCreateGuestResponse> {
      @Override
      protected Call<UserCreateGuestResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .createGuest(this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class UserExportRequest extends StreamRequest<UserExportResponse> {
    @NotNull private String userId;

    @Override
    protected Call<UserExportResponse> generateCall() {
      return StreamServiceGenerator.createService(UserService.class).export(userId);
    }
  }

  @RequiredArgsConstructor
  public static class UserUnbanRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String targetUserId;

    @Nullable private String type;

    @Nullable private String id;

    @NotNull
    public UserUnbanRequest type(@NotNull String type) {
      this.type = type;
      return this;
    }

    @NotNull
    public UserUnbanRequest id(@NotNull String id) {
      this.id = id;
      return this;
    }

    @Override
    protected Call<StreamResponseObject> generateCall() {
      return StreamServiceGenerator.createService(UserService.class).unban(targetUserId, type, id);
    }
  }

  @AllArgsConstructor
  public static class UserRevokeTokensRequest extends StreamRequest<UserPartialUpdateResponse> {
    @NotNull private List<String> userIds = new ArrayList<>();

    @Nullable private Date revokeTokensIssuedBefore;

    @Override
    protected Call<UserPartialUpdateResponse> generateCall() {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
      formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
      return StreamServiceGenerator.createService(UserService.class)
          .partialUpdate(
              new UserPartialUpdateRequest()
                  .users(
                      userIds.stream()
                          .map(
                              userId ->
                                  UserPartialUpdateRequestObject.builder()
                                      .id(userId)
                                      .setValue(
                                          "revoke_tokens_issued_before",
                                          revokeTokensIssuedBefore == null
                                              ? null
                                              : formatter.format(revokeTokensIssuedBefore))
                                      .build())
                          .collect(Collectors.toList()))
                  .internalBuild());
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

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UserMuteResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("mute")
    private UserMute mute;

    @Nullable
    @JsonProperty("mutes")
    private List<UserMute> mutes;

    @NotNull
    @JsonProperty("own_user")
    private OwnUser ownUser;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UserExportResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("user")
    private User user;

    @Nullable
    @JsonProperty("messages")
    private List<Message> messages;

    @Nullable
    @JsonProperty("reactions")
    private List<Reaction> reactions;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UserCreateGuestResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("user")
    private User user;

    @NotNull
    @JsonProperty("access_token")
    private String accessToken;
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
   * @param userId the user id to delete
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

  /**
   * Creates a flag request
   *
   * @param userId the user id to flag
   * @return the created request
   */
  @NotNull
  public static FlagCreateRequest flag(@NotNull String userId) {
    return new FlagCreateRequest().targetUserId(userId);
  }

  /**
   * Creates an unflag request
   *
   * @param userId the user id to unflag
   * @return the created request
   */
  @NotNull
  public static FlagDeleteRequest unflag(@NotNull String userId) {
    return new FlagDeleteRequest().targetUserId(userId);
  }

  /**
   * Creates a mute request
   *
   * @return the created request
   */
  @NotNull
  public static UserMuteRequest mute() {
    return new UserMuteRequest();
  }

  /**
   * Creates an unmute request
   *
   * @return the created request
   */
  @NotNull
  public static UserUnmuteRequest unmute() {
    return new UserUnmuteRequest();
  }

  /**
   * Creates an export request
   *
   * @param userId the user id to export
   * @return the created request
   */
  @NotNull
  public static UserExportRequest export(@NotNull String userId) {
    return new UserExportRequest(userId);
  }

  /**
   * Creates a create guest request
   *
   * @return the created request
   */
  @NotNull
  public static UserCreateGuestRequest createGuest() {
    return new UserCreateGuestRequest();
  }

  /**
   * Creates an unban request
   *
   * @param targetUserId the user id to unban
   * @return the created request
   */
  @NotNull
  public static UserUnbanRequest unban(@NotNull String targetUserId) {
    return new UserUnbanRequest(targetUserId);
  }

  /**
   * Creates a revoke token request
   *
   * @param userId the user id to revoke token for
   * @param revokeTokensIssuedBefore the limit date to revoke tokens
   * @return the created request
   */
  @NotNull
  public static UserRevokeTokensRequest revokeToken(
      @NotNull String userId, @Nullable Date revokeTokensIssuedBefore) {
    return new UserRevokeTokensRequest(Arrays.asList(userId), revokeTokensIssuedBefore);
  }

  /**
   * Creates a revoke token request
   *
   * @param userIds the user ids to revoke token for
   * @param revokeTokensIssuedBefore the limit date to revoke tokens
   * @return the created request
   */
  @NotNull
  public static UserRevokeTokensRequest revokeTokens(
      @NotNull List<String> userIds, @Nullable Date revokeTokensIssuedBefore) {
    return new UserRevokeTokensRequest(userIds, revokeTokensIssuedBefore);
  }

  @NotNull
  public static String createToken(
      @NotNull String userId, @Nullable Date expiresAt, @Nullable Date issuedAt) {
    String apiKey =
        System.getenv("STREAM_KEY") != null
            ? System.getenv("STREAM_KEY")
            : System.getProperty("STREAM_KEY");
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] apiKeySecretBytes = Base64.getDecoder().decode(apiKey);
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    return Jwts.builder()
        .claim("user_id", userId)
        .setExpiration(expiresAt)
        .setIssuedAt(issuedAt)
        .signWith(signingKey, signatureAlgorithm)
        .compact();
  }
}
