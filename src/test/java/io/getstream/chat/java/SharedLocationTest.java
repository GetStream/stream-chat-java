package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.SharedLocation;
import io.getstream.chat.java.models.SharedLocation.ActiveLiveLocationsResponse;
import io.getstream.chat.java.models.SharedLocation.SharedLocationRequest;
import io.getstream.chat.java.models.SharedLocation.SharedLocationResponse;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SharedLocationTest extends BasicTest {

  @DisplayName("Can update and get live location")
  @Test
  void whenUpdatingLiveLocation_thenNoException() throws StreamException {
    String deviceId = "device-" + UUID.randomUUID().toString();
    SharedLocationRequest request = new SharedLocation.SharedLocationRequest();
    request.setCreatedByDeviceId(deviceId);
    request.setLatitude(40.7128);
    request.setLongitude(-74.0060);
    request.setEndAt("2024-12-31T23:59:59Z");

    SharedLocationResponse response = SharedLocation.updateLocation().request(request).request();

    Assertions.assertNotNull(response);
    Assertions.assertEquals(deviceId, response.getCreatedByDeviceId());
    Assertions.assertEquals(40.7128, response.getLatitude());
    Assertions.assertEquals(-74.0060, response.getLongitude());
    Assertions.assertEquals("2024-12-31T23:59:59Z", response.getEndAt());

    ActiveLiveLocationsResponse locationsResponse = SharedLocation.getLocations().request();

    Assertions.assertNotNull(locationsResponse);
    Assertions.assertNotNull(locationsResponse.getActiveLiveLocations());
    Assertions.assertTrue(locationsResponse.getActiveLiveLocations().size() > 0);
  }

  @DisplayName("Can update live location")
  @Test
  void whenUpdatingLiveLocationWithMinimalData_thenNoException() throws StreamException {
    String deviceId = "device-" + UUID.randomUUID().toString();
    SharedLocationRequest request = new SharedLocation.SharedLocationRequest();
    request.setCreatedByDeviceId(deviceId);
    request.setLatitude(40.7128);
    request.setLongitude(-74.0060);

    SharedLocationResponse response = SharedLocation.updateLocation().request(request).request();

    Assertions.assertNotNull(response);
    Assertions.assertEquals(deviceId, response.getCreatedByDeviceId());
    Assertions.assertNull(response.getLatitude());
    Assertions.assertNull(response.getLongitude());
    Assertions.assertNull(response.getEndAt());
  }

  @DisplayName("Can get empty live locations list")
  @Test
  void whenGettingEmptyLiveLocations_thenNoException() throws StreamException {
    ActiveLiveLocationsResponse response = SharedLocation.getLocations().request();

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getActiveLiveLocations());
    Assertions.assertTrue(response.getActiveLiveLocations().isEmpty());
  }
}
