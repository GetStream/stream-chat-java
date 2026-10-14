package io.getstream.chat.java;

import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Message.MessageSendResponse;
import io.getstream.chat.java.models.ScheduledMessage;
import io.getstream.chat.java.models.ScheduledMessage.ScheduledMessagesFilter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

/**
 * Runs against a real app. The scheduled messages endpoints are behind a per-app flag, so these
 * tests run only when STREAM_SCHEDULED_MESSAGES_ENABLED=true says the flag is on for the app of
 * STREAM_KEY.
 */
@EnabledIfEnvironmentVariable(named = "STREAM_SCHEDULED_MESSAGES_ENABLED", matches = "true")
public class ScheduledMessageIntegrationTest extends BasicTest {

  private static final long ONE_HOUR = 60 * 60 * 1000L;

  private static MessageSendResponse schedule(String text, Date scheduledAt) {
    return Assertions.assertDoesNotThrow(
        () ->
            Message.send(testChannel.getType(), testChannel.getId())
                .message(
                    MessageRequestObject.builder()
                        .text(text)
                        .userId(testUserRequestObject.getId())
                        .build())
                .scheduledAt(scheduledAt)
                .request());
  }

  @DisplayName("Can schedule a message on behalf of a user")
  @Test
  void whenSchedulingAMessage_thenResponseCarriesTheScheduledMessage() {
    String text = UUID.randomUUID().toString();
    Date scheduledAt = new Date(System.currentTimeMillis() + ONE_HOUR);

    MessageSendResponse response = schedule(text, scheduledAt);

    ScheduledMessage sm = response.getScheduledMessage();
    Assertions.assertNotNull(sm);
    Assertions.assertEquals(ScheduledMessage.STATUS_SCHEDULED, sm.getStatus());
    Assertions.assertEquals(testChannel.getType() + ":" + testChannel.getId(), sm.getCid());
    Assertions.assertEquals(testUserRequestObject.getId(), sm.getUser().getId());
    Assertions.assertEquals(text, sm.getMessage().getText());
    Assertions.assertEquals(scheduledAt.getTime() / 1000, sm.getScheduledAt().getTime() / 1000);
  }

  @DisplayName("Can query a user's scheduled messages in a channel")
  @Test
  void whenQueryingScheduledMessages_thenTheScheduledMessageIsListed() {
    ScheduledMessage sm =
        schedule(UUID.randomUUID().toString(), new Date(System.currentTimeMillis() + ONE_HOUR))
            .getScheduledMessage();

    List<ScheduledMessage> listed =
        Assertions.assertDoesNotThrow(
                () ->
                    ScheduledMessage.query()
                        .userId(testUserRequestObject.getId())
                        .filter(ScheduledMessagesFilter.builder().cid(sm.getCid()).build())
                        .limit(100)
                        .request())
            .getScheduledMessages();

    ScheduledMessage found =
        listed.stream().filter(m -> m.getId().equals(sm.getId())).findFirst().orElse(null);
    Assertions.assertNotNull(found);
    Assertions.assertNotNull(found.getChannel());
    Assertions.assertEquals(testChannel.getId(), found.getChannel().getId());
  }

  @DisplayName("Can update the message and send time of a scheduled message")
  @Test
  void whenUpdatingAScheduledMessage_thenTheChangeIsReturned() {
    ScheduledMessage sm =
        schedule(UUID.randomUUID().toString(), new Date(System.currentTimeMillis() + ONE_HOUR))
            .getScheduledMessage();
    Date newTime = new Date(System.currentTimeMillis() + 2 * ONE_HOUR);

    ScheduledMessage updated =
        Assertions.assertDoesNotThrow(
                () ->
                    ScheduledMessage.update(sm.getId())
                        .message(MessageRequestObject.builder().text("edited").build())
                        .scheduledAt(newTime)
                        .request())
            .getScheduledMessage();

    Assertions.assertEquals(sm.getId(), updated.getId());
    Assertions.assertEquals("edited", updated.getMessage().getText());
    Assertions.assertEquals(newTime.getTime() / 1000, updated.getScheduledAt().getTime() / 1000);
  }

  @DisplayName("Can cancel a scheduled message")
  @Test
  void whenDeletingAScheduledMessage_thenItIsCanceled() {
    ScheduledMessage sm =
        schedule(UUID.randomUUID().toString(), new Date(System.currentTimeMillis() + ONE_HOUR))
            .getScheduledMessage();

    ScheduledMessage deleted =
        Assertions.assertDoesNotThrow(() -> ScheduledMessage.delete(sm.getId()).request())
            .getScheduledMessage();

    Assertions.assertEquals(sm.getId(), deleted.getId());
    Assertions.assertEquals(ScheduledMessage.STATUS_CANCELED, deleted.getStatus());
  }

  @DisplayName("Can send a scheduled message now")
  @Test
  void whenSendingAScheduledMessageNow_thenItsSendTimeMovesToNow() {
    ScheduledMessage sm =
        schedule(UUID.randomUUID().toString(), new Date(System.currentTimeMillis() + ONE_HOUR))
            .getScheduledMessage();

    ScheduledMessage sent =
        Assertions.assertDoesNotThrow(() -> ScheduledMessage.sendNow(sm.getId()).request())
            .getScheduledMessage();

    Assertions.assertEquals(sm.getId(), sent.getId());
    Assertions.assertTrue(sent.getScheduledAt().before(sm.getScheduledAt()));
  }
}
