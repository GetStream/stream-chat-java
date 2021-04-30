package io.stream.services;

import org.jetbrains.annotations.NotNull;
import io.stream.models.ChannelType.ChannelTypeCreateResponse;
import io.stream.models.ChannelType.ChannelTypeGetResponse;
import io.stream.models.ChannelType.ChannelTypeRequestObject;
import io.stream.models.ChannelType.ChannelTypeRequestObjectWithName;
import io.stream.models.ChannelType.ChannelTypeUpdateResponse;
import io.stream.models.ChannelType.ChannelTypeListResponse;
import io.stream.models.framework.StreamResponseObject;
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
      @NotNull @Body ChannelTypeRequestObjectWithName channelTypeRequestWithName);

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
      @NotNull @Body ChannelTypeRequestObject channelTypeRequest);
}
