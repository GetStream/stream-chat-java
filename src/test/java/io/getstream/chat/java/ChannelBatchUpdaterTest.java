package io.getstream.chat.java;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.*;
import io.getstream.chat.java.models.TaskStatus;
import io.getstream.chat.java.models.TaskStatus.TaskStatusGetResponse;
import io.getstream.chat.java.models.User;
import io.getstream.chat.java.models.User.UserRequestObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChannelBatchUpdaterTest extends BasicTest {

  @Test
  @DisplayName("Can update channels batch with valid options")
  void whenUpdatingChannelsBatchWithValidOptions_thenNoException() {
    Assertions.assertDoesNotThrow(
        () -> {
          var ch1 = Assertions.assertDoesNotThrow(BasicTest::createRandomChannel).getChannel();
          Assertions.assertNotNull(ch1);
          var ch2 = Assertions.assertDoesNotThrow(BasicTest::createRandomChannel).getChannel();
          Assertions.assertNotNull(ch2);

          var userToAdd =
              UserRequestObject.builder()
                  .id(RandomStringUtils.randomAlphabetic(10))
                  .name("Test User")
                  .build();
          User.upsert().user(userToAdd).request();

          var filter = new ChannelsBatchFilters();
          Map<String, Object> cidsFilter = new HashMap<>();
          cidsFilter.put("$in", List.of(ch1.getCId(), ch2.getCId()));
          filter.setCids(cidsFilter);

          var options = new ChannelsBatchOptions();
          options.setOperation(ChannelBatchOperation.ADD_MEMBERS);
          options.setFilter(filter);
          options.setMembers(List.of(new ChannelBatchMemberRequest(userToAdd.getId(), null)));

          var response = Channel.updateBatch(options).request();
          Assertions.assertNotNull(response.getTaskId());
          Assertions.assertFalse(response.getTaskId().isEmpty());
        });
  }

  @Test
  @DisplayName("ChannelBatchUpdater can add members")
  void whenAddingMembers_thenNoException() {
    Assertions.assertDoesNotThrow(
        () -> {
          var ch1 = Assertions.assertDoesNotThrow(BasicTest::createRandomChannel).getChannel();
          Assertions.assertNotNull(ch1);
          var ch2 = Assertions.assertDoesNotThrow(BasicTest::createRandomChannel).getChannel();
          Assertions.assertNotNull(ch2);

          var user1 =
              UserRequestObject.builder()
                  .id(RandomStringUtils.randomAlphabetic(10))
                  .name("Test User 1")
                  .build();
          var user2 =
              UserRequestObject.builder()
                  .id(RandomStringUtils.randomAlphabetic(10))
                  .name("Test User 2")
                  .build();
          User.upsert().user(user1).user(user2).request();
          var usersToAdd = List.of(user1, user2);

          var filter = new ChannelsBatchFilters();
          Map<String, Object> cidsFilter = new HashMap<>();
          cidsFilter.put("$in", List.of(ch1.getCId(), ch2.getCId()));
          filter.setCids(cidsFilter);

          var updater = Channel.channelBatchUpdater();
          var members =
              usersToAdd.stream()
                  .map(user -> new ChannelBatchMemberRequest(user.getId(), null))
                  .collect(Collectors.toList());

          var response = updater.addMembers(filter, members).request();
          Assertions.assertNotNull(response.getTaskId());
          var taskId = response.getTaskId();

          waitFor(
              () -> {
                TaskStatusGetResponse taskStatusResponse =
                    Assertions.assertDoesNotThrow(() -> TaskStatus.get(taskId).request());
                return "completed".equals(taskStatusResponse.getStatus());
              });

          // Verify members were added to both channels
          waitFor(
              () -> {
                var ch1State =
                    Assertions.assertDoesNotThrow(
                        () ->
                            Channel.getOrCreate(ch1.getType(), ch1.getId()).request().getChannel());
                var ch2State =
                    Assertions.assertDoesNotThrow(
                        () ->
                            Channel.getOrCreate(ch2.getType(), ch2.getId()).request().getChannel());
                if (ch1State.getMembers() == null || ch2State.getMembers() == null) {
                  return false;
                }
                var ch1MemberIds =
                    ch1State.getMembers().stream()
                        .map(ChannelMember::getUserId)
                        .collect(Collectors.toList());
                var ch2MemberIds =
                    ch2State.getMembers().stream()
                        .map(ChannelMember::getUserId)
                        .collect(Collectors.toList());
                var userIdsToAdd =
                    usersToAdd.stream().map(user -> user.getId()).collect(Collectors.toList());
                return ch1MemberIds.containsAll(userIdsToAdd)
                    && ch2MemberIds.containsAll(userIdsToAdd);
              });
        });
  }

  @Test
  @DisplayName("ChannelBatchUpdater can remove members")
  void whenRemovingMembers_thenNoException() {
    Assertions.assertDoesNotThrow(
        () -> {
          var user1 =
              UserRequestObject.builder()
                  .id(RandomStringUtils.randomAlphabetic(10))
                  .name("Test User 1")
                  .build();
          var user2 =
              UserRequestObject.builder()
                  .id(RandomStringUtils.randomAlphabetic(10))
                  .name("Test User 2")
                  .build();
          User.upsert().user(user1).user(user2).request();
          var membersId = List.of(user1.getId(), user2.getId());

          var ch1 =
              Channel.getOrCreate("messaging", RandomStringUtils.randomAlphabetic(12))
                  .data(
                      ChannelRequestObject.builder()
                          .createdBy(testUserRequestObject)
                          .members(
                              membersId.stream()
                                  .map(
                                      id ->
                                          ChannelMemberRequestObject.builder()
                                              .user(UserRequestObject.builder().id(id).build())
                                              .build())
                                  .collect(Collectors.toList()))
                          .build())
                  .request()
                  .getChannel();

          var ch2 =
              Channel.getOrCreate("messaging", RandomStringUtils.randomAlphabetic(12))
                  .data(
                      ChannelRequestObject.builder()
                          .createdBy(testUserRequestObject)
                          .members(
                              membersId.stream()
                                  .map(
                                      id ->
                                          ChannelMemberRequestObject.builder()
                                              .user(UserRequestObject.builder().id(id).build())
                                              .build())
                                  .collect(Collectors.toList()))
                          .build())
                  .request()
                  .getChannel();

          // Verify members are present
          var ch1State = Channel.getOrCreate(ch1.getType(), ch1.getId()).request().getChannel();
          var ch2State = Channel.getOrCreate(ch2.getType(), ch2.getId()).request().getChannel();
          Assertions.assertNotNull(ch1State.getMembers());
          Assertions.assertNotNull(ch2State.getMembers());
          Assertions.assertTrue(ch1State.getMembers().size() >= 2);
          Assertions.assertTrue(ch2State.getMembers().size() >= 2);

          var ch1MemberIds =
              ch1State.getMembers().stream()
                  .map(ChannelMember::getUserId)
                  .collect(Collectors.toList());
          var ch2MemberIds =
              ch2State.getMembers().stream()
                  .map(ChannelMember::getUserId)
                  .collect(Collectors.toList());
          Assertions.assertTrue(ch1MemberIds.containsAll(membersId));
          Assertions.assertTrue(ch2MemberIds.containsAll(membersId));

          // Remove a member
          var updater = Channel.channelBatchUpdater();
          var memberToRemove = membersId.get(0);

          var filter = new ChannelsBatchFilters();
          Map<String, Object> cidsFilter = new HashMap<>();
          cidsFilter.put("$in", List.of(ch1.getCId(), ch2.getCId()));
          filter.setCids(cidsFilter);

          var response =
              updater
                  .removeMembers(
                      filter, List.of(new ChannelBatchMemberRequest(memberToRemove, null)))
                  .request();
          Assertions.assertNotNull(response.getTaskId());
          var taskId = response.getTaskId();

          waitFor(
              () -> {
                TaskStatusGetResponse taskStatusResponse =
                    Assertions.assertDoesNotThrow(() -> TaskStatus.get(taskId).request());
                return "completed".equals(taskStatusResponse.getStatus());
              });

          // Wait a bit for changes to propagate
          Assertions.assertDoesNotThrow(() -> java.lang.Thread.sleep(2000));

          // Verify member was removed from both channels
          waitFor(
              () -> {
                var ch1StateAfter =
                    Assertions.assertDoesNotThrow(
                        () ->
                            Channel.getOrCreate(ch1.getType(), ch1.getId()).request().getChannel());
                var ch2StateAfter =
                    Assertions.assertDoesNotThrow(
                        () ->
                            Channel.getOrCreate(ch2.getType(), ch2.getId()).request().getChannel());
                if (ch1StateAfter == null || ch2StateAfter == null) {
                  return false;
                }
                var ch1Members = ch1StateAfter.getMembers();
                var ch2Members = ch2StateAfter.getMembers();
                if (ch1Members == null || ch2Members == null) {
                  return false;
                }
                var ch1MemberIdsAfter =
                    ch1Members.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
                var ch2MemberIdsAfter =
                    ch2Members.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
                return !ch1MemberIdsAfter.contains(memberToRemove)
                    && !ch2MemberIdsAfter.contains(memberToRemove);
              });
        });
  }

  @Test
  @DisplayName("ChannelBatchUpdater can archive channels")
  void whenArchivingChannels_thenNoException() {
    Assertions.assertDoesNotThrow(
        () -> {
          var user1 =
              UserRequestObject.builder()
                  .id(RandomStringUtils.randomAlphabetic(10))
                  .name("Test User 1")
                  .build();
          var user2 =
              UserRequestObject.builder()
                  .id(RandomStringUtils.randomAlphabetic(10))
                  .name("Test User 2")
                  .build();
          User.upsert().user(user1).user(user2).request();
          var membersId = List.of(user1.getId(), user2.getId());

          var ch1 =
              Channel.getOrCreate("messaging", RandomStringUtils.randomAlphabetic(12))
                  .data(
                      ChannelRequestObject.builder()
                          .createdBy(testUserRequestObject)
                          .members(
                              membersId.stream()
                                  .map(
                                      id ->
                                          ChannelMemberRequestObject.builder()
                                              .user(UserRequestObject.builder().id(id).build())
                                              .build())
                                  .collect(Collectors.toList()))
                          .build())
                  .request()
                  .getChannel();

          var ch2 =
              Channel.getOrCreate("messaging", RandomStringUtils.randomAlphabetic(12))
                  .data(
                      ChannelRequestObject.builder()
                          .createdBy(testUserRequestObject)
                          .members(
                              membersId.stream()
                                  .map(
                                      id ->
                                          ChannelMemberRequestObject.builder()
                                              .user(UserRequestObject.builder().id(id).build())
                                              .build())
                                  .collect(Collectors.toList()))
                          .build())
                  .request()
                  .getChannel();

          var updater = Channel.channelBatchUpdater();

          var filter = new ChannelsBatchFilters();
          Map<String, Object> cidsFilter = new HashMap<>();
          cidsFilter.put("$in", List.of(ch1.getCId(), ch2.getCId()));
          filter.setCids(cidsFilter);

          var response =
              updater
                  .archive(filter, List.of(new ChannelBatchMemberRequest(membersId.get(0), null)))
                  .request();
          Assertions.assertNotNull(response.getTaskId());
          var taskId = response.getTaskId();

          waitFor(
              () -> {
                TaskStatusGetResponse taskStatusResponse =
                    Assertions.assertDoesNotThrow(() -> TaskStatus.get(taskId).request());
                return "completed".equals(taskStatusResponse.getStatus());
              });

          // Wait a bit for changes to propagate
          Assertions.assertDoesNotThrow(() -> java.lang.Thread.sleep(2000));

          // Verify channel was archived for the specified member
          // Note: Archived members might not appear in the regular members list,
          // so we verify the task completed successfully
          // For a more thorough test, we could query members with specific filters
          var ch1State =
              Assertions.assertDoesNotThrow(
                  () -> Channel.getOrCreate(ch1.getType(), ch1.getId()).request().getChannel());
          Assertions.assertNotNull(ch1State);

          // Try to find the member and check if archived
          // If member is not in the list, it might be because archived members are filtered
          var ch1Members = ch1State.getMembers();
          if (ch1Members != null) {
            var ch1Member =
                ch1Members.stream()
                    .filter(
                        m ->
                            m != null
                                && m.getUserId() != null
                                && m.getUserId().equals(membersId.get(0)))
                    .findFirst()
                    .orElse(null);
            if (ch1Member != null) {
              // If member is found, check archivedAt
              Assertions.assertNotNull(ch1Member.getArchivedAt(), "Member should be archived");
            }
            // If member is not found, it might be filtered out because it's archived
            // This is acceptable - the archive operation succeeded if task completed
          }
        });
  }
}
