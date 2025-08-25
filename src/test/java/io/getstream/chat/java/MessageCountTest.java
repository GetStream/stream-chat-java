package io.getstream.chat.java;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.ChannelGetResponse;
import io.getstream.chat.java.models.Channel.ChannelRequestObject;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MessageCountTest extends BasicTest {

  @DisplayName("Message count is present when feature enabled")
  @Test
  void whenCountMessagesEnabled_thenMessagesCount() {
    // Create a fresh channel with default configuration (count_messages enabled by default)
    ChannelGetResponse channelGetResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.getOrCreate("team", RandomStringUtils.randomAlphabetic(12))
                    .data(
                        ChannelRequestObject.builder()
                            .createdBy(testUserRequestObject)
                            .members(buildChannelMembersList())
                            .build())
                    .request());

    String type = channelGetResponse.getChannel().getType();
    String id = channelGetResponse.getChannel().getId();

    // Post a message so the channel has at least one message
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text("test message")
            .userId(testUserRequestObject.getId())
            .build();
    Assertions.assertDoesNotThrow(() -> Message.send(type, id).message(messageRequest).request());

    // Retrieve the channel again to inspect server response
    ChannelGetResponse refreshed =
        Assertions.assertDoesNotThrow(() -> Channel.getOrCreate(type, id).request());

    Assertions.assertTrue(
        refreshed.getChannel().getMessageCount() == 1,
        "messages_count should be 1 when count_messages is enabled");
  }

  @DisplayName("Message count is not returned when feature disabled")
  @Test
  void whenCountMessagesDisabled_thenNoMessageCountPresent() {
    // Create a fresh channel first
    ChannelGetResponse channelGetResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.getOrCreate("team", RandomStringUtils.randomAlphabetic(12))
                    .data(
                        ChannelRequestObject.builder()
                            .createdBy(testUserRequestObject)
                            .members(buildChannelMembersList())
                            .build())
                    .request());

    String type = channelGetResponse.getChannel().getType();
    String id = channelGetResponse.getChannel().getId();

    // Disable count_messages via partial update (config_overrides)
    Map<String, Object> overrides = new HashMap<>();
    overrides.put("count_messages", false);
    Assertions.assertDoesNotThrow(
        () -> Channel.partialUpdate(type, id).setValue("config_overrides", overrides).request());


    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text("test message")
            .userId(testUserRequestObject.getId())
            .build();
    Assertions.assertDoesNotThrow(() -> Message.send(type, id).message(messageRequest).request());

    // Retrieve the channel again
    ChannelGetResponse refreshed =
        Assertions.assertDoesNotThrow(() -> Channel.getOrCreate(type, id).request());

    Integer messagesCount = refreshed.getChannel().getMessageCount();
    Assertions.assertNull(messagesCount, "messages_count should not be present when count_messages is disabled");
  }

}
