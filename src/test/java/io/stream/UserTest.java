package io.stream;

import io.stream.models.User;
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
    Assertions.assertDoesNotThrow(() -> User.list().withUserId("u1").request());
  }
}
