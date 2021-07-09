package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Permission.PermissionCreateRequestData;
import io.getstream.chat.java.models.Permission.PermissionGetResponse;
import io.getstream.chat.java.models.Permission.PermissionListResponse;
import io.getstream.chat.java.models.Permission.PermissionUpdateRequestData;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.*;

public interface PermissionService {

  @POST("permissions")
  @NotNull
  Call<StreamResponseObject> create(
      @NotNull @Body PermissionCreateRequestData permissionCreateRequestData);

  @GET("permissions/{id}")
  @NotNull
  Call<PermissionGetResponse> get(@NotNull @Path("id") String id);

  @PUT("permissions/{id}")
  @NotNull
  Call<StreamResponseObject> update(
      @NotNull @Path("id") String id,
      @NotNull @Body PermissionUpdateRequestData permissionUpdateRequestData);

  @DELETE("permissions/{id}")
  @NotNull
  Call<StreamResponseObject> delete(@NotNull @Path("id") String name);

  @GET("permissions")
  @NotNull
  Call<PermissionListResponse> list();
}
