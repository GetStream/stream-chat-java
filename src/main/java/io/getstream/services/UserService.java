package io.getstream.services;

import io.getstream.models.User.UserBanRequestData;
import io.getstream.models.User.UserCreateGuestRequestData;
import io.getstream.models.User.UserCreateGuestResponse;
import io.getstream.models.User.UserDeactivateRequestData;
import io.getstream.models.User.UserDeactivateResponse;
import io.getstream.models.User.UserDeleteResponse;
import io.getstream.models.User.UserExportResponse;
import io.getstream.models.User.UserListRequestData;
import io.getstream.models.User.UserListResponse;
import io.getstream.models.User.UserMuteRequestData;
import io.getstream.models.User.UserMuteResponse;
import io.getstream.models.User.UserPartialUpdateRequestData;
import io.getstream.models.User.UserPartialUpdateResponse;
import io.getstream.models.User.UserQueryBannedRequestData;
import io.getstream.models.User.UserQueryBannedResponse;
import io.getstream.models.User.UserReactivateRequestData;
import io.getstream.models.User.UserReactivateResponse;
import io.getstream.models.User.UserUnmuteRequestData;
import io.getstream.models.User.UserUpsertRequestData;
import io.getstream.models.User.UserUpsertResponse;
import io.getstream.models.framework.StreamResponseObject;
import io.getstream.services.framework.ToJson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
      @Nullable @Query("id") String channelId);
}
