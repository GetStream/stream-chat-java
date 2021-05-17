package io.getstream.services;

import io.getstream.models.Flag.FlagCreateRequestData;
import io.getstream.models.Flag.FlagCreateResponse;
import io.getstream.models.Flag.FlagDeleteRequestData;
import io.getstream.models.Flag.FlagDeleteResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FlagService {

  @POST("moderation/flag")
  Call<FlagCreateResponse> create(@NotNull @Body FlagCreateRequestData flagCreateRequestData);

  @POST("moderation/unflag")
  Call<FlagDeleteResponse> delete(@NotNull @Body FlagDeleteRequestData flagDeleteRequestData);
}
