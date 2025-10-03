package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.*;
import io.getstream.chat.java.models.MarkDeliveredOptions;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.framework.ToJson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.*;

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

  @POST("channels/delete")
  Call<Channel.ChannelDeleteManyResponse> deleteMany(
      @NotNull @Body Channel.ChannelDeleteManyRequest channelDeleteManyRequest);

  @POST("channels")
  Call<ChannelListResponse> list(@Nullable @Body ChannelListRequestData channelListRequestData);

  @POST("channels/{type}/{id}/truncate")
  Call<ChannelTruncateResponse> truncate(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @Nullable @Body ChannelTruncateRequestData channelTruncateRequestData);

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

  @POST("channels/{type}/{id}")
  Call<ChannelUpdateResponse> assignRoles(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Body AssignRoleRequestData assignRoleRequestData);

  @PATCH("channels/{type}/{id}/member/{user_id}")
  Call<ChannelMemberResponse> updateMemberPartial(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Path("user_id") String userId,
      @NotNull @Body ChannelMemberPartialUpdateRequestData updateMemberPartialRequestData);

  @POST("channels/delivered")
  Call<MarkDeliveredOptions.MarkDeliveredResponse> markChannelsDelivered(
      @NotNull @Body MarkDeliveredOptions markDeliveredOptions,
      @NotNull @Query("user_id") String userId);
}
