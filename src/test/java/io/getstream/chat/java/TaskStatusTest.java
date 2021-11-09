package io.getstream.chat.java;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.TaskStatus;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskStatusTest extends BasicTest {
  @Test
  @DisplayName("Can get task status by id")
  void whenTaskHasBeenExecuted_thenGetItById() {
    Assertions.assertDoesNotThrow(
        () -> {
          var ch1 = Assertions.assertDoesNotThrow(BasicTest::createRandomChannel).getChannel();
          Assertions.assertNotNull(ch1);
          var ch2 = Assertions.assertDoesNotThrow(BasicTest::createRandomChannel).getChannel();
          Assertions.assertNotNull(ch2);

          var cids = List.of(ch1.getCId(), ch2.getCId());
          var taskId = Channel.deleteMany(cids).request().getTaskId();
          waitFor(
              () -> {
                var taskStatusResponse =
                    Assertions.assertDoesNotThrow(() -> TaskStatus.get(taskId).request());
                return "completed".equals(taskStatusResponse.getStatus());
              });
        });
  }
}
