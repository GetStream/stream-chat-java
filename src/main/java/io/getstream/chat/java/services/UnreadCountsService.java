package io.getstream.chat.java.services;

import io.getstream.chat.java.models.UnreadCounts.*;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.*;

public interface UnreadCountsService {
  @GET("unread")
  Call<UnreadCountsResponse> get(@NotNull @Query("user_id") String userId);

  @POST("unread_batch")
  Call<UnreadCountsBatchResponse> batch(
      @NotNull @Body UnreadCountsBatchRequestData unreadCountsBatchRequestData);
}
