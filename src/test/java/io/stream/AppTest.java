package io.stream;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.stream.exceptions.StreamException;
import io.stream.models.App;
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
        () -> App.update().withDisableAuth(true).withDisablePermissions(true).request());
    Assertions.assertDoesNotThrow(
        () -> App.update().withDisableAuth(false).withDisablePermissions(false).request());
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
    Assertions.assertDoesNotThrow(() -> App.update().withDisableAuth(false).request());
    Field apiSecretField = StreamServiceGenerator.class.getDeclaredField("apiSecret");
    apiSecretField.setAccessible(true);
    apiSecretField.set(
        StreamServiceGenerator.class, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    StreamException exception =
        Assertions.assertThrows(StreamException.class, () -> App.get().request());
    Assertions.assertEquals(401, exception.getResponseData().getStatusCode());
  }
}
