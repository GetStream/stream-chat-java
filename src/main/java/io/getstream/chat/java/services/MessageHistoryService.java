package io.getstream.chat.java.services;

import io.getstream.chat.java.models.MessageHistory.MessageHistoryQueryRequestData;
import io.getstream.chat.java.models.MessageHistory.MessageHistoryQueryResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MessageHistoryService {
  @POST("messages/history")
  Call<MessageHistoryQueryResponse> query(
      @NotNull @Body MessageHistoryQueryRequestData messageHistoryQueryRequestData);
}
