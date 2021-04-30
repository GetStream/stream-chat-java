package io.stream.services;

import org.jetbrains.annotations.NotNull;
import io.stream.models.User.UserListRequestData;
import io.stream.models.User.UsersListResponse;
import io.stream.models.User.UserUpsertRequestData;
import io.stream.models.User.UsersUpsertResponse;
import io.stream.services.framework.ToJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
  @POST("users")
  Call<UsersUpsertResponse> upsert(@NotNull @Body UserUpsertRequestData userUpsertRequestData);

  @GET("users")
  Call<UsersListResponse> list(
      @NotNull @ToJson @Query("payload") UserListRequestData userListRequestData);
}
