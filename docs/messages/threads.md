Threads allow users to reply to specific messages without cluttering the main channel conversation. A thread is created when a message is sent with a `parent_id` referencing another message.

## Starting a Thread

Send a message with a `parent_id` to start a thread or add a reply to an existing thread.

```java
// Android SDK
Message message = new Message();
message.setText("This is a reply in a thread");
message.setParentId(parentMessage.getId());

channelClient.sendMessage(message).enqueue(result -> {
  if (result.isSuccess()) {
    Message sentMessage = result.data();
  } else {
    // Handle result.error()
  }
});

// Backend SDK
Message.send(type, id)
  .message(
    MessageRequestObject.builder()
      .text("This is a reply in a thread")
      .parentId(parentId)
      .showInChannel(false)
      .userId(userId)
      .build())
  .request();
```

### Thread Parameters

| Name            | Type    | Description                                                        | Default | Optional |
| --------------- | ------- | ------------------------------------------------------------------ | ------- | -------- |
| parent_id       | string  | ID of the parent message to reply to                               |         |          |
| show_in_channel | boolean | If true, the reply appears both in the thread and the main channel | false   | ✓        |

> [!NOTE]
> Messages in threads support the same features as regular messages: reactions, attachments, and mentions.


## Paginating Thread Replies

When querying a channel, thread replies are not included by default. The parent message includes a `reply_count` field. Use `getReplies` to fetch thread messages.

```java
// Android SDK
int limit = 20;

// Get the first 20 replies
client.getReplies(parentMessage.getId(), limit).enqueue(result -> {
  if (result.isSuccess()) {
    List<Message> replies = result.data();
  } else {
    // Handle result.error()
  }
});

// Get 20 more replies before message with ID "42"
client.getRepliesMore(parentMessage.getId(), "42", limit).enqueue(result -> { /* ... */ });

// Backend SDK
Message.getReplies(parentMessageId).limit(20).request();
Message.getReplies(parentMessageId).limit(20).idLte("42").request();
```

## Inline Replies

Reply to a message inline without creating a thread. The referenced message appears within the new message. Use `quoted_message_id` instead of `parent_id`.

```java
// Android SDK
Message message = new Message();
message.setText("I agree with this point");
message.setReplyMessageId(originalMessage.getId());

channelClient.sendMessage(message).enqueue(result -> { /* ... */ });

// Backend SDK
Message.send(type, id)
  .message(
    MessageRequestObject.builder()
      .text("I agree with this point")
      .quotedMessageId(originalMessageId)
      .userId(userId)
      .build())
  .request();
```

When querying messages, the `quoted_message` field is automatically populated:

```json
{
  "id": "new-message-id",
  "text": "I agree with this point",
  "quoted_message_id": "original-message-id",
  "quoted_message": {
    "id": "original-message-id",
    "text": "The original message text"
  }
}
```

> [!WARNING]
> Inline replies are only available one level deep. If Message A replies to Message B, and Message B replies to Message C, you cannot access Message C through Message A. Fetch Message B directly to access its referenced message.


## Thread List

Query all threads that the current user participates in. This is useful for building thread list views similar to Slack or Discord.

### Querying Threads

Threads are returned with unread replies first, sorted by the latest reply timestamp in descending order.


<details>
<summary>Example Response</summary>

```json
{
  "threads": [
    {
      "channel_cid": "messaging:general",
      "channel": {
        "id": "general",
        "type": "messaging",
        "name": "General"
      },
      "parent_message_id": "parent-123",
      "parent_message": {
        "id": "parent-123",
        "text": "Original message",
        "type": "regular"
      },
      "created_by_user_id": "user-1",
      "reply_count": 5,
      "participant_count": 3,
      "thread_participants": [
        {
          "user_id": "user-1",
          "user": { "id": "user-1", "name": "Alice" }
        },
        {
          "user_id": "user-2",
          "user": { "id": "user-2", "name": "Bob" }
        }
      ],
      "last_message_at": "2024-12-11T15:30:00Z",
      "latest_replies": [
        {
          "id": "reply-1",
          "text": "Latest reply",
          "type": "reply"
        }
      ],
      "read": [
        {
          "user": { "id": "user-1" },
          "last_read": "2024-12-11T15:00:00Z",
          "unread_messages": 2
        }
      ]
    }
  ]
}
```

</details>

### Query Options

| Name              | Type    | Description                                       | Default | Optional |
| ----------------- | ------- | ------------------------------------------------- | ------- | -------- |
| reply_limit       | number  | Number of latest replies to fetch per thread      | 2       | ✓        |
| participant_limit | number  | Number of thread participants to fetch per thread | 100     | ✓        |
| limit             | number  | Maximum number of threads to return               | 10      | ✓        |
| watch             | boolean | If true, watch channels for the returned threads  | true    | ✓        |
| member_limit      | number  | Number of members to fetch per thread channel     | 100     | ✓        |

### Filtering and Sorting

Filter and sort threads using MongoDB-style query operators.

#### Supported Filter Fields

| Field                | Type                      | Operators                           | Description               |
| -------------------- | ------------------------- | ----------------------------------- | ------------------------- |
| `channel_cid`        | string or list of strings | `$eq`, `$in`                        | Channel CID               |
| `channel.disabled`   | boolean                   | `$eq`                               | Channel disabled status   |
| `channel.team`       | string or list of strings | `$eq`, `$in`                        | Channel team              |
| `parent_message_id`  | string or list of strings | `$eq`, `$in`                        | Parent message ID         |
| `created_by_user_id` | string or list of strings | `$eq`, `$in`                        | Thread creator's user ID  |
| `created_at`         | string (RFC3339)          | `$eq`, `$gt`, `$lt`, `$gte`, `$lte` | Thread creation timestamp |
| `updated_at`         | string (RFC3339)          | `$eq`, `$gt`, `$lt`, `$gte`, `$lte` | Thread update timestamp   |
| `last_message_at`    | string (RFC3339)          | `$eq`, `$gt`, `$lt`, `$gte`, `$lte` | Last message timestamp    |

#### Supported Sort Fields

- `active_participant_count`
- `created_at`
- `last_message_at`
- `parent_message_id`
- `participant_count`
- `reply_count`
- `updated_at`

Use `1` for ascending order and `-1` for descending order.

```java
FilterCondition filter = FilterCondition.and(
  FilterCondition.eq("created_by_user_id", "user-1"),
  FilterCondition.gte("updated_at", "2024-01-01T00:00:00Z")
);

List<Sort> sort = Arrays.asList(
  Sort.builder().field("created_at").direction(Sort.Direction.DESC).build()
);

Thread.QueryThreadsResponse response = Thread.queryThreads()
  .userId(userId)
  .filter(filter)
  .sort(sort)
  .limit(10)
  .request();

// Get next page
if (response.getNext() != null) {
  Thread.QueryThreadsResponse nextPage = Thread.queryThreads()
    .userId(userId)
    .filter(filter)
    .sort(sort)
    .limit(10)
    .next(response.getNext())
    .request();
}
```

### Getting a Thread by ID

Retrieve a specific thread using the parent message ID.


### Updating Thread Title and Custom Data

Assign a title and custom data to a thread.


## Thread Unread Counts

### Total Unread Threads

The total number of unread threads is available after connecting.


### Marking Threads as Read or Unread


### Unread Count Per Thread


## Thread Manager

The `ThreadManager` class provides built-in pagination and state management for threads.


### Event Handling

Register subscriptions to receive real-time updates for threads.


> [!NOTE]
> The `watch` parameter is required when querying threads to receive real-time updates.


For `ThreadManager`, call `registerSubscriptions` once to automatically manage subscriptions for all loaded threads:
