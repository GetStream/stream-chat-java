package io.getstream.chat.java;

import io.getstream.chat.java.models.Role;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RoleTest extends BasicTest {
  @DisplayName("Can delete a role")
  @Test
  void whenDeletingRole_thenDeleted() {
    String name = RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(() -> Role.create().name(name).request());
    pause();
    Assertions.assertDoesNotThrow(() -> Role.delete(name).request());
    pause();
    List<Role> roles = Assertions.assertDoesNotThrow(() -> Role.list().request()).getRoles();
    Assertions.assertFalse(roles.stream().anyMatch(role -> role.getName().equals(name)));
  }

  @DisplayName("Can list roles")
  @Test
  void whenListingRole_thenCanRetrieve() {
    String name = RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(() -> Role.create().name(name).request());
    pause();
    List<Role> roles = Assertions.assertDoesNotThrow(() -> Role.list().request()).getRoles();
    Assertions.assertTrue(roles.stream().anyMatch(role -> role.getName().equals(name)));
    for (int i = 0; i < roles.size(); i++) {
      Role role = roles.get(i);
      if (role.getCustom()) {
        Assertions.assertDoesNotThrow(() -> Role.delete(role.getName()).request());
      }
    }
  }
}
