package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Message.MessageType;
import io.getstream.chat.java.models.SharedLocation;
import io.getstream.chat.java.models.SharedLocation.ActiveLiveLocationsResponse;
import io.getstream.chat.java.models.SharedLocation.SharedLocationRequest;
import io.getstream.chat.java.models.SharedLocation.SharedLocationResponse;
import io.getstream.chat.java.models.User.UserRequestObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SharedLocationTest extends BasicTest {

  @DisplayName("Can send message with shared location and verify")
  @Test
  void whenSendingMessageWithSharedLocation_thenCanGetThroughUsersLocations() throws StreamException, ParseException {
    // Create a unique device ID for this test
    String deviceId = "device-" + UUID.randomUUID().toString();
    
    // Create shared location request
    SharedLocationRequest locationRequest = new SharedLocation.SharedLocationRequest();
    locationRequest.setCreatedByDeviceId(deviceId);
    locationRequest.setLatitude(40.7128);
    locationRequest.setLongitude(-74.0060);
    locationRequest.setEndAt("2025-12-31T23:59:59Z");
    locationRequest.setUserId(testUserRequestObject.getId());

    // Convert request to SharedLocation
    SharedLocation sharedLocation = new SharedLocation();
    sharedLocation.setCreatedByDeviceId(locationRequest.getCreatedByDeviceId());
    sharedLocation.setLatitude(locationRequest.getLatitude());
    sharedLocation.setLongitude(locationRequest.getLongitude());
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    sharedLocation.setEndAt(dateFormat.parse(locationRequest.getEndAt()));

    // Send message with shared location
    MessageRequestObject messageRequest = MessageRequestObject.builder()
        .text("I'm sharing my live location")
        .userId(testUserRequestObject.getId())
        .type(MessageType.REGULAR)
        .sharedLocation(sharedLocation)
        .build();

    Message message = Message.send(testChannel.getType(), testChannel.getId())
        .message(messageRequest)
        .request()
        .getMessage();

    // Verify message was sent with correct shared location
    Assertions.assertNotNull(message);
    Assertions.assertNotNull(message.getSharedLocation());
    Assertions.assertEquals(deviceId, message.getSharedLocation().getCreatedByDeviceId());
    Assertions.assertEquals(40.7128, message.getSharedLocation().getLatitude());
    Assertions.assertEquals(-74.0060, message.getSharedLocation().getLongitude());
    
    // Parse and verify the endAt date
    Date expectedEndAt = dateFormat.parse("2025-12-31T23:59:59Z");
    Assertions.assertEquals(expectedEndAt, message.getSharedLocation().getEndAt());
  }

  @DisplayName("Can create live location, update it and verify the update")
  @Test
  void whenUpdatingLiveLocation_thenCanGetUpdatedLocation() throws StreamException, ParseException {
    // Create a unique device ID for this test
    String deviceId = "device-" + UUID.randomUUID().toString();
    
    // Create initial shared location request
    SharedLocationRequest initialLocationRequest = new SharedLocation.SharedLocationRequest();
    initialLocationRequest.setCreatedByDeviceId(deviceId);
    initialLocationRequest.setLatitude(40.7128);
    initialLocationRequest.setLongitude(-74.0060);
    initialLocationRequest.setEndAt("2025-12-31T23:59:59Z");
    initialLocationRequest.setUserId(testUserRequestObject.getId());

    // Convert request to SharedLocation
    SharedLocation initialSharedLocation = new SharedLocation();
    initialSharedLocation.setCreatedByDeviceId(initialLocationRequest.getCreatedByDeviceId());
    initialSharedLocation.setLatitude(initialLocationRequest.getLatitude());
    initialSharedLocation.setLongitude(initialLocationRequest.getLongitude());
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    initialSharedLocation.setEndAt(dateFormat.parse(initialLocationRequest.getEndAt()));

    // Send initial message with shared location
    MessageRequestObject initialMessageRequest = MessageRequestObject.builder()
        .text("I'm sharing my live location")
        .userId(testUserRequestObject.getId())
        .type(MessageType.REGULAR)
        .sharedLocation(initialSharedLocation)
        .build();

    Message initialMessage = Message.send(testChannel.getType(), testChannel.getId())
        .message(initialMessageRequest)
        .request()
        .getMessage();

    // Create updated location request
    SharedLocationRequest updatedLocationRequest = new SharedLocation.SharedLocationRequest();
    updatedLocationRequest.setMessageId(initialMessage.getId());
    updatedLocationRequest.setCreatedByDeviceId(deviceId);
    updatedLocationRequest.setLatitude(40.7589); // Updated latitude
    updatedLocationRequest.setLongitude(-73.9851); // Updated longitude
    updatedLocationRequest.setEndAt("2025-12-31T23:59:59Z");
    updatedLocationRequest.setUserId(testUserRequestObject.getId());

    // Update the location
    SharedLocation.SharedLocationResponse updateResponse = SharedLocation.updateLocation()
        .userId(testUserRequestObject.getId())
        .request(updatedLocationRequest)
        .request();

    // Get active live locations
    ActiveLiveLocationsResponse activeLocations = SharedLocation.getLocations()
        .userId(testUserRequestObject.getId())
        .request();

    // Verify the updated location
    Assertions.assertNotNull(activeLocations);
    Assertions.assertNotNull(activeLocations.getActiveLiveLocations());
    Assertions.assertFalse(activeLocations.getActiveLiveLocations().isEmpty());

    // Find our location in the response
    SharedLocation updatedLocation = activeLocations.getActiveLiveLocations().stream()
        .filter(loc -> deviceId.equals(loc.getCreatedByDeviceId()))
        .findFirst()
        .orElse(null);

    Assertions.assertNotNull(updatedLocation);
    Assertions.assertEquals(deviceId, updatedLocation.getCreatedByDeviceId());
    Assertions.assertEquals(40.7589, updatedLocation.getLatitude());
    Assertions.assertEquals(-73.9851, updatedLocation.getLongitude());
    
    // Verify the endAt date
    Date expectedEndAt = dateFormat.parse("2025-12-31T23:59:59Z");
    Assertions.assertEquals(expectedEndAt, updatedLocation.getEndAt());
  }
}
