package io.getstream.chat.java.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.getstream.chat.java.models.User.UserBanRequestData;
import io.getstream.chat.java.models.User.UserCreateGuestRequestData;
import io.getstream.chat.java.models.User.UserCreateGuestResponse;
import io.getstream.chat.java.models.User.UserDeactivateRequestData;
import io.getstream.chat.java.models.User.UserDeactivateResponse;
import io.getstream.chat.java.models.User.UserDeleteResponse;
import io.getstream.chat.java.models.User.UserExportResponse;
import io.getstream.chat.java.models.User.UserListRequestData;
import io.getstream.chat.java.models.User.UserListResponse;
import io.getstream.chat.java.models.User.UserMuteRequestData;
import io.getstream.chat.java.models.User.UserMuteResponse;
import io.getstream.chat.java.models.User.UserPartialUpdateRequestData;
import io.getstream.chat.java.models.User.UserPartialUpdateResponse;
import io.getstream.chat.java.models.User.UserQueryBannedRequestData;
import io.getstream.chat.java.models.User.UserQueryBannedResponse;
import io.getstream.chat.java.models.User.UserReactivateRequestData;
import io.getstream.chat.java.models.User.UserReactivateResponse;
import io.getstream.chat.java.models.User.UserUnmuteRequestData;
import io.getstream.chat.java.models.User.UserUpsertRequestData;
import io.getstream.chat.java.models.User.UserUpsertResponse;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.framework.ToJson;
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
