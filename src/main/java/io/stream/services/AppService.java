package io.stream.services;

import io.stream.models.App;
import io.stream.models.App.AppSettings;
import io.stream.models.framework.StreamResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;

public interface AppService {
  @GET("app")
  @NotNull
  Call<App> get();

  @PATCH("app")
  @NotNull
  Call<StreamResponse> update(@NotNull @Body AppSettings appSettings);
}
