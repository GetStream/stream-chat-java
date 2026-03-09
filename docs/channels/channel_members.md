Channel members are users who have been added to a channel and can participate in conversations. This page covers how to manage channel membership, including adding and removing members, controlling message history visibility, and managing member roles.

## Adding and Removing Members

### Adding Members

Using the `addMembers()` method adds the given users as members to a channel.

```java
// Android SDK
ChannelClient channelClient = client.channel("messaging", "general");

// Add members with ids "thierry" and "josh"
channelClient.addMembers(Arrays.asList("thierry", "josh"), null).enqueue(result -> {
  if (result.isSuccess()) {
    Channel channel = result.data();
  } else {
    // Handle result.error()
  }
});

// Backend SDK
Channel.update("messaging", "general")
  .addMembers(Arrays.asList("thierry", "josh"))
  .request();
```

> [!NOTE]
> **Note:** You can only add/remove up to 100 members at once.


Members can also be added when creating a channel:


### Removing Members

Using the `removeMembers()` method removes the given users from the channel.

```java
// Android SDK
channelClient.removeMembers(Arrays.asList("tommaso"), null).enqueue(result -> {
  if (result.isSuccess()) {
    Channel channel = result.data();
  } else {
    // Handle result.error()
  }
});

// Backend SDK
Channel.update("messaging", "general")
  .removeMember("tommaso")
  .request();
```

### Leaving a Channel

Users can leave a channel without moderator-level permissions. Ensure channel members have the `Leave Own Channel` permission enabled.


> [!NOTE]
> You can familiarize yourself with all permissions in the [Permissions section](/chat/docs/java/chat_permission_policies/).


## Hide History

When members join a channel, you can specify whether they have access to the channel's message history. By default, new members can see the history. Set `hide_history` to `true` to hide it for new members.

```java
// Backend SDK
Channel.update("channel-type", "channel-type")
  .addMember("thierry")
  .hideHistory(true)
  .request();
```

### Hide History Before a Specific Date

Alternatively, `hide_history_before` can be used to hide any history before a given timestamp while giving members access to later messages. The value must be a timestamp in the past in RFC 3339 format. If both parameters are defined, `hide_history_before` takes precedence over `hide_history`.

```java
// Backend SDK
Calendar calendar = Calendar.getInstance();
calendar.add(Calendar.DAY_OF_MONTH, -7);
Date cutoff = calendar.getTime();

Channel.update("channel-type", "channel-id")
  .addMember("thierry")
  .hideHistoryBefore(cutoff)
  .request();
```

## System Message Parameter

You can optionally include a message object when adding or removing members that client-side SDKs will use to display a system message. This works for both adding and removing members.

```java
// Android SDK
Message addMemberSystemMessage = new Message();
addMemberSystemMessage.setText("Thierry and Josh were added to this channel");
channelClient.addMembers(Arrays.asList("thierry", "josh"), addMemberSystemMessage).enqueue(result -> {
  if (result.isSuccess()) {
    Channel channel = result.data();
  } else {
    // Handle result.error()
  }
});

// Backend SDK
MessageRequestObject msg = MessageRequestObject
  .builder()
    .text("Thierry and Josh were added to this channel")
  .build();
Channel.update("messaging", "general")
  .addMembers(Arrays.asList("thierry", "josh"))
  .message(msg)
  .request();
```

## Adding and Removing Moderators

Using the `addModerators()` method adds the given users as moderators (or updates their role to moderator if already members), while `demoteModerators()` removes the moderator status.

### Add Moderators

```java
// Backend SDK
Channel.update("channel-type", "channel-type").addModerator("thierry").addModerator("josh").request();
```

### Remove Moderators

```java
// Backend SDK
Channel.update("channel-type", "channel-type").demoteModerator("tommaso").request();
```

> [!NOTE]
> These operations can only be performed server-side, and a maximum of 100 moderators can be added or removed at once.


## Member Custom Data

Custom data can be added at the channel member level. This is useful for storing member-specific information that is separate from user-level data. Ensure custom data does not exceed 5KB.

### Adding Custom Data


### Updating Member Data

Channel members can be partially updated. Only custom data and channel roles are eligible for modification. You can set or unset fields, either separately or in the same call.

```java
// Set some fields
Channel.updateMemberPartial(channel.getType(), channel.getId(), "user-1")
    .setValue("custom_key", "custom_value")
    .setValue("channel_role", "channel_moderator")
    .request();

// Unset some fields
Channel.updateMemberPartial(channel.getType(), channel.getId(), "user-1")
    .unsetValue("custom_key")
    .request();

// Set and unset in the same call
Channel.updateMemberPartial(channel.getType(), channel.getId(), "user-1")
    .setValue("color", "red")
    .unsetValue("age")
    .request();
```
