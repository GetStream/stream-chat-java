package io.getstream.chat.java.services;

import io.getstream.chat.java.models.User.*;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.framework.ToJson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserService {
  @POST("users")
  Call<UserUpsertResponse> upsert(@NotNull @Body UserUpsertRequestData userUpsertRequestData);

  @GET("users")
  Call<UserListResponse> list(
      @NotNull @ToJson @Query("payload") UserListRequestData userListRequestData);

  @PATCH("users")
  Call<UserPartialUpdateResponse> partialUpdate(
      @NotNull @Body UserPartialUpdateRequestData userPartialUpdateRequestData);

  @GET("query_banned_users")
  Call<UserQueryBannedResponse> queryBanned(
      @NotNull @ToJson @Query("payload") UserQueryBannedRequestData userQueryBannedRequestData);

  @POST("moderation/ban")
  Call<StreamResponseObject> ban(@NotNull @Body UserBanRequestData userBanRequestData);

  @POST("users/{user_id}/deactivate")
  Call<UserDeactivateResponse> deactivate(
      @NotNull @Path("user_id") String userId,
      @NotNull @Body UserDeactivateRequestData userDeactivateRequestData);

  @DELETE("users/{user_id}")
  Call<UserDeleteResponse> delete(
      @NotNull @Path("user_id") String userId,
      @Nullable @Query("mark_messages_deleted") Boolean markMessagesDeleted,
      @Nullable @Query("hard_delete") Boolean hardDelete,
      @Nullable @Query("delete_conversation_channels") Boolean deleteConversationChannels);

  @POST("users/delete")
  Call<UserDeleteManyResponse> deleteMany(@NotNull @Body UserDeleteManyRequestData data);

  @POST("users/{user_id}/reactivate")
  Call<UserReactivateResponse> reactivate(
      @NotNull @Path("user_id") String userId,
      @NotNull @Body UserReactivateRequestData userReactivateRequestData);

  @POST("moderation/mute")
  Call<UserMuteResponse> mute(@NotNull @Body UserMuteRequestData userMuteRequestData);

  @POST("moderation/unmute")
  Call<StreamResponseObject> unmute(@NotNull @Body UserUnmuteRequestData userUnmuteRequestData);

  @GET("users/{user_id}/export")
  Call<UserExportResponse> export(@NotNull @Path("user_id") String userId);

  @POST("guest")
  Call<UserCreateGuestResponse> createGuest(
      @NotNull @Body UserCreateGuestRequestData userCreateGuestRequestData);

  @DELETE("moderation/ban")
  Call<StreamResponseObject> unban(
      @NotNull @Query("target_user_id") String targetUserId,
      @Nullable @Query("type") String channelType,
      @Nullable @Query("id") String channelId,
      @Nullable @Query("shadow") Boolean shadow,
      @Nullable @Query("remove_future_channels_ban") Boolean removeFutureChannelsBan,
      @Nullable @Query("created_by") String createdBy);

  @GET("query_future_channel_bans")
  Call<UserQueryFutureChannelBansResponse> queryFutureChannelBans(
      @NotNull @ToJson @Query("payload") UserQueryFutureChannelBansRequestData requestData);
}
