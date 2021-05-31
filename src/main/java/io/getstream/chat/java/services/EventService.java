package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Event.EventSendRequestData;
import io.getstream.chat.java.models.Event.EventSendResponse;
import io.getstream.chat.java.models.Event.EventSendUserCustomRequestData;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventService {

  @POST("channels/{type}/{id}/event")
  Call<EventSendResponse> send(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Body EventSendRequestData eventSendRequestData);

  @POST("users/{user_id}/event")
  Call<StreamResponseObject> sendUserCustom(
      @NotNull @Path("user_id") String userId,
      @NotNull @Body EventSendUserCustomRequestData eventSendUserCustomRequestData);
}
