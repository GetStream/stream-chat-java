package io.stream;

import io.getstream.models.Device;
import io.getstream.models.Device.PushProvider;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeviceTest extends BasicTest {

  @DisplayName("Can create device")
  @Test
  void whenCreatingDevice_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            Device.create()
                .id(RandomStringUtils.randomAlphabetic(10))
                .user(testUserRequestObject)
                .pushProvider(PushProvider.APN)
                .request());
  }

  @DisplayName("Can delete device")
  @Test
  void whenDeletingDevice_thenDeleted() {
    String deviceId = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () ->
            Device.create()
                .id(deviceId)
                .user(testUserRequestObject)
                .pushProvider(PushProvider.APN)
                .request());
    Assertions.assertDoesNotThrow(
        () -> Device.delete(deviceId, testUserRequestObject.getId()).request());
    List<Device> devices =
        Assertions.assertDoesNotThrow(() -> Device.list(testUserRequestObject.getId()).request())
            .getDevices();
    Assertions.assertFalse(
        devices.stream().anyMatch(consideredDevice -> consideredDevice.getId().equals(deviceId)));
  }

  @DisplayName("Can list devices")
  @Test
  void whenListingCommand_thenCanRetrieve() {
    String deviceId = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () ->
            Device.create()
                .id(deviceId)
                .user(testUserRequestObject)
                .pushProvider(PushProvider.APN)
                .request());
    List<Device> devices =
        Assertions.assertDoesNotThrow(() -> Device.list(testUserRequestObject.getId()).request())
            .getDevices();
    Assertions.assertTrue(
        devices.stream().anyMatch(consideredDevice -> consideredDevice.getId().equals(deviceId)));
  }
}
