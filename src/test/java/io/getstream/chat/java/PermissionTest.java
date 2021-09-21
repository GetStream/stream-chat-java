package io.getstream.chat.java;

import io.getstream.chat.java.models.Permission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PermissionTest extends BasicTest {

  @DisplayName("Can create permission")
  @Test
  void whenCreatingPermission_thenNoException() {
    String name = RandomStringUtils.randomAlphabetic(10);
    Map<String, Object> condition = new HashMap<>();
    condition.put("$subject.magic_custom_field", "magic_value");
    Assertions.assertDoesNotThrow(
        () ->
            Permission.create()
                .id(RandomStringUtils.randomAlphabetic(10))
                .name(name)
                .action("DeleteChannel")
                .condition(condition)
                .request());
  }

  @DisplayName("Can update permission")
  @Test
  void whenUpdatingPermission_thenNoException() {
    String name = RandomStringUtils.randomAlphabetic(10);
    String id = RandomStringUtils.randomAlphabetic(10);
    Map<String, Object> condition = new HashMap<>();
    condition.put("$subject.magic_custom_field", "magic_value");
    Assertions.assertDoesNotThrow(
        () -> Permission.create().id(id).name(name).action("DeleteChannel").condition(condition).request());
    pause();
    Assertions.assertDoesNotThrow(
        () -> Permission.update(id, name).action("DeleteChannel").condition(condition).owner(true).request());
  }

  @DisplayName("Can retrieve permission")
  @Test
  void whenRetrievingPermission_thenCorrectName() {
    String name = RandomStringUtils.randomAlphabetic(10);
    String id = RandomStringUtils.randomAlphabetic(10);
    Map<String, Object> condition = new HashMap<>();
    condition.put("$subject.magic_custom_field", "magic_value");
    Assertions.assertDoesNotThrow(
        () -> Permission.create().id(id).name(name).action("DeleteChannel").condition(condition).request());
    pause();
    Permission retrievedPermission =
        Assertions.assertDoesNotThrow(() -> Permission.get(id).request()).getPermission();
    Assertions.assertEquals(id, retrievedPermission.getId());
  }

  @DisplayName("Can delete permission")
  @Test
  void whenDeletingPermission_thenDeleted() {
    String name = RandomStringUtils.randomAlphabetic(10);
    String id = RandomStringUtils.randomAlphabetic(10);
    Map<String, Object> condition = new HashMap<>();
    condition.put("$subject.magic_custom_field", "magic_value");
    Assertions.assertDoesNotThrow(
        () -> Permission.create().id(id).name(name).action("DeleteChannel").condition(condition).request());
    pause();
    Assertions.assertDoesNotThrow(() -> Permission.delete(id).request());
    pause();
    List<Permission> permissions =
        Assertions.assertDoesNotThrow(() -> Permission.list().request()).getPermissions();
    Assertions.assertFalse(
        permissions.stream()
            .anyMatch(consideredPermission -> consideredPermission.getId().equals(id)));
  }

  @DisplayName("Can list permissions")
  @Test
  void whenListingPermission_thenCanRetrieve() {
    String name = RandomStringUtils.randomAlphabetic(10);
    String id = RandomStringUtils.randomAlphabetic(10);
    Map<String, Object> condition = new HashMap<>();
    condition.put("$subject.magic_custom_field", "magic_value");
    Assertions.assertDoesNotThrow(
        () -> Permission.create().id(id).name(name).action("DeleteChannel").condition(condition).request());
    pause();
    List<Permission> permissions =
        Assertions.assertDoesNotThrow(() -> Permission.list().request()).getPermissions();
    Assertions.assertTrue(
        permissions.stream()
            .anyMatch(consideredPermission -> consideredPermission.getId().equals(id)));
  }
}
