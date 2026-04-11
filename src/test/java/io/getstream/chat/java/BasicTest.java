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
    setProperties();
    upsertUsers();
    createTestChannel();
    createTestMessage();
  }

  private static void createTestMessage() throws StreamException {
    waitFor(
        () -> {
          try {
            testMessage = sendTestMessage();
            return testMessage != null;
          } catch (StreamException e) {
            return false;
          }
        },
        1000L,
        60000L);
  }

  private static void createTestChannel() throws StreamException {
    waitFor(
        () -> {
          try {
            testChannelGetResponse = createRandomChannel();
            testChannel = testChannelGetResponse.getChannel();
            return testChannel != null;
          } catch (StreamException e) {
            return false;
          }
        },
        1000L,
        60000L);
  }

  static void upsertUsers() throws StreamException {
    testUsersRequestObjects.clear();
    testUserRequestObject =
        UserRequestObject.builder().id(uniqueId("gandalf")).name("Gandalf the Grey").build();
    testUsersRequestObjects.add(testUserRequestObject);
    testUsersRequestObjects.add(
        UserRequestObject.builder().id(uniqueId("frodo")).name("Frodo Baggins").build());
    testUsersRequestObjects.add(
        UserRequestObject.builder().id(uniqueId("pippin")).name("Frodo Baggins").build());
    testUsersRequestObjects.add(
        UserRequestObject.builder().id(uniqueId("sam")).name("Samwise Gamgee").build());
    UserUpsertRequest usersUpsertRequest = User.upsert();
    testUsersRequestObjects.forEach(user -> usersUpsertRequest.user(user));
    usersUpsertRequest.request();
    waitFor(
        () -> {
          var existingUsers = Assertions.assertDoesNotThrow(() -> User.list().request().getUsers());
          return testUsersRequestObjects.stream()
              .allMatch(
                  expectedUser ->
                      existingUsers.stream()
                          .anyMatch(
                              existingUser ->
                                  expectedUser.getId().equals(existingUser.getId())
                                      && existingUser.getDeletedAt() == null));
        },
        1000L,
        60000L);
  }

  static void setProperties() {
    System.setProperty(
        "java.util.logging.SimpleFormatter.format",
        "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
    // Keep CI logs readable by default; callers can still override this for local debugging.
    System.setProperty(
        "io.getstream.chat.debug.logLevel",
        System.getProperty("io.getstream.chat.debug.logLevel", "NONE"));
  }

  protected static List<ChannelMemberRequestObject> buildChannelMembersList() {
    return testUsersRequestObjects.stream()
        .map(user -> ChannelMemberRequestObject.builder().user(user).build())
        .collect(Collectors.toList());
  }

  protected static ChannelGetResponse createRandomChannel() throws StreamException {
    return Channel.getOrCreate("team", uniqueId("channel"))
        .data(
            ChannelRequestObject.builder()
                .createdBy(testUserRequestObject)
                .members(buildChannelMembersList())
                .build())
        .request();
  }

  protected static String uniqueId(String prefix) {
    return prefix + "-" + UUID.randomUUID();
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
    Throwable lastError = null;

    while (true) {
      if (timeout < (System.currentTimeMillis() - start)) {
        if (lastError != null) {
          Assertions.fail(lastError);
        }
        Assertions.fail(new TimeoutException());
      }

      try {
        if (predicate.get()) {
          return;
        }
        lastError = null;
      } catch (Throwable t) {
        lastError = t;
      }

      Assertions.assertDoesNotThrow(() -> java.lang.Thread.sleep(askInterval));
    }
  }

  protected static void waitForTaskCompletion(String taskId) {
    var start = System.currentTimeMillis();
    TaskStatusGetResponse lastResponse = null;
    var askInterval = 500L;
    var timeout = 120000L;

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
      System.out.printf("Task %s status=%s result=%s\n", taskId, status, lastResponse.getResult());
      if ("failed".equals(status) || "error".equals(status)) {
        Assertions.fail(
            String.format(
                "Task %s failed with status=%s result=%s",
                taskId, status, lastResponse.getResult()));
      }

      if (isTaskResultFailed(lastResponse)) {
        Assertions.fail(
            String.format(
                "Task %s failed with status=%s result=%s",
                taskId, status, lastResponse.getResult()));
      }

      if (("completed".equals(status) || "ok".equals(status))
          && isTaskResultTerminal(lastResponse)) {
        return;
      }

      Assertions.assertDoesNotThrow(() -> java.lang.Thread.sleep(askInterval));
    }
  }

  private static boolean isTaskResultTerminal(TaskStatusGetResponse response) {
    if (response == null || response.getResult() == null || response.getResult().isEmpty()) {
      return true;
    }

    var resultStatus = response.getResult().get("status");
    if (!(resultStatus instanceof String)) {
      return true;
    }

    var normalizedStatus = ((String) resultStatus).toLowerCase();
    return !"started".equals(normalizedStatus) && !"pending".equals(normalizedStatus);
  }

  private static boolean isTaskResultFailed(TaskStatusGetResponse response) {
    if (response == null || response.getResult() == null) {
      return false;
    }

    var resultStatus = response.getResult().get("status");
    if (!(resultStatus instanceof String)) {
      return false;
    }

    var normalizedStatus = ((String) resultStatus).toLowerCase();
    return "failed".equals(normalizedStatus) || "error".equals(normalizedStatus);
  }
}
