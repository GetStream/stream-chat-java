package io.getstream.services;

import io.getstream.models.Permission.PermissionCreateRequestData;
import io.getstream.models.Permission.PermissionGetResponse;
import io.getstream.models.Permission.PermissionListResponse;
import io.getstream.models.Permission.PermissionUpdateRequestData;
import io.getstream.models.framework.StreamResponseObject;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PermissionService {

  @POST("custom_permission")
  @NotNull
  Call<StreamResponseObject> create(
      @NotNull @Body PermissionCreateRequestData permissionCreateRequestData);

  @GET("custom_permission/{name}")
  @NotNull
  Call<PermissionGetResponse> get(@NotNull @Path("name") String name);

  @POST("custom_permission/{name}")
  @NotNull
  Call<StreamResponseObject> update(
      @NotNull @Path("name") String name,
      @NotNull @Body PermissionUpdateRequestData permissionUpdateRequestData);

  @DELETE("custom_permission/{name}")
  @NotNull
  Call<StreamResponseObject> delete(@NotNull @Path("name") String name);

  @GET("custom_permission")
  @NotNull
  Call<PermissionListResponse> list();
}
