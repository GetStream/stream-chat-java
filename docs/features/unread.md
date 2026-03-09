The following unread counts are provided by Stream

- A total count of unread messages
- Number of unread channels
- A count of unread threads
- Unread @mentions
- Unread messages per channel
- Unread @mentions per channel
- Unread counts by team
- Unread counts by channel type

Unread counts are first fetched when a user connects.
After that they are updated by events. (new message, mark read, delete message, delete channel etc.)

### Reading Unread Counts

Unread counts are returned when a user connects. After that, you can listen to events to keep them updated in real-time.

```java
// Step 1: Get initial unread counts when connecting
User user = new User();
user.setId("user-id");
client.connectUser(user, "{{ chat_user_token }}").enqueue(result -> {
  if (result.isSuccess()) {
    User userRes = result.data().getUser();
    int unreadChannels = userRes.getUnreadChannels();
    int totalUnreadCount = userRes.getTotalUnreadCount();
  }
});

// Step 2: Listen to events for real-time updates
client.subscribeFor(
   new Class[]{
       NewMessageEvent.class,
       NotificationMessageNewEvent.class,
       MarkAllReadEvent.class,
       NotificationMarkReadEvent.class
   },
   event -> {
     if (event instanceof NewMessageEvent) {
       NewMessageEvent e = (NewMessageEvent) event;
       Integer unreadChannels = e.getUnreadChannels();
       Integer totalUnreadCount = e.getTotalUnreadCount();
     } else if (event instanceof NotificationMessageNewEvent) {
       NotificationMessageNewEvent e = (NotificationMessageNewEvent) event;
       Integer unreadChannels = e.getUnreadChannels();
       Integer totalUnreadCount = e.getTotalUnreadCount();
     }
     // Handle other event types similarly
   }
);
```

Note that the higher level SDKs offer convenient endpoints for this. Hooks on react, stateflow on Android etc.
So you only need to use the events manually if you're using plain JS.

### Unread Counts - Server side

The unread endpoint can fetch unread counts server-side, eliminating the need for a client-side connection. It can also be used client-side without requiring a persistent connection to the chat service. This can be useful for including an unread count in notifications or for gently polling when a user loads the application to keep the client up to date without loading up the entire chat.

> [!NOTE]
> A user_id whose unread count is fetched through this method is automatically counted as a Monthly Active User. This may affect your bill.


> [!NOTE]
> This endpoint will return the last 100 unread channels, they are sorted by last_message_at.


#### Batch Fetch Unread

The batch unread endpoint works the same way as the non-batch version with the exception that it can handle multiple user IDs at once and that it is restricted to server-side only.


> [!NOTE]
> If a user ID is not returned in the response then the API couldn't find a user with that ID


### Mark Read

By default the UI component SDKs (React, React Native, ...) mark messages as read automatically when the channel is visible. You can also make the call manually like this:

```java
channelClient.markRead().enqueue(result -> {
  if (result.isSuccess()) {
    // Messages in the channel marked as read
  } else {
    // Handle result.error()
  }
});
```

The `markRead` function can also be executed server-side by passing a user ID as shown in the example below:


It's also possible to mark an already read message as unread:


The mark unread operation can also be executed server-side by passing a user ID:


#### Mark All As Read

You can mark all channels as read for a user like this:

```java
client.markAllRead().enqueue((result) -> {
  if (result.isSuccess()) {
    //Handle success
  } else {
    //Handle failure
  }
});
```

## Read State - Showing how far other users have read

When you retrieve a channel from the API (e.g. using query channels), the read state for all members is included in the response. This allows you to display which messages are read by each user. For each member, we include the last time they marked the channel as read.

```java
// Get channel
QueryChannelRequest queryChannelRequest = new QueryChannelRequest().withState();

client.queryChannel("channel-type", "channel-id", queryChannelRequest).enqueue((result) -> {
  if (result.isSuccess()) {
    // readState is the list of read states for each user on the channel
    List<ChannelUserRead> readState = result.data().getRead();
  } else {
    // Handle result.error()
  }
});
```

### Unread Messages Per Channel

You can retrieve the count of unread messages for the current user on a channel like this:

```java
// Get channel
QueryChannelRequest queryChannelRequest = new QueryChannelRequest().withState();

client.queryChannel("channel-type", "channel-id", queryChannelRequest).enqueue((result) -> {
  if (result.isSuccess()) {
    // Unread count for current user
    Integer unreadCount = result.data().getUnreadCount();
  } else {
    // Handle result.error()
  }
});
```

### Unread Mentions Per Channel

You can retrieve the count of unread messages mentioning the current user on a channel like this:

```java
// Get channel
QueryChannelRequest queryChannelRequest = new QueryChannelRequest().withState();
User currentUser = client.getCurrentUser();
if (currentUser == null) {
  // Handle user not connected state
  return;
}

client.queryChannel("channel-type", "channel-id", queryChannelRequest).enqueue((result) -> {
  if (result.isSuccess()) {
    // Unread mentions
    Channel channel = result.data();
    Integer unreadCount = ChannelExtensionKt.countUnreadMentionsForUser(channel, currentUser);
  } else {
    // Handle result.error()
  }
});
```
