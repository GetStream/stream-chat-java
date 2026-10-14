package io.getstream.chat.java.services;

import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessageDeleteResponse;
import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessageQueryRequestData;
import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessageQueryResponse;
import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessageSendNowResponse;
import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessageUpdateRequestData;
import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessageUpdateResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ScheduledMessageService {
  @PATCH("scheduled_messages/{id}")
  Call<ScheduledMessageUpdateResponse> update(
      @NotNull @Path("id") String id,
      @NotNull @Body ScheduledMessageUpdateRequestData scheduledMessageUpdateRequestData);

  @DELETE("scheduled_messages/{id}")
  Call<ScheduledMessageDeleteResponse> delete(@NotNull @Path("id") String id);

  @POST("scheduled_messages/{id}/send_now")
  Call<ScheduledMessageSendNowResponse> sendNow(@NotNull @Path("id") String id);

  @POST("scheduled_messages/query")
  Call<ScheduledMessageQueryResponse> query(
      @NotNull @Body ScheduledMessageQueryRequestData scheduledMessageQueryRequestData);
}
