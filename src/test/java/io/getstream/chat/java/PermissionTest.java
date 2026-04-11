package io.getstream.chat.java;

import io.getstream.chat.java.models.Permission;
import java.util.HashMap;
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
}
