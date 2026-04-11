package io.getstream.chat.java;

import io.getstream.chat.java.models.App;
import io.getstream.chat.java.models.App.FileUploadConfigRequestObject;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.*;
import io.getstream.chat.java.models.Sort;
import java.io.File;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MessageTest extends BasicTest {
  @DisplayName("Can retrieve a message")
  @Test
  void whenRetrievingAMessage_thenIsRetrieved() {
    Message retrievedMessage =
        Assertions.assertDoesNotThrow(() -> Message.get(testMessage.getId()).request())
            .getMessage();
    Assertions.assertTrue(retrievedMessage.getId().equals(testMessage.getId()));
  }

  @DisplayName("Can update a message")
  @Test
  void whenUpdatingAMessage_thenNoException() {
    // Should not use testMessage to not modify it
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    String updatedText = "This is an updated message";
    MessageRequestObject updatedMessageRequest =
        MessageRequestObject.builder()
            .text(updatedText)
            .userId(testUserRequestObject.getId())
            .build();
    Message updatedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.update(message.getId()).message(updatedMessageRequest).request())
            .getMessage();
    Assertions.assertEquals(updatedText, updatedMessage.getText());
  }

  @DisplayName("Searching with query and message filter conditions throws an exception")
  @Test
  void givenQueryAndMessageFilterConditions_whenSearchingMessages_thenThrowException() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            Message.search()
                .filterCondition("id", testChannel.getId())
                .query(testMessage.getText())
                .messageFilterCondition("text", testMessage.getText())
                .request());
  }

  @DisplayName("Searching without query or message filter conditions throws an exception")
  @Test
  void givenNothing_whenSearchingMessages_thenThrowException() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Message.search().filterCondition("id", testChannel.getId()).request());
  }

  @DisplayName("Searching with offset and next throws an exception")
  @Test
  void givenOffsetAndNext_whenSearchingMessages_thenThrowException() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            Message.search()
                .filterCondition("id", testChannel.getId())
                .query(testMessage.getText())
                .offset(1)
                .next("next")
                .request());
  }

  @DisplayName("Can search using next pagination and sorting")
  @Test
  @Disabled
  void givenNextSort_whenSearchingMessages_thenNoExceptionAndRetrievesMessages() {
    String text = UUID.randomUUID().toString();
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();
    Message msg1 =
        Assertions.assertDoesNotThrow(
            () ->
                Message.send(testChannel.getType(), testChannel.getId())
                    .message(messageRequest)
                    .request()
                    .getMessage());

    Message msg2 =
        Assertions.assertDoesNotThrow(
            () ->
                Message.send(testChannel.getType(), testChannel.getId())
                    .message(messageRequest)
                    .request()
                    .getMessage());

    Message.MessageSearchResponse responsePage1 =
        Assertions.assertDoesNotThrow(
            () ->
                Message.search()
                    .filterCondition("id", testChannel.getId())
                    .query(text)
                    .sort(Sort.builder().field("created_at").direction(Sort.Direction.ASC).build())
                    .limit(1)
                    .request());
    Assertions.assertEquals(1, responsePage1.getResults().size());
    Assertions.assertEquals(msg1.getId(), responsePage1.getResults().get(0).getMessage().getId());
    Assertions.assertNotNull(responsePage1.getNext());

    Message.MessageSearchResponse responsePage2 =
        Assertions.assertDoesNotThrow(
            () ->
                Message.search()
                    .filterCondition("id", testChannel.getId())
                    .query(text)
                    .next(responsePage1.getNext())
                    .limit(1)
                    .request());
    Assertions.assertEquals(1, responsePage2.getResults().size());
    Assertions.assertEquals(msg2.getId(), responsePage2.getResults().get(0).getMessage().getId());
    Assertions.assertNotNull(responsePage2.getPrevious());
  }

  @Test
  @DisplayName("Can upload txt file async with no exceptions")
  void whenUploadingTxtFileAsync_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            App.update()
                .fileUploadConfig(
                    FileUploadConfigRequestObject.builder()
                        .allowedFileExtensions(Collections.emptyList())
                        .build())
                .request());

    Assertions.assertDoesNotThrow(
        () -> {
          var testFileUrl = getClass().getClassLoader().getResource("upload_file.txt");
          assert testFileUrl != null;

          Message.uploadFile(
                  testChannel.getType(),
                  testChannel.getId(),
                  testUserRequestObject.getId(),
                  "text/plain")
              .file(new File(testFileUrl.getFile()))
              .requestAsync(Assertions::assertNotNull, Assertions::assertNull);
        });
  }

  @DisplayName("Can upload svg image with no exception")
  @Test
  void whenUploadingSvgImage_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            App.update()
                .imageUploadConfig(
                    FileUploadConfigRequestObject.builder()
                        .allowedFileExtensions(Collections.emptyList())
                        .build())
                .request());
    Assertions.assertDoesNotThrow(
        () ->
            Message.uploadImage(
                    testChannel.getType(),
                    testChannel.getId(),
                    testUserRequestObject.getId(),
                    "image/svg+xml")
                .file(
                    new File(getClass().getClassLoader().getResource("upload_image.svg").getFile()))
                .request());
  }

  @DisplayName("Can delete a message")
  @Test
  void whenDeletingMessage_thenIsDeleted() {
    String text = "This is a message";
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();
    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .message(messageRequest)
                        .request())
            .getMessage();
    Assertions.assertNull(message.getDeletedAt());
    Message deletedMessage =
        Assertions.assertDoesNotThrow(() -> Message.delete(message.getId()).request()).getMessage();
    Assertions.assertNotNull(deletedMessage.getDeletedAt());
  }

  @DisplayName("Can retrieve many messages")
  @Test
  void whenRetrievingManyMessage_thenAreRetrieved() {
    Message message1 = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    Message message2 = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    List<Message> retrievedMessages =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.getMany(
                            testChannel.getType(),
                            testChannel.getId(),
                            Arrays.asList(message1.getId(), message2.getId()))
                        .request())
            .getMessages();
    Assertions.assertEquals(2, retrievedMessages.size());
  }

  @Disabled
  @DisplayName("Can execute command action")
  @Test
  void whenExecutingCommandAction_thenNoException() {
    String text = "/giphy boom";

    MessageRequestObject messageCreateRequest =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();
    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .message(messageCreateRequest)
                        .request())
            .getMessage();
    Assertions.assertEquals(MessageType.EPHEMERAL, message.getType());
    Message afterActionMessage =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.runCommandAction(message.getId())
                        .formData(Collections.singletonMap("image_action", "send"))
                        .user(testUserRequestObject)
                        .request())
            .getMessage();
    Assertions.assertEquals(MessageType.REGULAR, afterActionMessage.getType());
  }

  @DisplayName("Can partially update a message")
  @Test
  void whenPartiallyUpdatingAMessage_thenIsUpdated() {
    // Should not use testMessage to not modify it
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    String updatedText = "This is an updated message";
    Message updatedMessage =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.partialUpdate(message.getId())
                        .setValue("text", updatedText)
                        .user(testUserRequestObject)
                        .request())
            .getMessage();
    Assertions.assertEquals(updatedText, updatedMessage.getText());
  }

  @DisplayName("Can use convenience method for hard delete")
  @Test
  void whenUsingHardDeleteConvenienceMethod_thenIsHardDeleted() {
    String text = "This is a message to hard delete using convenience method";
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();
    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .message(messageRequest)
                        .request())
            .getMessage();
    Assertions.assertNull(message.getDeletedAt());

    // Test convenience method for hard delete
    Message deletedMessage =
        Assertions.assertDoesNotThrow(() -> Message.hardDelete(message.getId()).request())
            .getMessage();
    Assertions.assertNotNull(deletedMessage.getDeletedAt());
  }
}
