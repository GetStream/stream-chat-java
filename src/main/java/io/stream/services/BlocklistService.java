package io.stream.services;

import org.jetbrains.annotations.NotNull;
import io.stream.models.Blocklist.BlocklistCreateRequestData;
import io.stream.models.Blocklist.BlocklistGetResponse;
import io.stream.models.Blocklist.BlocklistListResponse;
import io.stream.models.Blocklist.BlocklistUpdateRequestData;
import io.stream.models.framework.StreamResponseObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BlocklistService {

  @POST("blocklists")
  @NotNull
  Call<StreamResponseObject> create(
      @NotNull @Body BlocklistCreateRequestData blocklistCreateRequestData);

  @GET("blocklists/{name}")
  @NotNull
  Call<BlocklistGetResponse> get(@NotNull @Path("name") String name);

  @PUT("blocklists/{name}")
  @NotNull
  Call<StreamResponseObject> update(
      @NotNull @Path("name") String name,
      @NotNull @Body BlocklistUpdateRequestData blocklistUpdateRequestData);

  @DELETE("blocklists/{name}")
  @NotNull
  Call<StreamResponseObject> delete(@NotNull @Path("name") String name);

  @GET("blocklists")
  @NotNull
  Call<BlocklistListResponse> list();
}
