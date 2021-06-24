package io.getstream.chat.java;

import io.getstream.chat.java.models.Permission;
import io.getstream.chat.java.models.Permission.Resource;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PermissionTest extends BasicTest {

  @DisplayName("Can create permission")
  @Test
  void whenCreatingPermission_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            Permission.create()
                .name(RandomStringUtils.randomAlphabetic(10))
                .resource(Resource.ADD_LINKS)
                .request());
  }

  @DisplayName("Can update permission")
  @Test
  void whenUpdatingPermission_thenNoException() {
    String name = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> Permission.create().name(name).resource(Resource.ADD_LINKS).request());
    pause();
    Assertions.assertDoesNotThrow(
        () -> Permission.update(name).resource(Resource.ADD_LINKS).owner(true).request());
  }

  @DisplayName("Can retrieve permission")
  @Test
  void whenRetrievingPermission_thenCorrectName() {
    String name = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> Permission.create().name(name).resource(Resource.ADD_LINKS).request());
    pause();
    Permission retrievedPermission =
        Assertions.assertDoesNotThrow(() -> Permission.get(name).request()).getPermission();
    Assertions.assertEquals(name, retrievedPermission.getName());
  }

  @DisplayName("Can delete permission")
  @Test
  void whenDeletingPermission_thenDeleted() {
    String name = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> Permission.create().name(name).resource(Resource.ADD_LINKS).request());
    pause();
    Assertions.assertDoesNotThrow(() -> Permission.delete(name).request());
    pause();
    List<Permission> permissions =
        Assertions.assertDoesNotThrow(() -> Permission.list().request()).getPermissions();
    Assertions.assertFalse(
        permissions.stream()
            .anyMatch(consideredPermission -> consideredPermission.getName().equals(name)));
  }

  @DisplayName("Can list permissions")
  @Test
  void whenListingPermission_thenCanRetrieve() {
    String name = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> Permission.create().name(name).resource(Resource.ADD_LINKS).request());
    pause();
    List<Permission> permissions =
        Assertions.assertDoesNotThrow(() -> Permission.list().request()).getPermissions();
    Assertions.assertTrue(
        permissions.stream()
            .anyMatch(consideredPermission -> consideredPermission.getName().equals(name)));
  }
}
