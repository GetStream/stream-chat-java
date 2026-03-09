Muting a channel prevents it from triggering push notifications, unhiding, or incrementing the unread count for that user.

By default, mutes remain active indefinitely until removed. You can optionally set an expiration time. The list of muted channels and their expiration times is returned when the user connects.

## Mute a Channel

```java
// Android SDK

// Mute a channel
ChannelClient channelClient = client.channel("messaging", "general");
channelClient.mute().enqueue(result -> {
  if (result.isSuccess()) {
    // Channel is muted
  } else {
    // Handle result.error()
  }
});

// Get list of muted channels when user is connected
User user = new User();
user.setId("user-id");
client.connectUser(user, "token").enqueue(result -> {
  if (result.isSuccess()) {
    // Result contains the list of channel mutes
    List<ChannelMute> mutes = result.data().getUser().getChannelMutes();
  }
 });

// Get updates about muted channels
client.subscribeFor(
    new Class[]{NotificationChannelMutesUpdatedEvent.class},
    channelsMuteEvent -> {
      List<ChannelMute> mutes = ((NotificationChannelMutesUpdatedEvent) channelsMuteEvent).getMe().getChannelMutes();
    }
);

// Backend SDK
Channel.mute()
  .channelCid(channel.getType() + ":" + channel.getId())
  .user(user)
  .expiration(TimeUnit.MILLISECONDS.convert(14, TimeUnit.DAYS)) // Optionally set mute duration
  .request();
```

> [!NOTE]
> Messages added to muted channels do not increase the unread messages count.


### Query Muted Channels

Muted channels can be filtered or excluded by using the `muted` in your query channels filter.

```java
// Android SDK

// Filter for all channels excluding muted ones
FilterObject notMutedFilter = Filters.and(
    Filters.eq("muted", false),
    Filters.in("members", Arrays.asList(currentUserId))
);

// Filter for muted channels
FilterObject mutedFilter = Filters.eq("muted", true);

// Executing a channels query with either of the filters
int offset = 0;
int limit = 10;
QuerySorter<Channel> sort = new QuerySortByField<>();
int messageLimit = 0;
int memberLimit = 0;
client.queryChannels(new QueryChannelsRequest(
    filter, // Set the correct filter here
    offset,
    limit,
    sort,
    messageLimit,
    memberLimit)).enqueue(result -> {
  if (result.isSuccess()) {
    List<Channel> channels = result.data();
  } else {
    // Handle result.error()
  }
});

// Backend SDK

// retrieve all channels excluding muted ones
Channel.list()
  .filterCondition("muted", false) // Or 'true' for selected muted channels
  .filterCondition(FilterCondition.in("members", userId))
  .request();
```

### Remove a Channel Mute

Use the unmute method to restore normal notifications and unread behavior for a channel.

```java
// Android SDK

// Unmute channel for current user
channelClient.unmute().enqueue(result -> {
  if (result.isSuccess()) {
    // Channel is unmuted
  } else {
    // Handle result.error()
  }
});

// Backend SDK
Channel.unmute().channelCid(cid).userId(userId).request();
```
