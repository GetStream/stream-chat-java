package io.stream;

import io.getstream.chat.java.models.App;
import io.getstream.chat.java.models.App.FileUploadConfigRequestObject;
import io.getstream.chat.java.models.Language;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.ActionRequestObject;
import io.getstream.chat.java.models.Message.AttachmentRequestObject;
import io.getstream.chat.java.models.Message.Crop;
import io.getstream.chat.java.models.Message.FieldRequestObject;
import io.getstream.chat.java.models.Message.ImageSizeRequestObject;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Message.MessageType;
import io.getstream.chat.java.models.Message.Resize;
import io.getstream.chat.java.models.Message.SearchResult;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
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

  @DisplayName("Can search messages with no exception and retrieve given message")
  @Test
  void whenSearchingMessages_thenNoExceptionAndRetrievesMessage() {
    List<SearchResult> searchResults =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.search()
                        .filterCondition("id", testChannel.getId())
                        .messageFilterCondition("text", testMessage.getText())
                        .request())
            .getResults();
    Assertions.assertEquals(1, searchResults.size());
  }

  @DisplayName("Can upload txt file with no exception")
  @Test
  void whenUploadingTxtFile_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            App.update()
                .fileUploadConfig(
                    FileUploadConfigRequestObject.builder()
                        .allowedFileExtensions(Collections.emptyList())
                        .build())
                .request());
    Assertions.assertDoesNotThrow(
        () ->
            Message.uploadFile(
                    testChannel.getType(),
                    testChannel.getId(),
                    testUserRequestObject.getId(),
                    "text/plain")
                .file(
                    new File(getClass().getClassLoader().getResource("upload_file.txt").getFile()))
                .request());
  }

  @DisplayName("Can upload pdf file with no exception")
  @Test
  void whenUploadingPdfFile_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            App.update()
                .fileUploadConfig(
                    FileUploadConfigRequestObject.builder()
                        .allowedFileExtensions(Collections.emptyList())
                        .build())
                .request());
    Assertions.assertDoesNotThrow(
        () ->
            Message.uploadFile(
                    testChannel.getType(), testChannel.getId(), testUserRequestObject.getId(), null)
                .file(
                    new File(getClass().getClassLoader().getResource("upload_file.pdf").getFile()))
                .request());
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

  @DisplayName("Can upload png image to resize with no exception")
  @Test
  void whenUploadingPngImage_thenNoException() {
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
                    "image/png")
                .file(
                    new File(getClass().getClassLoader().getResource("upload_image.png").getFile()))
                .uploadSizes(
                    Arrays.asList(
                        ImageSizeRequestObject.builder()
                            .crop(Crop.TOP)
                            .resize(Resize.SCALE)
                            .height(200)
                            .width(200)
                            .build()))
                .request());
  }

  @DisplayName("Can delete file with no exception")
  @Test
  void whenDeletingFile_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            App.update()
                .fileUploadConfig(
                    FileUploadConfigRequestObject.builder()
                        .allowedFileExtensions(Collections.emptyList())
                        .build())
                .request());
    String url =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.uploadFile(
                            testChannel.getType(),
                            testChannel.getId(),
                            testUserRequestObject.getId(),
                            null)
                        .file(
                            new File(
                                getClass()
                                    .getClassLoader()
                                    .getResource("upload_file.pdf")
                                    .getFile()))
                        .request())
            .getFile();
    Assertions.assertDoesNotThrow(
        () -> Message.deleteFile(testChannel.getType(), testChannel.getId(), url).request());
  }

  @DisplayName("Can delete image with no exception")
  @Test
  void whenDeletingSvgImage_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            App.update()
                .imageUploadConfig(
                    FileUploadConfigRequestObject.builder()
                        .allowedFileExtensions(Collections.emptyList())
                        .build())
                .request());
    String url =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.uploadImage(
                            testChannel.getType(),
                            testChannel.getId(),
                            testUserRequestObject.getId(),
                            "image/svg+xml")
                        .file(
                            new File(
                                getClass()
                                    .getClassLoader()
                                    .getResource("upload_image.svg")
                                    .getFile()))
                        .request())
            .getFile();
    Assertions.assertDoesNotThrow(
        () -> Message.deleteImage(testChannel.getType(), testChannel.getId(), url).request());
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

  @DisplayName("Can retrieve replies")
  @Test
  void whenRetrievingReplies_thenAreRetrieved() {
    Message parentMessage = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    String text = "This is a reply";
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text(text)
            .userId(testUserRequestObject.getId())
            .parentId(parentMessage.getId())
            .build();
    Message firstReply =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .message(messageRequest)
                        .request())
            .getMessage();
    Assertions.assertDoesNotThrow(
        () ->
            Message.send(testChannel.getType(), testChannel.getId())
                .message(messageRequest)
                .request());
    List<Message> replies =
        Assertions.assertDoesNotThrow(() -> Message.getReplies(parentMessage.getId()).request())
            .getMessages();
    Assertions.assertEquals(2, replies.size());
    @SuppressWarnings("unused")
    List<Message> repliesAfterFirstMessage =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.getReplies(parentMessage.getId())
                        .createdAtAfter(firstReply.getCreatedAt())
                        .request())
            .getMessages();
    // This assertion is for now commented as there is an issue on the backend
    // Assertions.assertEquals(1, repliesAfterFirstMessage.size());
  }

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

  @DisplayName("Can translate a message")
  @Test
  void whenTranslatingMessage_thenNoException() {
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    Message translatedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.translate(message.getId()).language(Language.FR).request())
            .getMessage();
    Assertions.assertNotNull(translatedMessage.getI18n());
    Assertions.assertNotNull(translatedMessage.getI18n().get("fr_text"));
  }

  @SuppressWarnings("unchecked")
  @DisplayName("Can create a OwnUserRequestObject from OwnUser")
  @Test
  void whenCreatingAMessageRequestObject_thenIsCorrect() {
    Logger parent = Logger.getLogger("io.stream");
    parent.setLevel(Level.FINE);
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text("Sample text")
            .attachment(
                AttachmentRequestObject.builder()
                    .action(ActionRequestObject.builder().name("actionName").build())
                    .field(FieldRequestObject.builder().type("string").build())
                    .build())
            .userId(testUserRequestObject.getId())
            .build();
    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .message(messageRequest)
                        .request())
            .getMessage();
    Assertions.assertEquals(1, message.getAttachments().size());
    Assertions.assertEquals(1, message.getAttachments().get(0).getActions().size());
    Assertions.assertEquals(1, message.getAttachments().get(0).getFields().size());

    MessageRequestObject messageRequestObject =
        Assertions.assertDoesNotThrow(() -> MessageRequestObject.buildFrom(message));
    Assertions.assertDoesNotThrow(
        () -> {
          List<AttachmentRequestObject> attachments =
              (List<AttachmentRequestObject>)
                  getRequestObjectFieldValue("attachments", messageRequestObject);
          List<ActionRequestObject> actions =
              (List<ActionRequestObject>) getRequestObjectFieldValue("actions", attachments.get(0));
          List<FieldRequestObject> fields =
              (List<FieldRequestObject>) getRequestObjectFieldValue("fields", attachments.get(0));
          Assertions.assertEquals(message.getAttachments().size(), attachments.size());
          Assertions.assertEquals(
              message.getAttachments().get(0).getActions().size(), actions.size());
          Assertions.assertEquals(
              message.getAttachments().get(0).getFields().size(), fields.size());
        });
  }

  private Object getRequestObjectFieldValue(String fieldName, Object requestObject)
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
          IllegalAccessException {
    Field field = requestObject.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(requestObject);
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
}
