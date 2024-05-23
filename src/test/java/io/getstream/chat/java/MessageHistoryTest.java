package io.getstream.chat.java;

import io.getstream.chat.java.models.FilterCondition;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.MessageHistory;
import io.getstream.chat.java.models.Sort;
import io.getstream.chat.java.models.Sort.Direction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MessageHistoryTest extends BasicTest {
  @Test
  @DisplayName("Can get message history")
  void whenMessageUpdated_thenGetHistory() {
    Assertions.assertDoesNotThrow(
        () -> {
          var channel = Assertions.assertDoesNotThrow(BasicTest::createRandomChannel).getChannel();
          Assertions.assertNotNull(channel);

          final var initialText = "initial text";
          final var customField = "custom_field";
          final var initialCustomFieldValue = "custom value";
          MessageRequestObject messageRequest =
              MessageRequestObject.builder()
                  .text(initialText)
                  .userId(testUserRequestObject.getId())
                  .additionalField(customField, initialCustomFieldValue)
                  .build();
          var message =
              Message.send(channel.getType(), channel.getId())
                  .message(messageRequest)
                  .request()
                  .getMessage();

          final var updatedText1 = "updated text";
          final var updatedCustomFieldValue = "updated custom value";
          Assertions.assertDoesNotThrow(
              () ->
                  Message.update(message.getId())
                      .message(
                          MessageRequestObject.builder()
                              .text(updatedText1)
                              .userId(testUserRequestObject.getId())
                              .additionalField(customField, updatedCustomFieldValue)
                              .build())
                      .request());

          final var updatedText2 = "updated text 2";
          var secondUser = testUsersRequestObjects.get(1);
          var r =
              Assertions.assertDoesNotThrow(
                      () ->
                          Message.update(message.getId())
                              .message(
                                  MessageRequestObject.builder()
                                      .text(updatedText2)
                                      .userId(secondUser.getId())
                                      .build())
                              .request())
                  .getMessage();
          Assertions.assertEquals(updatedText2, r.getText());

          var messageHistoryQueryRequest =
              MessageHistory.query().filter(FilterCondition.eq("message_id", message.getId()));

          var messageHistoryResponse =
              Assertions.assertDoesNotThrow(() -> messageHistoryQueryRequest.request());

          var history = messageHistoryResponse.getMessageHistory();
          Assertions.assertEquals(2, history.size());

          var firstUpdate = history.get(1);
          Assertions.assertEquals(initialText, firstUpdate.getText());
          Assertions.assertEquals(
              testUserRequestObject.getId(), firstUpdate.getMessageUpdatedById());
          Assertions.assertEquals(
              initialCustomFieldValue, firstUpdate.getAdditionalFields().get(customField));
          var secondUpdate = history.get(0);
          Assertions.assertEquals(updatedText1, secondUpdate.getText());
          Assertions.assertEquals(secondUser.getId(), secondUpdate.getMessageUpdatedById());
          Assertions.assertEquals(
              updatedCustomFieldValue, secondUpdate.getAdditionalFields().get(customField));

          // Test sorting
          var sortedHistory =
              Assertions.assertDoesNotThrow(
                      () ->
                          MessageHistory.query()
                              .filter(FilterCondition.eq("message_id", message.getId()))
                              .sort(
                                  Sort.builder()
                                      .field("message_updated_at")
                                      .direction(Direction.ASC)
                                      .build())
                              .request())
                  .getMessageHistory();

          Assertions.assertEquals(2, sortedHistory.size());

          firstUpdate = sortedHistory.get(0);
          Assertions.assertEquals(initialText, firstUpdate.getText());
          Assertions.assertEquals(
              testUserRequestObject.getId(), firstUpdate.getMessageUpdatedById());

          secondUpdate = sortedHistory.get(1);
          Assertions.assertEquals(updatedText1, secondUpdate.getText());
          Assertions.assertEquals(secondUser.getId(), secondUpdate.getMessageUpdatedById());
        });
  }
}
