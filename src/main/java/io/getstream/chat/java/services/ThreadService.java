package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Thread.QueryThreadsRequestData;
import io.getstream.chat.java.models.Thread.QueryThreadsResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.*;

public interface ThreadService {
  @POST("threads")
  Call<QueryThreadsResponse> queryThreads(
      @NotNull @Body QueryThreadsRequestData queryThreadsRequestData);
}
