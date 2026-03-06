Hiding a channel removes it from query channel requests for that user until a new message is added. Only channel members can hide a channel.

Hidden channels may still have unread messages. Consider [marking the channel as read](/chat/docs/java/unread/) before hiding it.

You can optionally clear the message history for that user when hiding. When a new message is received, it will be the only message visible to that user.

## Hide a Channel

```java
// Hides the channel until a new message is added there
channelClient.hide(false).enqueue(result -> {
  if (result.isSuccess()) {
    // Channel is hidden
  } else {
    // Handle result.error()
  }
});

// Shows a previously hidden channel
channelClient.show().enqueue(result -> {
  if (result.isSuccess()) {
    // Channel is shown
  } else {
    // Handle result.error()
  }
});

// Hide the channel and clear the message history
channelClient.hide(true).enqueue(result -> {
  if (result.isSuccess()) {
    // Channel is hidden
  } else {
    // Handle result.error()
  }
});
```

> [!NOTE]
> You can still retrieve the list of hidden channels using the `{ "hidden" : true }` query parameter.
