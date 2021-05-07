package io.stream.services;

import org.jetbrains.annotations.NotNull;
import io.stream.models.Role.RoleCreateRequestData;
import io.stream.models.Role.RoleListResponse;
import io.stream.models.framework.StreamResponseObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RoleService {

  @POST("custom_role")
  @NotNull
  Call<StreamResponseObject> create(@NotNull @Body RoleCreateRequestData roleCreateRequestData);

  @DELETE("custom_role/{name}")
  @NotNull
  Call<StreamResponseObject> delete(@NotNull @Path("name") String name);

  @GET("custom_role")
  @NotNull
  Call<RoleListResponse> list();
}
