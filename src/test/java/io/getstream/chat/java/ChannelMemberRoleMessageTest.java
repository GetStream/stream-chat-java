package io.getstream.chat.java;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.*;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Role;
import io.getstream.chat.java.models.User.UserRequestObject;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChannelMemberRoleMessageTest extends BasicTest {

  @DisplayName("Messages include channel member role")
  @Test
  void whenSendingMessages_thenMemberRoleIsIncluded() {
    // Create a unique custom role
    String customRole = "custom_role_" + RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(() -> Role.create().name(customRole).request());
    // Wait for the role to be fully propagated
    pause();

    // Select two different users from the pre-created user list
    UserRequestObject userWithRole = testUsersRequestObjects.get(0);
    UserRequestObject userWithoutRole = testUsersRequestObjects.get(1);

    // Build channel members – assign the custom role to the first user only
    var memberWithRole =
        ChannelMemberRequestObject.builder().user(userWithRole).role(customRole).build();
    var memberWithoutRole =
        ChannelMemberRequestObject.builder().user(userWithoutRole).build();

    // Create a new channel with these members
    var channelResp =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.getOrCreate("team", RandomStringUtils.randomAlphabetic(12))
                    .data(
                        ChannelRequestObject.builder()
                            .createdBy(testUserRequestObject)
                            .member(memberWithRole)
                            .member(memberWithoutRole)
                            .build())
                    .request());
    var channel = channelResp.getChannel();

    // User with role sends a message
    Message messageWithRole =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(channel.getType(), channel.getId())
                        .message(
                            MessageRequestObject.builder()
                                .text("Message from user with role")
                                .userId(userWithRole.getId())
                                .build())
                        .request())
            .getMessage();

    // User without role sends a message
    Message messageWithoutRole =
        Assertions.assertDoesNotThrow(
                () ->
                    Message.send(channel.getType(), channel.getId())
                        .message(
                            MessageRequestObject.builder()
                                .text("Message from user without role")
                                .userId(userWithoutRole.getId())
                                .build())
                        .request())
            .getMessage();

    // Assert the role information in the immediate responses
    Assertions.assertNotNull(messageWithRole.getMember());
    Assertions.assertEquals(customRole, messageWithRole.getMember().getRole());

    Assertions.assertNotNull(messageWithoutRole.getMember());
    Assertions.assertNull(messageWithoutRole.getMember().getRole());

    // Retrieve the channel again and ensure both messages still carry the correct member role
    var channelState =
        Assertions.assertDoesNotThrow(
            () -> Channel.getOrCreate(channel.getType(), channel.getId()).state(true).request());

    List<Message> messages = channelState.getMessages();
    Assertions.assertNotNull(messages);
    // The messages list may include more than our two messages – find them by id
    Message storedWithRole =
        messages.stream()
            .filter(m -> m.getId().equals(messageWithRole.getId()))
            .findFirst()
            .orElse(null);
    Message storedWithoutRole =
        messages.stream()
            .filter(m -> m.getId().equals(messageWithoutRole.getId()))
            .findFirst()
            .orElse(null);

    Assertions.assertNotNull(storedWithRole);
    Assertions.assertNotNull(storedWithRole.getMember());
    Assertions.assertEquals(customRole, storedWithRole.getMember().getRole());

    Assertions.assertNotNull(storedWithoutRole);
    Assertions.assertNotNull(storedWithoutRole.getMember());
    Assertions.assertNull(storedWithoutRole.getMember().getRole());
  }
}

