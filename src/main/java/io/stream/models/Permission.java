package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Permission.PermissionCreateRequestData.PermissionCreateRequest;
import io.stream.models.Permission.PermissionUpdateRequestData.PermissionUpdateRequest;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.PermissionService;
import io.stream.services.framework.StreamServiceGenerator;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Permission {
  @NotNull
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("resource")
  private Resource resource;

  @Nullable
  @JsonProperty("owner")
  private Boolean owner;

  @Nullable
  @JsonProperty("same_team")
  private Boolean sameTeam;

  @NotNull
  @JsonProperty("custom")
  private Boolean custom;

  @Nullable
  @JsonProperty("condition")
  private Condition condition;

  @Data
  @NoArgsConstructor
  public static class Condition {
    @NotNull private String expression;
  }

  public enum Resource {
    @JsonProperty("CreateChannel")
    CREATE_CHANNEL,
    @JsonProperty("CreateDistinctChannelForOthers")
    CREATE_DISTINCT_CHANNEL_FOR_OTHERS,
    @JsonProperty("ReadChannel")
    READ_CHANNEL,
    @JsonProperty("UpdateChannelMembers")
    UPDATE_CHANNEL_MEMBERS,
    @JsonProperty("RemoveOwnChannelMembership")
    REMOVE_OWN_CHANNEL_MEMBERSHIP,
    @JsonProperty("UpdateChannel")
    UPDATE_CHANNEL,
    @JsonProperty("UseFrozenChannel")
    USE_FROZEN_CHANNEL,
    @JsonProperty("UpdateUserRole")
    UPDATE_USER_ROLE,
    @JsonProperty("DeleteChannel")
    DELETE_CHANNEL,
    @JsonProperty("CreateMessage")
    CREATE_MESSAGE,
    @JsonProperty("UpdateMessage")
    UPDATE_MESSAGE,
    @JsonProperty("PinMessage")
    PIN_MESSAGE,
    @JsonProperty("DeleteMessage")
    DELETE_MESSAGE,
    @JsonProperty("RunMessageAction")
    RUN_MESSAGE_ACTION,
    @JsonProperty("MuteUser")
    MUTE_USER,
    @JsonProperty("BanUser")
    BAN_USER,
    @JsonProperty("UploadAttachment")
    UPLOAD_ATTACHMENT,
    @JsonProperty("DeleteAttachment")
    DELETE_ATTACHMENT,
    @JsonProperty("AddLinks")
    ADD_LINKS,
    @JsonProperty("CreateReaction")
    CREATE_REACTION,
    @JsonProperty("DeleteReaction")
    DELETE_REACTION,
    @JsonProperty("SendCustomEvent")
    SEND_CUSTOM_EVENT,
    @JsonProperty("SkipMessageModeration")
    SKIP_MESSAGE_MODERATION,
    @JsonProperty("UseCommands")
    USE_COMMANDS,
    @JsonProperty("EditUser")
    EDIT_USER,
    @JsonProperty("*")
    ALL
  }

  @Builder(
      builderClassName = "PermissionCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class PermissionCreateRequestData {
    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("resource")
    private Resource resource;

    @Nullable
    @JsonProperty("owner")
    private Boolean owner;

    @Nullable
    @JsonProperty("same_team")
    private Boolean sameTeam;

    @Nullable
    @JsonProperty("condition")
    private String condition;

    public static class PermissionCreateRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(PermissionService.class)
            .create(this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class PermissionGetRequest extends StreamRequest<PermissionGetResponse> {
    @NotNull private String name;

    @Override
    protected Call<PermissionGetResponse> generateCall() {
      return StreamServiceGenerator.createService(PermissionService.class).get(name);
    }
  }

  @Builder(
      builderClassName = "PermissionUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class PermissionUpdateRequestData {
    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("resource")
    private Resource resource;

    @Nullable
    @JsonProperty("owner")
    private Boolean owner;

    @Nullable
    @JsonProperty("same_team")
    private Boolean sameTeam;

    @Nullable
    @JsonProperty("condition")
    private String condition;

    public static class PermissionUpdateRequest extends StreamRequest<StreamResponseObject> {
      private PermissionUpdateRequest(@NotNull String name) {
        this.name = name;
      }

      @SuppressWarnings("unused")
      private PermissionUpdateRequest name(@NotNull String name) {
        throw new IllegalStateException("Should not use as it is only to hide builder method");
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(PermissionService.class)
            .update(name, this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class PermissionDeleteRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String name;

    @Override
    protected Call<StreamResponseObject> generateCall() {
      return StreamServiceGenerator.createService(PermissionService.class).delete(name);
    }
  }

  public static class PermissionListRequest extends StreamRequest<PermissionListResponse> {
    @Override
    protected Call<PermissionListResponse> generateCall() {
      return StreamServiceGenerator.createService(PermissionService.class).list();
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PermissionGetResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("permission")
    private Permission permission;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PermissionListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("permissions")
    private List<Permission> permissions;
  }

  /**
   * Creates a create request
   *
   * @return the created request
   */
  @NotNull
  public static PermissionCreateRequest create() {
    return new PermissionCreateRequest();
  }

  /**
   * Creates a get request
   *
   * @param name the permission name
   * @return the created request
   */
  @NotNull
  public static PermissionGetRequest get(@NotNull String name) {
    return new PermissionGetRequest(name);
  }

  /**
   * Creates an update request
   *
   * @param name the permission name
   * @return the created request
   */
  @NotNull
  public static PermissionUpdateRequest update(@NotNull String name) {
    return new PermissionUpdateRequest(name);
  }

  /**
   * Creates a delete request
   *
   * @param name the permission name
   * @return the created request
   */
  @NotNull
  public static PermissionDeleteRequest delete(@NotNull String name) {
    return new PermissionDeleteRequest(name);
  }

  /**
   * Creates a list request
   *
   * @return the created request
   */
  @NotNull
  public static PermissionListRequest list() {
    return new PermissionListRequest();
  }
}
