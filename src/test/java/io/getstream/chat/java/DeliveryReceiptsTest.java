package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.DeliveredMessageConfirmation;
import io.getstream.chat.java.models.MarkDeliveredOptions;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Message.MessageType;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for the delivery receipts functionality. */
public class DeliveryReceiptsTest extends BasicTest {

  @DisplayName("Can mark channels as delivered")
  @Test
  void whenMarkingChannelsDelivered_thenNoException() throws StreamException {
    // Send a test message first
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text("Test message for delivery receipts")
            .userId(testUserRequestObject.getId())
            .type(MessageType.REGULAR)
            .build();

    Message message =
        Message.send(testChannel.getType(), testChannel.getId())
            .message(messageRequest)
            .request()
            .getMessage();

    // Create delivered message confirmation
    DeliveredMessageConfirmation confirmation = new DeliveredMessageConfirmation();
    confirmation.setCid(testChannel.getCId());
    confirmation.setId(message.getId());

    // Mark channels as delivered
    MarkDeliveredOptions.MarkDeliveredResponse response =
        MarkDeliveredOptions.markChannelsDelivered(Arrays.asList(confirmation)).request();

    // Verify response is not null (successful call)
    Assertions.assertNotNull(response);
  }

  @DisplayName("Can mark channels as delivered with options")
  @Test
  void whenMarkingChannelsDeliveredWithOptions_thenNoException() throws StreamException {
    // Send a test message first
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text("Test message for delivery receipts with options")
            .userId(testUserRequestObject.getId())
            .type(MessageType.REGULAR)
            .build();

    Message message =
        Message.send(testChannel.getType(), testChannel.getId())
            .message(messageRequest)
            .request()
            .getMessage();

    // Create delivered message confirmation
    DeliveredMessageConfirmation confirmation = new DeliveredMessageConfirmation();
    confirmation.setCid(testChannel.getCId());
    confirmation.setId(message.getId());

    // Create mark delivered options
    MarkDeliveredOptions options = new MarkDeliveredOptions();
    options.setLatestDeliveredMessages(Arrays.asList(confirmation));
    options.setUserId(testUserRequestObject.getId());

    // Mark channels as delivered
    MarkDeliveredOptions.MarkDeliveredResponse response =
        MarkDeliveredOptions.markChannelsDelivered(options).request();

    // Verify response is not null (successful call)
    Assertions.assertNotNull(response);
  }

  @DisplayName("Can mark channels as delivered with multiple messages")
  @Test
  void whenMarkingChannelsDeliveredWithMultipleMessages_thenNoException() throws StreamException {
    // Send multiple test messages
    List<Message> messages =
        Arrays.asList(
            Message.send(testChannel.getType(), testChannel.getId())
                .message(
                    MessageRequestObject.builder()
                        .text("Test message 1")
                        .userId(testUserRequestObject.getId())
                        .type(MessageType.REGULAR)
                        .build())
                .request()
                .getMessage(),
            Message.send(testChannel.getType(), testChannel.getId())
                .message(
                    MessageRequestObject.builder()
                        .text("Test message 2")
                        .userId(testUserRequestObject.getId())
                        .type(MessageType.REGULAR)
                        .build())
                .request()
                .getMessage());

    // Create delivered message confirmations
    List<DeliveredMessageConfirmation> confirmations =
        Arrays.asList(
            createConfirmation(testChannel.getCId(), messages.get(0).getId()),
            createConfirmation(testChannel.getCId(), messages.get(1).getId()));

    // Mark channels as delivered
    MarkDeliveredOptions.MarkDeliveredResponse response =
        MarkDeliveredOptions.markChannelsDelivered(confirmations).request();

    // Verify response is not null (successful call)
    Assertions.assertNotNull(response);
  }

  private DeliveredMessageConfirmation createConfirmation(String cid, String messageId) {
    DeliveredMessageConfirmation confirmation = new DeliveredMessageConfirmation();
    confirmation.setCid(cid);
    confirmation.setId(messageId);
    return confirmation;
  }
}
