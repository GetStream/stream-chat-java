package io.getstream.chat.java;

import static io.getstream.chat.java.models.User.*;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.DeleteStrategy;
import io.getstream.chat.java.models.FilterCondition;
import io.getstream.chat.java.models.Language;
import io.getstream.chat.java.models.User;
import io.getstream.chat.java.models.User.Ban;
import io.getstream.chat.java.models.User.ChannelMuteRequestObject;
import io.getstream.chat.java.models.User.OwnUser;
import io.getstream.chat.java.models.User.OwnUserRequestObject;
import io.getstream.chat.java.models.User.UserMute;
import io.getstream.chat.java.models.User.UserPartialUpdateRequestObject;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.User.UserUpsertRequestData.UserUpsertRequest;
import io.getstream.chat.java.models.User.UserUpsertResponse;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

public class UserTest extends BasicTest {

  @DisplayName("Can list users with no Exception")
  @Test
  void whenListingUsers_thenNoException() {
    Assertions.assertDoesNotThrow(() -> User.list().request());
  }

  @DisplayName("Can parse language correctly")
  @Test
  void whenParsingLanguage_thenNoException() {
    var id = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () ->
            User.upsert()
                .user(UserRequestObject.builder().id(id).language(Language.FR).build())
                .request());

    var lang =
        Assertions.assertDoesNotThrow(
            () -> User.list().filterCondition("id", id).request().getUsers().get(0).getLanguage());
    Assertions.assertEquals(lang, Language.FR);
  }

  @DisplayName("Not defined language is set to unknown")
  @Test
  void whenParsingNotDefinedLanguage_thenUnknown() {
    var id = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () ->
            User.upsert()
                .user(
                    UserRequestObject.builder()
                        .id(id)
                        .additionalField("language", "something-unknown")
                        .build())
                .request());

    var lang =
        Assertions.assertDoesNotThrow(
            () -> User.list().filterCondition("id", id).request().getUsers().get(0).getLanguage());
    Assertions.assertEquals(lang, Language.UNKNOWN);
  }

  @DisplayName("Can create a user with team and teams_role")
  @Test
  void whenCreatingUserWithTeam_thenNoException() {
    var id = RandomStringUtils.randomAlphabetic(10);
    var team = "blue";
    var role = "admin";

    // Create user with team and teams_role
    UserUpsertResponse response =
        Assertions.assertDoesNotThrow(
            () ->
                User.upsert()
                    .user(
                        UserRequestObject.builder()
                            .id(id)
                            .teams(Collections.singletonList(team))
                            .teamsRole(Collections.singletonMap(team, role))
                            .build())
                    .request());

    // Verify the user was created with correct team and role
    User createdUser = response.getUsers().get(id);
    Assertions.assertNotNull(createdUser);
    Assertions.assertEquals(team, createdUser.getTeams().get(0));
    Assertions.assertEquals(role, createdUser.getTeamsRole().get(team));
  }

  @DisplayName("Can partial update a user")
  @Test
  void whenPartiallyUpdatingUser_thenNoException() {
    UserUpsertRequest usersUpsertRequest = User.upsert();
    User user =
        Assertions.assertDoesNotThrow(
                () ->
                    usersUpsertRequest
                        .user(
                            UserRequestObject.builder()
                                .id(RandomStringUtils.randomAlphabetic(10))
                                .name("Samwise Gamgee")
                                .build())
                        .request())
            .getUsers()
            .values()
            .iterator()
            .next();
    String addedKey = "extrafield";
    String addedValue = "extra value";
    UserPartialUpdateRequestObject userPartialUpdateRequestObject =
        UserPartialUpdateRequestObject.builder()
            .id(user.getId())
            .unsetValue("name")
            .setValue(addedKey, addedValue)
            .build();
    User updatedUser =
        Assertions.assertDoesNotThrow(
                () ->
                    User.partialUpdate()
                        .users(Arrays.asList(userPartialUpdateRequestObject))
                        .request())
            .getUsers()
            .get(user.getId());
    Assertions.assertNull(updatedUser.getName());
    Assertions.assertEquals(addedValue, updatedUser.getAdditionalFields().get(addedKey));
  }

  @DisplayName("Can partial update a user with team and teams_role")
  @Test
  void whenPartiallyUpdatingUserWithTeam_thenNoException() {
    // First create a basic user
    UserUpsertRequest usersUpsertRequest = User.upsert();
    User user =
        Assertions.assertDoesNotThrow(
                () ->
                    usersUpsertRequest
                        .user(
                            UserRequestObject.builder()
                                .id(RandomStringUtils.randomAlphabetic(10))
                                .name("Test User")
                                .build())
                        .request())
            .getUsers()
            .values()
            .iterator()
            .next();

    // Partially update the user with team and teams_role
    UserPartialUpdateRequestObject userPartialUpdateRequestObject =
        UserPartialUpdateRequestObject.builder()
            .id(user.getId())
            .setValue("teams", Collections.singletonList("blue"))
            .setValue("teams_role", Collections.singletonMap("blue", "admin"))
            .build();

    User updatedUser =
        Assertions.assertDoesNotThrow(
                () ->
                    User.partialUpdate()
                        .users(Arrays.asList(userPartialUpdateRequestObject))
                        .request())
            .getUsers()
            .get(user.getId());

    // Verify the changes
    Assertions.assertEquals("blue", updatedUser.getTeams().get(0));
    Assertions.assertEquals("admin", updatedUser.getTeamsRole().get("blue"));
  }

  @DisplayName("Can ban user")
  @Test
  void whenBanUser_thenIsBanned() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(UserRequestObject.builder().id(userId).name("User to ban").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    Assertions.assertDoesNotThrow(
        () -> User.ban().userId(testUserRequestObject.getId()).targetUserId(userId).request());
    List<Ban> bans = Assertions.assertDoesNotThrow(() -> User.queryBanned().request()).getBans();
    Assertions.assertTrue(bans.stream().anyMatch(ban -> ban.getUser().getId().equals(userId)));
  }

  @DisplayName("Can shadow ban user")
  @Test
  void whenShadowBanUser_thenIsShadowBanned() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(
        UserRequestObject.builder().id(userId).name("User to shadowban").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    Assertions.assertDoesNotThrow(
        () ->
            User.shadowBan().userId(testUserRequestObject.getId()).targetUserId(userId).request());
    List<Ban> bans = Assertions.assertDoesNotThrow(() -> User.queryBanned().request()).getBans();
    var banned =
        bans.stream().filter(ban -> ban.getUser().getId().equals(userId)).findFirst().get();
    Assertions.assertTrue(banned.getShadow());
  }

  @DisplayName("Can list banned user")
  @Test
  void whenListingBannedUsers_thenContainsBanned() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(UserRequestObject.builder().id(userId).name("User to ban").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    Assertions.assertDoesNotThrow(
        () -> User.ban().userId(testUserRequestObject.getId()).targetUserId(userId).request());
    List<Ban> bans = Assertions.assertDoesNotThrow(() -> User.queryBanned().request()).getBans();
    Assertions.assertTrue(bans.stream().anyMatch(ban -> ban.getUser().getId().equals(userId)));
  }

  @DisplayName("Can deactivate user")
  @Test
  void whenDeactivateUser_thenIsDeactivated() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(
        UserRequestObject.builder().id(userId).name("User to deactivate").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    User deactivatedUser =
        Assertions.assertDoesNotThrow(
                () -> User.deactivate(userId).createdById(testUserRequestObject.getId()).request())
            .getUser();
    Assertions.assertNotNull(deactivatedUser.getDeactivatedAt());
  }

  @DisplayName("Can reactivate user")
  @Test
  void whenReactivateUser_thenIsReactivated() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(
        UserRequestObject.builder().id(userId).name("User to deactivate").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    Assertions.assertDoesNotThrow(
            () -> User.deactivate(userId).createdById(testUserRequestObject.getId()).request())
        .getUser();
    User reactivatedUser =
        Assertions.assertDoesNotThrow(
                () -> User.reactivate(userId).createdById(testUserRequestObject.getId()).request())
            .getUser();
    Assertions.assertNull(reactivatedUser.getDeactivatedAt());
  }

  @DisplayName("Can list deactivated users")
  @Test
  void whenListingDeactivateUsers_thenIncludesDeactivated() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    String deactivatedUserId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(UserRequestObject.builder().id(userId).name("User").build());
    usersUpsertRequest.user(
        UserRequestObject.builder().id(deactivatedUserId).name("User to deactivate").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());

    Assertions.assertDoesNotThrow(
        () ->
            User.deactivate(deactivatedUserId)
                .createdById(testUserRequestObject.getId())
                .request());

    List<User> users =
        Assertions.assertDoesNotThrow(
                () ->
                    User.list()
                        .filterConditions(FilterCondition.in("id", userId, deactivatedUserId))
                        .includeDeactivatedUsers(true)
                        .request())
            .getUsers();

    Assertions.assertEquals(2, users.size());

    for (User user : users) {
      if (user.getId().equals(deactivatedUserId)) {
        Assertions.assertNotNull(user.getDeactivatedAt());
      } else {
        Assertions.assertNull(user.getDeactivatedAt());
      }
    }

    users =
        Assertions.assertDoesNotThrow(
                () ->
                    User.list()
                        .filterConditions(FilterCondition.in("id", userId, deactivatedUserId))
                        .request())
            .getUsers();

    Assertions.assertEquals(1, users.size());

    User user = users.get(0);
    Assertions.assertEquals(userId, user.getId());

    Assertions.assertNull(user.getDeactivatedAt());
  }

  @DisplayName("Can delete user")
  @Test
  void whenDeleteUser_thenIsDeleted() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(UserRequestObject.builder().id(userId).name("User to delete").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    User deletedUser = Assertions.assertDoesNotThrow(() -> User.delete(userId).request()).getUser();
    Assertions.assertNotNull(deletedUser.getDeletedAt());
  }

  @Test
  @DisplayName("Can delete many users")
  void whenDeleteManyUsers_thenTaskIdIsReturned() {
    for (var strategy : List.of(DeleteStrategy.HARD, DeleteStrategy.SOFT)) {
      var userIds = new ArrayList<String>();

      for (var i = 0; i < 3; i++) {
        String userId = RandomStringUtils.randomAlphabetic(10);
        UserUpsertRequest usersUpsertRequest = User.upsert();
        var userObject = UserRequestObject.builder().id(userId).name("User to delete").build();
        usersUpsertRequest.user(userObject);
        Assertions.assertDoesNotThrow(
            (ThrowingSupplier<UserUpsertResponse>) usersUpsertRequest::request);
        userIds.add(userId);
      }

      Assertions.assertDoesNotThrow(
          () -> {
            var taskId =
                User.deleteMany(userIds).deleteUserStrategy(strategy).request().getTaskId();

            Assertions.assertNotNull(taskId);
          });
    }
  }

  @Test
  @DisplayName("Illegal condition of deleteMany users")
  void whenDeleteManyUsersWithIllegalCondition_thenItFails() {
    Assertions.assertThrows(
        StreamException.class,
        () -> {
          User.deleteMany(List.of("a"))
              .deleteUserStrategy(DeleteStrategy.HARD)
              .deleteMessagesStrategy(DeleteStrategy.SOFT)
              .request();
        });
  }

  @DisplayName("Can mute user")
  @Test
  void whenMutingUser_thenIsMuted() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(UserRequestObject.builder().id(userId).name("User to mute").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    UserMute userMute =
        Assertions.assertDoesNotThrow(
                () -> User.mute().singleTargetId(userId).user(testUserRequestObject).request())
            .getMute();
    Assertions.assertEquals(userId, userMute.getTarget().getId());
  }

  @DisplayName("Can mute user")
  @Test
  void whenUnmutingUser_thenNoException() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(UserRequestObject.builder().id(userId).name("User to mute").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    Assertions.assertDoesNotThrow(
        () -> User.mute().singleTargetId(userId).user(testUserRequestObject).request());
    Assertions.assertDoesNotThrow(
        () -> User.unmute().singleTargetId(userId).user(testUserRequestObject).request());
  }

  @DisplayName("Can export user")
  @Test
  void whenExportingUser_thenNoException() {
    Assertions.assertDoesNotThrow(() -> User.export(testUserRequestObject.getId()).request());
  }

  @DisplayName("Can create guest user")
  @Test
  void whenCreatingGuestUser_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            User.createGuest()
                .user(
                    UserRequestObject.builder()
                        .id(RandomStringUtils.randomAlphabetic(10))
                        .name("Guest user")
                        .build())
                .request());
  }

  @DisplayName("Can unban user")
  @Test
  void whenUnbanUser_thenIsNotBannedAnymore() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(UserRequestObject.builder().id(userId).name("User to ban").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    Assertions.assertDoesNotThrow(
        () -> User.ban().userId(testUserRequestObject.getId()).targetUserId(userId).request());
    List<Ban> bans = Assertions.assertDoesNotThrow(() -> User.queryBanned().request()).getBans();
    Assertions.assertTrue(bans.stream().anyMatch(ban -> ban.getUser().getId().equals(userId)));
    Assertions.assertDoesNotThrow(() -> User.unban(userId).request());
    bans = Assertions.assertDoesNotThrow(() -> User.queryBanned().request()).getBans();
    Assertions.assertFalse(bans.stream().anyMatch(ban -> ban.getUser().getId().equals(userId)));
  }

  @DisplayName("Can remove a shadow ban")
  @Test
  void whenRemovingShadowBan_thenIsRemoved() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.user(
        UserRequestObject.builder().id(userId).name("User to shadowban").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    Assertions.assertDoesNotThrow(
        () ->
            User.shadowBan().userId(testUserRequestObject.getId()).targetUserId(userId).request());
    Assertions.assertDoesNotThrow(() -> User.removeShadowBan(userId).request());
    List<Ban> bans = Assertions.assertDoesNotThrow(() -> User.queryBanned().request()).getBans();
    Assertions.assertFalse(bans.stream().anyMatch(ban -> ban.getUser().getId().equals(userId)));
  }

  @SuppressWarnings("unchecked")
  @DisplayName("Can create a OwnUserRequestObject from OwnUser")
  @Test
  void whenCreatingAOwnUserRequestObject_thenIsCorrect() {
    Logger parent = Logger.getLogger("io.stream");
    parent.setLevel(Level.FINE);
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    OwnUser ownUser =
        Assertions.assertDoesNotThrow(
                () ->
                    Channel.mute()
                        .channelCid(channel.getType() + ":" + channel.getId())
                        .user(testUserRequestObject)
                        .request())
            .getOwnUser();
    OwnUserRequestObject ownUserRequestObject =
        Assertions.assertDoesNotThrow(() -> OwnUserRequestObject.buildFrom(ownUser));
    Assertions.assertDoesNotThrow(
        () -> {
          Assertions.assertEquals(
              ownUser.getChannelMutes().size(),
              ((List<ChannelMuteRequestObject>)
                      getRequestObjectFieldValue("channelMutes", ownUserRequestObject))
                  .size());
        });
  }

  private Object getRequestObjectFieldValue(String fieldName, Object requestObject)
      throws NoSuchFieldException,
          SecurityException,
          IllegalArgumentException,
          IllegalAccessException {
    Field field = requestObject.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(requestObject);
  }

  @DisplayName("Can revoke user token")
  @Test
  void whenRevokingUserToken_thenNoException() {
    Calendar calendar = new GregorianCalendar();
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    Assertions.assertDoesNotThrow(
        () ->
            User.revokeToken(testUsersRequestObjects.get(1).getId(), calendar.getTime()).request());
  }

  @DisplayName("Can revoke users tokens")
  @Test
  void whenRevokingUsersTokens_thenNoException() {
    Calendar calendar = new GregorianCalendar();
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    Assertions.assertDoesNotThrow(
        () ->
            User.revokeTokens(
                    Arrays.asList(
                        testUsersRequestObjects.get(1).getId(),
                        testUsersRequestObjects.get(2).getId()),
                    calendar.getTime())
                .request());
  }

  @DisplayName("Can generate a user token")
  @Test
  void whenGeneratingUserToken_thenNoException() {
    String userId = RandomStringUtils.randomAlphabetic(10);

    String token = createToken(userId, null, null);

    Assertions.assertEquals(197, token.length());
  }
}
