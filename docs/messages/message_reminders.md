Message reminders let users schedule notifications for specific messages, making it easier to follow up later. When a reminder includes a timestamp, it's like saying "remind me later about this message," and the user who set it will receive a notification at the designated time. If no timestamp is provided, the reminder functions more like a bookmark, allowing the user to save the message for later reference.

Reminders require Push V3 to be enabled - see details [here](/chat/docs/java/push_template/)

## Enabling Reminders

The Message Reminders feature must be activated at the channel level before it can be used. You have two configuration options: activate it for a single channel using configuration overrides, or enable it globally for all channels of a particular type.

```java
// Backend SDK

// Enabling it for a channel type
ChannelType.update("messaging")
    .userMessageReminders(true)
    .request();
```

Message reminders allow users to:

- schedule a notification after given amount of time has elapsed
- bookmark a message without specifying a deadline

## Limits

- A user cannot have more than 250 reminders scheduled
- A user can only have one reminder created per message

## Creating a Message Reminder

You can create a reminder for any message. When creating a reminder, you can specify a reminder time or save it for later without a specific time.

```java
// Backend SDK

// Create a reminder with a specific due date
Calendar cal = Calendar.getInstance();
cal.add(Calendar.HOUR, 1);
Date remindAt = cal.getTime();

MessageReminder reminder = MessageReminder.create("message-id", "user-id", remindAt).request();

// Create a "Save for later" reminder without a specific time
MessageReminder reminder = MessageReminder.create("message-id", "user-id").request();
```

## Updating a Message Reminder

You can update an existing reminder for a message to change the reminder time.

```java
// Backend SDK

// Update a reminder with a new due date
Calendar cal = Calendar.getInstance();
cal.add(Calendar.HOUR, 2);
Date remindAt = cal.getTime();

MessageReminder updatedReminder = MessageReminder.update("message-id", "user-id", remindAt).request();

// Convert a timed reminder to "Save for later"
MessageReminder updatedReminder = MessageReminder.update("message-id", "user-id", null).request();
```

## Deleting a Message Reminder

You can delete a reminder for a message when it's no longer needed.

```java
// Backend SDK

// Delete the reminder for the message
MessageReminder.delete("message-id", "user-id").request();
```

## Querying Message Reminders

The SDK allows you to fetch all reminders of the current user. You can filter, sort, and paginate through all the user's reminders.

```java
// Backend SDK

// Query reminders for a user
List<MessageReminder> reminders = MessageReminder.list("user-id").request();

// Query reminders with filters
Map<String, Object> filter = new HashMap<>();
filter.put("channel_cid", "messaging:general");
List<MessageReminder> reminders = MessageReminder.list("user-id").filter(filter).request();
```

### Filtering Reminders

You can filter the reminders based on different criteria:

- `message_id` - Filter by the message that the reminder is created on.
- `remind_at` - Filter by the reminder time.
- `created_at` - Filter by the creation date.
- `channel_cid` - Filter by the channel ID.

The most common use case would be to filter by the reminder time. Like filtering overdue reminders, upcoming reminders, or reminders with no due date (saved for later).

```java
// Backend SDK
import java.util.Date;

// Filter overdue reminders
Map<String, Object> overdueFilter = new HashMap<>();
overdueFilter.put("remind_at", Map.of("$lt", new Date()));
List<MessageReminder> overdueReminders = MessageReminder.list("user-id").filter(overdueFilter).request();

// Filter upcoming reminders
Map<String, Object> upcomingFilter = new HashMap<>();
upcomingFilter.put("remind_at", Map.of("$gt", new Date()));
List<MessageReminder> upcomingReminders = MessageReminder.list("user-id").filter(upcomingFilter).request();

// Filter reminders with no due date (saved for later)
Map<String, Object> savedFilter = new HashMap<>();
savedFilter.put("remind_at", null);
List<MessageReminder> savedReminders = MessageReminder.list("user-id").filter(savedFilter).request();
```

### Pagination

If you have many reminders, you can paginate the results.

```java
// Backend SDK

// Load reminders with pagination
List<MessageReminder> reminders = MessageReminder.list("user-id")
    .limit(10)
    .offset(0)
    .request();

// Load next page
List<MessageReminder> nextReminders = MessageReminder.list("user-id")
    .limit(10)
    .offset(10)
    .request();
```

## Events

The following WebSocket events are available for message reminders:

- `reminder.created` - Triggered when a reminder is created
- `reminder.updated` - Triggered when a reminder is updated
- `reminder.deleted` - Triggered when a reminder is deleted
- `notification.reminder_due` - Triggered when a reminder's due time is reached

When a reminder's due time is reached, the server also sends a push notification to the user. Ensure push notifications are configured in your app.


## Webhooks

The same events are available as webhooks to notify your backend systems:

- `reminder.created`
- `reminder.updated`
- `reminder.deleted`
- `notification.reminder_due`

These webhook events contain the same payload structure as their WebSocket counterparts. For more information on configuring webhooks, see the [Webhooks documentation](/chat/docs/java/webhook_events/).
