Invites allow you to add users to a channel with a pending state. The invited user receives a notification and can accept or reject the invite.

Unread counts are not incremented for channels with a pending invite.

## Invite Users

```java
// Android SDK
ChannelClient channelClient = client.channel("messaging", "general");

List<String> memberIds = Arrays.asList("thierry", "tommaso");
Map<String, Object> data = new HashMap<>();
data.put("invites", Arrays.asList("nick"));

channelClient.create(memberIds, data).enqueue(result -> {
  if (result.isSuccess()) {
    Channel channel = result.data();
  } else {
    // Handle result.error()
  }
 });


// Backend SDK
Channel.update("messaging", "general")
  .invites(Arrays.asList("thierry", "tommaso"))
  .request();
```

## Accept an Invite

Call `acceptInvite` to accept a pending invite. You can optionally include a `message` parameter to post a system message when the user joins (e.g., "Nick joined this channel!").

```java
// Android SDK
channelClient.acceptInvite("Nick joined this channel!").enqueue(result -> {
  if (result.isSuccess()) {
    Channel channel = result.data();
  } else {
    // Handle result.error()
  }
});

// Backend SDK
Channel.update("messaging", "general")
  .acceptInvite(true)
  .userId("nick")
  .message(MessageRequestObject.builder().text("Nick joined the channel").build())
  .request();
```

## Reject an Invite

Call `rejectInvite` to decline a pending invite. Client-side calls use the currently connected user. Server-side calls require a `user_id` parameter.

```java
// Android SDK
channelClient.rejectInvite().enqueue(result -> {
  if (result.isSuccess()) {
    // Invite rejected
  } else {
    // Handle result.error()
  }
});

// Backend SDK
Channel.update("messaging", "general")
  .acceptInvite(false)
  .userId("nick")
  .request();
```

## Query Invites by Status

Use `queryChannels` with the `invite` filter to retrieve channels based on invite status. Valid values are `pending`, `accepted`, and `rejected`.

### Query Accepted Invites

```java
// Android SDK
FilterObject filter = Filters.eq("invite", "accepted");
int offset = 0;
int limit = 10;
QuerySorter<Channel> sort = new QuerySortByField<>();
int messageLimit = 0;
int memberLimit = 0;
QueryChannelsRequest request = new QueryChannelsRequest(filter, offset, limit, sort, messageLimit, memberLimit);

client.queryChannels(request).enqueue(result -> {
  if (result.isSuccess()) {
    List<Channel> channels = result.data();
  } else {
    // Handle result.error()
  }
});

// Backend SDK
Channel.queryMembers().type("messaging").id("general").filterCondition("invite", "accepted").request();
```

### Query Rejected Invites

```java
// Android SDK
FilterObject filter = Filters.eq("invite", "rejected");
int offset = 0;
int limit = 10;
QuerySorter<Channel> sort = new QuerySortByField<>();
int messageLimit = 0;
int memberLimit = 0;
QueryChannelsRequest request = new QueryChannelsRequest(filter, offset, limit, sort, messageLimit, memberLimit);

client.queryChannels(request).enqueue(result -> {
  if (result.isSuccess()) {
    List<Channel> channels = result.data();
  } else {
    // Handle result.error()
  }
});

// Backend SDK
Channel.queryMembers()
  .type("messaging")
  .id("general")
  .filterCondition("invite", "rejected")
  .request();
```

### Query Pending Invites

```java
FilterObject filter = Filters.eq("invite", "pending");
int offset = 0;
int limit = 10;
QuerySorter<Channel> sort = new QuerySortByField<>();
int messageLimit = 0;
int memberLimit = 0;
QueryChannelsRequest request = new QueryChannelsRequest(filter, offset, limit, sort, messageLimit, memberLimit);

client.queryChannels(request).enqueue(result -> {
  if (result.isSuccess()) {
    List<Channel> channels = result.data();
  } else {
    // Handle result.error()
  }
});
```
