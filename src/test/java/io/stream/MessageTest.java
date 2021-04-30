package io.stream;

import io.stream.models.Channel;
import io.stream.models.Message;
import io.stream.models.Message.MessageRequestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MessageTest extends BasicTest {

  @DisplayName("Can send a message")
  @Test
  void whenSendingAMessage_thenNoException() {
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    String text = "This is a message";
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().withText(text).withUserId(serverUser.getId()).build();
    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(channel.getType(), channel.getId())
                        .withMessage(messageRequest)
                        .request())
            .getMessage();
    Assertions.assertEquals(text, message.getText());
  }

  @DisplayName("Can update a message")
  @Test
  void whenUpdatingAMessage_thenNoException() {
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    String text = "This is a message";
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().withText(text).withUserId(serverUser.getId()).build();
    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(channel.getType(), channel.getId())
                        .withMessage(messageRequest)
                        .request())
            .getMessage();
    String updatedText = "This is an updated message";
    MessageRequestObject updatedMessageRequest =
        MessageRequestObject.builder().withText(updatedText).withUserId(serverUser.getId()).build();
    Message updatedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.update(message.getId()).withMessage(updatedMessageRequest).request())
            .getMessage();
    Assertions.assertEquals(updatedText, updatedMessage.getText());
  }
}
