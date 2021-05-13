package io.stream;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.stream.exceptions.StreamException;
import io.stream.models.App;
import io.stream.models.App.APNConfigRequestObject;
import io.stream.models.App.AppCheckPushResponse;
import io.stream.models.App.AppCheckSqsResponse;
import io.stream.models.App.FirebaseConfigRequestObject;
import io.stream.models.App.AppCheckSqsResponse.Status;
import io.stream.models.App.PushConfigRequestObject;
import io.stream.models.App.PushVersion;
import io.stream.models.Message;
import io.stream.models.Message.MessageRequestObject;
import io.stream.services.framework.StreamServiceGenerator;

public class AppTest extends BasicTest {

  @DisplayName("App Get does not throw Exception")
  @Test
  void whenCallingGetApp_thenNoException() {
    Assertions.assertDoesNotThrow(() -> App.get().request());
  }

  @DisplayName("App Settings update does not throw Exception")
  @Test
  void whenUpdatingAppSettings_thenNoException() {
    Assertions.assertDoesNotThrow(
        () -> App.update().disableAuthChecks(true).disablePermissionsChecks(true).request());
    Assertions.assertDoesNotThrow(
        () -> App.update().disableAuthChecks(false).disablePermissionsChecks(false).request());
  }

  @DisplayName("App Get fails with bad key")
  @Test
  void givenBadKey_whenGettingApp_thenException()
      throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    Field apiKeyField = StreamServiceGenerator.class.getDeclaredField("apiKey");
    apiKeyField.setAccessible(true);
    apiKeyField.set(StreamServiceGenerator.class, "XXX");
    StreamException exception =
        Assertions.assertThrows(StreamException.class, () -> App.get().request());
    Assertions.assertEquals(401, exception.getResponseData().getStatusCode());
  }

  @DisplayName("App Get fails with bad secret (after enabling auth)")
  @Test
  void givenBadSecret_whenEnableAuthAndGettingApp_thenException()
      throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    Assertions.assertDoesNotThrow(() -> App.update().disableAuthChecks(false).request());
    Field apiSecretField = StreamServiceGenerator.class.getDeclaredField("apiSecret");
    apiSecretField.setAccessible(true);
    apiSecretField.set(
        StreamServiceGenerator.class, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    StreamException exception =
        Assertions.assertThrows(StreamException.class, () -> App.get().request());
    Assertions.assertEquals(401, exception.getResponseData().getStatusCode());
  }

  @DisplayName("Get rate limits does not throw Exception")
  @Test
  void whenCallingGetRateLimits_thenNoException() {
    Assertions.assertDoesNotThrow(() -> App.getRateLimits().request());
  }

  @DisplayName("Can check sqs")
  @Test
  void whenCheckingBadSqs_thenError() {
    AppCheckSqsResponse response =
        Assertions.assertDoesNotThrow(
            () ->
                App.checkSqs()
                    .sqsKey("key")
                    .sqsSecret("secret")
                    .sqsUrl("https://foo.com/bar")
                    .request());
    Assertions.assertEquals(Status.ERROR, response.getStatus());
  }

  @DisplayName("Can check push templates")
  @Test
  void whenCheckingPushTemplates_thenNoException() {
    String firstUserId = testUserRequestObject.getId();
    String secondUserId = testUsersRequestObjects.get(1).getId();
    String text = "Hello @" + secondUserId;
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(firstUserId).build();
    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(testChannel.getType(), testChannel.getId())
                        .message(messageRequest)
                        .request())
            .getMessage();
    Assertions.assertDoesNotThrow(
        () ->
            App.update()
                .pushConfig(PushConfigRequestObject.builder().version(PushVersion.V2).build())
                .request());
    Assertions.assertDoesNotThrow(
        () ->
            App.checkPush()
                .messageId(message.getId())
                .skipDevices(true)
                .userId(secondUserId)
                .request());
  }
}
