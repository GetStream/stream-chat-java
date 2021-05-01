package io.stream;

import io.stream.models.Channel;
import io.stream.models.Channel.ChannelGetResponse;
import io.stream.models.Channel.ChannelMember;
import io.stream.models.Channel.ChannelUpdateResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChannelTest extends BasicTest {

  @DisplayName("Can create channel")
  @Test
  void whenCreatingChannel_thenNoException() {
    Assertions.assertDoesNotThrow(() -> createRandomChannel());
  }

  @DisplayName("Can retrieve channel by type")
  @Test
  void whenRetrievingChannel_thenNoException() {
    Assertions.assertDoesNotThrow(() -> createRandomChannel());
    Assertions.assertDoesNotThrow(() -> getRandomChannel());
  }

  @DisplayName("Can add a moderator to a channel (update)")
  @Test
  void whenAddingModerator_thenHasModerator() {
    ChannelGetResponse channelGetResponse =
        Assertions.assertDoesNotThrow(() -> createRandomChannel());
    Assertions.assertEquals(0, countModerators(channelGetResponse.getMembers()));

    ChannelUpdateResponse channelUpdateResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Channel.update(
                        channelGetResponse.getChannel().getType(),
                        channelGetResponse.getChannel().getId())
                    .withUser(serverUser)
                    .withAddModerators(Arrays.asList(serverUser.getId()))
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
    Assertions.assertDoesNotThrow(() -> Channel.list().withUser(serverUser).request());
  }

  @DisplayName("Can truncate channel")
  @Test
  void whenTruncateChannel_thenNoException() {
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
    ChannelGetResponse channelGetResponse =
        Assertions.assertDoesNotThrow(() -> createRandomChannel());
    List<ChannelMember> channelMembers =
        Assertions.assertDoesNotThrow(
                () ->
                    Channel.queryMembers()
                        .withId(channelGetResponse.getChannel().getId())
                        .withType(channelGetResponse.getChannel().getType())
                        .request())
            .getMembers();
    Assertions.assertEquals(testUsers.size(), channelMembers.size());
  }
}
