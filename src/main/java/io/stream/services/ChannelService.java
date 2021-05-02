package io.stream.services;

import io.stream.models.Channel.ChannelDeleteResponse;
import io.stream.models.Channel.ChannelExportRequestData;
import io.stream.models.Channel.ChannelExportResponse;
import io.stream.models.Channel.ChannelExportStatusResponse;
import io.stream.models.Channel.ChannelGetRequestData;
import io.stream.models.Channel.ChannelGetResponse;
import io.stream.models.Channel.ChannelHideRequestData;
import io.stream.models.Channel.ChannelListRequestData;
import io.stream.models.Channel.ChannelListResponse;
import io.stream.models.Channel.ChannelMarkAllReadRequestData;
import io.stream.models.Channel.ChannelMarkReadRequestData;
import io.stream.models.Channel.ChannelMarkReadResponse;
import io.stream.models.Channel.ChannelMuteRequestData;
import io.stream.models.Channel.ChannelMuteResponse;
import io.stream.models.Channel.ChannelQueryMembersRequestData;
import io.stream.models.Channel.ChannelQueryMembersResponse;
import io.stream.models.Channel.ChannelShowRequestData;
import io.stream.models.Channel.ChannelTruncateResponse;
import io.stream.models.Channel.ChannelUnMuteRequestData;
import io.stream.models.Channel.ChannelUnMuteResponse;
import io.stream.models.Channel.ChannelUpdateRequestData;
import io.stream.models.Channel.ChannelUpdateResponse;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.framework.ToJson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChannelService {
  @POST("channels/{type}/{id}")
  Call<ChannelUpdateResponse> update(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Body ChannelUpdateRequestData channelUpdateRequestData);

  @POST("channels/{type}/{id}/query")
  Call<ChannelGetResponse> getOrCreateWithId(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @Nullable @Body ChannelGetRequestData channelGetRequestData);

  @POST("channels/{type}/query")
  Call<ChannelGetResponse> getOrCreateWithoutId(
      @NotNull @Path("type") String channelType,
      @Nullable @Body ChannelGetRequestData channelGetRequestData);

  @DELETE("channels/{type}/{id}")
  Call<ChannelDeleteResponse> delete(
      @NotNull @Path("type") String channelType, @NotNull @Path("id") String channelId);

  @POST("channels")
  Call<ChannelListResponse> list(@Nullable @Body ChannelListRequestData channelListRequestData);

  @POST("channels/{type}/{id}/truncate")
  Call<ChannelTruncateResponse> truncate(
      @NotNull @Path("type") String channelType, @NotNull @Path("id") String channelId);

  @GET("members")
  Call<ChannelQueryMembersResponse> queryMembers(
      @NotNull @ToJson @Query("payload")
          ChannelQueryMembersRequestData channelQueryMembersRequestData);

  @POST("export_channels")
  Call<ChannelExportResponse> export(
      @NotNull @Body ChannelExportRequestData channelExportRequestData);

  @GET("export_channels/{id}")
  Call<ChannelExportStatusResponse> exportStatus(@NotNull @Path("id") String taskId);

  @POST("channels/{type}/{id}/hide")
  Call<StreamResponseObject> hide(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Body ChannelHideRequestData channelHideRequestData);

  @POST("channels/read")
  Call<StreamResponseObject> markAllRead(
      @NotNull @Body ChannelMarkAllReadRequestData channelMarkAllReadRequestData);

  @POST("channels/{type}/{id}/read")
  Call<ChannelMarkReadResponse> markRead(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Body ChannelMarkReadRequestData channelMarkReadRequestData);

  @POST("moderation/mute/channel")
  Call<ChannelMuteResponse> mute(@NotNull @Body ChannelMuteRequestData channelMuteRequestData);

  @POST("channels/{type}/{id}/show")
  Call<StreamResponseObject> show(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Body ChannelShowRequestData channelShowRequestData);

  @POST("moderation/unmute/channel")
  Call<ChannelUnMuteResponse> unmute(
      @NotNull @Body ChannelUnMuteRequestData channelUnMuteRequestData);
}
