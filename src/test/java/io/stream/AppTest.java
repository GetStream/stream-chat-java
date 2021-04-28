package io.stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.stream.exceptions.StreamException;
import io.stream.models.App;

public class AppTest extends BasicTest {

  @DisplayName("App Get does not throw Exception")
  @Test
  void whenCallingGetApp_thenNoException() {
    Assertions.assertDoesNotThrow(() -> App.get());
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
  void givenBadKey_whenGettingApp_thenException() {
    System.setProperty("STREAM_KEY", "XXX");
    StreamException exception = Assertions.assertThrows(StreamException.class, () -> App.get());
    Assertions.assertEquals(401, exception.getResponseData().getStatusCode());
  }

  @DisplayName("App Get fails with bad secret (after enabling auth)")
  @Test
  void givenBadSecret_whenEnableAuthAndGettingApp_thenException() {
    Assertions.assertDoesNotThrow(() -> App.update().withDisableAuth(false).request());
    System.setProperty("STREAM_SECRET", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    StreamException exception = Assertions.assertThrows(StreamException.class, () -> App.get());
    Assertions.assertEquals(401, exception.getResponseData().getStatusCode());
  }

}
