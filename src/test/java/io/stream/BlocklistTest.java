package io.stream;

import io.getstream.chat.java.models.Blocklist;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlocklistTest extends BasicTest {

  @DisplayName("Can create blocklist")
  @Test
  void whenCreatingBlocklist_thenNoException() {
    Assertions.assertDoesNotThrow(
        () ->
            Blocklist.create()
                .name(RandomStringUtils.randomAlphabetic(5))
                .words(Arrays.asList(RandomStringUtils.randomAlphabetic(10)))
                .request());
  }

  @DisplayName("Can update blocklist")
  @Test
  void whenUpdatingBlocklist_thenNoException() {
    String name = RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(
        () ->
            Blocklist.create()
                .name(name)
                .words(Arrays.asList(RandomStringUtils.randomAlphabetic(10)))
                .request());
    pause();
    Assertions.assertDoesNotThrow(
        () ->
            Blocklist.update(name)
                .words(Arrays.asList(RandomStringUtils.randomAlphabetic(10)))
                .request());
  }

  @DisplayName("Can retrieve blocklist")
  @Test
  void whenRetrievingBlocklist_thenCorrectDescription() {
    String name = RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(
        () ->
            Blocklist.create()
                .name(name)
                .words(Arrays.asList(RandomStringUtils.randomAlphabetic(10)))
                .request());
    pause();
    Blocklist retrievedBlocklist =
        Assertions.assertDoesNotThrow(() -> Blocklist.get(name).request()).getBlocklist();
    Assertions.assertEquals(name, retrievedBlocklist.getName());
  }

  @DisplayName("Can delete blocklist")
  @Test
  void whenDeletingBlocklist_thenDeleted() {
    String name = RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(
        () ->
            Blocklist.create()
                .name(name)
                .words(Arrays.asList(RandomStringUtils.randomAlphabetic(10)))
                .request());
    pause();
    Assertions.assertDoesNotThrow(() -> Blocklist.delete(name).request());
    pause();
    List<Blocklist> blocklists =
        Assertions.assertDoesNotThrow(() -> Blocklist.list().request()).getBlocklists();
    Assertions.assertFalse(
        blocklists.stream()
            .anyMatch(consideredBlocklist -> consideredBlocklist.getName().equals(name)));
  }

  @DisplayName("Can list blocklists")
  @Test
  void whenListingBlocklist_thenCanRetrieve() {
    String name = RandomStringUtils.randomAlphabetic(5);
    Assertions.assertDoesNotThrow(
        () ->
            Blocklist.create()
                .name(name)
                .words(Arrays.asList(RandomStringUtils.randomAlphabetic(10)))
                .request());
    pause();
    List<Blocklist> blocklists =
        Assertions.assertDoesNotThrow(() -> Blocklist.list().request()).getBlocklists();
    Assertions.assertTrue(
        blocklists.stream()
            .anyMatch(consideredBlocklist -> consideredBlocklist.getName().equals(name)));
  }
}
