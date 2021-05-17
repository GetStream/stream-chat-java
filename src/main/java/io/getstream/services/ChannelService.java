package io.getstream.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.getstream.models.Channel.ChannelDeleteResponse;
import io.getstream.models.Channel.ChannelExportRequestData;
import io.getstream.models.Channel.ChannelExportResponse;
import io.getstream.models.Channel.ChannelExportStatusResponse;
import io.getstream.models.Channel.ChannelGetRequestData;
import io.getstream.models.Channel.ChannelGetResponse;
import io.getstream.models.Channel.ChannelHideRequestData;
import io.getstream.models.Channel.ChannelListRequestData;
import io.getstream.models.Channel.ChannelListResponse;
import io.getstream.models.Channel.ChannelMarkAllReadRequestData;
import io.getstream.models.Channel.ChannelMarkReadRequestData;
import io.getstream.models.Channel.ChannelMarkReadResponse;
import io.getstream.models.Channel.ChannelMuteRequestData;
import io.getstream.models.Channel.ChannelMuteResponse;
import io.getstream.models.Channel.ChannelPartialUpdateRequestData;
import io.getstream.models.Channel.ChannelPartialUpdateResponse;
import io.getstream.models.Channel.ChannelQueryMembersRequestData;
import io.getstream.models.Channel.ChannelQueryMembersResponse;
import io.getstream.models.Channel.ChannelShowRequestData;
import io.getstream.models.Channel.ChannelTruncateResponse;
import io.getstream.models.Channel.ChannelUnMuteRequestData;
import io.getstream.models.Channel.ChannelUnMuteResponse;
import io.getstream.models.Channel.ChannelUpdateRequestData;
import io.getstream.models.Channel.ChannelUpdateResponse;
import io.getstream.models.framework.StreamResponseObject;
import io.getstream.services.framework.ToJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
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

  @PATCH("channels/{type}/{id}")
  Call<ChannelPartialUpdateResponse> partialUpdate(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Body ChannelPartialUpdateRequestData channelPartialUpdateRequestData);
}
