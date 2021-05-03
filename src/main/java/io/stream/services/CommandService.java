package io.stream.services;

import io.stream.models.Command.CommandCreateRequestData;
import io.stream.models.Command.CommandCreateResponse;
import io.stream.models.Command.CommandDeleteResponse;
import io.stream.models.Command.CommandGetResponse;
import io.stream.models.Command.CommandListResponse;
import io.stream.models.Command.CommandUpdateRequestData;
import io.stream.models.Command.CommandUpdateResponse;
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
