package io.stream.services;

import io.stream.models.User.UserBanRequestData;
import io.stream.models.User.UserDeactivateRequestData;
import io.stream.models.User.UserDeactivateResponse;
import io.stream.models.User.UserDeleteResponse;
import io.stream.models.User.UserListRequestData;
import io.stream.models.User.UserListResponse;
import io.stream.models.User.UserPartialUpdateRequestData;
import io.stream.models.User.UserPartialUpdateResponse;
import io.stream.models.User.UserQueryBannedRequestData;
import io.stream.models.User.UserQueryBannedResponse;
import io.stream.models.User.UserReactivateRequestData;
import io.stream.models.User.UserReactivateResponse;
import io.stream.models.User.UserUpsertRequestData;
import io.stream.models.User.UserUpsertResponse;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.framework.ToJson;
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
}
