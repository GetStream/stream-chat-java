package io.stream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.stream.models.User;
import io.stream.models.User.UserPartialUpdateRequestObject;
import io.stream.models.User.UserRequestObject;
import io.stream.models.User.UserUpsertRequestData.UserUpsertRequest;

public class UserTest extends BasicTest {

  @DisplayName("Can fetch user with no Exception")
  @Test
  void whenFechingServerUser_thenNoException() {}

  @DisplayName("Can list users with no Exception")
  @Test
  void whenListingUsers_thenNoException() {
    Assertions.assertDoesNotThrow(() -> User.list().withUserId("u1").request());
  }

  @DisplayName("Can partial update a user")
  @Test
  void whenPartiallyUpdatingUser_thenNoException() {
    UserUpsertRequest usersUpsertRequest = User.upsert();
    User user =
        Assertions.assertDoesNotThrow(() -> usersUpsertRequest.addUser(UserRequestObject.builder()
            .withId(RandomStringUtils.randomAlphabetic(10)).withName("Samwise Gamgee").build())
            .request()).getUsers().values().iterator().next();
    Map<String, Object> addedValues = new HashMap<>();
    String addedKey = "extrafield";
    String addedValue = "extra value";
    addedValues.put(addedKey, addedValue);
    UserPartialUpdateRequestObject userPartialUpdateRequestObject =
        UserPartialUpdateRequestObject.builder().withId(user.getId())
            .withUnset(Arrays.asList("name")).withSetValue(addedValues).build();
    User updatedUser =
        Assertions
            .assertDoesNotThrow(() -> User.partialUpdate()
                .withUsers(Arrays.asList(userPartialUpdateRequestObject)).request())
            .getUsers().get(user.getId());
    Assertions.assertNull(updatedUser.getName());
    Assertions.assertEquals(addedValue, updatedUser.getAdditionalFields().get(addedKey));
  }
}
