package io.stream.services;

import org.jetbrains.annotations.NotNull;
import io.stream.models.Flag.FlagCreateRequestData;
import io.stream.models.Flag.FlagCreateResponse;
import io.stream.models.Flag.FlagDeleteRequestData;
import io.stream.models.Flag.FlagDeleteResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FlagService {

  @POST("moderation/flag")
  Call<FlagCreateResponse> create(@NotNull @Body FlagCreateRequestData internalBuild);

  @POST("moderation/unflag")
  Call<FlagDeleteResponse> delete(@NotNull @Body FlagDeleteRequestData internalBuild);
}
