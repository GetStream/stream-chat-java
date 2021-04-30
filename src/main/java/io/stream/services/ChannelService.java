package io.stream.services;

import io.stream.models.Channel.ChannelDeleteResponse;
import io.stream.models.Channel.ChannelGetRequestData;
import io.stream.models.Channel.ChannelGetResponse;
import io.stream.models.Channel.ChannelListRequestData;
import io.stream.models.Channel.ChannelListResponse;
import io.stream.models.Channel.ChannelTruncateResponse;
import io.stream.models.Channel.ChannelUpdateRequestData;
import io.stream.models.Channel.ChannelUpdateResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChannelService {
  @POST("/channels/{type}/{id}")
  Call<ChannelUpdateResponse> update(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Body ChannelUpdateRequestData channelUpdateRequestData);

  @POST("/channels/{type}/{id}/query")
  Call<ChannelGetResponse> getOrCreateWithId(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @Nullable @Body ChannelGetRequestData channelGetRequestData);

  @POST("/channels/{type}/query")
  Call<ChannelGetResponse> getOrCreateWithoutId(
      @NotNull @Path("type") String channelType,
      @Nullable @Body ChannelGetRequestData channelGetRequestData);

  @DELETE("/channels/{type}/{id}")
  Call<ChannelDeleteResponse> delete(
      @NotNull @Path("type") String channelType, @NotNull @Path("id") String channelId);

  @POST("/channels")
  Call<ChannelListResponse> list(@Nullable @Body ChannelListRequestData channelListRequestData);

  @POST("/channels/{type}/{id}/truncate")
  Call<ChannelTruncateResponse> truncate(
      @NotNull @Path("type") String channelType, @NotNull @Path("id") String channelId);
}
