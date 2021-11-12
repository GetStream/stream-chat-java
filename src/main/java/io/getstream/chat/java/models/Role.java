package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.Role.RoleCreateRequestData.RoleCreateRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.RoleService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Role {
  @NotNull
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("custom")
  private Boolean custom;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

  @Builder(
      builderClassName = "RoleCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class RoleCreateRequestData {
    @Nullable
    @JsonProperty("name")
    private String name;

    public static class RoleCreateRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall(Client client) {
        return client.create(RoleService.class).create(this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class RoleDeleteRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String name;

    @Override
    protected Call<StreamResponseObject> generateCall(Client client) {
      return client.create(RoleService.class).delete(name);
    }
  }

  public static class RoleListRequest extends StreamRequest<RoleListResponse> {
    @Override
    protected Call<RoleListResponse> generateCall(Client client) {
      return client.create(RoleService.class).list();
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class RoleListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("roles")
    private List<Role> roles;
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
