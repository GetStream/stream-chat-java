package io.stream.services;

import org.jetbrains.annotations.NotNull;
import io.stream.models.User.UserListRequestData;
import io.stream.models.User.UserListResponse;
import io.stream.models.User.UserPartialUpdateRequestData;
import io.stream.models.User.UserPartialUpdateResponse;
import io.stream.models.User.UserUpsertRequestData;
import io.stream.models.User.UserUpsertResponse;
import io.stream.services.framework.ToJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
  @POST("users")
  Call<UserUpsertResponse> upsert(@NotNull @Body UserUpsertRequestData userUpsertRequestData);

  @GET("users")
  Call<UserListResponse> list(
      @NotNull @ToJson @Query("payload") UserListRequestData userListRequestData);
  
  @PATCH("users")
  Call<UserPartialUpdateResponse> partialUpdate(@NotNull @Body UserPartialUpdateRequestData userPartialUpdateRequestData);
}
