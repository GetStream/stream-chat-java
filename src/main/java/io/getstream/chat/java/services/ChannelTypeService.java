package io.getstream.chat.java.services;

import org.jetbrains.annotations.NotNull;
import io.getstream.chat.java.models.ChannelType.ChannelTypeCreateRequestData;
import io.getstream.chat.java.models.ChannelType.ChannelTypeCreateResponse;
import io.getstream.chat.java.models.ChannelType.ChannelTypeGetResponse;
import io.getstream.chat.java.models.ChannelType.ChannelTypeListResponse;
import io.getstream.chat.java.models.ChannelType.ChannelTypeUpdateRequestData;
import io.getstream.chat.java.models.ChannelType.ChannelTypeUpdateResponse;
import io.getstream.chat.java.models.framework.StreamResponseObject;
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
