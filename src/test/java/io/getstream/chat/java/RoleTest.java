package io.getstream.chat.java;

import io.getstream.chat.java.models.Role;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoleTest extends BasicTest {

  @DisplayName("Can create a role")
  @Test
  void whenCreatingRole_thenNoException() {
    Assertions.assertDoesNotThrow(
        () -> Role.create().name(RandomStringUtils.randomAlphabetic(5)).request());
  }

  @DisplayName("Can delete a role")
  @Test
  void whenDeletingRole_thenDeleted() {
    String name = RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(() -> Role.create().name(name).request());
    pause();
    Assertions.assertDoesNotThrow(() -> Role.delete(name).request());
    pause();
    List<String> roles = Assertions.assertDoesNotThrow(() -> Role.list().request()).getRoles();
    Assertions.assertFalse(roles.contains(name));
  }

  @DisplayName("Can list roles")
  @Test
  void whenListingRole_thenCanRetrieve() {
    String name = RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(() -> Role.create().name(name).request());
    pause();
    List<String> roles = Assertions.assertDoesNotThrow(() -> Role.list().request()).getRoles();
    Assertions.assertTrue(roles.contains(name));
  }
}
