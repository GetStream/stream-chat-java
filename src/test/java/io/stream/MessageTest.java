package io.stream;

import io.stream.models.App;
import io.stream.models.App.FileUploadConfigRequestObject;
import io.stream.models.Message;
import io.stream.models.Message.Crop;
import io.stream.models.Message.ImageSizeRequestObject;
import io.stream.models.Message.MessageRequestObject;
import io.stream.models.Message.Resize;
import io.stream.models.Message.SearchResult;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MessageTest extends BasicTest {

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
    Map<String, Object> channelConditions = new HashMap<>();
    channelConditions.put("id", testChannel.getId());
    Map<String, Object> messageConditions = new HashMap<>();
    messageConditions.put("text", testMessage.getText());
    List<SearchResult> searchResults =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.search()
                        .filterConditions(channelConditions)
                        .messageFilterConditions(messageConditions)
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
                    testChannel.getType(), testChannel.getId(), testUserRequestObject.getId())
                .file(
                    new File(getClass().getClassLoader().getResource("upload_file.txt").getFile()))
                .contentType("text/plain")
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
                    testChannel.getType(), testChannel.getId(), testUserRequestObject.getId())
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
}
