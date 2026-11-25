package io.getstream.chat.java;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.chat.java.models.DeleteStrategy;
import io.getstream.chat.java.models.User;
import io.getstream.chat.java.models.User.UserDeleteManyRequestData;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDeleteManyExcludeChannelsTest {

  // Mirrors the visibility configuration of DefaultClient's mapper.
  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
          .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

  // Serializes the request body exactly as generateCall builds it.
  private static String body(User.UserDeleteManyRequestData.UserDeleteManyRequest request)
      throws Exception {
    Method build = request.getClass().getDeclaredMethod("internalBuild");
    build.setAccessible(true);
    UserDeleteManyRequestData data = (UserDeleteManyRequestData) build.invoke(request);
    return MAPPER.writeValueAsString(data);
  }

  @DisplayName("Delete many users sends exclude_channel_cids when set")
  @Test
  void whenExcludeChannelCidsSet_thenBodyCarriesThem() throws Exception {
    String json =
        body(
            User.deleteMany(Arrays.asList("user-1"))
                .deleteUserStrategy(DeleteStrategy.SOFT)
                .excludeChannelCids(Arrays.asList("messaging:held-1", "messaging:held-2")));

    Assertions.assertTrue(
        json.contains("\"exclude_channel_cids\":[\"messaging:held-1\",\"messaging:held-2\"]"),
        json);
  }

  @DisplayName("Delete many users omits exclude_channel_cids when unset")
  @Test
  void whenExcludeChannelCidsUnset_thenBodyOmitsThem() throws Exception {
    String json =
        body(User.deleteMany(Arrays.asList("user-1")).deleteUserStrategy(DeleteStrategy.SOFT));

    Assertions.assertFalse(json.contains("exclude_channel_cids"), json);
  }
}
