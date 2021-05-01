package io.stream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.stream.models.Channel;
import io.stream.models.Message;
import io.stream.models.Message.MessageRequestObject;
import io.stream.models.Message.SearchResult;

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

  @DisplayName("Can search messages with no exception and retrieve given message")
  @Test
  void whenSearchingMessages_thenNoExceptionAndRetrievesMessage() {
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    String text = "This is a message";
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().withText(text).withUserId(serverUser.getId()).build();
    Assertions.assertDoesNotThrow(
        () ->
            Message.send(channel.getType(), channel.getId()).withMessage(messageRequest).request());
    Map<String, Object> channelConditions = new HashMap<>();
    channelConditions.put("id", channel.getId());
    Map<String, Object> messageConditions = new HashMap<>();
    messageConditions.put("text", text);
    List<SearchResult> searchResults =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.search()
                        .withFilterConditions(channelConditions)
                        .withMessageFilterConditions(messageConditions)
                        .request())
            .getResults();
    Assertions.assertEquals(1, searchResults.size());
  }
}
