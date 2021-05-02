package io.stream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.stream.models.Channel;
import io.stream.models.Channel.ChannelExportRequestObject;
import io.stream.models.Channel.ChannelGetResponse;
import io.stream.models.Channel.ChannelMember;
import io.stream.models.Channel.ChannelRequestObject;
import io.stream.models.Channel.ChannelUpdateResponse;
import io.stream.models.User;
import io.stream.models.User.ChannelMute;

public class ChannelTest extends BasicTest {

  @DisplayName("Can retrieve channel by type")
  @Test
  void whenRetrievingChannel_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            Channel.getOrCreate(testChannel.getType(), null)
                .withData(
                    ChannelRequestObject.builder()
                        .withCreatedBy(testUserRequestObject)
                        .withMembers(buildChannelMembersList())
                        .build())
                .request());
  }

  @DisplayName("Can add a moderator to a channel (update)")
  @Test
  void whenAddingModerator_thenHasModerator() {
    Assertions.assertEquals(0, countModerators(testChannelGetResponse.getMembers()));

    ChannelUpdateResponse channelUpdateResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.update(testChannel.getType(), testChannel.getId())
                    .withUser(testUserRequestObject)
                    .withAddModerators(Arrays.asList(testUserRequestObject.getId()))
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

  @DisplayName("Can list channels")
  @Test
  void whenListingChannels_thenNoException() {
    Assertions.assertDoesNotThrow(() -> Channel.list().withUser(testUserRequestObject).request());
  }

  @DisplayName("Can truncate channel")
  @Test
  void whenTruncateChannel_thenNoException() {
    // We should not use testChannel to not remove testMessage
    ChannelGetResponse channelGetResponse =
        Assertions.assertDoesNotThrow(() -> createRandomChannel());
    Assertions.assertDoesNotThrow(
        () ->
            Channel.truncate(
                    channelGetResponse.getChannel().getType(),
                    channelGetResponse.getChannel().getId())
                .request());
  }

  @DisplayName("Can query channel members")
  @Test
  void whenQueryingChannelMembers_thenRetrieveAll() {
    List<ChannelMember> channelMembers =
        Assertions.assertDoesNotThrow(
                () ->
                    Channel.queryMembers()
                        .withId(testChannel.getId())
                        .withType(testChannel.getType())
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
                        .addChannel(
                            ChannelExportRequestObject.builder()
                                .withType(testChannel.getType())
                                .withId(testChannel.getId())
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
                        .addChannel(
                            ChannelExportRequestObject.builder()
                                .withType(testChannel.getType())
                                .withId(testChannel.getId())
                                .build())
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
                .withUser(testUserRequestObject)
                .request());
  }

  @DisplayName("Can mark all channel read")
  @Test
  void whenMarkingAllChannelsRead_thenNoException() {
    Assertions.assertDoesNotThrow(
        () -> Channel.markAllRead().withUser(testUserRequestObject).request());
  }

  @DisplayName("Can mark channel read")
  @Test
  void whenMarkingChannelsRead_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            Channel.markRead(testChannel.getType(), testChannel.getId())
                .withUser(testUserRequestObject)
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
                .withChannelCid(channel.getType() + ":" + channel.getId())
                .withUser(testUserRequestObject)
                .request());
    Assertions.assertTrue(isChannelMutedForTestUser(channel.getType(), channel.getId()));
  }

  private boolean isChannelMutedForTestUser(String channelType, String channelId) {
    Map<String, Object> userConditions = new HashMap<>();
    userConditions.put("id", testUserRequestObject.getId());
    List<ChannelMute> channelMutes =
        Assertions.assertDoesNotThrow(
            () ->
                User.list()
                    .withFilterConditions(userConditions)
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
                .withUser(testUserRequestObject)
                .request());
  }

  @DisplayName("Can unmute a channel")
  @Test
  void whenUnMutingChannel_thenIsNotMutedAnymore() {
    Assertions.assertFalse(isChannelMutedForTestUser(testChannel.getType(), testChannel.getId()));
    Assertions.assertDoesNotThrow(
        () ->
            Channel.mute()
                .withChannelCid(testChannel.getType() + ":" + testChannel.getId())
                .withUser(testUserRequestObject)
                .request());
    Assertions.assertTrue(isChannelMutedForTestUser(testChannel.getType(), testChannel.getId()));
    Assertions.assertDoesNotThrow(
        () ->
            Channel.unmute()
                .withChannelCid(testChannel.getType() + ":" + testChannel.getId())
                .withUser(testUserRequestObject)
                .request());
    Assertions.assertFalse(isChannelMutedForTestUser(testChannel.getType(), testChannel.getId()));
  }
}
