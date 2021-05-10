package io.stream;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.stream.exceptions.StreamException;
import io.stream.models.App;
import io.stream.models.App.AppCheckSqsResponse;
import io.stream.models.App.AppCheckSqsResponse.Status;
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
        () -> App.update().disableAuth(true).disablePermissions(true).request());
    Assertions.assertDoesNotThrow(
        () -> App.update().disableAuth(false).disablePermissions(false).request());
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
    Assertions.assertDoesNotThrow(() -> App.update().disableAuth(false).request());
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
}
