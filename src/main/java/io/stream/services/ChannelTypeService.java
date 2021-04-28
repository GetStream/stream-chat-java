package io.stream.services;

import io.stream.models.ChannelType;
import io.stream.models.ChannelType.ChannelTypeRequestData;
import io.stream.models.ChannelType.ChannelTypeRequestDataWithoutNameSerialization;
import io.stream.models.ChannelType.ListChannelTypeResponse;
import io.stream.models.framework.StreamResponse;
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
  Call<ChannelTypeRequestData> create(@NotNull @Body ChannelTypeRequestData channelType);

  @DELETE("channeltypes/{name}")
  Call<StreamResponse> delete(@Path("name") String name);

  @GET("channeltypes/{name}")
  @NotNull
  Call<ChannelType> get(@Path("name") String name);

  @GET("channeltypes")
  @NotNull
  Call<ListChannelTypeResponse> list();

  @PUT("channeltypes/{name}")
  @NotNull
  Call<ChannelTypeRequestData> update(
      @Path("name") String name,
      @NotNull @Body ChannelTypeRequestDataWithoutNameSerialization channelTypeRequest);
}
