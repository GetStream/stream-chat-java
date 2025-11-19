package io.getstream.chat.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.ChannelType;
import io.getstream.chat.java.models.ChannelType.AutoMod;
import io.getstream.chat.java.models.ChannelType.ChannelTypeListResponse;
import io.getstream.chat.java.models.Command;
import io.getstream.chat.java.models.ResourceAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

public class ChannelTypeTest extends BasicTest {
  @Test
  @DisplayName("Get channel type with populated commands")
  void whenPopulatingCommands_thenFetchChannelTypeWithoutAnyIssues() {
    var commandName = java.util.UUID.randomUUID().toString();

    try {
      Command.create().name(commandName).description("awesome").request();
    } catch (StreamException ex) {
      // If command already exists ignore the exception
      Assertions.assertEquals(4, ex.getResponseData().getCode());
    }

    waitFor(
        () -> {
          var commands =
              Assertions.assertDoesNotThrow(() -> Command.list().request().getCommands());
          return commands != null
              && commands.stream().anyMatch(c -> c.getName().equals(commandName));
        });

    var channelTypeName = java.util.UUID.randomUUID().toString();
    var channelTypeResponse =
        Assertions.assertDoesNotThrow(
            () -> {
              var response =
                  ChannelType.create()
                      .withDefaultConfig()
                      .name(channelTypeName)
                      .commands(List.of(commandName))
                      .request();

              assert response.getCommands() != null;
              return ChannelType.get(response.getName()).request();
            });

    Assertions.assertEquals(channelTypeName, channelTypeResponse.getName());
    Assertions.assertNotNull(channelTypeResponse.getCommands());
    Assertions.assertEquals(1, channelTypeResponse.getCommands().size());
  }

  @DisplayName("Can fetch channel type after creation with no Exception and correct name")
  @Test
  void whenCreatingDefaultChannelType_thenCanFetchWithNoExceptionAndCorrectName() {
    String channelName = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> ChannelType.create().withDefaultConfig().name(channelName).request());
    pause();
    ChannelType channelType =
        Assertions.assertDoesNotThrow(() -> ChannelType.get(channelName).request());
    assertEquals(channelType.getName(), channelName);
    Assertions.assertDoesNotThrow(() -> ChannelType.delete(channelName));
  }

  @DisplayName("Can create channel types with permissions")
  @Test
  void whenCreatingChannelType_thenSetPermissions() {
    String channelTypeName = java.util.UUID.randomUUID().toString();

    var permission =
        ChannelType.PermissionRequestObject.builder()
            .name("Deny creating channels")
            .priority(1)
            .resources(List.of(ResourceAction.CREATE_CHANNEL))
            .action(ChannelType.Action.DENY)
            .build();

    Assertions.assertDoesNotThrow(
        () -> {
          ChannelType.create()
              .withDefaultConfig()
              .name(channelTypeName)
              .permission(permission)
              .request();
        });

    pause();

    ChannelType channelType =
        Assertions.assertDoesNotThrow(() -> ChannelType.get(channelTypeName).request());
    assertEquals(channelType.getName(), channelTypeName);
    Assertions.assertDoesNotThrow(() -> ChannelType.delete(channelTypeName));
    Assertions.assertNotNull(channelType.getPermissions());
    var firstResource = channelType.getPermissions().get(0).getResources().get(0);
    Assertions.assertEquals(ResourceAction.CREATE_CHANNEL, firstResource);
  }

  @DisplayName("Can delete channel type after creation with no Exception")
  @Test
  void whenCreatingDefaultChannelType_thenCanDeleteWithNoException() {
    String channelName = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> ChannelType.create().withDefaultConfig().name(channelName).request());
    pause();
    Assertions.assertDoesNotThrow(() -> ChannelType.delete(channelName).request());
  }

  @DisplayName("Can see created channel type in list after creation with no Exception")
  @Test
  void whenCreatingDefaultChannelType_thenCanListAndRetrieveItWithNoException() {
    String channelName = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> ChannelType.create().withDefaultConfig().name(channelName).request());
    pause();
    ChannelTypeListResponse listChannelTypeResponse =
        Assertions.assertDoesNotThrow(() -> ChannelType.list().request());
    Assertions.assertTrue(listChannelTypeResponse.getChannelTypes().containsKey(channelName));
    Assertions.assertDoesNotThrow(() -> ChannelType.delete(channelName));
  }

  @DisplayName("Can update channel type after creation with no Exception")
  @Test
  void whenCreatingDefaultChannelType_thenCanUpdateWithNoException() {
    String channelName = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> ChannelType.create().withDefaultConfig().name(channelName).request());
    pause();
    Assertions.assertDoesNotThrow(
        () -> ChannelType.update(channelName).automod(AutoMod.SIMPLE).request());
  }

  @DisplayName("Can update channel type with quotes with no Exception")
  @Test
  void whenCreatingDefaultChannelType_thenCanUpdateQuotesWithNoException() {
    String channelName = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> ChannelType.create().withDefaultConfig().name(channelName).request());
    pause();
    Assertions.assertDoesNotThrow(() -> ChannelType.update(channelName).quotes(false).request());
  }

  @DisplayName("Can create channel type with specific grants")
  @Test
  void whenManipulatingChannelTypeWithGrants_throwsNoException() {
    String channelTypeName = RandomStringUtils.randomAlphabetic(10);
    var expectedGrants = List.of("read-channel", "create-message");
    var channelGrants = new HashMap<String, List<String>>();
    channelGrants.put("channel_member", expectedGrants);
    Assertions.assertDoesNotThrow(
        () ->
            ChannelType.create()
                .withDefaultConfig()
                .grants(channelGrants)
                .name(channelTypeName)
                .request());
    waitFor(
        () -> {
          var channelType =
              Assertions.assertDoesNotThrow(() -> ChannelType.get(channelTypeName).request());
          var actualGrants = channelType.getGrants().get("channel_member");

          return new HashSet<>(actualGrants).equals(new HashSet<>(expectedGrants));
        });
  }

  @DisplayName("Can set user_message_reminders field on channel type")
  @Test
  void whenSettingUserMessageRemindersOnChannelType_thenFieldIsAccessible() {
    String channelTypeName = RandomStringUtils.randomAlphabetic(10);

    // Create a basic channel type first
    Assertions.assertDoesNotThrow(
        () -> ChannelType.create().withDefaultConfig().name(channelTypeName).request());
    pause();

    // Test that the field can be set (even if Push V3 is not enabled, the field should be settable)
    // The API will reject enabling it without Push V3, but the field should still be in the model
    Assertions.assertDoesNotThrow(
        () -> ChannelType.update(channelTypeName).userMessageReminders(false).request());
    pause();

    // Retrieve and verify the field is accessible
    var retrieved = Assertions.assertDoesNotThrow(() -> ChannelType.get(channelTypeName).request());
    Assertions.assertEquals(channelTypeName, retrieved.getName());
    // The field should be present in the response (even if false)
    Assertions.assertNotNull(retrieved.getUserMessageReminders());

    Assertions.assertDoesNotThrow(() -> ChannelType.delete(channelTypeName));
  }
}
