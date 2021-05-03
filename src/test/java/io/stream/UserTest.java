package io.stream;

import io.stream.models.User;
import io.stream.models.User.UserPartialUpdateRequestObject;
import io.stream.models.User.UserRequestObject;
import io.stream.models.User.UserUpsertRequestData.UserUpsertRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest extends BasicTest {

  @DisplayName("Can fetch user with no Exception")
  @Test
  void whenFechingServerUser_thenNoException() {}

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
                        .addUser(
                            UserRequestObject.builder()
                                .id(RandomStringUtils.randomAlphabetic(10))
                                .name("Samwise Gamgee")
                                .build())
                        .request())
            .getUsers()
            .values()
            .iterator()
            .next();
    Map<String, Object> addedValues = new HashMap<>();
    String addedKey = "extrafield";
    String addedValue = "extra value";
    addedValues.put(addedKey, addedValue);
    UserPartialUpdateRequestObject userPartialUpdateRequestObject =
        UserPartialUpdateRequestObject.builder()
            .id(user.getId())
            .unset(Arrays.asList("name"))
            .setValue(addedValues)
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

  @DisplayName("Can ban user with no Exception")
  @Test
  void whenBanUser_thenNoException() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.addUser(UserRequestObject.builder().id(userId).name("User to ban").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    Assertions.assertDoesNotThrow(
        () -> User.ban().userId(testUserRequestObject.getId()).targetUserId(userId).request());
  }

  @DisplayName("Can list banned user with no Exception")
  @Test
  void whenListingBannedUsers_thenNoException() {
    String userId = RandomStringUtils.randomAlphabetic(10);
    UserUpsertRequest usersUpsertRequest = User.upsert();
    usersUpsertRequest.addUser(UserRequestObject.builder().id(userId).name("User to ban").build());
    Assertions.assertDoesNotThrow(() -> usersUpsertRequest.request());
    Assertions.assertDoesNotThrow(
        () -> User.ban().userId(testUserRequestObject.getId()).targetUserId(userId).request());
    Assertions.assertDoesNotThrow(() -> User.queryBanned().request());
  }
}
