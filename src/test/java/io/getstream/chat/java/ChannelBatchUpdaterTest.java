package io.getstream.chat.java;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.*;
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

          waitForTaskCompletion(taskId);

          // Verify members were added to both channels
          waitFor(
              () -> {
                var ch1Members =
                    Assertions.assertDoesNotThrow(
                            () ->
                                Channel.queryMembers()
                                    .id(ch1.getId())
                                    .type(ch1.getType())
                                    .request())
                        .getMembers();
                var ch2Members =
                    Assertions.assertDoesNotThrow(
                            () ->
                                Channel.queryMembers()
                                    .id(ch2.getId())
                                    .type(ch2.getType())
                                    .request())
                        .getMembers();
                if (ch1Members == null || ch2Members == null) {
                  return false;
                }
                var ch1MemberIds =
                    ch1Members.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
                var ch2MemberIds =
                    ch2Members.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
                var userIdsToAdd =
                    usersToAdd.stream().map(user -> user.getId()).collect(Collectors.toList());
                return ch1MemberIds.containsAll(userIdsToAdd)
                    && ch2MemberIds.containsAll(userIdsToAdd);
              },
              1000L,
              120000L);
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
          waitFor(
              () -> {
                var ch1Members =
                    Assertions.assertDoesNotThrow(
                            () ->
                                Channel.queryMembers()
                                    .id(ch1.getId())
                                    .type(ch1.getType())
                                    .request())
                        .getMembers();
                var ch2Members =
                    Assertions.assertDoesNotThrow(
                            () ->
                                Channel.queryMembers()
                                    .id(ch2.getId())
                                    .type(ch2.getType())
                                    .request())
                        .getMembers();
                if (ch1Members == null || ch2Members == null) {
                  return false;
                }
                var ch1MemberIds =
                    ch1Members.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
                var ch2MemberIds =
                    ch2Members.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
                return ch1MemberIds.containsAll(membersId) && ch2MemberIds.containsAll(membersId);
              },
              1000L,
              120000L);

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

          waitForTaskCompletion(taskId);

          // Wait a bit for changes to propagate
          Assertions.assertDoesNotThrow(() -> java.lang.Thread.sleep(2000));

          // Verify member was removed from both channels
          waitFor(
              () -> {
                var ch1Members =
                    Assertions.assertDoesNotThrow(
                            () ->
                                Channel.queryMembers()
                                    .id(ch1.getId())
                                    .type(ch1.getType())
                                    .request())
                        .getMembers();
                var ch2Members =
                    Assertions.assertDoesNotThrow(
                            () ->
                                Channel.queryMembers()
                                    .id(ch2.getId())
                                    .type(ch2.getType())
                                    .request())
                        .getMembers();
                if (ch1Members == null || ch2Members == null) {
                  return false;
                }
                var ch1MemberIdsAfter =
                    ch1Members.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
                var ch2MemberIdsAfter =
                    ch2Members.stream().map(ChannelMember::getUserId).collect(Collectors.toList());
                return !ch1MemberIdsAfter.contains(memberToRemove)
                    && !ch2MemberIdsAfter.contains(memberToRemove);
              },
              1000L,
              120000L);
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

          waitForTaskCompletion(taskId);

          // Wait a bit for changes to propagate
          Assertions.assertDoesNotThrow(() -> java.lang.Thread.sleep(2000));

          // Verify channel was archived for the specified member
          waitFor(
              () -> {
                var ch1Members =
                    Assertions.assertDoesNotThrow(
                            () ->
                                Channel.queryMembers()
                                    .id(ch1.getId())
                                    .type(ch1.getType())
                                    .request())
                        .getMembers();
                if (ch1Members == null) {
                  return false;
                }
                var ch1Member =
                    ch1Members.stream()
                        .filter(
                            m ->
                                m != null
                                    && m.getUserId() != null
                                    && m.getUserId().equals(membersId.get(0)))
                        .findFirst()
                        .orElse(null);
                return ch1Member != null && ch1Member.getArchivedAt() != null;
              },
              1000L,
              120000L);
        });
  }
}
