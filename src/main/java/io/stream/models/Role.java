package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Role.RoleCreateRequestData.RoleCreateRequest;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.RoleService;
import io.stream.services.framework.StreamServiceGenerator;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;

public class Role {

  @Builder(
      builderClassName = "RoleCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class RoleCreateRequestData {
    @NotNull
    @JsonProperty("name")
    private String name;

    public static class RoleCreateRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(RoleService.class).create(this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class RoleDeleteRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String name;

    @Override
    protected Call<StreamResponseObject> generateCall() {
      return StreamServiceGenerator.createService(RoleService.class).delete(name);
    }
  }

  public static class RoleListRequest extends StreamRequest<RoleListResponse> {
    @Override
    protected Call<RoleListResponse> generateCall() {
      return StreamServiceGenerator.createService(RoleService.class).list();
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class RoleListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("roles")
    private List<String> roles;
  }

  /**
   * Creates a create request
   *
   * @return the created request
   */
  @NotNull
  public static RoleCreateRequest create() {
    return new RoleCreateRequest();
  }

  /**
   * Creates a delete request
   *
   * @param name the role name
   * @return the created request
   */
  @NotNull
  public static RoleDeleteRequest delete(@NotNull String name) {
    return new RoleDeleteRequest(name);
  }

  /**
   * Creates a list request
   *
   * @return the created request
   */
  @NotNull
  public static RoleListRequest list() {
    return new RoleListRequest();
  }
}