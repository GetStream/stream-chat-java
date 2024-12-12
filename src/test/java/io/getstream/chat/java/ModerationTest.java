package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Moderation;
import io.getstream.chat.java.models.Moderation.*;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ModerationTest extends BasicTest {
  @DisplayName("Can upsert, get and delete moderation config")
  @Test
  void whenUpsertingGetttingDeletingModerationConfig_thenNoException() {
    BlockListRule rule =
        BlockListRule.builder().name("test").action(Moderation.Action.REMOVE).build();

    String key = "chat:messaging:1234";
    Assertions.assertDoesNotThrow(
        () ->
            Moderation.upsertConfig(key)
                .blockListConfig(
                    BlockListConfigRequestObject.builder().rules(List.of(rule)).build())
                .request());

    ConfigGetResponse response =
        Assertions.assertDoesNotThrow(() -> Moderation.getConfig(key).request());

    Assertions.assertEquals(
        response.getConfig().getBlockListConfig().getRules().get(0).getName(), "test");

    Assertions.assertDoesNotThrow(() -> Moderation.deleteConfig(key).request());

    Assertions.assertThrows(StreamException.class, () -> Moderation.getConfig(key).request());
  }
}
