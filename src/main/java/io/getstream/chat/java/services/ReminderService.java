package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Reminder.ReminderCreateRequestData;
import io.getstream.chat.java.models.Reminder.ReminderCreateResponse;
import io.getstream.chat.java.models.Reminder.ReminderDeleteResponse;
import io.getstream.chat.java.models.Reminder.ReminderQueryRequestData;
import io.getstream.chat.java.models.Reminder.ReminderQueryResponse;
import io.getstream.chat.java.models.Reminder.ReminderUpdateRequestData;
import io.getstream.chat.java.models.Reminder.ReminderUpdateResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReminderService {
  @POST("messages/{id}/reminders")
  Call<ReminderCreateResponse> create(
      @NotNull @Path("id") String messageId,
      @NotNull @Body ReminderCreateRequestData reminderCreateRequestData);

  @PATCH("messages/{id}/reminders")
  Call<ReminderUpdateResponse> update(
      @NotNull @Path("id") String messageId,
      @NotNull @Body ReminderUpdateRequestData reminderUpdateRequestData);

  @DELETE("messages/{id}/reminders")
  Call<ReminderDeleteResponse> delete(
      @NotNull @Path("id") String messageId, @NotNull @Query("user_id") String userId);

  @POST("reminders/query")
  Call<ReminderQueryResponse> query(
      @NotNull @Body ReminderQueryRequestData reminderQueryRequestData);
}
