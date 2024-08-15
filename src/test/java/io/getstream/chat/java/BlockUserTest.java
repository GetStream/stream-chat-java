package io.getstream.chat.java;

import io.getstream.chat.java.models.*;
import io.getstream.chat.java.models.BlockUser.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlockUserTest extends BasicTest {
  @Test
  @DisplayName("Block User")
  void blockUserGetUnBlock() {
    Assertions.assertDoesNotThrow(
        () -> {
          var channel = Assertions.assertDoesNotThrow(BasicTest::createRandomChannel).getChannel();
          Assertions.assertNotNull(channel);

          var blockingUser = testUsersRequestObjects.get(0);
          var blockedUser = testUsersRequestObjects.get(1);
          BlockUser.BlockUserRequestData.BlockUserRequest blockRequest =
              BlockUser.blockUser().blockedUserID(blockedUser.getId()).userID(blockingUser.getId());
          BlockUser.BlockUserResponse blockResponse = blockRequest.request();
          Assertions.assertEquals(blockResponse.getBlockedByUserID(), blockingUser.getId());
          Assertions.assertEquals(blockResponse.getBlockedUserID(), blockedUser.getId());
          Assertions.assertNotNull(blockResponse.getCreatedAt());

          BlockUser.GetBlockedUsersRequestData.GetBlockedUsersRequest getBlockedUsersRequest =
              BlockUser.getBlockedUsers(blockingUser.getId());

          BlockUser.GetBlockedUsersResponse getBlockedUsersResponse =
              getBlockedUsersRequest.request();
          Assertions.assertFalse(getBlockedUsersResponse.getBlockedUsers().isEmpty());
          Assertions.assertEquals(
              getBlockedUsersResponse.getBlockedUsers().get(0).getBlockedUserID(),
              blockedUser.getId());

          var users = User.list().filterCondition("id", blockingUser.getId()).request();
          Assertions.assertNotNull(users.getUsers().get(0).getBlockedUserIDs());
          Assertions.assertEquals(
              users.getUsers().get(0).getBlockedUserIDs().get(0), blockedUser.getId());

          // Unblocking the user
          BlockUser.UnblockUserRequestData.UnblockUserRequest unblockRequest =
              BlockUser.unblockUser()
                  .blockedUserID(blockedUser.getId())
                  .userID(blockingUser.getId());

          BlockUser.UnblockUserResponse unblockResponse = unblockRequest.request();
          Assertions.assertNotNull(unblockResponse);

          // Verify user is unblocked
          getBlockedUsersRequest = BlockUser.getBlockedUsers(blockingUser.getId());

          getBlockedUsersResponse = getBlockedUsersRequest.request();
          Assertions.assertTrue(getBlockedUsersResponse.getBlockedUsers().isEmpty());

          users = User.list().filterCondition("id", blockingUser.getId()).request();
          Assertions.assertTrue(
              users.getUsers().get(0).getBlockedUserIDs() == null
                  || users.getUsers().get(0).getBlockedUserIDs().isEmpty());
        });
  }
}
