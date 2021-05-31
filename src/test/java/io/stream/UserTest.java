package io.stream;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.User;
import io.getstream.chat.java.models.User.Ban;
import io.getstream.chat.java.models.User.ChannelMuteRequestObject;
import io.getstream.chat.java.models.User.OwnUser;
import io.getstream.chat.java.models.User.OwnUserRequestObject;
import io.getstream.chat.java.models.User.UserMute;
import io.getstream.chat.java.models.User.UserPartialUpdateRequestObject;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.User.UserUpsertRequestData.UserUpsertRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest extends BasicTest {

  @DisplayName("Can list users with no Exception")
  @Test
  void whenListingUsers_thenNoException() {
    Assertions.assertDoesNotThrow(() -> User.list().userId("u1").request());
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
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
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
}
