package io.getstream.chat.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.chat.java.models.Channel.ChannelListResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParsedPredefinedFilterResponseTest {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @DisplayName("Can deserialize ParsedPredefinedFilterResponse from JSON")
  @Test
  void whenDeserializingPredefinedFilterResponse_thenCorrectlyParsed() throws Exception {
    String json =
        """
        {
          "channels": [],
          "predefined_filter": {
            "name": "user_messaging",
            "filter": {"type": "messaging", "members": {"$in": ["user123"]}},
            "sort": [{"field": "last_message_at", "direction": -1}]
          },
          "duration": "0.01s"
        }
        """;

    ChannelListResponse response = objectMapper.readValue(json, ChannelListResponse.class);

    Assertions.assertNotNull(response.getPredefinedFilter());
    Assertions.assertEquals("user_messaging", response.getPredefinedFilter().getName());
    Assertions.assertNotNull(response.getPredefinedFilter().getFilter());
    Assertions.assertEquals("messaging", response.getPredefinedFilter().getFilter().get("type"));
    Assertions.assertNotNull(response.getPredefinedFilter().getSort());
    Assertions.assertEquals(1, response.getPredefinedFilter().getSort().size());
    Assertions.assertEquals(
        "last_message_at", response.getPredefinedFilter().getSort().get(0).getField());
  }

  @DisplayName("Can deserialize response without predefined_filter")
  @Test
  void whenDeserializingResponseWithoutPredefinedFilter_thenNullField() throws Exception {
    String json =
        """
        {
          "channels": [],
          "duration": "0.01s"
        }
        """;

    ChannelListResponse response = objectMapper.readValue(json, ChannelListResponse.class);

    Assertions.assertNull(response.getPredefinedFilter());
  }

  @DisplayName("Can deserialize predefined_filter without sort")
  @Test
  void whenDeserializingPredefinedFilterWithoutSort_thenSortIsNull() throws Exception {
    String json =
        """
        {
          "channels": [],
          "predefined_filter": {
            "name": "simple_filter",
            "filter": {"type": "messaging"}
          },
          "duration": "0.01s"
        }
        """;

    ChannelListResponse response = objectMapper.readValue(json, ChannelListResponse.class);

    Assertions.assertNotNull(response.getPredefinedFilter());
    Assertions.assertEquals("simple_filter", response.getPredefinedFilter().getName());
    Assertions.assertNull(response.getPredefinedFilter().getSort());
  }
}
