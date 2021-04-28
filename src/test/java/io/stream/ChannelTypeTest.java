package io.stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.stream.models.ChannelConfig.AutoMod;
import io.stream.models.ChannelType;
import io.stream.models.ChannelType.ListChannelTypeResponse;
import lombok.extern.java.Log;

@Log
public class ChannelTypeTest extends BasicTest {

  @DisplayName("Can fetch channel type after creation with no Exception and correct name")
  @Test
  void whenCreatingDefaultChannelType_thenCanFetchWithNoExceptionAndCorrectName() {
    String channelName = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> ChannelType.create().withDefaultConfig().withName(channelName).request());
    log.info("Channel created");
    ChannelType channelType = Assertions.assertDoesNotThrow(() -> ChannelType.get(channelName));
    log.info("Channel retrieved");
    assertEquals(channelType.getName(), channelName);
    Assertions.assertDoesNotThrow(() -> ChannelType.delete(channelName));
  }


  @DisplayName("Can delete channel type after creation with no Exception")
  @Test
  void whenCreatingDefaultChannelType_thenCanDeleteWithNoException() {
    String channelName = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> ChannelType.create().withDefaultConfig().withName(channelName).request());
    log.info("Channel created");
    Assertions.assertDoesNotThrow(() -> ChannelType.delete(channelName));
    log.info("Channel deleted");
  }

  @DisplayName("Can see created channel type in list after creation with no Exception")
  @Test
  void whenCreatingDefaultChannelType_thenCanListAndRetrieveItWithNoException() {
    String channelName = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> ChannelType.create().withDefaultConfig().withName(channelName).request());
    log.info("Channel created");
    ListChannelTypeResponse listChannelTypeResponse =
        Assertions.assertDoesNotThrow(() -> ChannelType.list());
    log.info("Channels listed");
    Assertions.assertTrue(listChannelTypeResponse.getChannelTypes().containsKey(channelName));
    Assertions.assertDoesNotThrow(() -> ChannelType.delete(channelName));
  }

  @DisplayName("Can update channel type after creation with no Exception")
  @Test
  void whenCreatingDefaultChannelType_thenCanUpdateWithNoException() {
    String channelName = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () -> ChannelType.create().withDefaultConfig().withName(channelName).request());
    log.info("Channel created");
    Assertions.assertDoesNotThrow(
        () -> ChannelType.update().withName(channelName).withAutomod(AutoMod.SIMPLE).request());
    log.info("Channel updated");
    Assertions.assertDoesNotThrow(() -> ChannelType.delete(channelName));
  }

}
