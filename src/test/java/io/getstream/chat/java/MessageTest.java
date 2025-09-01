package io.getstream.chat.java;

import io.getstream.chat.java.models.App;
import io.getstream.chat.java.models.App.FileUploadConfigRequestObject;
import io.getstream.chat.java.models.Blocklist;
import io.getstream.chat.java.models.Language;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.*;
import io.getstream.chat.java.models.Moderation;
import io.getstream.chat.java.models.Moderation.*;
import io.getstream.chat.java.models.Sort;
import io.getstream.chat.java.models.framework.DefaultFileHandler;
import io.getstream.chat.java.services.framework.DefaultClient;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
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

  @DisplayName("Can send a message with restricted visibility")
  @Test
  void whenSendingMessageWithRestrictedVisibility_thenMessageHasRestrictedVisibility() {
    List<String> restrictedUsers = Arrays.asList(testUserRequestObject.getId());
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text("This is a restricted message")
            .userId(testUserRequestObject.getId())
            .restrictedVisibility(restrictedUsers)
            .build();

    Message restrictedMessage =
        Assertions.assertDoesNotThrow(
            () ->
                Message.send(testChannel.getType(), testChannel.getId())
                    .message(messageRequest)
                    .request()
                    .getMessage());

    Assertions.assertEquals(restrictedUsers, restrictedMessage.getRestrictedVisibility());
  }

  @DisplayName("Can retrieve a deleted message")
  @Test
  void whenRetrievingADeletedMessage_thenIsRetrieved() {
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    Assertions.assertDoesNotThrow(() -> Message.delete(message.getId()).request());
    Message retrievedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.get(message.getId()).showDeletedMessages(false).request())
            .getMessage();

    Assertions.assertTrue(retrievedMessage.getId().equals(message.getId()));
    Assertions.assertFalse(retrievedMessage.getText().equals(message.getText()));

    retrievedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.get(message.getId()).showDeletedMessages(true).request())
            .getMessage();
    Assertions.assertTrue(retrievedMessage.getId().equals(message.getId()));
    Assertions.assertTrue(retrievedMessage.getText().equals(message.getText()));
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

  @DisplayName("Can update a message with restricted visibility")
  @Test
  void whenUpdatingAMessageWithRestrictedVisibility_thenNoException() {
    // Should not use testMessage to not modify it
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    String updatedText = "This is an updated message";
    List<String> restrictedUsers = Arrays.asList(testUserRequestObject.getId());

    MessageRequestObject updatedMessageRequest =
        MessageRequestObject.builder()
            .text(updatedText)
            .userId(testUserRequestObject.getId())
            .restrictedVisibility(restrictedUsers)
            .build();
    Message updatedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.update(message.getId()).message(updatedMessageRequest).request())
            .getMessage();
    Assertions.assertEquals(updatedText, updatedMessage.getText());
    Assertions.assertEquals(restrictedUsers, updatedMessage.getRestrictedVisibility());
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

  @DisplayName("Can search messages using query with no exception and retrieve given message")
  @Test
  void givenQuery_whenSearchingMessage_thenNoExceptionAndRetrievesMessage() {
    List<SearchResult> searchResults =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.search()
                        .filterCondition("id", testChannel.getId())
                        .query(testMessage.getText())
                        .request())
            .getResults();
    Assertions.assertEquals(1, searchResults.size());
  }

  @DisplayName("Can search messages with no exception and retrieve given message")
  @Test
  void givenMessageFilterConditions_whenSearchingMessages_thenNoExceptionAndRetrievesMessage() {
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

  @Test
  @DisplayName("Can upload txt file using custom client with no exceptions")
  void whenUploadingTxtFileUsingCustomClient_thenNoException() {
    var client = new DefaultClient(System.getProperties());
    var fileHandler = new DefaultFileHandler(client);

    Assertions.assertDoesNotThrow(
        () ->
            App.update()
                .fileUploadConfig(
                    FileUploadConfigRequestObject.builder()
                        .allowedFileExtensions(Collections.emptyList())
                        .build())
                .withClient(client)
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
              .withFileHandler(fileHandler)
              .requestAsync(Assertions::assertNotNull, Assertions::assertNull);
        });
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

  @DisplayName("Can send a pending message")
  @Test
  void whenSendingPending() {
    String text = "This is a message";
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();
    Map<String, Object> metadata = new HashMap<String, Object>();
    metadata.put("boo", "cute");

    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .message(messageRequest)
                        .isPendingMessage(true)
                        .pendingMessageMetadata(metadata)
                        .request())
            .getMessage();
    Assertions.assertNull(message.getDeletedAt());

    Assertions.assertDoesNotThrow(() -> Message.commit(message.getId()).request());
  }

  @DisplayName("Can send a silent message")
  @Test
  void whenSendingSilent() {
    String text = "This is a silent message";
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text(text)
            .userId(testUserRequestObject.getId())
            .silent(true)
            .build();

    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .message(messageRequest)
                        .request())
            .getMessage();
    Assertions.assertTrue(message.getSilent());
  }

  @DisplayName("Can send a system message")
  @Test
  void whenSendingSystem() {
    String text = "This is a system message";
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text(text)
            .type(MessageType.SYSTEM)
            .userId(testUserRequestObject.getId())
            .build();

    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .message(messageRequest)
                        .request())
            .getMessage();
    Assertions.assertEquals(MessageType.SYSTEM, message.getType());
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

  @DisplayName("Can delete a message with deletedBy specified")
  @Test
  void whenDeletingMessageWithDeletedBy_thenIsDeletedWithSpecifiedUser() {
    String text = "This is a message to be deleted with deletedBy";
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

    String deletedByUserId = testUsersRequestObjects.get(1).getId();
    Message deletedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.delete(message.getId()).deletedBy(deletedByUserId).request())
            .getMessage();
    Assertions.assertNotNull(deletedMessage.getDeletedAt());
    // Additional assertions can be added here once the backend supports checking the deletedBy
    // field
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

    // test deleted reply count
    Assertions.assertDoesNotThrow(() -> Message.delete(firstReply.getId()).request());
    Message m =
        Assertions.assertDoesNotThrow(() -> Message.get(parentMessage.getId()).request())
            .getMessage();

    Assertions.assertEquals(2, m.getReplyCount());
    Assertions.assertEquals(1, m.getDeletedReplyCount());
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
      throws NoSuchFieldException,
          SecurityException,
          IllegalArgumentException,
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

  @DisplayName("Can partially update a message with restricted visibility")
  @Test
  void whenPartiallyUpdatingAMessageWithRestrictedVisibility_thenIsUpdated() {
    // Should not use testMessage to not modify it
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    String updatedText = "This is an updated message";
    List<String> restrictedUsers = Arrays.asList(testUserRequestObject.getId());

    Message updatedMessage =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.partialUpdate(message.getId())
                        .setValue("text", updatedText)
                        .setValue("restricted_visibility", restrictedUsers)
                        .user(testUserRequestObject)
                        .request())
            .getMessage();
    Assertions.assertEquals(updatedText, updatedMessage.getText());
    Assertions.assertEquals(restrictedUsers, updatedMessage.getRestrictedVisibility());
  }

  @DisplayName("Can pin a message")
  @Test
  void whenPinningAMessage_thenIsPinned() {
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    Message updatedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.pinMessage(message.getId(), testUserRequestObject.getId()).request())
            .getMessage();
    Assertions.assertTrue(updatedMessage.getPinned());
  }

  @DisplayName("Can unpin a message")
  @Test
  void whenUnpinningAMessage_thenIsUnpinned() {
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    Assertions.assertDoesNotThrow(
            () -> Message.pinMessage(message.getId(), testUserRequestObject.getId()).request())
        .getMessage();
    var unPinnedMessage =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.unpinMessage(message.getId(), testUserRequestObject.getId()).request())
            .getMessage();
    Assertions.assertFalse(unPinnedMessage.getPinned());
  }

  @DisplayName("Can force enable or disable moderation on a message")
  @Test
  void whenForcingModerationOnAMessage_thenIsForced() {
    final String text = "This is a shitty message";
    final String blocklistName = RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(
        () -> Blocklist.create().name(blocklistName).words(Arrays.asList("shitty")).request());

    Assertions.assertDoesNotThrow(() -> Thread.sleep(5000));

    Assertions.assertDoesNotThrow(
        () -> {
          String key = String.format("chat:%s:%s", testChannel.getType(), testChannel.getId());
          BlockListRule rule =
              BlockListRule.builder().name(blocklistName).action(Moderation.Action.REMOVE).build();
          Moderation.upsertConfig(key)
              .blockListConfig(BlockListConfigRequestObject.builder().rules(List.of(rule)).build())
              .request();
        });

    Assertions.assertDoesNotThrow(() -> Thread.sleep(5000));

    MessageRequestObject messageRequest1 =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();
    Message msg1 =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .forceModeration(true)
                        .message(messageRequest1)
                        .request())
            .getMessage();

    Assertions.assertEquals("Message was blocked by moderation policies", msg1.getText());

    MessageRequestObject messageRequest2 =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();
    Message msg2 =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .forceModeration(false)
                        .message(messageRequest2)
                        .request())
            .getMessage();

    Assertions.assertEquals(text, msg2.getText());

    Assertions.assertDoesNotThrow(() -> Blocklist.delete(blocklistName).request());
  }

  @DisplayName("Can unblock a message")
  @Test
  @Disabled("Need to implement unblock with moderation v2")
  void whenUnblockingAMessage_thenIsUnblocked() {
    final String swearText = "This is a hate message";
    final String blocklistName = RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(
        () -> Blocklist.create().name(blocklistName).words(Arrays.asList("hate")).request());

    Assertions.assertDoesNotThrow(() -> Thread.sleep(5000));

    Assertions.assertDoesNotThrow(
        () -> {
          String key = String.format("chat:%s:%s", testChannel.getType(), testChannel.getId());
          BlockListRule rule =
              BlockListRule.builder().name(blocklistName).action(Moderation.Action.REMOVE).build();
          Moderation.upsertConfig(key)
              .blockListConfig(BlockListConfigRequestObject.builder().rules(List.of(rule)).build())
              .request();
        });

    Assertions.assertDoesNotThrow(() -> Thread.sleep(5000));

    MessageRequestObject messageRequest1a =
        MessageRequestObject.builder()
            .text(swearText)
            .userId(testUserRequestObject.getId())
            .build();
    Message msg1a =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .forceModeration(true)
                        .message(messageRequest1a)
                        .request())
            .getMessage();

    Assertions.assertEquals("Message was blocked by moderation policies", msg1a.getText());

    Assertions.assertDoesNotThrow(
        () -> Message.unblock(msg1a.getId()).userId(testUserRequestObject.getId()).request());

    Message msg1b =
        Assertions.assertDoesNotThrow(() -> Message.get(msg1a.getId()).request()).getMessage();

    Assertions.assertEquals(swearText, msg1b.getText());
    Assertions.assertEquals(msg1a.getId(), msg1b.getId());

    Assertions.assertDoesNotThrow(() -> Blocklist.delete(blocklistName).request());
  }

  @DisplayName("Can delete message for me only")
  @Test
  void whenDeletingMessageForMe_thenIsDeletedForMe() {
    String text = "This is a message to delete for me only";
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

    // Test delete for me only
    Message deletedMessage =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.delete(message.getId())
                        .deleteForMe(true)
                        .deletedBy(testUserRequestObject.getId())
                        .request())
            .getMessage();
    
    // Verify the delete request was successful
    Assertions.assertNotNull(deletedMessage);
    
    // For delete for me, the message should still exist but be marked as deleted for the specific user
    // The deletedAt might be null as this is a "soft delete for me" operation
    System.out.println("Delete for me response - deletedAt: " + deletedMessage.getDeletedAt());
    
    // Verify the message still exists (delete for me doesn't permanently delete)
    Message retrievedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.get(message.getId()).request())
            .getMessage();
    Assertions.assertNotNull(retrievedMessage);
    Assertions.assertEquals(message.getId(), retrievedMessage.getId());
  }

  @DisplayName("Can use convenience method for delete for me")
  @Test
  void whenUsingDeleteForMeConvenienceMethod_thenIsDeletedForMe() {
    String text = "This is a message to delete for me using convenience method";
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

    // Test convenience method for delete for me
    Message deletedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.deleteForMe(message.getId(), testUserRequestObject.getId()).request())
            .getMessage();
    
    // Verify the delete request was successful
    Assertions.assertNotNull(deletedMessage);
    
    // Verify the message still exists (delete for me doesn't permanently delete)
    Message retrievedMessage =
        Assertions.assertDoesNotThrow(
                () -> Message.get(message.getId()).request())
            .getMessage();
    Assertions.assertNotNull(retrievedMessage);
    Assertions.assertEquals(message.getId(), retrievedMessage.getId());
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
