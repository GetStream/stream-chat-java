Disabling a channel is a visibility and access toggle. The channel and all its data remain intact, but client-side read and write operations return a `403 Not Allowed` error. Server-side access is preserved for admin operations like moderation and data export.

Disabled channels still appear in query results by default. This means users see the channel in their list but receive errors when attempting to open it. To hide disabled channels from users, filter them out in your queries:


Re-enabling a channel restores full client-side access with all historical messages intact.

## Disable a Channel

```java
// disable a channel with full update
Channel.update("<channel-type>", "<channel-id>")
  .data(ChannelRequestObject.builder().additionalField("disabled", true).build())
  .request();

// disable a channel with partial update
Channel.partialUpdate("<channel-type>", "<channel-id>")
  .setValue("disabled", true)
  .request();


// enable a channel with full update
Channel.update("<channel-type>", "<channel-id>")
  .data(ChannelRequestObject.builder().additionalField("disabled", false).build())
  .request();

// enable a channel with partial update
Channel.partialUpdate("<channel-type>", "<channel-id>")
  .setValue("disabled", false)
  .request();
```

> [!NOTE]
> To prevent new messages while still allowing users to read existing messages, use [freeze the channel](/chat/docs/java/freezing_channels/) instead.
