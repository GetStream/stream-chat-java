Scheduled messages let a user write a message now and have it sent later. A server-side client can schedule, list, edit, cancel and send early the scheduled messages of any user.

Scheduled messages must be enabled for your app. Until the scheduled message is sent it is visible only to the user who scheduled it (and to your backend); the channel's other members see nothing until the message is sent.

## Scheduling a Message

Pass `scheduledAt` to a regular send. The message is not sent: it is stored and sent at that time. The send time must be at least 1 minute and at most the app's maximum lead time (120 days by default) away.

```java
// Backend SDK
Date in1Hour = new Date(System.currentTimeMillis() + 60 * 60 * 1000);

MessageSendResponse response =
    Message.send("messaging", "general")
        .message(MessageRequestObject.builder().text("Good morning!").userId("john").build())
        .scheduledAt(in1Hour)
        .request();

ScheduledMessage scheduled = response.getScheduledMessage();
scheduled.getId();          // ID of the scheduled message
scheduled.getStatus();      // "scheduled"
scheduled.getScheduledAt(); // when it will be sent
```

`response.getMessage()` holds the message as it will be sent; it is not in the channel yet. Slash commands cannot be scheduled, and `scheduledAt` cannot be combined with `pending`.

## Listing Scheduled Messages

Lists one user's scheduled messages across channels: pending ones first, then failed ones, each by `scheduled_at`. `userId` is required. Each item carries a summary of its channel.

```java
ScheduledMessageQueryResponse page =
    ScheduledMessage.query()
        .userId("john")
        .filter(
            ScheduledMessagesFilter.builder()
                .cid("messaging:general") // optional: one channel only
                .status("scheduled")      // optional: scheduled, sending, failed
                .build())
        .limit(100) // 1 to 100, defaults to 20
        .request();

List<ScheduledMessage> items = page.getScheduledMessages();

// Next page
if (page.getNext() != null && !page.getNext().isEmpty()) {
  ScheduledMessage.query().userId("john").next(page.getNext()).request();
}
```

Only sorting by `scheduled_at` ascending is supported.

## Editing a Scheduled Message

Replace the message, move the send time, or both. A new `message` replaces the stored one as a whole; the sender stays the user who scheduled it.

```java
ScheduledMessage.update(scheduled.getId())
    .message(MessageRequestObject.builder().text("Good morning, everyone!").build())
    .scheduledAt(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000))
    .request();
```

## Canceling a Scheduled Message

Cancels a scheduled message so it is never sent, or removes one that failed.

```java
ScheduledMessage canceled = ScheduledMessage.delete(scheduled.getId()).request().getScheduledMessage();
canceled.getStatus(); // "canceled"
```

## Sending a Scheduled Message Now

Moves the send time to now and queues the send.

```java
ScheduledMessage.sendNow(scheduled.getId()).request();
```

Editing, canceling and sending now return a 409 error while the message is being sent.

## Statuses and Failures

| Status      | Meaning                                                                         |
| ----------- | ------------------------------------------------------------------------------- |
| `scheduled` | Waiting for its send time                                                       |
| `sending`   | Being sent                                                                      |
| `failed`    | The send failed; `getFailureReason()` says why. It can only be deleted          |
| `canceled`  | Canceled; returned by `delete`                                                  |
| `sent`      | Sent; reported only by the `scheduled_message.sent` event                       |

Failure reasons: `banned`, `no_permission`, `channel_frozen`, `channel_deleted`, `user_deleted`, `moderation_blocked`, `hook_rejected`, `expired`, `internal_error`, `invalid_message`, `pending_not_supported`.

## Events and Webhooks

| Event                       | Webhook | When                                                                 |
| --------------------------- | ------- | -------------------------------------------------------------------- |
| `scheduled_message.created` | No      | A message was scheduled                                              |
| `scheduled_message.updated` | No      | A scheduled message was edited or sent now                           |
| `scheduled_message.deleted` | No      | A scheduled message was canceled                                     |
| `scheduled_message.sent`    | Yes     | A scheduled message was sent; `getMessageId()` is the sent message ID |
| `scheduled_message.failed`  | Yes     | A scheduled message failed to send                                   |

`Event.getScheduledMessage()` holds the scheduled message on all of them. The sent message itself also triggers the usual `message.new`.

## Permissions

Scheduling needs the `ScheduleMessage` permission (channel capability `schedule-message`) in addition to the permission to send the message.
