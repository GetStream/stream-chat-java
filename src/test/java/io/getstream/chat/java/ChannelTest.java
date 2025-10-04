package io.getstream.chat.java;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.*;
import io.getstream.chat.java.models.DeleteStrategy;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Sort;
import io.getstream.chat.java.models.Sort.Direction;
import io.getstream.chat.java.models.User;
import io.getstream.chat.java.models.User.ChannelMute;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChannelTest extends BasicTest {

  @DisplayName("Can retrieve channel by type")
  @Test
  void whenRetrievingChannel_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            Channel.getOrCreate(testChannel.getType(), null)
                .data(
                    ChannelRequestObject.builder()
                        .createdBy(testUserRequestObject)
                        .members(buildChannelMembersList())
                        .build())
                .request());
  }

  @DisplayName("Can create channel with invites")
  @Test
  void whenCreatingChannelWithInvites_thenNoException() {
    var lastUser = testUsersRequestObjects.get(testUsersRequestObjects.size() - 1);
    // invite last user and add the rest as members
    var channelReq =
        ChannelRequestObject.builder()
            .createdBy(testUserRequestObject)
            .members(
                testUsersRequestObjects.stream()
                    .limit(testUsersRequestObjects.size() - 1)
                    .map(user -> ChannelMemberRequestObject.builder().user(user).build())
                    .collect(Collectors.toList()))
            .invite(ChannelMemberRequestObject.builder().user(lastUser).build())
            .build();
    var channel =
        Assertions.assertDoesNotThrow(
            () -> Channel.getOrCreate(testChannel.getType()).data(channelReq).request());

    var invitedMember =
        channel.getMembers().stream()
            .filter(
                channelMember -> {
                  var invited = channelMember.getInvited();
                  return invited != null && invited;
                })
            .findFirst()
            .get();

    Assertions.assertNotNull(invitedMember);
    Assertions.assertEquals(invitedMember.getUserId(), lastUser.getId());
  }

  @DisplayName("Can set custom field on channel")
  @Test
  void whenSettingCustomField_thenNoException() {
    var channelReq =
        ChannelRequestObject.builder()
            .createdBy(testUserRequestObject)
            .members(buildChannelMembersList())
            .build();
    channelReq.setAdditionalField("fieldkey", "fieldvalue");
    var channel =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.getOrCreate(testChannel.getType(), RandomStringUtils.randomAlphabetic(12))
                    .data(channelReq)
                    .request());
    Assertions.assertEquals(
        "fieldvalue", channel.getChannel().getAdditionalFields().get("fieldkey"));
  }

  @DisplayName("Can set custom field on channel after fields have already been set in builder")
  @Test
  void whenSettingCustomFieldInBuilderAndAfer_thenNoException() {
    var channelReq =
        ChannelRequestObject.builder()
            .createdBy(testUserRequestObject)
            .members(buildChannelMembersList())
            .additionalField("fieldkey1", "fieldvalue1")
            .build();
    channelReq.setAdditionalField("fieldkey2", "fieldvalue2");
    var channel =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.getOrCreate(testChannel.getType(), RandomStringUtils.randomAlphabetic(12))
                    .data(channelReq)
                    .request());
    Assertions.assertEquals(
        "fieldvalue1", channel.getChannel().getAdditionalFields().get("fieldkey1"));
    Assertions.assertEquals(
        "fieldvalue2", channel.getChannel().getAdditionalFields().get("fieldkey2"));
  }

  @DisplayName("Can add a moderator to a channel (update)")
  @Test
  void whenAddingModerator_thenHasModerator() {
    // We should not use testChannel to not modify it
    ChannelGetResponse channelGetResponse =
        Assertions.assertDoesNotThrow(() -> createRandomChannel());
    Assertions.assertEquals(0, countModerators(channelGetResponse.getMembers()));

    ChannelUpdateResponse channelUpdateResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.update(
                        channelGetResponse.getChannel().getType(),
                        channelGetResponse.getChannel().getId())
                    .user(testUserRequestObject)
                    .addModerators(Arrays.asList(testUserRequestObject.getId()))
                    .request());
    Assertions.assertEquals(1, countModerators(channelUpdateResponse.getMembers()));
  }

  private long countModerators(List<ChannelMember> members) {
    return members.stream()
        .filter(
            channelMember ->
                channelMember.getIsModerator() != null && channelMember.getIsModerator())
        .count();
  }

  @DisplayName("Can delete a channel")
  @Test
  void whenDeletingChannel_thenIsDeleted() {
    // We should not use testChannel to not delete it
    ChannelGetResponse channelGetResponse =
        Assertions.assertDoesNotThrow(() -> createRandomChannel());
    Assertions.assertNull(channelGetResponse.getChannel().getDeletedAt());
    Channel deletedChannel =
        Assertions.assertDoesNotThrow(
                () ->
                    Channel.delete(
                            channelGetResponse.getChannel().getType(),
                            channelGetResponse.getChannel().getId())
                        .request())
            .getChannel();
    Assertions.assertNotNull(deletedChannel.getDeletedAt());
  }

  @Test
  @DisplayName("Can delete many channels")
  void whenDeletingManyChannels_thenTaskIdIsReturned() {
    for (var deleteStrategy : List.of(DeleteStrategy.SOFT, DeleteStrategy.HARD)) {
      Assertions.assertDoesNotThrow(
          () -> {
            var ch1 = Assertions.assertDoesNotThrow(BasicTest::createRandomChannel).getChannel();
            Assertions.assertNotNull(ch1);
            var ch2 = Assertions.assertDoesNotThrow(BasicTest::createRandomChannel).getChannel();
            Assertions.assertNotNull(ch2);

            var cids = List.of(ch1.getCId(), ch2.getCId());
            var deleteManyResponse =
                Channel.deleteMany(cids).setDeleteStrategy(deleteStrategy).request();
            Assertions.assertNotNull(deleteManyResponse.getTaskId());
          });
    }
  }

  @DisplayName("Can list channels")
  @Test
  void whenListingChannels_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            Channel.list()
                .user(testUserRequestObject)
                .sort(Sort.builder().field("id").direction(Direction.DESC).build())
                .request());
  }

  @DisplayName("Can truncate channel")
  @Test
  void whenTruncateChannel_thenNoException() {
    // We should not use testChannel to not remove testMessage
    ChannelGetResponse channelGetResponse =
        Assertions.assertDoesNotThrow(() -> createRandomChannel());
    ChannelTruncateResponse resp =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.truncate(
                        channelGetResponse.getChannel().getType(),
                        channelGetResponse.getChannel().getId())
                    .request());
    Assertions.assertNotNull(resp.getChannel());
  }

  @DisplayName("Can truncate channel with options")
  @Test
  void whenTruncateChannelWithOptions_thenNoException() {
    // We should not use testChannel to not remove testMessage
    ChannelGetResponse channelGetResponse =
        Assertions.assertDoesNotThrow(() -> createRandomChannel());
    MessageRequestObject message =
        MessageRequestObject.builder()
            .text("Truncate channel")
            .userId(testUserRequestObject.getId())
            .build();
    ChannelTruncateResponse resp =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.truncate(
                        channelGetResponse.getChannel().getType(),
                        channelGetResponse.getChannel().getId())
                    .skipPush(true)
                    .message(message)
                    .request());
    Assertions.assertNotNull(resp.getChannel());
  }

  @DisplayName("Can query channel members")
  @Test
  void whenQueryingChannelMembers_thenRetrieveAll() {
    List<ChannelMember> channelMembers =
        Assertions.assertDoesNotThrow(
                () ->
                    Channel.queryMembers()
                        .id(testChannel.getId())
                        .type(testChannel.getType())
                        .request())
            .getMembers();
    Assertions.assertEquals(testUsersRequestObjects.size(), channelMembers.size());
  }

  @DisplayName("Can export channel")
  @Test
  void whenExportingChannel_thenNoException() {
    String taskId =
        Assertions.assertDoesNotThrow(
                () ->
                    Channel.export()
                        .channel(
                            ChannelExportRequestObject.builder()
                                .type(testChannel.getType())
                                .id(testChannel.getId())
                                .messagesUntil(new Date())
                                .build())
                        .request())
            .getTaskId();
    Assertions.assertNotNull(taskId);
  }

  @DisplayName("Can query the status of a channel export")
  @Test
  void whenQueryingExportChannelStatus_thenNoException() {
    String taskId =
        Assertions.assertDoesNotThrow(
                () ->
                    Channel.export()
                        .channel(
                            ChannelExportRequestObject.builder()
                                .type(testChannel.getType())
                                .id(testChannel.getId())
                                .build())
                        .exportUsers(false)
                        .request())
            .getTaskId();
    Assertions.assertDoesNotThrow(() -> Channel.exportStatus(taskId).request());
  }

  @DisplayName("Can hide a channel")
  @Test
  void whenHidingChannel_thenNoException() {
    // We should not use testChannel to not hide it
    ChannelGetResponse channelGetResponse =
        Assertions.assertDoesNotThrow(() -> createRandomChannel());
    Assertions.assertDoesNotThrow(
        () ->
            Channel.hide(
                    channelGetResponse.getChannel().getType(),
                    channelGetResponse.getChannel().getId())
                .user(testUserRequestObject)
                .request());
  }

  @DisplayName("Can mark all channel read")
  @Test
  void whenMarkingAllChannelsRead_thenNoException() {
    Assertions.assertDoesNotThrow(
        () -> Channel.markAllRead().user(testUserRequestObject).request());
  }

  @DisplayName("Can mark channel read")
  @Test
  void whenMarkingChannelsRead_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            Channel.markRead(testChannel.getType(), testChannel.getId())
                .user(testUserRequestObject)
                .request());
  }

  @DisplayName("Can mute a channel")
  @Test
  void whenMutingChannel_thenIsMuted() {
    // We should not use testChannel to not mute it
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    Assertions.assertFalse(isChannelMutedForTestUser(channel.getType(), channel.getId()));
    Assertions.assertDoesNotThrow(
        () ->
            Channel.mute()
                .channelCid(channel.getType() + ":" + channel.getId())
                .user(testUserRequestObject)
                .request());
    Assertions.assertTrue(isChannelMutedForTestUser(channel.getType(), channel.getId()));
    ChannelMember channelMember =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.queryMembers()
                    .id(channel.getId())
                    .type(channel.getType())
                    .request()
                    .getMembers()
                    .stream()
                    .filter(cm -> cm.getUserId().equals(testUserRequestObject.getId()))
                    .findFirst()
                    .get());
    Assertions.assertTrue(channelMember.getNotificationsMuted());

    ChannelMember chm =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.queryMembers()
                    .filterCondition("notifications_muted", true)
                    .id(channel.getId())
                    .type(channel.getType())
                    .request()
                    .getMembers()
                    .stream()
                    .filter(cm -> cm.getUserId().equals(testUserRequestObject.getId()))
                    .findFirst()
                    .get());
    Assertions.assertTrue(chm.getNotificationsMuted());
  }

  private boolean isChannelMutedForTestUser(String channelType, String channelId) {
    List<ChannelMute> channelMutes =
        Assertions.assertDoesNotThrow(
            () ->
                User.list()
                    .filterCondition("id", testUserRequestObject.getId())
                    .request()
                    .getUsers()
                    .get(0)
                    .getChannelMutes());
    return channelMutes != null
        && channelMutes.stream()
            .filter(
                channelMute ->
                    channelMute.getChannel().getId().equals(channelId)
                        && channelMute.getChannel().getType().equals(channelType))
            .findAny()
            .isPresent();
  }

  @DisplayName("Can show a channel")
  @Test
  void whenShowingChannel_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            Channel.show(testChannel.getType(), testChannel.getId())
                .user(testUserRequestObject)
                .request());
  }

  @DisplayName("Can unmute a channel")
  @Test
  void whenUnMutingChannel_thenIsNotMutedAnymore() {
    // We should not use testChannel to not mute it
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    Assertions.assertFalse(isChannelMutedForTestUser(channel.getType(), channel.getId()));
    Assertions.assertFalse(isChannelMutedForTestUser(channel.getType(), channel.getId()));
    Assertions.assertDoesNotThrow(
        () ->
            Channel.mute()
                .channelCid(channel.getType() + ":" + channel.getId())
                .user(testUserRequestObject)
                .request());
    Assertions.assertTrue(isChannelMutedForTestUser(channel.getType(), channel.getId()));
    Assertions.assertDoesNotThrow(
        () ->
            Channel.unmute()
                .channelCid(channel.getType() + ":" + channel.getId())
                .user(testUserRequestObject)
                .request());
    Assertions.assertFalse(isChannelMutedForTestUser(channel.getType(), channel.getId()));
  }

  @DisplayName("Can update a channel (partial update)")
  @Test
  void whenUpdatingTeamWithPartialUpdate_thenIsUpdated() {
    // We should not use testChannel to not modify it
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    String updatedTeam = RandomStringUtils.randomAlphabetic(10);
    Channel updateChannel =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.partialUpdate(channel.getType(), channel.getId())
                    .setValue("team", updatedTeam)
                    .user(testUserRequestObject)
                    .request()
                    .getChannel());
    Assertions.assertEquals(updatedTeam, updateChannel.getTeam());
  }

  @DisplayName("Can assign roles")
  @Test
  void whenAssigningRole_throwsNoError() {
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    var assignment = new RoleAssignment();
    assignment.setChannelRole("channel_moderator");
    assignment.setUserId(testUserRequestObject.getId());

    Assertions.assertDoesNotThrow(
        () ->
            Channel.assignRoles(channel.getType(), channel.getId())
                .assignRole(assignment)
                .request());
  }

  @DisplayName("Can update a channel member partially")
  @Test
  void whenUpdatingChannelMemberPartially_thenIsUpdated() {
    // We should not use testChannel to not modify it
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    ChannelMemberResponse channelMemberResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.updateMemberPartial(
                        channel.getType(), channel.getId(), testUserRequestObject.getId())
                    .setValue("custom_key", "custom_value")
                    .setValue("channel_role", "channel_moderator")
                    .request());

    Assertions.assertEquals(
        "custom_value", channelMemberResponse.getMember().getAdditionalFields().get("custom_key"));
    Assertions.assertEquals(
        "channel_moderator", channelMemberResponse.getMember().getChannelRole());

    // unset
    channelMemberResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.updateMemberPartial(
                        channel.getType(), channel.getId(), testUserRequestObject.getId())
                    .unsetValue("custom_key")
                    .request());

    Assertions.assertNull(
        channelMemberResponse.getMember().getAdditionalFields().get("custom_key"));
  }

  @DisplayName("Can pin and unpin a channel")
  @Test
  void whenPinningAndUnpinningAChannel_thenIsPinnedAndUnpinned() {
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    ChannelMemberResponse channelMemberResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.pin(channel.getType(), channel.getId(), testUserRequestObject.getId())
                    .request());
    Date pinnedAt = channelMemberResponse.getMember().getPinnedAt();
    Assertions.assertNotNull(pinnedAt);

    channelMemberResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.unpin(channel.getType(), channel.getId(), testUserRequestObject.getId())
                    .request());
    Assertions.assertNull(channelMemberResponse.getMember().getPinnedAt());
  }

  @DisplayName("Can pin and unpin a channel using unset")
  @Test
  void whenPinningAndUnpinningAChannelUsingUnset_thenIsPinnedAndUnpinned() {
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    ChannelMemberResponse channelMemberResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.pin(channel.getType(), channel.getId(), testUserRequestObject.getId())
                    .request());
    Assertions.assertNotNull(channelMemberResponse.getMember().getPinnedAt());

    channelMemberResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.updateMemberPartial(
                        channel.getType(), channel.getId(), testUserRequestObject.getId())
                    .unsetValue("pinned")
                    .request());
    Assertions.assertNull(channelMemberResponse.getMember().getPinnedAt());
  }

  @DisplayName("Can archive and unarchive a channel")
  @Test
  void whenArchivingChannel_thenIsArchived() {
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    ChannelMemberResponse channelMemberResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.archive(channel.getType(), channel.getId(), testUserRequestObject.getId())
                    .request());
    Assertions.assertNotNull(channelMemberResponse.getMember().getArchivedAt());

    ChannelListResponse channelListResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.list()
                    .userId(testUserRequestObject.getId())
                    .filterCondition("archived", true)
                    .request());
    Assertions.assertEquals(1, channelListResponse.getChannels().size());
    Assertions.assertEquals(
        channel.getId(), channelListResponse.getChannels().get(0).getChannel().getId());

    channelMemberResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.unarchive(channel.getType(), channel.getId(), testUserRequestObject.getId())
                    .request());
    Assertions.assertNull(channelMemberResponse.getMember().getArchivedAt());
  }

  @DisplayName("Can archive and unarchive a channel using unset")
  @Test
  void whenArchivingChannelUsingUnset_thenIsArchived() {
    Channel channel = Assertions.assertDoesNotThrow(() -> createRandomChannel()).getChannel();
    ChannelMemberResponse channelMemberResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.archive(channel.getType(), channel.getId(), testUserRequestObject.getId())
                    .request());
    Assertions.assertNotNull(channelMemberResponse.getMember().getArchivedAt());

    channelMemberResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.updateMemberPartial(
                        channel.getType(), channel.getId(), testUserRequestObject.getId())
                    .unsetValue("archived")
                    .request());
    Assertions.assertNull(channelMemberResponse.getMember().getArchivedAt());
  }

  @DisplayName("Can set custom field on channel member")
  @Test
  void whenSettingCustomFieldOnMember_thenNoException() {
    final var customField = "custom_field_on_member";
    final var customValue = "custom_value";

    var channelMemberReq =
        ChannelMemberRequestObject.builder()
            .user(testUserRequestObject)
            .additionalField(customField, customValue)
            .build();
    var channelReq =
        ChannelRequestObject.builder()
            .createdBy(testUserRequestObject)
            .members(List.of(channelMemberReq))
            .build();
    var channel =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.getOrCreate(testChannel.getType(), RandomStringUtils.randomAlphabetic(12))
                    .data(channelReq)
                    .request());

    Assertions.assertNotNull(channel.getMembers());
    var members = channel.getMembers();
    Assertions.assertEquals(1, members.size());
    var member = members.get(0);
    Assertions.assertEquals(customValue, member.getAdditionalFields().get(customField));
  }

  @DisplayName("Can set custom field on channel member")
  @Test
  void whenPartialUpdateMember_thenNoException() {
    var channelType = "messaging";
    var channelId = RandomStringUtils.randomAlphabetic(12);
    var channelMemberReq =
        ChannelMemberRequestObject.builder()
            .user(testUserRequestObject)
            .additionalField("field1", "value1")
            .build();
    var channelReq =
        ChannelRequestObject.builder()
            .createdBy(testUserRequestObject)
            .members(List.of(channelMemberReq))
            .build();
    var channel =
        Assertions.assertDoesNotThrow(
            () -> Channel.getOrCreate(channelType, channelId).data(channelReq).request());

    Assertions.assertNotNull(channel.getMembers());
    var members = channel.getMembers();
    Assertions.assertEquals(1, members.size());
    var member = members.get(0);
    Assertions.assertEquals("value1", member.getAdditionalFields().get("field1"));

    // update partial
    var partialUpdateResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.updateMemberPartial(channelType, channelId, testUserRequestObject.getId())
                    .setValue("field1", "updated_value1")
                    .setValue("field2", "value2")
                    .request());

    var updatedMember = partialUpdateResponse.getMember();
    Assertions.assertEquals("updated_value1", updatedMember.getAdditionalFields().get("field1"));
    Assertions.assertEquals("value2", updatedMember.getAdditionalFields().get("field2"));
  }

  @DisplayName("Can mark messages delivered")
  @Test
  void whenMarkingDelivered_thenNoException() {
    Assertions.assertDoesNotThrow(
        () -> Channel.markDelivered().userId(testUserRequestObject.getId()).request());
  }
}
