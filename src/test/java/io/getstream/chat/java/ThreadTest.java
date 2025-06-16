package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.ChannelMemberRequestObject;
import io.getstream.chat.java.models.Channel.ChannelRequestObject;
import io.getstream.chat.java.models.FilterCondition;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Sort;
import io.getstream.chat.java.models.Thread;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for the thread functionality. */
public class ThreadTest extends BasicTest {

  @DisplayName("Can query threads with filter parameters")
  @Test
  void whenQueryingThreadsWithFilter_thenNoException() throws StreamException {
    // Create a channel with a thread
    String channelId = UUID.randomUUID().toString();
    Channel.ChannelGetResponse channel =
        Channel.getOrCreate("messaging", channelId)
            .data(
                ChannelRequestObject.builder()
                    .createdBy(testUserRequestObject)
                    .members(
                        List.of(
                            ChannelMemberRequestObject.builder()
                                .user(testUserRequestObject)
                                .build()))
                    .build())
            .request();

    // Create a parent message for the thread
    Message.MessageRequestObject parentMessage =
        Message.MessageRequestObject.builder()
            .text("Parent message for thread")
            .userId(testUserRequestObject.getId())
            .build();

    Message.MessageSendResponse parentMessageResponse =
        Message.send(channel.getChannel().getType(), channel.getChannel().getId())
            .message(parentMessage)
            .request();

    // Create a reply to the parent message to form a thread
    Message.MessageRequestObject replyMessage =
        Message.MessageRequestObject.builder()
            .text("Reply in thread")
            .userId(testUserRequestObject.getId())
            .parentId(parentMessageResponse.getMessage().getId())
            .build();

    Message.send(channel.getChannel().getType(), channel.getChannel().getId())
        .message(replyMessage)
        .request();

    // Query threads with filter for the specific channel
    Thread.QueryThreadsResponse response =
        Thread.queryThreads()
            .userId(testUserRequestObject.getId())
            .filter(FilterCondition.eq("channel_cid", channel.getChannel().getCId()))
            .request();

    // Verify the response
    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getThreads());
    Assertions.assertEquals(1, response.getThreads().size());
    Assertions.assertEquals(
        channel.getChannel().getCId(), response.getThreads().get(0).getChannelCid());
    Assertions.assertEquals(
        parentMessageResponse.getMessage().getId(),
        response.getThreads().get(0).getParentMessageId());
  }

  @DisplayName("Can query threads with sort parameters")
  @Test
  void whenQueryingThreadsWithSort_thenNoException() throws StreamException {
    // Create two channels with threads
    String channel1Id = UUID.randomUUID().toString();
    Channel.ChannelGetResponse channel1 =
        Channel.getOrCreate("messaging", channel1Id)
            .data(
                ChannelRequestObject.builder()
                    .createdBy(testUserRequestObject)
                    .members(
                        List.of(
                            ChannelMemberRequestObject.builder()
                                .user(testUserRequestObject)
                                .build()))
                    .build())
            .request();

    // Create a parent message for the first thread
    Message.MessageRequestObject parentMessage1 =
        Message.MessageRequestObject.builder()
            .text("Parent message for thread 1")
            .userId(testUserRequestObject.getId())
            .build();

    Message.MessageSendResponse parentMessageResponse1 =
        Message.send(channel1.getChannel().getType(), channel1.getChannel().getId())
            .message(parentMessage1)
            .request();

    // Create a reply to the parent message to form a thread
    Message.MessageRequestObject replyMessage1 =
        Message.MessageRequestObject.builder()
            .text("Reply in thread 1")
            .userId(testUserRequestObject.getId())
            .parentId(parentMessageResponse1.getMessage().getId())
            .build();

    Message.send(channel1.getChannel().getType(), channel1.getChannel().getId())
        .message(replyMessage1)
        .request();

    // Create a second channel with a thread
    String channel2Id = UUID.randomUUID().toString();
    Channel.ChannelGetResponse channel2 =
        Channel.getOrCreate("messaging", channel2Id)
            .data(
                ChannelRequestObject.builder()
                    .createdBy(testUserRequestObject)
                    .members(
                        List.of(
                            ChannelMemberRequestObject.builder()
                                .user(testUserRequestObject)
                                .build()))
                    .build())
            .request();

    // Create a parent message for the second thread
    Message.MessageRequestObject parentMessage2 =
        Message.MessageRequestObject.builder()
            .text("Parent message for thread 2")
            .userId(testUserRequestObject.getId())
            .build();

    Message.MessageSendResponse parentMessageResponse2 =
        Message.send(channel2.getChannel().getType(), channel2.getChannel().getId())
            .message(parentMessage2)
            .request();

    // Create a reply to the parent message to form a thread
    Message.MessageRequestObject replyMessage2 =
        Message.MessageRequestObject.builder()
            .text("Reply in thread 2")
            .userId(testUserRequestObject.getId())
            .parentId(parentMessageResponse2.getMessage().getId())
            .build();

    Message.send(channel2.getChannel().getType(), channel2.getChannel().getId())
        .message(replyMessage2)
        .request();

    // Query threads with sort by created_at in descending order
    Thread.QueryThreadsResponse response =
        Thread.queryThreads()
            .userId(testUserRequestObject.getId())
            .sort(Sort.builder().field("created_at").direction(Sort.Direction.DESC).build())
            .request();

    // Verify the response
    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getThreads());
    Assertions.assertEquals(2, response.getThreads().size());

    // The second thread should be first in the list (most recent)
    Assertions.assertEquals(
        channel2.getChannel().getCId(), response.getThreads().get(0).getChannelCid());
    Assertions.assertEquals(
        parentMessageResponse2.getMessage().getId(),
        response.getThreads().get(0).getParentMessageId());

    // The first thread should be second in the list
    Assertions.assertEquals(
        channel1.getChannel().getCId(), response.getThreads().get(1).getChannelCid());
    Assertions.assertEquals(
        parentMessageResponse1.getMessage().getId(),
        response.getThreads().get(1).getParentMessageId());
  }

  @DisplayName("Can query threads with both filter and sort parameters")
  @Test
  void whenQueryingThreadsWithFilterAndSort_thenNoException() throws StreamException {
    // Create a channel with multiple threads
    String channelId = UUID.randomUUID().toString();
    Channel.ChannelGetResponse channel =
        Channel.getOrCreate("messaging", channelId)
            .data(
                ChannelRequestObject.builder()
                    .createdBy(testUserRequestObject)
                    .members(
                        List.of(
                            ChannelMemberRequestObject.builder()
                                .user(testUserRequestObject)
                                .build()))
                    .build())
            .request();

    // Create a parent message for the first thread
    Message.MessageRequestObject parentMessage1 =
        Message.MessageRequestObject.builder()
            .text("Parent message for thread 1")
            .userId(testUserRequestObject.getId())
            .build();

    Message.MessageSendResponse parentMessageResponse1 =
        Message.send(channel.getChannel().getType(), channel.getChannel().getId())
            .message(parentMessage1)
            .request();

    // Create a reply to the parent message to form a thread
    Message.MessageRequestObject replyMessage1 =
        Message.MessageRequestObject.builder()
            .text("Reply in thread 1")
            .userId(testUserRequestObject.getId())
            .parentId(parentMessageResponse1.getMessage().getId())
            .build();

    Message.send(channel.getChannel().getType(), channel.getChannel().getId())
        .message(replyMessage1)
        .request();

    // Create a parent message for the second thread
    Message.MessageRequestObject parentMessage2 =
        Message.MessageRequestObject.builder()
            .text("Parent message for thread 2")
            .userId(testUserRequestObject.getId())
            .build();

    Message.MessageSendResponse parentMessageResponse2 =
        Message.send(channel.getChannel().getType(), channel.getChannel().getId())
            .message(parentMessage2)
            .request();

    // Create a reply to the parent message to form a thread
    Message.MessageRequestObject replyMessage2 =
        Message.MessageRequestObject.builder()
            .text("Reply in thread 2")
            .userId(testUserRequestObject.getId())
            .parentId(parentMessageResponse2.getMessage().getId())
            .build();

    Message.send(channel.getChannel().getType(), channel.getChannel().getId())
        .message(replyMessage2)
        .request();

    // Query threads with filter for the specific channel and sort by created_at in descending order
    Thread.QueryThreadsResponse response =
        Thread.queryThreads()
            .userId(testUserRequestObject.getId())
            .filter(FilterCondition.eq("channel_cid", channel.getChannel().getCId()))
            .sort(Sort.builder().field("created_at").direction(Sort.Direction.DESC).build())
            .request();

    // Verify the response
    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getThreads());
    Assertions.assertEquals(2, response.getThreads().size());

    // The second thread should be first in the list (most recent)
    Assertions.assertEquals(
        channel.getChannel().getCId(), response.getThreads().get(0).getChannelCid());
    Assertions.assertEquals(
        parentMessageResponse2.getMessage().getId(),
        response.getThreads().get(0).getParentMessageId());

    // The first thread should be second in the list
    Assertions.assertEquals(
        channel.getChannel().getCId(), response.getThreads().get(1).getChannelCid());
    Assertions.assertEquals(
        parentMessageResponse1.getMessage().getId(),
        response.getThreads().get(1).getParentMessageId());
  }
}
