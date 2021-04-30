package io.stream.services;

import io.stream.models.Message.MessageSendRequestData;
import io.stream.models.Message.MessageSendResponse;
import io.stream.models.Message.MessageUpdateRequestData;
import io.stream.models.Message.MessageUpdateResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {
  @POST("channels/{type}/{id}/message")
  Call<MessageSendResponse> send(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Body MessageSendRequestData messageSendRequestData);

  @POST("messages/{id}")
  Call<MessageUpdateResponse> update(
      @NotNull @Path("id") String id,
      @NotNull @Body MessageUpdateRequestData messageUpdateRequestData);
}
