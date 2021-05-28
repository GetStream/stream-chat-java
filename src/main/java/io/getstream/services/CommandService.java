package io.getstream.services;

import io.getstream.models.Command.CommandCreateRequestData;
import io.getstream.models.Command.CommandCreateResponse;
import io.getstream.models.Command.CommandDeleteResponse;
import io.getstream.models.Command.CommandGetResponse;
import io.getstream.models.Command.CommandListResponse;
import io.getstream.models.Command.CommandUpdateRequestData;
import io.getstream.models.Command.CommandUpdateResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommandService {

  @POST("commands")
  @NotNull
  Call<CommandCreateResponse> create(
      @NotNull @Body CommandCreateRequestData commandCreateRequestData);

  @GET("commands/{name}")
  @NotNull
  Call<CommandGetResponse> get(@NotNull @Path("name") String name);

  @PUT("commands/{name}")
  @NotNull
  Call<CommandUpdateResponse> update(
      @NotNull @Path("name") String name,
      @NotNull @Body CommandUpdateRequestData commandUpdateRequestData);

  @DELETE("commands/{name}")
  @NotNull
  Call<CommandDeleteResponse> delete(@NotNull @Path("name") String name);

  @GET("commands")
  @NotNull
  Call<CommandListResponse> list();
}
