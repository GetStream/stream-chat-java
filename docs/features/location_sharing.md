Location sharing allows users to send a static position or share their real-time location with other participants in a channel. Stream Chat supports both static and live location sharing.

There are two types of location sharing:

- **Static Location**: A one-time location share that does not update over time.
- **Live Location**: A real-time location sharing that updates over time.

> [!NOTE]
> The SDK handles location message creation and updates, but location tracking must be implemented by the application using device location services.


## Enabling location sharing

The location sharing feature must be activated at the channel level before it can be used. You have two configuration options: activate it for a single channel using configuration overrides, or enable it globally for all channels of a particular type via [channel type settings](/chat/docs/java/channel_features/).

```java
// Enabling it for a channel type
ChannelType.update("messaging")
  .sharedLocations(true)
  .request();
```

## Sending static location

Static location sharing allows you to send a message containing a static location.

```java
// Create shared location request
SharedLocationRequest locationRequest = new SharedLocation.SharedLocationRequest();
locationRequest.setCreatedByDeviceId(deviceId);
locationRequest.setLatitude(latitude);
locationRequest.setLongitude(longitude);

// Convert request to SharedLocation
SharedLocation sharedLocation = new SharedLocation();
sharedLocation.setCreatedByDeviceId(locationRequest.getCreatedByDeviceId());
sharedLocation.setLatitude(locationRequest.getLatitude());
sharedLocation.setLongitude(locationRequest.getLongitude());

// Send message with shared location
MessageRequestObject messageRequest =
    MessageRequestObject.builder()
        .sharedLocation(sharedLocation)
        .build();

Message message =
    Message.send(testChannel.getType(), testChannel.getId())
        .message(messageRequest)
        .request()
        .getMessage();
```

## Starting live location sharing

Live location sharing enables real-time location updates for a specified duration. The SDK manages the location message lifecycle, but your application is responsible for providing location updates.

```java
// Create shared location request
SharedLocationRequest locationRequest = new SharedLocation.SharedLocationRequest();
locationRequest.setCreatedByDeviceId(deviceId);
locationRequest.setLatitude(latitude);
locationRequest.setLongitude(longitude);
locationRequest.setEndAt(end_at);

// Convert request to SharedLocation
SharedLocation sharedLocation = new SharedLocation();
sharedLocation.setCreatedByDeviceId(locationRequest.getCreatedByDeviceId());
sharedLocation.setLatitude(locationRequest.getLatitude());
sharedLocation.setLongitude(locationRequest.getLongitude());
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
sharedLocation.setEndAt(dateFormat.parse(locationRequest.getEndAt()));

// Send message with shared location
MessageRequestObject messageRequest =
    MessageRequestObject.builder()
        .sharedLocation(sharedLocation)
        .build();

Message message =
    Message.send(testChannel.getType(), testChannel.getId())
        .message(messageRequest)
        .request()
        .getMessage();
```

## Stopping live location sharing

You can stop live location sharing for a specific message using the message controller:

```java
// Create SharedLocation request with time.now
SharedLocationRequest updatedLocationRequest = new SharedLocation.SharedLocationRequest();
updatedLocationRequest.setMessageId(initialMessage.getId());
updatedLocationRequest.setCreatedByDeviceId(deviceId);
updatedLocationRequest.setEndAt(LocalDate.now());

// Update the live location
SharedLocation.SharedLocationResponse updateResponse =
    SharedLocation.updateLocation()
        .userId(testUserRequestObject.getId())
        .request(updatedLocationRequest)
        .request();
```

## Updating live location

Your application must implement location tracking and provide updates to the SDK. The SDK handles updating all the current user's active live location messages and provides a throttling mechanism to prevent excessive API calls.

```java
// Get active live locations
ActiveLiveLocationsResponse activeLocations =
    SharedLocation.getLocations().userId(userRequestObject.getId()).request();

// Create updated location request
SharedLocationRequest updatedLocationRequest = new SharedLocation.SharedLocationRequest();
updatedLocationRequest.setMessageId(initialMessage.getId());
updatedLocationRequest.setCreatedByDeviceId(deviceId);
updatedLocationRequest.setEndAt(end_at);

// Update the live location
SharedLocation.SharedLocationResponse updateResponse =
    SharedLocation.updateLocation()
        .request(updatedLocationRequest)
        .request();
```

Whenever the location is updated, the message will automatically be updated with the new location.

The SDK will also notify your application when it should start or stop location tracking as well as when the active live location messages change.


## Events

Whenever a location is created or updated, the following WebSocket events will be sent:

- `message.new`: When a new location message is created.
- `message.updated`: When a location message is updated.

> [!NOTE]
> In Dart, these events are resolved to more specific location events:
>
> - `location.shared`: When a new location message is created.
> - `location.updated`: When a location message is updated.


You can easily check if a message is a location message by checking the `message.sharedLocation` property. For example, you can use this events to render the locations in a map view.
