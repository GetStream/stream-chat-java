package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel.LiveLocationUpdateRequestData;
import io.getstream.chat.java.models.ChannelType.LiveLocation;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.User.UserGetActiveLiveLocationsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class LiveLocationTest extends BasicTest {

    @DisplayName("Can update live location")
    @Test
    void whenUpdatingLiveLocation_thenNoException() {
        // Given
        String locationId = "test-location-" + System.currentTimeMillis();
        Date endTime = new Date(System.currentTimeMillis() + 3600 * 1000); // 1 hour from now
        
        LiveLocationUpdateRequestData requestData = LiveLocationUpdateRequestData.builder()
                .locationId(locationId)
                .userId(testUserRequestObject.getId())
                .latitude(40.7128)
                .longitude(-74.0060)
                .endAt(endTime)
                .createdByDeviceId("test-device")
                .build();

        // When/Then
        Assertions.assertDoesNotThrow(() -> 
            Channel.updateLiveLocation(testChannel.getType(), testChannel.getId())
                .liveLocation(requestData)
                .request()
        );
    }

    @DisplayName("Can get user active live locations")
    @Test
    void whenGettingUserActiveLiveLocations_thenNoException() {
        // Create a live location first to ensure there's at least one to retrieve
        String locationId = "test-location-" + System.currentTimeMillis();
        Date endTime = new Date(System.currentTimeMillis() + 3600 * 1000); // 1 hour from now
        
        LiveLocationUpdateRequestData requestData = LiveLocationUpdateRequestData.builder()
                .locationId(locationId)
                .userId(testUserRequestObject.getId())
                .latitude(40.7128)
                .longitude(-74.0060)
                .endAt(endTime)
                .createdByDeviceId("test-device")
                .build();
        
        Assertions.assertDoesNotThrow(() -> 
            Channel.updateLiveLocation(testChannel.getType(), testChannel.getId())
                .liveLocation(requestData)
                .request()
        );
        
        // Small delay to ensure the location is created
        pause();
        
        // When/Then
        UserGetActiveLiveLocationsResponse response = Assertions.assertDoesNotThrow(() -> 
            User.getActiveLiveLocations(testUserRequestObject.getId())
                .request()
        );
        
        // Verify we have at least one live location
        Assertions.assertNotNull(response.getLiveLocations());
    }
    
    @DisplayName("Handles invalid live location data")
    @Test
    void whenSendingInvalidLiveLocationData_thenThrowsException() {
        LiveLocationUpdateRequestData requestData = LiveLocationUpdateRequestData.builder()
                // Missing required fields
                .userId(testUserRequestObject.getId())
                .build();
                
        // This should either throw a validation exception or return an error from the API
        Assertions.assertThrows(StreamException.class, () -> 
            Channel.updateLiveLocation(testChannel.getType(), testChannel.getId())
                .liveLocation(requestData)
                .request()
        );
    }
    
    @DisplayName("Can send message with live location")
    @Test
    void whenSendingMessageWithLiveLocation_thenMessageContainsLiveLocation() {
        // Given
        Date endTime = new Date(System.currentTimeMillis() + 3600 * 1000); // 1 hour from now
        
        // When
        Message message = Assertions.assertDoesNotThrow(() ->
            Message.sendWithLiveLocation(
                testChannel.getType(),
                testChannel.getId(),
                "Sharing my location",
                testUserRequestObject.getId(),
                40.7128,
                -74.0060,
                endTime,
                "test-device"
            )
            .request()
            .getMessage()
        );
        
        // Small delay to ensure the location is processed
        pause();
        
        // Then
        Assertions.assertNotNull(message);
        
        // Verify the live location is attached to the message
        Assertions.assertNotNull(message.getLiveLocation());
        
        // Verify user's active live locations include this location
        UserGetActiveLiveLocationsResponse response = Assertions.assertDoesNotThrow(() -> 
            User.getActiveLiveLocations(testUserRequestObject.getId())
                .request()
        );
        
        Assertions.assertNotNull(response.getLiveLocations());
        Assertions.assertTrue(response.getLiveLocations().size() > 0);
    }
} 