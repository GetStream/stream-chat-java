Freezing a channel prevents users from sending new messages and adding or deleting reactions.

Sending a message to a frozen channel returns an error message. Attempting to add or delete reactions returns a `403 Not Allowed` error.

User roles with the `UseFrozenChannel` permission can still use frozen channels normally. By default, no user role has this permission.

## Freeze a Channel

```java
// Android SDK
ChannelClient channelClient = client.channel("messaging", "general");
Map<String, Object> set = new HashMap<>();
set.put("freeze", true);
List<String> unset = new ArrayList<>();

channelClient.updatePartial(set, unset).enqueue(result -> {
  if (result.isSuccess()) {
    Channel channel = result.data();
  } else {
    // Handle result.error()
  }
});

// Backend SDK
ChannelRequestObject channelRequestObject = ChannelRequestObject.buildFrom(channel);
channelRequestObject.setFrozen(true);
Channel.update(channel.getType(), channel.getId())
  .data(channelRequestObject)
  .message(
    MessageRequestObject.builder()
      .text("Thierry has frozen the channel")
      .userId("Thierry")
      .build())
  .request();
```

## Unfreeze a Channel

```java
// Android SDK
ChannelClient channelClient = client.channel("messaging", "general");
Map<String, Object> set = new HashMap<>();
List<String> unset = new ArrayList<>();
unset.add("freeze");

channelClient.updatePartial(set, unset).enqueue(result -> {
  if (result.isSuccess()) {
    Channel channel = result.data();
  } else {
    // Handle result.error()
  }
});

// Backend SDK
ChannelRequestObject channelRequestObject = ChannelRequestObject.buildFrom(channel);
channelRequestObject.setFrozen(false);
Channel.update(channel.getType(), channel.getId())
  .data(channelRequestObject)
  .message(
    MessageRequestObject.builder()
      .text("Thierry has unfrozen the channel")
      .userId("Thierry")
      .build())
  .request();
```

## Granting the Frozen Channel Permission

Permissions are typically managed in the [Stream Dashboard](https://dashboard.getstream.io/) under your app's **Roles & Permissions** settings. This is the recommended approach for most use cases.

To grant permissions programmatically, update the channel type using a server-side API call. See [user permissions](/chat/docs/java/chat_permission_policies/) for more details.

```java
// Backend SDK
ChannelTypeGetResponse resp = ChannelType.get("messaging").request();
Map<String, List<String>> currentGrants = resp.getGrants();

List<String> adminGrants = currentGrants.get("admin");
adminGrants.add("use-frozen-channel");
currentGrants.put("admin", adminGrants);

ChannelType.update("messaging")
 .grants(currentGrants)
 .request();
```
