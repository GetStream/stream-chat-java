package io.getstream.services;

import org.jetbrains.annotations.NotNull;
import io.getstream.models.Blocklist.BlocklistCreateRequestData;
import io.getstream.models.Blocklist.BlocklistGetResponse;
import io.getstream.models.Blocklist.BlocklistListResponse;
import io.getstream.models.Blocklist.BlocklistUpdateRequestData;
import io.getstream.models.framework.StreamResponseObject;
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
