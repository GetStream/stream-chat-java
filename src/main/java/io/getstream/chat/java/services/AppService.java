package io.getstream.chat.java.services;

import io.getstream.chat.java.models.App;
import io.getstream.chat.java.models.App.AppCheckPushRequestData;
import io.getstream.chat.java.models.App.AppCheckPushResponse;
import io.getstream.chat.java.models.App.AppCheckSnsRequestData;
import io.getstream.chat.java.models.App.AppCheckSnsResponse;
import io.getstream.chat.java.models.App.AppCheckSqsRequestData;
import io.getstream.chat.java.models.App.AppCheckSqsResponse;
import io.getstream.chat.java.models.App.AppGetRateLimitsResponse;
import io.getstream.chat.java.models.App.AppUpdateRequestData;
import io.getstream.chat.java.models.App.ListPushProviderResponse;
import io.getstream.chat.java.models.App.PushProviderRequestData;
import io.getstream.chat.java.models.App.UpsertPushProviderResponse;
import io.getstream.chat.java.models.framework.StreamResponseObject;
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

  @POST("check_sns")
  Call<AppCheckSnsResponse> checkSns(@NotNull @Body AppCheckSnsRequestData appCheckSnsRequestData);

  @POST("check_push")
  Call<AppCheckPushResponse> checkPush(@NotNull @Body AppCheckPushRequestData internalBuild);

  @POST("push_providers")
  Call<UpsertPushProviderResponse> upsertPushProvider(
      @NotNull @Body PushProviderRequestData pushProviderRequestData);

  @GET("push_providers")
  Call<ListPushProviderResponse> listPushProviders();

  @DELETE("push_providers/{type}/{name}")
  Call<StreamResponseObject> deletePushProvider(
      @NotNull @Path("type") String providerType, @NotNull @Path("name") String name);
}
