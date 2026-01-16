package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.ChannelGetResponse;
import io.getstream.chat.java.models.Channel.ChannelRequestObject;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Message.MessageType;
import io.getstream.chat.java.models.SharedLocation;
import io.getstream.chat.java.models.SharedLocation.ActiveLiveLocationsResponse;
import io.getstream.chat.java.models.SharedLocation.SharedLocationRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SharedLocationTest extends BasicTest {

  @BeforeAll
  static void setupSharedLocations() {
    Map<String, Object> configOverrides = new HashMap<>();
    configOverrides.put("shared_locations", true);

    Assertions.assertDoesNotThrow(
        () ->
            Channel.partialUpdate(testChannel.getType(), testChannel.getId())
                .setValue("config_overrides", configOverrides)
                .request());
  }

  /**
   * Helper method to create an endAt date that is at least 2 minutes in the future
   * to ensure it passes the server validation requirement of being more than 1 minute in the future.
   */
  private Date createFutureEndAt() {
    // Add 5 minutes to current time to ensure it's well beyond the 1 minute requirement
    // This accounts for any network delays or processing time
    return new Date(System.currentTimeMillis() + (5 * 60 * 1000));
  }

  /**
   * Helper method to format a Date to ISO 8601 string format
   */
  private String formatDateToISO(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return dateFormat.format(date);
  }

  @DisplayName("Can send message with shared location and verify")
  @Test
  void whenSendingMessageWithSharedLocation_thenCanGetThroughUsersLocations()
      throws StreamException, ParseException {
    // Create a unique device ID for this test
    String deviceId = "device-" + UUID.randomUUID().toString();

    // Create a future endAt date (at least 2 minutes in the future)
    Date endAtDate = createFutureEndAt();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    String endAtString = formatDateToISO(endAtDate);

    // Create shared location request
    SharedLocationRequest locationRequest = new SharedLocation.SharedLocationRequest();
    locationRequest.setCreatedByDeviceId(deviceId);
    locationRequest.setLatitude(40.7128);
    locationRequest.setLongitude(-74.0060);
    locationRequest.setEndAt(endAtString);
    locationRequest.setUserId(testUserRequestObject.getId());

    // Convert request to SharedLocation
    SharedLocation sharedLocation = new SharedLocation();
    sharedLocation.setCreatedByDeviceId(locationRequest.getCreatedByDeviceId());
    sharedLocation.setLatitude(locationRequest.getLatitude());
    sharedLocation.setLongitude(locationRequest.getLongitude());
    sharedLocation.setEndAt(endAtDate);

    // Send message with shared location
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text("I'm sharing my live location")
            .userId(testUserRequestObject.getId())
            .type(MessageType.REGULAR)
            .sharedLocation(sharedLocation)
            .build();

    Message message =
        Message.send(testChannel.getType(), testChannel.getId())
            .message(messageRequest)
            .request()
            .getMessage();

    // Verify message was sent with correct shared location
    Assertions.assertNotNull(message);
    Assertions.assertNotNull(message.getSharedLocation());
    Assertions.assertEquals(deviceId, message.getSharedLocation().getCreatedByDeviceId());
    Assertions.assertEquals(40.7128, message.getSharedLocation().getLatitude());
    Assertions.assertEquals(-74.0060, message.getSharedLocation().getLongitude());

    // Verify the endAt date is set (allowing for small timing differences)
    Assertions.assertNotNull(message.getSharedLocation().getEndAt());
    // The endAt should be close to our expected time (within a reasonable range)
    long timeDiff = Math.abs(message.getSharedLocation().getEndAt().getTime() - endAtDate.getTime());
    Assertions.assertTrue(timeDiff < 60000, "EndAt time should be within 1 minute of expected time");
  }

  @DisplayName("Can create live location, update it and verify the update")
  @Test
  void whenUpdatingLiveLocation_thenCanGetUpdatedLocation() throws StreamException, ParseException {
    // Create a unique device ID for this test
    String deviceId = "device-" + UUID.randomUUID().toString();

    // Create a future endAt date (at least 2 minutes in the future)
    Date initialEndAtDate = createFutureEndAt();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    String initialEndAtString = formatDateToISO(initialEndAtDate);

    // Create initial shared location request
    SharedLocationRequest initialLocationRequest = new SharedLocation.SharedLocationRequest();
    initialLocationRequest.setCreatedByDeviceId(deviceId);
    initialLocationRequest.setLatitude(40.7128);
    initialLocationRequest.setLongitude(-74.0060);
    initialLocationRequest.setEndAt(initialEndAtString);
    initialLocationRequest.setUserId(testUserRequestObject.getId());

    // Convert request to SharedLocation
    SharedLocation initialSharedLocation = new SharedLocation();
    initialSharedLocation.setCreatedByDeviceId(initialLocationRequest.getCreatedByDeviceId());
    initialSharedLocation.setLatitude(initialLocationRequest.getLatitude());
    initialSharedLocation.setLongitude(initialLocationRequest.getLongitude());
    initialSharedLocation.setEndAt(initialEndAtDate);

    // Send initial message with shared location
    MessageRequestObject initialMessageRequest =
        MessageRequestObject.builder()
            .text("I'm sharing my live location")
            .userId(testUserRequestObject.getId())
            .type(MessageType.REGULAR)
            .sharedLocation(initialSharedLocation)
            .build();

    Message initialMessage =
        Message.send(testChannel.getType(), testChannel.getId())
            .message(initialMessageRequest)
            .request()
            .getMessage();

    // Create updated location request with a new future endAt date
    Date updatedEndAtDate = createFutureEndAt();
    String updatedEndAtString = formatDateToISO(updatedEndAtDate);

    SharedLocationRequest updatedLocationRequest = new SharedLocation.SharedLocationRequest();
    updatedLocationRequest.setMessageId(initialMessage.getId());
    updatedLocationRequest.setCreatedByDeviceId(deviceId);
    updatedLocationRequest.setLatitude(40.7589); // Updated latitude
    updatedLocationRequest.setLongitude(-73.9851); // Updated longitude
    updatedLocationRequest.setEndAt(updatedEndAtString);
    updatedLocationRequest.setUserId(testUserRequestObject.getId());

    // Update the location
    SharedLocation.SharedLocationResponse updateResponse =
        SharedLocation.updateLocation()
            .userId(testUserRequestObject.getId())
            .request(updatedLocationRequest)
            .request();

    // Get active live locations
    ActiveLiveLocationsResponse activeLocations =
        SharedLocation.getLocations().userId(testUserRequestObject.getId()).request();

    // Verify the updated location
    Assertions.assertNotNull(activeLocations);
    Assertions.assertNotNull(activeLocations.getActiveLiveLocations());
    Assertions.assertFalse(activeLocations.getActiveLiveLocations().isEmpty());

    // Find our location in the response
    SharedLocation updatedLocation =
        activeLocations.getActiveLiveLocations().stream()
            .filter(loc -> deviceId.equals(loc.getCreatedByDeviceId()))
            .findFirst()
            .orElse(null);

    Assertions.assertNotNull(updatedLocation);
    Assertions.assertEquals(deviceId, updatedLocation.getCreatedByDeviceId());
    Assertions.assertEquals(40.7589, updatedLocation.getLatitude());
    Assertions.assertEquals(-73.9851, updatedLocation.getLongitude());

    // Verify the endAt date is set (allowing for small timing differences)
    Assertions.assertNotNull(updatedLocation.getEndAt());
    long timeDiff = Math.abs(updatedLocation.getEndAt().getTime() - updatedEndAtDate.getTime());
    Assertions.assertTrue(timeDiff < 60000, "EndAt time should be within 1 minute of expected time");
  }

  @DisplayName("Can verify live location in channel")
  @Test
  void whenQueryingChannel_thenShouldHaveLiveLocation() throws StreamException, ParseException {
    // Create a unique device ID for this test
    String deviceId = "device-" + UUID.randomUUID().toString();

    // Create a future endAt date (at least 2 minutes in the future)
    Date endAtDate = createFutureEndAt();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    String endAtString = formatDateToISO(endAtDate);

    // Create shared location request
    SharedLocationRequest locationRequest = new SharedLocation.SharedLocationRequest();
    locationRequest.setCreatedByDeviceId(deviceId);
    locationRequest.setLatitude(40.7128);
    locationRequest.setLongitude(-74.0060);
    locationRequest.setEndAt(endAtString);
    locationRequest.setUserId(testUserRequestObject.getId());

    // Convert request to SharedLocation
    SharedLocation sharedLocation = new SharedLocation();
    sharedLocation.setCreatedByDeviceId(locationRequest.getCreatedByDeviceId());
    sharedLocation.setLatitude(locationRequest.getLatitude());
    sharedLocation.setLongitude(locationRequest.getLongitude());
    sharedLocation.setEndAt(endAtDate);

    // Send message with shared location
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text("I'm sharing my live location")
            .userId(testUserRequestObject.getId())
            .type(MessageType.REGULAR)
            .sharedLocation(sharedLocation)
            .build();

    Message message =
        Message.send(testChannel.getType(), testChannel.getId())
            .message(messageRequest)
            .request()
            .getMessage();

    // Verify message was sent with correct shared location
    Assertions.assertNotNull(message);
    Assertions.assertNotNull(message.getSharedLocation());
    Assertions.assertEquals(deviceId, message.getSharedLocation().getCreatedByDeviceId());
    Assertions.assertEquals(40.7128, message.getSharedLocation().getLatitude());
    Assertions.assertEquals(-74.0060, message.getSharedLocation().getLongitude());

    // Verify the endAt date is set (allowing for small timing differences)
    Assertions.assertNotNull(message.getSharedLocation().getEndAt());
    long timeDiff = Math.abs(message.getSharedLocation().getEndAt().getTime() - endAtDate.getTime());
    Assertions.assertTrue(timeDiff < 60000, "EndAt time should be within 1 minute of expected time");

    // Query the channel to verify it has the live location
    ChannelGetResponse response =
        Channel.getOrCreate(testChannel.getType(), testChannel.getId())
            .data(ChannelRequestObject.builder().createdBy(testUserRequestObject).build())
            .request();

    // Verify the channel has active live locations
    Assertions.assertNotNull(response.getActiveLiveLocations());
    Assertions.assertFalse(response.getActiveLiveLocations().isEmpty());

    // Find our location in the active live locations
    SharedLocation channelLocation =
        response.getActiveLiveLocations().stream()
            .filter(loc -> deviceId.equals(loc.getCreatedByDeviceId()))
            .findFirst()
            .orElse(null);

    // Verify the location details
    Assertions.assertNotNull(channelLocation);
    Assertions.assertEquals(deviceId, channelLocation.getCreatedByDeviceId());
    Assertions.assertEquals(40.7128, channelLocation.getLatitude());
    Assertions.assertEquals(-74.0060, channelLocation.getLongitude());
    Assertions.assertNotNull(channelLocation.getEndAt());
    // Allow for small timing differences in the endAt comparison
    long channelTimeDiff = Math.abs(channelLocation.getEndAt().getTime() - endAtDate.getTime());
    Assertions.assertTrue(channelTimeDiff < 60000, "EndAt time should be within 1 minute of expected time");
  }
}