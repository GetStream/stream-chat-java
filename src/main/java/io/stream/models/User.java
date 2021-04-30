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
import io.stream.models.User.UserListRequestData.UserQueryRequest;
import io.stream.models.User.UserPartialUpdateRequestData.UserPartialUpdateRequest;
import io.stream.models.User.UserUpsertRequestData.UserUpsertRequest;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.UserService;
import io.stream.services.framework.StreamServiceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import retrofit2.Call;

@Data
public class User {
  public User() {
    additionalFields = new HashMap<>();
  }

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

  @Nullable @JsonIgnore private Map<String, Object> additionalFields;

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
    public Mute() {}

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
  public static class ChannelMute {
    public ChannelMute() {}

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

    private UserRequestObject(Builder builder) {
      this.id = builder.id;
      this.name = builder.name;
      this.role = builder.role;
      this.banned = builder.banned;
      this.banExpires = builder.banExpires;
      this.language = builder.language;
      this.teams = builder.teams;
      this.additionalFields = builder.additionalFields;
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
     * Creates builder to build {@link UserRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link UserRequestObject}. */
    public static final class Builder {
      private String id;
      private String name;
      private String role;
      private Boolean banned;
      private String banExpires;
      private String language;
      private List<String> teams = Collections.emptyList();
      private Map<String, Object> additionalFields = Collections.emptyMap();

      private Builder() {}

      @NotNull
      public Builder withId(@NotNull String id) {
        this.id = id;
        return this;
      }

      @NotNull
      public Builder withName(@NotNull String name) {
        this.name = name;
        return this;
      }

      @NotNull
      public Builder withRole(@NotNull String role) {
        this.role = role;
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
      public Builder withLanguage(@NotNull String language) {
        this.language = language;
        return this;
      }

      @NotNull
      public Builder withTeams(@NotNull List<String> teams) {
        this.teams = teams;
        return this;
      }

      @NotNull
      public Builder withAdditionalFields(@NotNull Map<String, Object> additionalFields) {
        this.additionalFields = additionalFields;
        return this;
      }

      @NotNull
      public UserRequestObject build() {
        return new UserRequestObject(this);
      }
    }
  }
  
  public static class UserPartialUpdateRequestObject {
    public UserPartialUpdateRequestObject() {}

    @NotNull
    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("set")
    private Map<String, Object> setValue;

    @NotNull
    @JsonProperty("unset")
    private List<String> unset;

    private UserPartialUpdateRequestObject(Builder builder) {
      this.id = builder.id;
      this.setValue = builder.setValue;
      this.unset = builder.unset;
    }

    /**
     * Creates builder to build {@link UserPartialUpdateRequestObject}.
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /**
     * Builder to build {@link UserPartialUpdateRequestObject}.
     */
    public static final class Builder {
      private String id;
      private Map<String, Object> setValue = Collections.emptyMap();
      private List<String> unset = Collections.emptyList();

      private Builder() {}

      @NotNull
      public Builder withId(@NotNull String id) {
        this.id = id;
        return this;
      }

      @NotNull
      public Builder withSetValue(@NotNull Map<String, Object> setValue) {
        this.setValue = setValue;
        return this;
      }

      @NotNull
      public Builder withUnset(@NotNull List<String> unset) {
        this.unset = unset;
        return this;
      }

      @NotNull
      public UserPartialUpdateRequestObject build() {
        return new UserPartialUpdateRequestObject(this);
      }
    }
    
    
  }

  public static class UserUpsertRequestData {

    @NotNull
    @JsonProperty("users")
    private Map<String, UserRequestObject> users;

    private UserUpsertRequestData(UserUpsertRequest builder) {
      this.users = builder.users;
    }

    public static class UserUpsertRequest extends StreamRequest<UserUpsertResponse> {
      private Map<String, UserRequestObject> users = new HashMap<>();

      private UserUpsertRequest() {}

      @NotNull
      public UserUpsertRequest withUsers(@NotNull Map<String, UserRequestObject> users) {
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

    private UserListRequestData(UserQueryRequest builder) {
      this.filterConditions = builder.filterConditions;
      this.sort = builder.sort;
      this.presence = builder.presence;
      this.limit = builder.limit;
      this.offset = builder.offset;
      this.userId = builder.userId;
      this.user = builder.user;
      this.connectionId = builder.connectionId;
    }

    public static class UserQueryRequest extends StreamRequest<UserListResponse> {
      private Map<String, Object> filterConditions = Collections.emptyMap();
      private List<Sort> sort = Collections.emptyList();
      private Boolean presence;
      private Integer limit;
      private Integer offset;
      private String userId;
      private User user;
      private String connectionId;

      private UserQueryRequest() {}

      @NotNull
      public UserQueryRequest withFilterConditions(@NotNull Map<String, Object> filterConditions) {
        this.filterConditions = filterConditions;
        return this;
      }

      @NotNull
      public UserQueryRequest withSort(@NotNull List<Sort> sort) {
        this.sort = sort;
        return this;
      }

      @NotNull
      public UserQueryRequest withPresence(@NotNull Boolean presence) {
        this.presence = presence;
        return this;
      }

      @NotNull
      public UserQueryRequest withLimit(@NotNull Integer limit) {
        this.limit = limit;
        return this;
      }

      @NotNull
      public UserQueryRequest withOffset(@NotNull Integer offset) {
        this.offset = offset;
        return this;
      }

      @NotNull
      public UserQueryRequest withUserId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public UserQueryRequest withUser(@NotNull User user) {
        this.user = user;
        return this;
      }

      @NotNull
      public UserQueryRequest withConnectionId(@NotNull String connectionId) {
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
    public UserPartialUpdateRequestData() {}

    @NotNull
    @JsonProperty("users")
    private List<UserPartialUpdateRequestObject> users;
    
    private UserPartialUpdateRequestData(UserPartialUpdateRequest userPartialUpdateRequest) {
      this.users = userPartialUpdateRequest.users;
    }

    public static class UserPartialUpdateRequest extends StreamRequest<UserPartialUpdateResponse> {
      private List<UserPartialUpdateRequestObject> users;

      private UserPartialUpdateRequest() {}

      @NotNull
      public UserPartialUpdateRequest withUsers(@NotNull List<UserPartialUpdateRequestObject> users) {
        this.users = users;
        return this;
      }

      @Override
      protected Call<UserPartialUpdateResponse> generateCall() {
        return StreamServiceGenerator.createService(UserService.class)
            .partialUpdate(new UserPartialUpdateRequestData(this));
      }
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class UserUpsertResponse extends StreamResponseObject {
    public UserUpsertResponse() {}

    @NotNull
    @JsonProperty("users")
    private Map<String, User> users;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class UserListResponse extends StreamResponseObject {
    public UserListResponse() {}

    @NotNull
    @JsonProperty("users")
    private List<User> users;
  }
  
  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class UserPartialUpdateResponse extends StreamResponseObject {
    public UserPartialUpdateResponse() {}

    @NotNull
    @JsonProperty("users")
    private Map<String, User> users;
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
}
