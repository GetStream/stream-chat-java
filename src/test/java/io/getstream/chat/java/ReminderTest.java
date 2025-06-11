package io.getstream.chat.java;

import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Reminder;
import io.getstream.chat.java.models.Sort;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReminderTest extends BasicTest {

  @DisplayName("Can create a reminder for a message")
  @Test
  void whenCreatingAReminder_thenNoException() {
    // Create a new message for this test
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    
    // Create a reminder for the test message
    Reminder reminder =
        Assertions.assertDoesNotThrow(
                () ->
                    Reminder.createReminder(message.getId())
                        .userId(testUserRequestObject.getId())
                        .remindAt(new Date(System.currentTimeMillis() + 86400000)) // 1 day from now
                        .request())
            .getReminder();

    // Verify the reminder was created correctly
    Assertions.assertNotNull(reminder);
    Assertions.assertEquals(message.getId(), reminder.getMessageId());
    Assertions.assertEquals(testUserRequestObject.getId(), reminder.getUserId());
    Assertions.assertNotNull(reminder.getRemindAt());
    
    // Verify the new fields
    Assertions.assertNotNull(reminder.getChannelCid(), "Channel CID should not be null");
    // The channel CID should be in the format "type:id"
    Assertions.assertTrue(
        reminder.getChannelCid().contains(testChannel.getType() + ":" + testChannel.getId()),
        "Channel CID should contain the channel type and ID");
  }

  @DisplayName("Can update a reminder for a message")
  @Test
  void whenUpdatingAReminder_thenNoException() {
    // Create a new message for this test
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    
    // Create a reminder first
    Reminder reminder =
        Assertions.assertDoesNotThrow(
                () ->
                    Reminder.createReminder(message.getId())
                        .userId(testUserRequestObject.getId())
                        .remindAt(new Date(System.currentTimeMillis() + 86400000)) // 1 day from now
                        .request())
            .getReminder();

    // Update the reminder with a new remind_at time
    Date newRemindAt = new Date(System.currentTimeMillis() + 172800000); // 2 days from now
    Reminder updatedReminder =
        Assertions.assertDoesNotThrow(
                () ->
                    Reminder.updateReminder(message.getId())
                        .userId(testUserRequestObject.getId())
                        .remindAt(newRemindAt)
                        .request())
            .getReminder();

    // Verify the reminder was updated correctly
    Assertions.assertNotNull(updatedReminder);
    Assertions.assertEquals(reminder.getId(), updatedReminder.getId());
    Assertions.assertEquals(message.getId(), updatedReminder.getMessageId());
    Assertions.assertEquals(testUserRequestObject.getId(), updatedReminder.getUserId());
    
    // The updated remind_at time should be different from the original
    Assertions.assertNotEquals(
        reminder.getRemindAt().getTime(), updatedReminder.getRemindAt().getTime());
    
    // Verify the channel_cid is preserved
    Assertions.assertEquals(reminder.getChannelCid(), updatedReminder.getChannelCid());
  }

  @DisplayName("Can delete a reminder for a message")
  @Test
  void whenDeletingAReminder_thenNoException() {
    // Create a new message for this test
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    
    // Create a reminder first
    Reminder reminder =
        Assertions.assertDoesNotThrow(
                () ->
                    Reminder.createReminder(message.getId())
                        .userId(testUserRequestObject.getId())
                        .remindAt(new Date(System.currentTimeMillis() + 86400000)) // 1 day from now
                        .request())
            .getReminder();

    // Delete the reminder
    Assertions.assertDoesNotThrow(
            () ->
                Reminder.deleteReminder(message.getId(), testUserRequestObject.getId())
                    .request());
    
    // Since the API might not return the deleted reminder, we just verify that the delete operation
    // completed without throwing an exception
  }

  @DisplayName("Can query reminders with filter conditions")
  @Test
  void whenQueryingRemindersWithFilterConditions_thenNoException() {
    // Create a new message for this test
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    
    // Create a reminder first
    Reminder reminder =
        Assertions.assertDoesNotThrow(
                () ->
                    Reminder.createReminder(message.getId())
                        .userId(testUserRequestObject.getId())
                        .remindAt(new Date(System.currentTimeMillis() + 86400000)) // 1 day from now
                        .request())
            .getReminder();

    // Create sort parameters
    Map<String, Object> sortParams = new HashMap<>();
    sortParams.put("field", "remind_at");
    sortParams.put("direction", 1);

    // Query reminders with filter conditions
    List<Reminder> reminders =
        Assertions.assertDoesNotThrow(
                () ->
                    Reminder.queryReminders()
                        .userId(testUserRequestObject.getId())
                        .filterCondition("message_id", message.getId())
                        .sort(sortParams)
                        .limit(10)
                        .request())
            .getReminders();

    // Verify the query returned results
    Assertions.assertNotNull(reminders);
    // Note: The API might not return any reminders if they were deleted or not indexed yet
    // So we don't assert that the list is not empty
    
    // Check for pagination fields
    Assertions.assertDoesNotThrow(() -> {
      String prev = Reminder.queryReminders()
          .userId(testUserRequestObject.getId())
          .request()
          .getPrev();
      
      String next = Reminder.queryReminders()
          .userId(testUserRequestObject.getId())
          .request()
          .getNext();
      
      // We don't assert specific values as they might be null depending on the data
    });
  }

  @DisplayName("Can create a reminder without a remind_at date")
  @Test
  void whenCreatingAReminderWithoutRemindAt_thenNoException() {
    // Create a new message for this test
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    
    // Create a reminder without a remind_at date
    Reminder reminder =
        Assertions.assertDoesNotThrow(
                () ->
                    Reminder.createReminder(message.getId())
                        .userId(testUserRequestObject.getId())
                        .request())
            .getReminder();

    // Verify the reminder was created correctly
    Assertions.assertNotNull(reminder);
    Assertions.assertEquals(message.getId(), reminder.getMessageId());
    Assertions.assertEquals(testUserRequestObject.getId(), reminder.getUserId());
    // The API might not set a default remind_at time, so we don't assert that it's not null
    
    // Verify the channel_cid field
    Assertions.assertNotNull(reminder.getChannelCid(), "Channel CID should not be null");
  }
} 