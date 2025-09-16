package io.getstream.chat.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.App;
import io.getstream.chat.java.models.App.AppCheckSnsResponse;
import io.getstream.chat.java.models.App.AppCheckSqsResponse;
import io.getstream.chat.java.models.App.AppConfig;
import io.getstream.chat.java.models.App.EventHook;
import io.getstream.chat.java.models.App.PushConfigRequestObject;
import io.getstream.chat.java.models.App.PushVersion;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.services.framework.DefaultClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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

  @DisplayName("Can update app settings with webhook event hooks with different products")
  @Test
  void whenUpdatingAppSettingsWithWebhookEventHooks_thenNoException() throws StreamException {
    EventHook defaultWebhookHook = new EventHook();
    defaultWebhookHook.setHookType(App.HookType.WEBHOOK);
    defaultWebhookHook.setEnabled(true);
    defaultWebhookHook.setEventTypes(Arrays.asList("message.new", "message.updated"));
    defaultWebhookHook.setWebhookURL("https://example.com/webhook-default");

    EventHook chatWebhookHook = new EventHook();
    chatWebhookHook.setHookType(App.HookType.WEBHOOK);
    chatWebhookHook.setEnabled(true);
    chatWebhookHook.setEventTypes(Arrays.asList("message.new", "message.updated"));
    chatWebhookHook.setWebhookURL("https://example.com/webhook-chat");
    chatWebhookHook.setProduct(App.Product.CHAT);

    try {
      App.update().eventHooks(Arrays.asList(defaultWebhookHook, chatWebhookHook)).request();
    } catch (StreamException e) {
      if (e.getMessage().contains("cannot set event hooks in hook v1 system")) {
        return;
      }
      throw e;
    }
  }

  @DisplayName("Can update app settings with SQS event hook")
  @Test
  void whenUpdatingAppSettingsWithSQSEventHook_thenNoException() throws StreamException {
    EventHook sqsHook = new EventHook();
    sqsHook.setId("4f811340-1cbb-40ef-8393-1c2b2e0d339a");
    sqsHook.setHookType(App.HookType.SQS);
    sqsHook.setEnabled(true);
    sqsHook.setEventTypes(Arrays.asList("message.new", "user.updated"));
    sqsHook.setSqsQueueURL("https://sqs.us-east-1.amazonaws.com/123456789012/MyQueue");
    sqsHook.setSqsRegion("us-east-1");
    sqsHook.setSqsAuthType(App.AuthType.RESOURCE);
    sqsHook.setCreatedAt(new Date());
    sqsHook.setUpdatedAt(new Date());

    try {
      App.update().eventHooks(Collections.singletonList(sqsHook)).request();
    } catch (StreamException e) {
      if (e.getMessage().contains("cannot set event hooks in hook v1 system")) {
        return;
      }
      throw e;
    }
  }

  @DisplayName("Can update app settings with SNS event hook")
  @Test
  void whenUpdatingAppSettingsWithSNSEventHook_thenNoException() throws StreamException {
    EventHook snsHook = new EventHook();
    snsHook.setId("7469e77e-52cd-4765-8ca0-e6a28e95e482");
    snsHook.setHookType(App.HookType.SNS);
    snsHook.setEnabled(true);
    snsHook.setEventTypes(Arrays.asList("channel.created", "channel.updated"));
    snsHook.setSnsTopicARN("arn:aws:sns:us-east-1:123456789012:MyTopic");
    snsHook.setSnsRegion("us-east-1");
    snsHook.setSnsAuthType(App.AuthType.RESOURCE);
    snsHook.setCreatedAt(new Date());
    snsHook.setUpdatedAt(new Date());

    try {
      App.update().eventHooks(Collections.singletonList(snsHook)).request();
    } catch (StreamException e) {
      if (e.getMessage().contains("cannot set event hooks in hook v1 system")) {
        return;
      }
      throw e;
    }
  }

  @DisplayName("Can update app settings with pending message event hook")
  @Test
  void whenUpdatingAppSettingsWithPendingMessageEventHook_thenNoException() throws StreamException {
    EventHook pendingMessageHook = new EventHook();
    pendingMessageHook.setId("5944d247-8b4f-4108-a970-fe1d11fca989");
    pendingMessageHook.setHookType(App.HookType.PENDING_MESSAGE);
    pendingMessageHook.setEnabled(true);
    pendingMessageHook.setWebhookURL("https://example.com/pending-message-webhook");
    pendingMessageHook.setTimeoutMs(3000);

    App.PendingMessageCallback callback = new App.PendingMessageCallback();
    callback.setMode(App.CallbackMode.REST);
    pendingMessageHook.setCallback(callback);

    try {
      App.update().eventHooks(Collections.singletonList(pendingMessageHook)).request();
    } catch (StreamException e) {
      if (e.getMessage().contains("cannot set event hooks in hook v1 system")) {
        return;
      }
      throw e;
    }
  }

  @DisplayName("AppConfig encoding should not include null fields")
  @Test
  void whenEncodingAppConfig_thenNoNullFields() {
    var appConfig = App.update().internalBuild();
    final ObjectMapper mapper = new ObjectMapper();

    String json = Assertions.assertDoesNotThrow(() -> mapper.writeValueAsString(appConfig));

    // When we didn't set any fields, the JSON should be empty
    Assertions.assertEquals("{}", json);

    // We set some fields where we mean to reset them
    var appConfigNull =
        App.update()
            .webhookEvents(new ArrayList<>())
            .revokeTokensIssuedBefore(null)
            .grants(new HashMap<>())
            .internalBuild();

    json = Assertions.assertDoesNotThrow(() -> mapper.writeValueAsString(appConfigNull));

    Assertions.assertTrue(
        json.contains("\"webhook_events\":[]"), "JSON should contain null webhook_events field");

    Assertions.assertTrue(
        json.contains("\"revoke_tokens_issued_before\":null"),
        "JSON should contain null revoke_tokens_issued_before field");

    Assertions.assertTrue(json.contains("\"grants\":{}"), "JSON should contain null grants field");
  }
}
