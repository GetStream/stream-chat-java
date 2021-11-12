package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.*;
import io.getstream.chat.java.models.Channel.ChannelGetResponse;
import io.getstream.chat.java.models.Channel.ChannelMemberRequestObject;
import io.getstream.chat.java.models.Channel.ChannelRequestObject;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.User.UserUpsertRequestData.UserUpsertRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

public class BasicTest {
  protected static UserRequestObject testUserRequestObject;
  protected static List<UserRequestObject> testUsersRequestObjects = new ArrayList<>();
  protected static ChannelGetResponse testChannelGetResponse;
  protected static Channel testChannel;
  protected static Message testMessage;

  @BeforeAll
  static void setup() throws StreamException, SecurityException, IllegalArgumentException {
    // failOnUnknownProperties();
    setProperties();
    cleanChannelTypes();
    cleanBlocklists();
    cleanCommands();
    upsertUsers();
    createTestChannel();
    createTestMessage();
  }

  private static void cleanChannelTypes() throws StreamException {
    ChannelType.list()
        .request()
        .getChannelTypes()
        .values()
        .forEach(
            channelType -> {
              try {
                ChannelType.delete(channelType.getName()).request();
              } catch (StreamException e) {
                // Do nothing. Happens when there are channels of that type
              }
            });
  }

  private static void cleanBlocklists() throws StreamException {
    Blocklist.list()
        .request()
        .getBlocklists()
        .forEach(
            blocklist -> {
              try {
                Blocklist.delete(blocklist.getName()).request();
              } catch (StreamException e) {
                // Do nothing this happens for built in
              }
            });
  }

  private static void cleanCommands() throws StreamException {
    Command.list()
        .request()
        .getCommands()
        .forEach(
            command -> {
              try {
                Command.delete(command.getName()).request();
              } catch (StreamException e) {
                // Do nothing
              }
            });

    waitFor(
        () -> {
          var commands =
              Assertions.assertDoesNotThrow(() -> Command.list().request().getCommands());
          return commands == null || commands.size() == 0;
        });
  }

  private static void createTestMessage() throws StreamException {
    testMessage = sendTestMessage();
  }

  private static void createTestChannel() throws StreamException {
    testChannelGetResponse = createRandomChannel();
    testChannel = testChannelGetResponse.getChannel();
  }

  static void upsertUsers() throws StreamException {
    testUserRequestObject =
        UserRequestObject.builder()
            .id(RandomStringUtils.randomAlphabetic(10))
            .name("Gandalf the Grey")
            .build();
    testUsersRequestObjects.add(testUserRequestObject);
    testUsersRequestObjects.add(
        UserRequestObject.builder()
            .id(RandomStringUtils.randomAlphabetic(10))
            .name("Frodo Baggins")
            .build());
    testUsersRequestObjects.add(
        UserRequestObject.builder()
            .id(RandomStringUtils.randomAlphabetic(10))
            .name("Frodo Baggins")
            .build());
    testUsersRequestObjects.add(
        UserRequestObject.builder()
            .id(RandomStringUtils.randomAlphabetic(10))
            .name("Samwise Gamgee")
            .build());
    UserUpsertRequest usersUpsertRequest = User.upsert();
    testUsersRequestObjects.forEach(user -> usersUpsertRequest.user(user));
    usersUpsertRequest.request();
  }

  static void setProperties() {
    System.setProperty(
        "java.util.logging.SimpleFormatter.format",
        "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
  }

  protected static List<ChannelMemberRequestObject> buildChannelMembersList() {
    return testUsersRequestObjects.stream()
        .map(user -> ChannelMemberRequestObject.builder().user(user).build())
        .collect(Collectors.toList());
  }

  protected static ChannelGetResponse createRandomChannel() throws StreamException {
    return Channel.getOrCreate("team", RandomStringUtils.randomAlphabetic(12))
        .data(
            ChannelRequestObject.builder()
                .createdBy(testUserRequestObject)
                .members(buildChannelMembersList())
                .build())
        .request();
  }

  protected static Message sendTestMessage() throws StreamException {
    String text = UUID.randomUUID().toString();
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();
    return Message.send(testChannel.getType(), testChannel.getId())
        .message(messageRequest)
        .request()
        .getMessage();
  }

  /**
   * This is used to pause after creation, as there can be a small delay before we can act upon the
   * resource
   */
  protected void pause() {
    try {
      Thread.sleep(6000);
    } catch (InterruptedException e) {
      // Do nothing
    }
  }

  protected static void waitFor(Supplier<Boolean> predicate) {
    waitFor(predicate, 500L, 15000L);
  }

  protected static void waitFor(Supplier<Boolean> predicate, Long askInterval, Long timeout) {
    var start = System.currentTimeMillis();

    while (true) {
      if (timeout < (System.currentTimeMillis() - start)) {
        Assertions.fail(new TimeoutException());
      }

      if (Assertions.assertDoesNotThrow(predicate::get)) {
        return;
      }

      Assertions.assertDoesNotThrow(() -> Thread.sleep(askInterval));
    }
  }
}
