Draft messages allow users to save messages as drafts for later use. This feature is useful when users want to compose a message but aren't ready to send it yet.

## Creating a draft message

It is possible to create a draft message for a channel or a thread. Only one draft per channel/thread can exist at a time, so a newly created draft overrides the existing one.

```java
// Create/update a draft message in a channel
Draft.CreateDraftResponse response = Draft.createDraft(channelType, channelId)
    .message(MessageRequestObject.builder()
        .text("This is a draft message")
        .userId(userId)
        .build())
    .userId(userId)
    .request();

// Create/update a draft message in a thread (parent message)
Draft.CreateDraftResponse threadResponse = Draft.createDraft(channelType, channelId)
    .message(MessageRequestObject.builder()
        .text("This is a draft message")
        .parentId(parentMessageId)
        .userId(userId)
        .build())
    .userId(userId)
    .request();
```

## Deleting a draft message

You can delete a draft message for a channel or a thread as well.

```java
// Delete the draft message for a channel
Draft.deleteDraft(channelType, channelId)
    .userId(userId)
    .request();

// Delete the draft message for a thread
Draft.deleteDraft(channelType, channelId)
    .userId(userId)
    .parentId(parentMessageId)
    .request();
```

## Loading a draft message

It is also possible to load a draft message for a channel or a thread. Although, when querying channels, each channel will contain the draft message payload, in case there is one. The same for threads (parent messages). So, for the most part this function will not be needed.

```java
// Load the draft message for a channel
Draft.GetDraftResponse draftResponse = Draft.getDraft(channelType, channelId)
    .userId(userId)
    .request();

// Load the draft message for a thread
Draft.GetDraftResponse threadDraftResponse = Draft.getDraft(channelType, channelId)
    .userId(userId)
    .parentId(parentMessageId)
    .request();
```

## Querying draft messages

The Stream Chat SDK provides a way to fetch all the draft messages for the current user. This can be useful to for the current user to manage all the drafts they have in one place.

```java
// Query all user drafts
Draft.QueryDraftsResponse queryResponse = Draft.queryDrafts()
    .userId(userId)
    .limit(10)
    .request();

// Query drafts for certain channels and sort
Draft.QueryDraftsResponse filteredResponse = Draft.queryDrafts()
    .userId(userId)
    .filter(FilterCondition.in("channel_cid", "messaging:channel-1", "messaging:channel-2"))
    .sort(Sort.builder().field("created_at").direction(Sort.Direction.DESC).build())
    .request();
```

Filtering is possible on the following fields:

| Name        | Type                       | Description                    | Supported operations      | Example                                                |
| ----------- | -------------------------- | ------------------------------ | ------------------------- | ------------------------------------------------------ |
| channel_cid | string                     | the ID of the message          | $in, $eq                  | { channel_cid: { $in: [ 'channel-1', 'channel-2' ] } } |
| parent_id   | string                     | the ID of the parent message   | $in, $eq, $exists         | { parent_id: 'parent-message-id' }                     |
| created_at  | string (RFC3339 timestamp) | the time the draft was created | $eq, $gt, $lt, $gte, $lte | { created_at: { $gt: '2024-04-24T15:50:00.00Z' }       |

Sorting is possible on the `created_at` field. By default, draft messages are returned with the newest first.

### Pagination

In case the user has a lot of draft messages, you can paginate the results.

```java
// Query drafts with a limit
Draft.QueryDraftsResponse firstPage = Draft.queryDrafts()
    .userId(userId)
    .limit(5)
    .request();

// Query the next page
Draft.QueryDraftsResponse secondPage = Draft.queryDrafts()
    .userId(userId)
    .limit(5)
    .next(firstPage.getNext())
    .request();
```

## Events

The following WebSocket events are available for draft messages:

- `draft.updated`, triggered when a draft message is updated.
- `draft.deleted`, triggered when a draft message is deleted.

You can subscribe to these events using the Stream Chat SDK.
