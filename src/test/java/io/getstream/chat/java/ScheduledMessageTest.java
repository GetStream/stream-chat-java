package io.getstream.chat.java;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.getstream.chat.java.models.Event;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Message.MessageSendResponse;
import io.getstream.chat.java.models.ScheduledMessage;
import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessageQueryResponse;
import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessagesFilter;
import io.getstream.chat.java.models.Sort;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScheduledMessageTest {

  // Mirrors the visibility, date and unknown-property configuration of DefaultClient's mapper.
  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
          .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
          .setDateFormat(
              new StdDateFormat()
                  .withColonInTimeZone(true)
                  .withTimeZone(TimeZone.getTimeZone("UTC")));

  private static final Date SCHEDULED_AT = new Date(1893456000000L); // 2030-01-01T00:00:00Z
  private static final String SCHEDULED_AT_JSON = "\"2030-01-01T00:00:00.000+00:00\"";

  private static final String SCHEDULED_MESSAGE_JSON =
      "{\"id\":\"sm-1\",\"cid\":\"messaging:general\","
          + "\"user\":{\"id\":\"gandalf\"},"
          + "\"message\":{\"text\":\"hello\",\"mentioned_users\":[\"frodo\"],"
          + "\"shared_location\":{\"latitude\":1.5,\"longitude\":2.5,"
          + "\"created_by_device_id\":\"dev\"},\"custom\":{\"color\":\"red\"}},"
          + "\"scheduled_at\":\"2030-01-01T00:00:00Z\",\"status\":\"failed\","
          + "\"failure_reason\":\"channel_frozen\","
          + "\"created_at\":\"2029-12-31T00:00:00Z\",\"updated_at\":\"2029-12-31T00:00:00Z\","
          + "\"channel\":{\"cid\":\"messaging:general\",\"type\":\"messaging\","
          + "\"id\":\"general\",\"name\":\"General\"}}";

  @DisplayName("Send carries scheduled_at when set")
  @Test
  void whenSendingWithScheduledAt_thenBodyCarriesIt() throws Exception {
    String body =
        MAPPER.writeValueAsString(
            Message.send("messaging", "general")
                .message(MessageRequestObject.builder().text("hi").userId("gandalf").build())
                .scheduledAt(SCHEDULED_AT)
                .internalBuild());

    Assertions.assertTrue(body.contains("\"scheduled_at\":" + SCHEDULED_AT_JSON), body);
  }

  @DisplayName("Send response reads scheduled_message")
  @Test
  void whenSendResponseHasScheduledMessage_thenGetterReturnsIt() throws Exception {
    MessageSendResponse response =
        MAPPER.readValue(
            "{\"message\":{\"id\":\"sm-1\",\"text\":\"hello\"},\"scheduled_message\":"
                + SCHEDULED_MESSAGE_JSON
                + ",\"duration\":\"1ms\"}",
            MessageSendResponse.class);

    Assertions.assertEquals("sm-1", response.getScheduledMessage().getId());
    Assertions.assertEquals("hello", response.getMessage().getText());
  }

  @DisplayName("Update sends only the fields that are set")
  @Test
  void whenUpdatingOnlyScheduledAt_thenMessageIsOmitted() throws Exception {
    String body =
        MAPPER.writeValueAsString(
            ScheduledMessage.update("sm-1").scheduledAt(SCHEDULED_AT).internalBuild());

    Assertions.assertEquals("{\"scheduled_at\":" + SCHEDULED_AT_JSON + "}", body);
  }

  @DisplayName("Update sends the replacement message")
  @Test
  void whenUpdatingMessage_thenBodyCarriesIt() throws Exception {
    String body =
        MAPPER.writeValueAsString(
            ScheduledMessage.update("sm-1")
                .message(MessageRequestObject.builder().text("edited").build())
                .internalBuild());

    Assertions.assertTrue(body.contains("\"message\":{"), body);
    Assertions.assertTrue(body.contains("\"text\":\"edited\""), body);
    Assertions.assertFalse(body.contains("scheduled_at"), body);
  }

  @DisplayName("Query sends user_id, filter, sort, limit and next")
  @Test
  void whenQuerying_thenBodyCarriesAllFields() throws Exception {
    String body =
        MAPPER.writeValueAsString(
            ScheduledMessage.query()
                .userId("gandalf")
                .filter(
                    ScheduledMessagesFilter.builder()
                        .cid("messaging:general")
                        .status(ScheduledMessage.STATUS_SCHEDULED)
                        .status(ScheduledMessage.STATUS_FAILED)
                        .build())
                .sort(Sort.builder().field("scheduled_at").direction(Sort.Direction.ASC).build())
                .limit(50)
                .next("cursor")
                .internalBuild());

    Assertions.assertTrue(body.contains("\"user_id\":\"gandalf\""), body);
    Assertions.assertTrue(
        body.contains(
            "\"filter\":{\"cid\":\"messaging:general\",\"status\":[\"scheduled\",\"failed\"]}"),
        body);
    Assertions.assertTrue(
        body.contains("\"sort\":[{\"field\":\"scheduled_at\",\"direction\":1}]"), body);
    Assertions.assertTrue(body.contains("\"limit\":50"), body);
    Assertions.assertTrue(body.contains("\"next\":\"cursor\""), body);
  }

  @DisplayName("Query without options sends user_id and no sort")
  @Test
  void whenQueryingWithoutOptions_thenOnlyUserIdIsSent() throws Exception {
    String body =
        MAPPER.writeValueAsString(ScheduledMessage.query().userId("gandalf").internalBuild());

    Assertions.assertEquals("{\"user_id\":\"gandalf\",\"sort\":[]}", body);
  }

  @DisplayName("Query response reads every scheduled message field")
  @Test
  void whenQueryResponseIsRead_thenFieldsAreTyped() throws Exception {
    ScheduledMessageQueryResponse response =
        MAPPER.readValue(
            "{\"scheduled_messages\":["
                + SCHEDULED_MESSAGE_JSON
                + "],\"next\":\"n\","
                + "\"duration\":\"1ms\"}",
            ScheduledMessageQueryResponse.class);

    Assertions.assertEquals("n", response.getNext());
    List<ScheduledMessage> items = response.getScheduledMessages();
    Assertions.assertEquals(1, items.size());
    ScheduledMessage sm = items.get(0);
    Assertions.assertEquals("sm-1", sm.getId());
    Assertions.assertEquals("messaging:general", sm.getCid());
    Assertions.assertEquals("gandalf", sm.getUser().getId());
    Assertions.assertEquals(SCHEDULED_AT, sm.getScheduledAt());
    Assertions.assertEquals(ScheduledMessage.STATUS_FAILED, sm.getStatus());
    Assertions.assertEquals("channel_frozen", sm.getFailureReason());
    Assertions.assertEquals("hello", sm.getMessage().getText());
    Assertions.assertEquals(List.of("frodo"), sm.getMessage().getMentionedUsers());
    Assertions.assertEquals(1.5, sm.getMessage().getSharedLocation().getLatitude());
    Assertions.assertEquals("red", sm.getMessage().getCustom().get("color"));
    Assertions.assertEquals("General", sm.getChannel().getName());
    Assertions.assertTrue(sm.getAdditionalFields().isEmpty(), sm.getAdditionalFields().toString());
  }

  @DisplayName("scheduled_message.sent events read the scheduled message and the sent message ID")
  @Test
  void whenEventIsScheduledMessageSent_thenFieldsAreTyped() throws Exception {
    Event event =
        MAPPER.readValue(
            "{\"type\":\"scheduled_message.sent\",\"cid\":\"messaging:general\","
                + "\"message_id\":\"m-1\",\"scheduled_message\":"
                + SCHEDULED_MESSAGE_JSON
                + "}",
            Event.class);

    Assertions.assertEquals("m-1", event.getMessageId());
    Assertions.assertEquals("sm-1", event.getScheduledMessage().getId());
    Assertions.assertFalse(event.getAdditionalFields().containsKey("scheduled_message"));
  }
}
