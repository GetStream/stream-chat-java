package io.getstream.services;

import io.getstream.models.ChannelType.ChannelTypeCreateRequestData;
import io.getstream.models.ChannelType.ChannelTypeCreateResponse;
import io.getstream.models.ChannelType.ChannelTypeGetResponse;
import io.getstream.models.ChannelType.ChannelTypeListResponse;
import io.getstream.models.ChannelType.ChannelTypeUpdateRequestData;
import io.getstream.models.ChannelType.ChannelTypeUpdateResponse;
import io.getstream.models.framework.StreamResponseObject;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ChannelTypeService {
  @POST("channeltypes")
  @NotNull
  Call<ChannelTypeCreateResponse> create(
      @NotNull @Body ChannelTypeCreateRequestData channelTypeCreateRequestData);

  @DELETE("channeltypes/{name}")
  @NotNull
  Call<StreamResponseObject> delete(@NotNull @Path("name") String name);

  @GET("channeltypes/{name}")
  @NotNull
  Call<ChannelTypeGetResponse> get(@NotNull @Path("name") String name);

  @GET("channeltypes")
  @NotNull
  Call<ChannelTypeListResponse> list();

  @PUT("channeltypes/{name}")
  @NotNull
  Call<ChannelTypeUpdateResponse> update(
      @NotNull @Path("name") String name,
      @NotNull @Body ChannelTypeUpdateRequestData channelTypeUpdateRequestData);
}
