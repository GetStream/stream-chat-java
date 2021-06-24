package io.getstream.chat.java;

import io.getstream.chat.java.models.Command;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommandTest extends BasicTest {

  @DisplayName("Can create command")
  @Test
  void whenCreatingCommand_thenCorrectDescription() {
    String description = "test description";
    Command command =
        Assertions.assertDoesNotThrow(
                () ->
                    Command.create()
                        .name(RandomStringUtils.randomAlphabetic(5))
                        .description(description)
                        .request())
            .getCommand();
    Assertions.assertEquals(description, command.getDescription());
  }

  @DisplayName("Can update command")
  @Test
  void whenUpdatingCommand_thenCorrectDescription() {
    String description = "test description";
    Command command =
        Assertions.assertDoesNotThrow(
                () ->
                    Command.create()
                        .name(RandomStringUtils.randomAlphabetic(5))
                        .description(description)
                        .request())
            .getCommand();
    pause();
    String updatedDescription = "updated description";
    Command updatedCommand =
        Assertions.assertDoesNotThrow(
                () -> Command.update(command.getName()).description(updatedDescription).request())
            .getCommand();
    Assertions.assertEquals(updatedDescription, updatedCommand.getDescription());
  }

  @DisplayName("Can retrieve command")
  @Test
  void whenRetrievingCommand_thenCorrectDescription() {
    String description = "test description";
    Command command =
        Assertions.assertDoesNotThrow(
                () ->
                    Command.create()
                        .name(RandomStringUtils.randomAlphabetic(5))
                        .description(description)
                        .request())
            .getCommand();
    pause();
    Command retrievedCommand =
        Assertions.assertDoesNotThrow(() -> Command.get(command.getName()).request());
    Assertions.assertEquals(command.getName(), retrievedCommand.getName());
  }

  @DisplayName("Can delete command")
  @Test
  void whenDeletingCommand_thenDeleted() {
    String description = "test description";
    Command command =
        Assertions.assertDoesNotThrow(
                () ->
                    Command.create()
                        .name(RandomStringUtils.randomAlphabetic(5))
                        .description(description)
                        .request())
            .getCommand();
    pause();
    Assertions.assertDoesNotThrow(() -> Command.delete(command.getName()).request());
    pause();
    List<Command> commands =
        Assertions.assertDoesNotThrow(() -> Command.list().request()).getCommands();
    Assertions.assertFalse(
        commands.stream()
            .anyMatch(consideredCommand -> consideredCommand.getName().equals(command.getName())));
  }

  @DisplayName("Can list commands")
  @Test
  void whenListingCommand_thenCanRetrieve() {
    String description = "test description";
    Command command =
        Assertions.assertDoesNotThrow(
                () ->
                    Command.create()
                        .name(RandomStringUtils.randomAlphabetic(5))
                        .description(description)
                        .request())
            .getCommand();
    pause();
    List<Command> commands =
        Assertions.assertDoesNotThrow(() -> Command.list().request()).getCommands();
    Assertions.assertTrue(
        commands.stream()
            .anyMatch(consideredCommand -> consideredCommand.getName().equals(command.getName())));
  }
}
