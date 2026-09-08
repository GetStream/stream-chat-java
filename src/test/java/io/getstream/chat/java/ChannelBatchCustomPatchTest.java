package io.getstream.chat.java;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.ChannelBatchOperation;
import io.getstream.chat.java.models.Channel.ChannelDataUpdate;
import io.getstream.chat.java.models.Channel.ChannelsBatchFilters;
import io.getstream.chat.java.models.Channel.ChannelsBatchOptions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChannelBatchCustomPatchTest {

  // Mirrors the visibility configuration of DefaultClient's mapper.
  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
          .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

  private static ChannelsBatchFilters filterByCids() {
    var filter = new ChannelsBatchFilters();
    Map<String, Object> cids = new HashMap<>();
    cids.put("$in", List.of("messaging:a", "messaging:b"));
    filter.setCids(cids);
    return filter;
  }

  @DisplayName("The custom patch serializes at the request root, not inside data")
  @Test
  void whenSettingCustomPatch_thenSerializedAtRequestRoot() throws Exception {
    var options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.UPDATE_DATA);
    options.setFilter(filterByCids());
    Map<String, Object> customSet = new HashMap<>();
    customSet.put("group", "old");
    options.setCustomSet(customSet);
    options.setCustomUnset(List.of("location_id"));

    JsonNode root = MAPPER.readTree(MAPPER.writeValueAsString(options));

    Assertions.assertEquals("old", root.path("custom_set").path("group").asText());
    Assertions.assertEquals(1, root.path("custom_unset").size());
    Assertions.assertEquals("location_id", root.path("custom_unset").get(0).asText());
    // The fields are siblings of operation and filter. Inside data they would be
    // collected into custom by the v1 extra-fields sink instead.
    Assertions.assertEquals("updateData", root.path("operation").asText());
    Assertions.assertTrue(root.hasNonNull("filter"));
    Assertions.assertFalse(root.path("data").has("custom_set"));
    Assertions.assertFalse(root.path("data").has("custom_unset"));
  }

  @DisplayName("The custom patch fields are omitted when not set")
  @Test
  void whenCustomPatchNotSet_thenOmittedFromTheRequest() throws Exception {
    var options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.UPDATE_DATA);
    options.setFilter(filterByCids());
    var data = new ChannelDataUpdate();
    data.setFrozen(true);
    options.setData(data);

    JsonNode root = MAPPER.readTree(MAPPER.writeValueAsString(options));

    Assertions.assertFalse(root.has("custom_set"));
    Assertions.assertFalse(root.has("custom_unset"));
  }

  @DisplayName("ChannelBatchUpdater carries the custom patch without data")
  @Test
  void whenUpdatingDataWithCustomPatchOnly_thenOptionsCarryTheFields() {
    var options =
        Channel.channelBatchUpdater()
            .updateData(filterByCids(), null, Map.of("group", "old"), List.of("location_id"))
            .getOptions();

    Assertions.assertEquals(ChannelBatchOperation.UPDATE_DATA, options.getOperation());
    Assertions.assertNull(options.getData());
    Assertions.assertEquals(Map.of("group", "old"), options.getCustomSet());
    Assertions.assertEquals(List.of("location_id"), options.getCustomUnset());
  }

  @DisplayName("ChannelBatchUpdater leaves the custom patch unset when only data is given")
  @Test
  void whenUpdatingDataOnly_thenCustomPatchStaysNull() {
    var data = new ChannelDataUpdate();
    data.setFrozen(true);

    var options = Channel.channelBatchUpdater().updateData(filterByCids(), data).getOptions();

    Assertions.assertNull(options.getCustomSet());
    Assertions.assertNull(options.getCustomUnset());
  }
}
