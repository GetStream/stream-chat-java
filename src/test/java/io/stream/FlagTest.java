package io.stream;

import io.getstream.models.Flag;
import io.getstream.models.Flag.FlagMessageQueryResponse;
import io.getstream.models.Message;
import io.getstream.models.User;
import io.getstream.models.User.UserRequestObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FlagTest extends BasicTest {

  @DisplayName("Can flag a message")
  @Test
  void whenFlaggingAMessage_thenIsFlagged() {
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    Flag flag =
        Assertions.assertDoesNotThrow(
                () ->
                    Flag.create()
                        .targetMessageId(message.getId())
                        .user(testUserRequestObject)
                        .request())
            .getFlag();
    Assertions.assertEquals(message.getId(), flag.getTargetMessageId());
  }

  @DisplayName("Can flag a user")
  @Test
  void whenFlaggingAUser_thenIsFlagged() {
    UserRequestObject userRequestObject =
        UserRequestObject.builder()
            .id(RandomStringUtils.randomAlphabetic(10))
            .name("User to flag")
            .build();
    Assertions.assertDoesNotThrow(() -> User.upsert().user(userRequestObject).request());
    Flag flag =
        Assertions.assertDoesNotThrow(
                () ->
                    Flag.create()
                        .targetUserId(userRequestObject.getId())
                        .user(testUserRequestObject)
                        .request())
            .getFlag();
    Assertions.assertEquals(userRequestObject.getId(), flag.getTargetUser().getId());
  }

  @DisplayName("Can unflag a message")
  @Test
  void whenUnFlaggingAMessage_thenNoException() {
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    Assertions.assertDoesNotThrow(
        () -> Flag.create().targetMessageId(message.getId()).user(testUserRequestObject).request());
    Assertions.assertDoesNotThrow(
            () ->
                Flag.delete()
                    .targetMessageId(message.getId())
                    .user(testUserRequestObject)
                    .request())
        .getFlag();
  }

  @DisplayName("Can unflag a user")
  @Test
  void whenUnFlaggingAUser_thenNoException() {
    UserRequestObject userRequestObject =
        UserRequestObject.builder()
            .id(RandomStringUtils.randomAlphabetic(10))
            .name("User to flag")
            .build();
    Assertions.assertDoesNotThrow(() -> User.upsert().user(userRequestObject).request());
    Assertions.assertDoesNotThrow(
        () ->
            Flag.create()
                .targetUserId(userRequestObject.getId())
                .user(testUserRequestObject)
                .request());
    Assertions.assertDoesNotThrow(
        () ->
            Flag.delete()
                .targetUserId(userRequestObject.getId())
                .user(testUserRequestObject)
                .request());
  }

  @DisplayName("Can search flagged messages")
  @Test
  void whenQueryingMessageFlags_thenRetrieved() {
    Message message = Assertions.assertDoesNotThrow(() -> sendTestMessage());
    Assertions.assertDoesNotThrow(
            () ->
                Flag.create()
                    .targetMessageId(message.getId())
                    .user(testUserRequestObject)
                    .request())
        .getFlag();
    FlagMessageQueryResponse response =
        Assertions.assertDoesNotThrow(() -> Message.queryFlags().filterCondition("user_id", testUserRequestObject.getId()).user(testUserRequestObject).request());
    Assertions.assertTrue(
        response.getFlags().stream()
            .anyMatch(flag -> flag.getMessage().getId().equals(message.getId())));
  }
}
