package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Role.RoleCreateRequestData;
import io.getstream.chat.java.models.Role.RoleListResponse;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RoleService {

  @POST("roles")
  @NotNull
  Call<StreamResponseObject> create(@NotNull @Body RoleCreateRequestData roleCreateRequestData);

  @DELETE("roles/{name}")
  @NotNull
  Call<StreamResponseObject> delete(@NotNull @Path("name") String name);

  @GET("roles")
  @NotNull
  Call<RoleListResponse> list();
}
