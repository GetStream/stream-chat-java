package io.getstream.services;

import io.getstream.models.App;
import io.getstream.models.App.AppCheckPushRequestData;
import io.getstream.models.App.AppCheckPushResponse;
import io.getstream.models.App.AppCheckSqsRequestData;
import io.getstream.models.App.AppCheckSqsResponse;
import io.getstream.models.App.AppGetRateLimitsResponse;
import io.getstream.models.App.AppUpdateRequestData;
import io.getstream.models.framework.StreamResponseObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AppService {
  @GET("app")
  @NotNull
  Call<App> get();

  @PATCH("app")
  @NotNull
  Call<StreamResponseObject> update(@NotNull @Body AppUpdateRequestData appSettings);

  @GET("rate_limits")
  Call<AppGetRateLimitsResponse> getRateLimits(
      @Nullable @Query("serverSide") Boolean serverSide,
      @Nullable @Query("android") Boolean android,
      @Nullable @Query("ios") Boolean ios,
      @Nullable @Query("web") Boolean web,
      @Nullable @Query("endpoints") String endpoints);

  @POST("check_sqs")
  Call<AppCheckSqsResponse> checkSqs(@NotNull @Body AppCheckSqsRequestData appCheckSqsRequestData);

  @POST("check_push")
  Call<AppCheckPushResponse> checkPush(@NotNull @Body AppCheckPushRequestData internalBuild);
}
