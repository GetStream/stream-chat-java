package io.getstream.chat.java;

import io.getstream.chat.java.models.*;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExportUsersTest extends BasicTest {
  @DisplayName("Export users")
  @Test
  void exportUsersTest() {
    var userId = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> User.upsert().user(User.UserRequestObject.builder().id(userId).build()).request());

    var userIds = List.of(userId);
    var exportUsersResponse =
        Assertions.assertDoesNotThrow(() -> ExportUsers.exportUsers(userIds).request());
    Assertions.assertNotEquals("", exportUsersResponse.getTaskId());

    var taskId = exportUsersResponse.getTaskId();

    var taskCompleted = false;
    for (int i = 0; i < 10; i++) {
      var taskStatus =
          Assertions.assertDoesNotThrow(() -> TaskStatus.get(taskId).request()).getStatus();
      if (taskStatus.equals("completed")) {
        taskCompleted = true;
        break;
      }
      Assertions.assertDoesNotThrow(() -> Thread.sleep(500));
    }
    Assertions.assertTrue(taskCompleted);
  }
}
