package io.getstream.chat.java;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.services.ChannelService;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ChannelDeleteSkipTruncateTest {

  // Mirrors the visibility configuration of DefaultClient's mapper.
  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
          .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

  private static ChannelService service() {
    return new Retrofit.Builder()
        .baseUrl("https://chat.example.com/")
        .addConverterFactory(JacksonConverterFactory.create(MAPPER))
        .build()
        .create(ChannelService.class);
  }

  @DisplayName("Delete sends skip_truncate as a query param when set")
  @Test
  void whenSkipTruncateSet_thenQueryParamIsSent() {
    String url = service().delete("messaging", "chan", true).request().url().toString();

    Assertions.assertTrue(url.contains("skip_truncate=true"), url);
  }

  @DisplayName("Delete omits skip_truncate when unset")
  @Test
  void whenSkipTruncateUnset_thenQueryParamIsOmitted() {
    String url = service().delete("messaging", "chan", null).request().url().toString();

    Assertions.assertFalse(url.contains("skip_truncate"), url);
  }

  @DisplayName("The two argument delete overload is still callable")
  @Test
  void whenCallingTwoArgumentDelete_thenNoQueryParamIsSent() {
    String url = service().delete("messaging", "chan").request().url().toString();

    Assertions.assertFalse(url.contains("skip_truncate"), url);
  }

  @DisplayName("Delete request carries the flag to the service call")
  @Test
  void whenSettingSkipTruncateOnRequest_thenFlagIsKept() {
    Assertions.assertEquals(
        true, Channel.delete("messaging", "chan").setSkipTruncate(true).getSkipTruncate());
    Assertions.assertNull(Channel.delete("messaging", "chan").getSkipTruncate());
  }

  @DisplayName("Delete many serializes skip_truncate only when set")
  @Test
  void whenSettingSkipTruncateOnDeleteMany_thenBodyCarriesIt() throws Exception {
    Assertions.assertFalse(
        MAPPER
            .writeValueAsString(Channel.deleteMany(Arrays.asList("messaging:chan")))
            .contains("skip_truncate"));
    Assertions.assertTrue(
        MAPPER
            .writeValueAsString(
                Channel.deleteMany(Arrays.asList("messaging:chan")).setSkipTruncate(true))
            .contains("\"skip_truncate\":true"));
  }
}
