package io.getstream.chat.java;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.UnreadCounts;
import io.getstream.chat.java.models.UnreadCounts.UnreadCountsBatchResponse;
import io.getstream.chat.java.models.UnreadCounts.UnreadCountsChannel;
import io.getstream.chat.java.models.UnreadCounts.UnreadCountsResponse;
import io.getstream.chat.java.models.UnreadCounts.UnreadCountsThread;
import io.getstream.chat.java.models.User.UserRequestObject;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UnreadCountsTest extends BasicTest {
  @DisplayName("UnreadCounts get does not throw Exception")
  @Test
  void whenCallingGetUnreadCounts_thenNoException() {
    String firstUserId = testUserRequestObject.getId();
    String secondUserId = testUsersRequestObjects.get(1).getId();

    UnreadCountsResponse r =
        Assertions.assertDoesNotThrow(() -> UnreadCounts.get(secondUserId).request());
    var totalAtStart = r.getTotalUnreadCount();

    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    String text = "Hello @" + secondUserId;

    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(firstUserId).build();
    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(channel.getType(), channel.getId())
                        .message(messageRequest)
                        .request())
            .getMessage();

    r = Assertions.assertDoesNotThrow(() -> UnreadCounts.get(secondUserId).request());
    Assertions.assertEquals(totalAtStart + 1, r.getTotalUnreadCount());
    List<UnreadCountsChannel> unreadChannels = r.getChannels();
    Assertions.assertNotEquals(0, unreadChannels.size());

    UnreadCountsChannel unreadChannel = null;
    for (UnreadCountsChannel uc : unreadChannels) {
      if (uc.getChannelId().equals(channel.getCId())) {
        unreadChannel = uc;
        break;
      }
    }
    Assertions.assertNotNull(unreadChannel);
    Assertions.assertEquals(1, unreadChannel.getUnreadCount());

    // threads
    Assertions.assertDoesNotThrow(
        () ->
            Message.send(channel.getType(), channel.getId())
                .message(
                    MessageRequestObject.builder()
                        .text("second user replies to the message")
                        .userId(secondUserId)
                        .parentId(message.getId())
                        .build())
                .request());

    Assertions.assertDoesNotThrow(
        () ->
            Message.send(channel.getType(), channel.getId())
                .message(
                    MessageRequestObject.builder()
                        .text("first user also replies")
                        .userId(firstUserId)
                        .parentId(message.getId())
                        .build())
                .request());

    r = Assertions.assertDoesNotThrow(() -> UnreadCounts.get(secondUserId).request());

    Assertions.assertEquals(1, r.getTotalUnreadThreadsCount());
    List<UnreadCountsThread> unreadThreads = r.getThreads();
    Assertions.assertEquals(1, unreadThreads.size());
    Assertions.assertEquals(message.getId(), unreadThreads.get(0).getParentMessageId());
  }

  @DisplayName("UnreadCounts batch works")
  @Test
  void whenCallingUnreadCountsBatch_thenNoException() {
    String firstUserId = testUserRequestObject.getId();

    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    String text = "Hello world!";

    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(firstUserId).build();
    Message message =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(channel.getType(), channel.getId())
                        .message(messageRequest)
                        .request())
            .getMessage();

    UnreadCountsBatchResponse r =
        Assertions.assertDoesNotThrow(
            () ->
                UnreadCounts.batch()
                    .userIds(testUsersRequestObjects.stream().map(tu -> tu.getId()).toList())
                    .request());

    var countsByUser = r.getCountsByUser();
    for (UserRequestObject u : testUsersRequestObjects) {
      var userId = u.getId();
      Assertions.assertTrue(countsByUser.keySet().contains(userId));
      if (userId.equals(firstUserId)) {
        Assertions.assertEquals(0, countsByUser.get(userId).getTotalUnreadCount());
        continue;
      }
      Assertions.assertEquals(2, countsByUser.get(userId).getTotalUnreadCount());

      // send this message to add user to the thread
      Assertions.assertDoesNotThrow(
          () ->
              Message.send(channel.getType(), channel.getId())
                  .message(
                      MessageRequestObject.builder()
                          .text("second user replies to the message")
                          .userId(userId)
                          .parentId(message.getId())
                          .build())
                  .request());
    }

    Assertions.assertDoesNotThrow(
        () ->
            Message.send(channel.getType(), channel.getId())
                .message(
                    MessageRequestObject.builder()
                        .text("first user also replies")
                        .userId(firstUserId)
                        .parentId(message.getId())
                        .build())
                .request());

    r =
        Assertions.assertDoesNotThrow(
            () ->
                UnreadCounts.batch()
                    .userIds(testUsersRequestObjects.stream().map(tu -> tu.getId()).toList())
                    .request());

    countsByUser = r.getCountsByUser();
    for (UserRequestObject u : testUsersRequestObjects) {
      var userId = u.getId();
      Assertions.assertTrue(countsByUser.keySet().contains(userId));
      if (userId.equals(firstUserId)) {
        Assertions.assertEquals(1, countsByUser.get(userId).getTotalUnreadThreadsCount());
        continue;
      }
      Assertions.assertEquals(1, countsByUser.get(userId).getTotalUnreadThreadsCount());
    }
  }
}
