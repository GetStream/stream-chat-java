For live events or concerts, you can sometimes have so many users, that the sheer volume of messages overloads the browser or mobile device. This can cause the UI to freeze, high CPU usage, and degraded user experience. Stream offers 3 features to help with this:

1. Channel Slow Mode

2. Automatic feature Throttling

3. Message Throttling

Stream scales to 5 million concurrent users on a channel, see [Scaling Chat to 5 Million Concurrent Connections](https://getstream.io/blog/scaling-chat-5-million-concurrent-connections/).

### Channel Slow Mode

Slow mode helps reduce noise on a channel by limiting users to a maximum of 1 message per cooldown interval.

The cooldown interval is configurable and can be anything between 1 and 120 seconds. For instance, if you enable slow mode and set the cooldown interval to 30 seconds a user will be able to post at most 1 message every 30 seconds.

> [!NOTE]
> Moderators, admins and server-side API calls are not restricted by the cooldown period and can post messages as usual.


Slow mode is disabled by default and can be enabled/disabled via the Dashboard, using the Chat Explorer:

![](https://getstream.imgix.net/docs/Screenshot%202021-08-13%20105802.png?auto=compress&fit=clip&w=800&h=600)

It can also be enabled/disabled by admins and moderators via SDK.

```java
// Android SDK
final ChannelClient channelClient = client.channel("messaging", "general");

// Enable slow mode and set cooldown to 1s
channelClient.enableSlowMode(1).enqueue(result -> { /* Result handling */ });

// Increase cooldown to 30s
channelClient.enableSlowMode(30).enqueue(result -> { /* Result handling */ });

// Disable slow mode
channelClient.disableSlowMode().enqueue(result -> { /* Result handling */ });

// Backend SDK

// Enable slow mode and set cooldown to 1s
Channel.update("messaging", "general").cooldown(1).request();

// Increase cooldown to 30s
Channel.update("messaging", "general").cooldown(30).request();

// Disable slow mode
Channel.update("messaging", "general").cooldown(0).request();
```

When a user posts a message during the cooldown period, the API returns an error message. You can avoid hitting the APIs and instead show such limitation on the send message UI directly. When slow mode is enabled, channels include a `cooldown` field containing the current cooldown period in seconds.

```java
final ChannelClient channelClient = client.channel("messaging", "general");

// Get the cooldown value
channelClient.query(new QueryChannelRequest()).enqueue(channelResult -> {
  if (channelResult.isSuccess()) {
    final Channel channel = channelResult.data();
    int cooldown = channel.getCooldown();

    Message message = new Message();
    message.setText("Hello");
    channelClient.sendMessage(message).enqueue((messageResult) -> {
      // After sending a message, block the UI temporarily
      // The disable/enable UI methods have to be implemented by you
      disableMessageSendingUi();

      new Handler(Looper.getMainLooper())
          .postDelayed(() -> enableMessageSendingUi(), cooldown);
    });
  }
});
```

### Automatic Feature Throttling

When a channel has more than 100 active watchers Stream Chat automatically toggles off some features. This is to avoid performance degradation for end-users. Processing large amount of events can potentially increase CPU and memory usage on mobile and web apps.

1. Read events and typing indicator events are discarded

2. Watcher start/stop events are only sent once every 5 seconds

### Message throttling

Message throttling that protects the client from message flooding. Chat clients will receive up to 5 messages per second and the API servers will allow small surges of messages to be delivered even if that means exceeding the 5 msg/s rate.

Here is an example of how message throttling works:

![](https://user-images.githubusercontent.com/88735/96602562-70938100-12f3-11eb-8379-cb316dc7969f.png)

In this example, the client will receive several more messages above the 5/s limit (the yellow bar), and once this burst credit is over, the client will stop receiving more than 5 messages per second. The burst credit is set to 10 messages on an 8 seconds rolling window.

> [!NOTE]
> If you are on an [Enterprise Plan](https://getstream.io/enterprise/), message throttling can be disabled or increased for your application by our support team
