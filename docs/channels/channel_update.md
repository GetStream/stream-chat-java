There are two ways to update a channel with the Stream API: partial updates and full updates. A partial update preserves existing custom key–value data, while a full update replaces the entire channel object and removes any fields not included in the request.

## Partial Update

A partial update lets you set or unset specific fields without affecting the rest of the channel’s custom data — essentially a patch-style update.

```java
// Android SDK

// Here's a channel with some custom field data that might be useful
ChannelClient channelClient = client.channel("messaging", "general");

List<String> members = Arrays.asList("thierry", "tommaso");

Map<String, String> channelDetail = new HashMap<>();
channelDetail.put("topic", "Plants and Animals");
channelDetail.put("rating", "pg");

Map<String, Integer> userId = new HashMap<>();
userId.put("user_id", 123);

Map<String, Object> extraData = new HashMap<>();
extraData.put("source", "user");
extraData.put("source_detail", userId);
extraData.put("channel_detail", channelDetail);

channelClient.create(members, extraData).execute();

// let's change the source of this channel
Map<String, Object> setField = Collections.singletonMap("source", "system");
channelClient.updatePartial(setField, emptyList()).execute();

// since it's system generated we no longer need source_detail
List<String> unsetField = Collections.singletonList("source_detail");
channelClient.updatePartial(emptyMap(), unsetField).execute();

// and finally update one of the nested fields in the channel_detail
Map<String, Object> setNestedField = Collections.singletonMap("channel_detail.topic", "Nature");
channelClient.updatePartial(setNestedField, emptyList()).execute();

// and maybe we decide we no longer need a rating
List<String> unsetNestedField = Collections.singletonList("channel_detail.rating");
channelClient.updatePartial(emptyMap(), unsetNestedField).execute();

// Backend SDK
Channel.partialUpdate("messaging", "general")
  .setValue("source", "system")
  .setValue("channel_detail.topic", "Nature")
  .user(user)
  .unsetValue("source_detail")
  .request()
  .getChannel();
```

## Full Update

The `update` function updates all of the channel data. **Any data that is present on the channel and not included in a full update will be deleted.**

```java
// Android SDK
ChannelClient channelClient = client.channel("messaging", "general");

Map<String, Object> channelData = new HashMap<>();
channelData.put("name", "myspecialchannel");
channelData.put("color", "green");
Message updateMessage = new Message();
updateMessage.setText("Thierry changed the channel color to green");

channelClient.update(updateMessage, channelData).enqueue(result -> {
  if (result.isSuccess()) {
    Channel channel = result.data();
  } else {
    // Handle result.error()
  }
 });


// Backend SDK
MessageRequestObject msg = MessageRequestObject
  .builder()
    .text("Thierry changed the channel color to green")
  .build();

Channel.update("messaging", "general")
  .message(msg)
  .data(ChannelRequestObject.builder()
   .additionalField("name", "myspecialchannel")
   .additionalField("color", "green")
   .build())
  .request();
```

### Request Params

| Name         | Type   | Description                                                                                                                                                                          | Optional |
| ------------ | ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | -------- |
| channel data | object | Object with the new channel information. One special field is "frozen". Setting this field to true will freeze the channel. Read more about freezing channels in "Freezing Channels" |          |
| text         | object | Message object allowing you to show a system message in the Channel that something changed.                                                                                          | Yes      |

> [!NOTE]
> Updating a channel using these methods cannot be used to add or remove members. For this, you must use specific methods for adding/removing members, more information can be found [here](/chat/docs/java/channel_members/).
