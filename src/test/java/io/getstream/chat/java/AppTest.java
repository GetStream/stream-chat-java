package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.App;
import io.getstream.chat.java.models.App.AppCheckSnsResponse;
import io.getstream.chat.java.models.App.AppCheckSqsResponse;
import io.getstream.chat.java.models.App.AppConfig;
import io.getstream.chat.java.models.App.PushConfigRequestObject;
import io.getstream.chat.java.models.App.PushVersion;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.services.framework.DefaultClient;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AppTest extends BasicTest {
  private AppCheckSnsResponse.Status SnsStatus;
  private AppCheckSqsResponse.Status SqsStatus;

  @DisplayName("App Get does not throw Exception")
  @Test
  void whenCallingGetApp_thenNoException() {
    Assertions.assertDoesNotThrow(() -> App.get().request());
  }

  @Test
  @DisplayName("App get async does not throw Exception")
  void whenCallingGetAppAsync_thenNoException() {
    App.get().requestAsync(Assertions::assertNotNull, Assertions::assertNull);
  }

  @DisplayName("App Settings update does not throw Exception")
  @Test
  void whenUpdatingAppSettings_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            App.update()
                .disableAuthChecks(true)
                .disablePermissionsChecks(true)
                .asyncModerationConfig(
                    App.AsyncModerationConfigRequestObject.builder()
                        .callback(
                            App.AsyncModerationCallback.builder()
                                .mode("CALLBACK_MODE_REST")
                                .serverUrl("http://localhost.com")
                                .build())
                        .timeoutMs(3000)
                        .build())
                .request());
    Assertions.assertDoesNotThrow(
        () -> App.update().disableAuthChecks(false).disablePermissionsChecks(false).request());
  }

  @DisplayName("App Get fails with bad key")
  @Test
  void givenBadKey_whenGettingApp_thenException() {
    var properties = new Properties();
    properties.put(DefaultClient.API_KEY_PROP_NAME, "XXX");

    var client = new DefaultClient(properties);

    StreamException exception =
        Assertions.assertThrows(
            StreamException.class, () -> App.get().withClient(client).request());
    Assertions.assertEquals(401, exception.getResponseData().getStatusCode());
  }

  @DisplayName("App Get fails with bad secret (after enabling auth)")
  @Test
  void givenBadSecret_whenEnableAuthAndGettingApp_thenException() {
    Assertions.assertDoesNotThrow(() -> App.update().disableAuthChecks(false).request());
    var properties = new Properties();
    properties.put(
        DefaultClient.API_SECRET_PROP_NAME, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

    var client = new DefaultClient(properties);

    StreamException exception =
        Assertions.assertThrows(
            StreamException.class, () -> App.get().withClient(client).request());
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
    Assertions.assertEquals(SqsStatus.ERROR, response.getStatus());
  }

  @DisplayName("Can check sns")
  @Test
  void whenCheckingBadSns_thenError() {
    AppCheckSnsResponse response =
        Assertions.assertDoesNotThrow(
            () ->
                App.checkSns()
                    .snsKey("key")
                    .snsSecret("secret")
                    .snsTopicArn("arn:aws:sns:us-east-1:123456789012:sns-topic")
                    .request());
    Assertions.assertEquals(SnsStatus.ERROR, response.getStatus());
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
                .pushConfig(
                    PushConfigRequestObject.builder()
                        .version(PushVersion.V2)
                        .offlineOnly(false)
                        .build())
                .request());
    Assertions.assertDoesNotThrow(
        () ->
            App.checkPush()
                .messageId(message.getId())
                .skipDevices(true)
                .userId(secondUserId)
                .request());
  }

  @DisplayName("Can revoke tokens")
  @Test
  void whenRevokingTokens_thenNoException() {
    Calendar calendar = new GregorianCalendar();
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    Assertions.assertDoesNotThrow(() -> App.revokeTokens(calendar.getTime()).request());
  }

  @DisplayName("App Settings update size limit does not throw Exception")
  @Test
  void whenUpdatingAppSettingsSizeLimit_thenNoException() {
    AppConfig appConfig = Assertions.assertDoesNotThrow(() -> App.get().request()).getApp();
    int newSizeLimit = (new Random()).nextInt(100 * 1024 * 1024);
    Assertions.assertDoesNotThrow(
        () ->
            App.update()
                .fileUploadConfig(
                    App.FileUploadConfigRequestObject.builder().sizeLimit(newSizeLimit).build())
                .request());

    appConfig = Assertions.assertDoesNotThrow(() -> App.get().request()).getApp();
    Assertions.assertEquals(newSizeLimit, appConfig.getFileUploadConfig().getSizeLimit());
  }
}
