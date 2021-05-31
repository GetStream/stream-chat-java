package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Permission.PermissionCreateRequestData;
import io.getstream.chat.java.models.Permission.PermissionGetResponse;
import io.getstream.chat.java.models.Permission.PermissionListResponse;
import io.getstream.chat.java.models.Permission.PermissionUpdateRequestData;
import io.getstream.chat.java.models.framework.StreamResponseObject;
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
