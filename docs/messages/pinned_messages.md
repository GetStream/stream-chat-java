Pinned messages highlight important content in a channel. Use them for announcements, key information, or temporarily promoted content. Each channel can have multiple pinned messages, with optional expiration times.

## Pinning and Unpinning Messages

Pin an existing message using `pinMessage`, or create a pinned message by setting `pinned: true` when sending.

```java
// Android SDK
Calendar calendar = Calendar.getInstance();
calendar.set(2077, 1, 1);
Date pinExpirationDate = calendar.getTime();

Message message = new Message();
message.setText("Important announcement");
message.setPinned(true);
message.setPinExpires(pinExpirationDate);

channelClient.sendMessage(message).enqueue(result -> { /* ... */ });

// Pin message for 120 seconds
channelClient.pinMessage(message, 120).enqueue(result -> { /* ... */ });

// Pin with expiration date
channelClient.pinMessage(message, pinExpirationDate).enqueue(result -> { /* ... */ });

// Pin indefinitely
channelClient.pinMessage(message, null).enqueue(result -> { /* ... */ });

// Unpin message
channelClient.unpinMessage(message).enqueue(result -> { /* ... */ });

// Backend SDK
Message message =
  Message.send(channelType, channelId)
    .message(
      MessageRequestObject.builder()
        .text("Important announcement")
        .pinned(true)
        .pinExpires(
          new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            .parse("2077-01-01T00:00:00Z"))
        .userId(userId)
        .build())
    .request()
    .getMessage();

// Unpin message
MessageRequestObject messageRequestObject = MessageRequestObject.buildFrom(message);
messageRequestObject.setPinned(false);
Message.update(message.getId()).message(messageRequestObject).request();
```

### Pin Parameters

| Name        | Type    | Description                                                            | Default | Optional |
| ----------- | ------- | ---------------------------------------------------------------------- | ------- | -------- |
| pinned      | boolean | Whether the message is pinned                                          | false   | ✓        |
| pinned_at   | string  | Timestamp when the message was pinned                                  | -       | ✓        |
| pin_expires | string  | Timestamp when the pin expires. Null means the message does not expire | null    | ✓        |
| pinned_by   | object  | The user who pinned the message                                        | -       | ✓        |

> [!NOTE]
> Pinning a message requires the `PinMessage` permission. See [Permission Resources](/chat/docs/java/permissions_reference/) and [Default Permissions](/chat/docs/java/chat_permission_policies/) for details.


## Retrieving Pinned Messages

Query a channel to retrieve the 10 most recent pinned messages from `pinned_messages`.

```java
// Android SDK
channelClient.query(new QueryChannelRequest()).enqueue(result -> {
  if (result.isSuccess()) {
    List<Message> pinnedMessages = result.data().getPinnedMessages();
  } else {
    // Handle result.error()
  }
});

// Backend SDK
ChannelGetResponse resp = Channel.getOrCreate("type", "id").request();
List<Message> messages = resp.getPinnedMessages();
```

## Paginating Pinned Messages

Use the dedicated pinned messages endpoint to retrieve all pinned messages with pagination.

```java
// Android SDK
channelClient.getPinnedMessages(
  10,
  QuerySortByField.descByName("pinnedAt"),
  new PinnedMessagesPagination.BeforeDate(new Date(), false)
).enqueue(result -> {
  if (result.isSuccess()) {
    List<Message> pinnedMessages = result.data();
  } else {
    // Handle result.error()
  }
});

// Next page
Date nextDate = new Date();
channelClient.getPinnedMessages(
  10,
  QuerySortByField.descByName("pinnedAt"),
  new PinnedMessagesPagination.BeforeDate(nextDate, false)
).enqueue(result -> { /* ... */ });

// Backend SDK
Message.search()
  .filterCondition("pinned", true)
  .request();
```
