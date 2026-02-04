package io.getstream.chat.java;

import io.getstream.chat.java.models.Channel.ChannelListResponse;
import io.getstream.chat.java.models.Channel.ParsedPredefinedFilterResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParsedPredefinedFilterResponseTest extends BasicTest {

  @DisplayName("ChannelListResponse has predefinedFilter field")
  @Test
  void whenCreatingChannelListResponse_thenPredefinedFilterFieldExists() {
    ChannelListResponse response = new ChannelListResponse();
    // Verify the getter exists and returns null by default
    Assertions.assertNull(response.getPredefinedFilter());
  }

  @DisplayName("ParsedPredefinedFilterResponse has all expected fields")
  @Test
  void whenCreatingParsedPredefinedFilterResponse_thenAllFieldsExist() {
    ParsedPredefinedFilterResponse filter = new ParsedPredefinedFilterResponse();
    // Verify getters exist and return null by default
    Assertions.assertNull(filter.getName());
    Assertions.assertNull(filter.getFilter());
    Assertions.assertNull(filter.getSort());
  }
}
