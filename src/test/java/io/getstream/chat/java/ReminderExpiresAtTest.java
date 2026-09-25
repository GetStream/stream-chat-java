package io.getstream.chat.java;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.getstream.chat.java.models.Reminder;
import io.getstream.chat.java.models.Reminder.ReminderQueryResponse;
import java.util.Date;
import java.util.TimeZone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReminderExpiresAtTest {

  // Mirrors the visibility and date configuration of DefaultClient's mapper.
  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
          .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
          .setDateFormat(
              new StdDateFormat()
                  .withColonInTimeZone(true)
                  .withTimeZone(TimeZone.getTimeZone("UTC")));

  private static final Date EXPIRES_AT = new Date(1893456000000L); // 2030-01-01T00:00:00Z

  @DisplayName("Create sends expires_at when set")
  @Test
  void whenCreatingWithExpiresAt_thenBodyCarriesIt() throws Exception {
    String body =
        MAPPER.writeValueAsString(
            Reminder.createReminder("msg").userId("user").expiresAt(EXPIRES_AT).internalBuild());

    Assertions.assertTrue(body.contains("\"expires_at\":\"2030-01-01T00:00:00.000+00:00\""), body);
  }

  @DisplayName("Update sends expires_at when set")
  @Test
  void whenUpdatingWithExpiresAt_thenBodyCarriesIt() throws Exception {
    String body =
        MAPPER.writeValueAsString(
            Reminder.updateReminder("msg").userId("user").expiresAt(EXPIRES_AT).internalBuild());

    Assertions.assertTrue(body.contains("\"expires_at\":\"2030-01-01T00:00:00.000+00:00\""), body);
  }

  @DisplayName("Create without expires_at sends null, which means no expiry")
  @Test
  void whenCreatingWithoutExpiresAt_thenBodyCarriesNull() throws Exception {
    String body =
        MAPPER.writeValueAsString(Reminder.createReminder("msg").userId("user").internalBuild());

    Assertions.assertTrue(body.contains("\"expires_at\":null"), body);
  }

  @DisplayName("Responses read expires_at into the typed field")
  @Test
  void whenResponseHasExpiresAt_thenGetterReturnsIt() throws Exception {
    ReminderQueryResponse response =
        MAPPER.readValue(
            "{\"reminders\":[{\"id\":\"r\",\"message_id\":\"msg\",\"user_id\":\"user\","
                + "\"channel_cid\":\"messaging:chan\",\"expires_at\":\"2030-01-01T00:00:00Z\"}],"
                + "\"duration\":\"1ms\"}",
            ReminderQueryResponse.class);

    Reminder reminder = response.getReminders().get(0);
    Assertions.assertEquals(EXPIRES_AT, reminder.getExpiresAt());
    Assertions.assertFalse(reminder.getAdditionalFields().containsKey("expires_at"));
  }
}
