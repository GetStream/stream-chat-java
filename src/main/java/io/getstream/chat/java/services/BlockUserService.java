package io.getstream.chat.java.services;

import io.getstream.chat.java.models.BlockUser;
import io.getstream.chat.java.models.BlockUser.BlockUserResponse;
import io.getstream.chat.java.models.BlockUser.GetBlockedUsersResponse;
import io.getstream.chat.java.models.BlockUser.BlockUserRequestData.BlockUserRequest;
import io.getstream.chat.java.models.BlockUser.GetBlockedUsersRequestData.GetBlockedUsersRequest;
import io.getstream.chat.java.models.BlockUser.UnblockUserRequestData.UnblockUserRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BlockUserService {
  @POST("users/block")
  Call<BlockUserResponse> blockUser(@NotNull @Body BlockUser.BlockUserRequestData data);

  @POST("users/unblock")
  Call<BlockUser.UnblockUserResponse> unblockUser(@NotNull @Body BlockUser.UnblockUserRequestData data);

  @GET("users/block")
  Call<GetBlockedUsersResponse> getBlockedUsers(@Query("user_id") String blockedByUserID);
}
