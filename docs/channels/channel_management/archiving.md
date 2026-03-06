Channel members can archive a channel for themselves. This is a per-user setting that does not affect other members.

Archived channels function identically to regular channels via the API, but your UI can display them separately. When a channel is archived, the timestamp is recorded and returned as `archived_at` in the response.

When querying channels, filter by `archived: true` to retrieve only archived channels, or `archived: false` to exclude them.

## Archive a Channel

```java
// Archive the channel for user amy.
Channel.archive(channel.getType(), channel.getId(), "amy").request();

// Query for amy's channels that are archived.
Channel.list()
    .userId("amy")
    .filterCondition(FilterCondition.in("members", "amy"))
    .filterCondition("archived", true)
    .request();

// Unarchive
Channel.unarchive(channel.getType(), channel.getId(), "amy").request();
```

## Global Archiving

Channels are archived for a specific member. If the channel should instead be archived for all users, this can be stored as custom data in the channel itself. The value cannot collide with existing fields, so use a value such as `globally_archived: true`.
