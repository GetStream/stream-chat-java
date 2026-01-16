package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.*;
import io.getstream.chat.java.models.Channel.ChannelGetResponse;
import io.getstream.chat.java.models.Channel.ChannelMemberRequestObject;
import io.getstream.chat.java.models.Channel.ChannelRequestObject;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.TaskStatus.TaskStatusGetResponse;
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
    cleanChannels();
    cleanChannelTypes();
    cleanBlocklists();
    cleanCommands();
    cleanUsers();
    upsertUsers();
    createTestChannel();
    createTestMessage();
  }

  private static void cleanChannels() throws StreamException {
    while (true) {
      List<String> channels =
          Channel.list().request().getChannels().stream()
              .map(channel -> channel.getChannel().getCId())
              .collect(Collectors.toList());

      if (channels.size() == 0) {
        break;
      }

      var deleteManyResponse =
          Channel.deleteMany(channels).setDeleteStrategy(DeleteStrategy.HARD).request();
      String taskId = deleteManyResponse.getTaskId();
      Assertions.assertNotNull(taskId);

      System.out.printf("Waiting for channel deletion task %s to complete...\n", taskId);

      while (true) {
        TaskStatusGetResponse response = TaskStatus.get(taskId).request();
        String status = response.getStatus();

        if (status.equals("completed") || status.equals("ok")) {
          break;
        }
        if (status.equals("failed") || status.equals("error")) {
          throw new StreamException(
              String.format("Failed to delete channel(task_id: %s): %s", response.getId(), status),
              (Throwable) null);
        }

        // wait for the channels to delete
        Assertions.assertDoesNotThrow(() -> java.lang.Thread.sleep(500));
      }
    }
  }

  private static void cleanUsers() throws StreamException {
    while (true) {
      List<String> users =
          User.list().request().getUsers().stream()
              .map(user -> user.getId())
              .collect(Collectors.toList());

      if (users.size() == 0) {
        break;
      }

      var deleteManyResponse =
          User.deleteMany(users).deleteUserStrategy(DeleteStrategy.HARD).request();
      String taskId = deleteManyResponse.getTaskId();
      Assertions.assertNotNull(taskId);

      System.out.printf("Waiting for user deletion task %s to complete...\n", taskId);

      while (true) {
        TaskStatusGetResponse response = TaskStatus.get(taskId).request();
        String status = response.getStatus();

        if (status.equals("completed") || status.equals("ok")) {
          break;
        }
        if (status.equals("failed") || status.equals("error")) {
          throw new StreamException(
              String.format("Failed to delete user(task_id: %s): %s", response.getId(), status),
              (Throwable) null);
        }

        // wait for the channels to delete
        Assertions.assertDoesNotThrow(() -> java.lang.Thread.sleep(500));
      }
    }
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
          return commands.size() == 5; // Built-in 5 commands
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
    testUsersRequestObjects.clear();
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
    // Enable HTTP request/response logging for debugging test failures.
    System.setProperty("io.getstream.chat.debug.logLevel", "BODY");
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
      java.lang.Thread.sleep(6000);
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

      Assertions.assertDoesNotThrow(() -> java.lang.Thread.sleep(askInterval));
    }
  }

  protected static void waitForTaskCompletion(String taskId) {
    var start = System.currentTimeMillis();
    TaskStatusGetResponse lastResponse = null;
    var askInterval = 500L;
    var timeout = 15000L;

    System.out.printf("Waiting for task %s to complete...\n", taskId);

    while (true) {
      if (timeout < (System.currentTimeMillis() - start)) {
        var status = lastResponse != null ? lastResponse.getStatus() : "unknown";
        var result = lastResponse != null ? lastResponse.getResult() : null;
        Assertions.fail(
            new TimeoutException(
                String.format(
                    "Timed out waiting for task %s to complete. status=%s result=%s",
                    taskId, status, result)));
      }

      lastResponse = Assertions.assertDoesNotThrow(() -> TaskStatus.get(taskId).request());
      var status = lastResponse.getStatus();
      System.out.printf(
          "Task %s status=%s result=%s\n", taskId, status, lastResponse.getResult());
      if ("completed".equals(status) || "ok".equals(status)) {
        return;
      }
      if ("failed".equals(status) || "error".equals(status)) {
        Assertions.fail(
            String.format(
                "Task %s failed with status=%s result=%s", taskId, status, lastResponse.getResult()));
      }

      Assertions.assertDoesNotThrow(() -> java.lang.Thread.sleep(askInterval));
    }
  }
}
