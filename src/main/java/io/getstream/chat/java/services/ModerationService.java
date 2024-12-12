package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Moderation.*;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.*;

public interface ModerationService {
  @GET("api/v2/moderation/config/{key}")
  Call<ConfigGetResponse> getConfig(@NotNull @Path("key") String key);

  @DELETE("api/v2/moderation/config/{key}")
  Call<StreamResponseObject> deleteConfig(@NotNull @Path("key") String key);

  @POST("api/v2/moderation/config")
  Call<UpsertConfigResponse> upsertConfig(@Nullable @Body UpsertConfigRequestData upsertConfig);
}
