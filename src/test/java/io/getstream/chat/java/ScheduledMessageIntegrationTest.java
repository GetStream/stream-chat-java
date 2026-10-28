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

/**
 * Runs against a real app. The scheduled messages endpoints are behind a per-app flag, which is on
 * for the app of STREAM_KEY that CI uses.
 */
public class ScheduledMessageIntegrationTest extends BasicTest {

  private static final long ONE_MINUTE = 60 * 1000L;
  private static final long ONE_HOUR = 60 * ONE_MINUTE;

  /**
   * Returns a time offsetMillis from now, floored to a whole minute. The server rounds a future
   * scheduled_at up to the next whole UTC minute, so only a whole-minute time is stored unchanged.
   */
  private static Date wholeMinuteFromNow(long offsetMillis) {
    long t = System.currentTimeMillis() + offsetMillis;
    return new Date(t - t % ONE_MINUTE);
  }

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
    Date scheduledAt = wholeMinuteFromNow(ONE_HOUR);

    MessageSendResponse response = schedule(text, scheduledAt);

    ScheduledMessage sm = response.getScheduledMessage();
    Assertions.assertNotNull(sm);
    Assertions.assertEquals(ScheduledMessage.STATUS_SCHEDULED, sm.getStatus());
    Assertions.assertEquals(testChannel.getType() + ":" + testChannel.getId(), sm.getCid());
    Assertions.assertEquals(testUserRequestObject.getId(), sm.getUser().getId());
    Assertions.assertEquals(text, sm.getMessage().getText());
    Assertions.assertEquals(scheduledAt, sm.getScheduledAt());
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
    Date newTime = wholeMinuteFromNow(2 * ONE_HOUR);

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
    Assertions.assertEquals(newTime, updated.getScheduledAt());
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
