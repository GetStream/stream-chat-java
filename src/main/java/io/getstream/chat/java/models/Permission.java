package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.Permission.PermissionCreateRequestData.PermissionCreateRequest;
import io.getstream.chat.java.models.Permission.PermissionUpdateRequestData.PermissionUpdateRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.PermissionService;
import io.getstream.chat.java.services.framework.StreamServiceGenerator;
import java.util.List;
import java.util.Map;
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
  @JsonProperty("id")
  private String id;

  @NotNull
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("action")
  private String action;

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

  @Builder(
      builderClassName = "PermissionCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class PermissionCreateRequestData {
    @NotNull
    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("action")
    private String action;

    @Nullable
    @JsonProperty("owner")
    private Boolean owner;

    @Nullable
    @JsonProperty("same_team")
    private Boolean sameTeam;

    @Nullable
    @JsonProperty("condition")
    private Map<String, Object> condition;

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
    @NotNull private String id;

    @Override
    protected Call<PermissionGetResponse> generateCall() {
      return StreamServiceGenerator.createService(PermissionService.class).get(id);
    }
  }

  @Builder(
      builderClassName = "PermissionUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class PermissionUpdateRequestData {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("action")
    private String action;

    @Nullable
    @JsonProperty("owner")
    private Boolean owner;

    @Nullable
    @JsonProperty("same_team")
    private Boolean sameTeam;

    @Nullable
    @JsonProperty("condition")
    private Map<String, Object> condition;

    public static class PermissionUpdateRequest extends StreamRequest<StreamResponseObject> {
      private PermissionUpdateRequest(@NotNull String id, @NotNull String name) {
        this.id = id;
        this.name = name;
      }

      @SuppressWarnings("unused")
      private PermissionUpdateRequest name(@NotNull String name) {
        throw new IllegalStateException("Should not use as it is only to hide builder method");
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(PermissionService.class)
            .update(id, this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class PermissionDeleteRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String id;

    @Override
    protected Call<StreamResponseObject> generateCall() {
      return StreamServiceGenerator.createService(PermissionService.class).delete(id);
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
   * @param id the permission id
   * @return the created request
   */
  @NotNull
  public static PermissionGetRequest get(@NotNull String id) {
    return new PermissionGetRequest(id);
  }

  /**
   * Creates an update request
   *
   * @param id the permission id
   * @param name the permission name
   * @return the created request
   */
  @NotNull
  public static PermissionUpdateRequest update(@NotNull String id, @NotNull String name) {
    return new PermissionUpdateRequest(id, name);
  }

  /**
   * Creates a delete request
   *
   * @param id the permission id
   * @return the created request
   */
  @NotNull
  public static PermissionDeleteRequest delete(@NotNull String id) {
    return new PermissionDeleteRequest(id);
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
