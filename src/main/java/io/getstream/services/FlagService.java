package io.getstream.services;

import io.getstream.models.Flag.FlagCreateRequestData;
import io.getstream.models.Flag.FlagCreateResponse;
import io.getstream.models.Flag.FlagDeleteRequestData;
import io.getstream.models.Flag.FlagDeleteResponse;
import io.getstream.models.Flag.FlagMessageQueryRequestData;
import io.getstream.models.Flag.FlagMessageQueryResponse;
import io.getstream.services.framework.ToJson;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FlagService {

  @POST("moderation/flag")
  Call<FlagCreateResponse> create(@NotNull @Body FlagCreateRequestData flagCreateRequestData);

  @POST("moderation/unflag")
  Call<FlagDeleteResponse> delete(@NotNull @Body FlagDeleteRequestData flagDeleteRequestData);

  @GET("moderation/flags/message")
  Call<FlagMessageQueryResponse> messageQuery(
      @NotNull @ToJson @Query("payload") FlagMessageQueryRequestData flagMessageQueryRequestData);
}
