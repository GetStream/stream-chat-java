package io.getstream.chat.java;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.services.ChannelService;
import io.getstream.chat.java.services.framework.DefaultClient;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetChannelTest {

  @Test
  @DisplayName("Get channel sends a GET with its options in the payload query parameter")
  void whenBuildingGetChannelRequest_thenOptionsGoIntoPayloadQueryParameter() {
    var data = Channel.getChannel("messaging", "abc").state(true).messagesLimit(5).internalBuild();

    var request =
        client().create(ChannelService.class).getChannel("messaging", "abc", data).request();

    Assertions.assertEquals("GET", request.method());
    Assertions.assertEquals("/channels/messaging/abc", request.url().encodedPath());

    var payload = request.url().queryParameter("payload");
    Assertions.assertNotNull(payload);
    Assertions.assertTrue(payload.contains("\"state\":true"), payload);
    Assertions.assertTrue(payload.contains("\"messages_limit\":5"), payload);
  }

  @Test
  @DisplayName("Get channel sends its message cursors in the payload query parameter")
  void whenBuildingGetChannelRequestWithCursors_thenCursorsGoIntoPayloadQueryParameter() {
    var data =
        Channel.getChannel("messaging", "abc")
            .state(true)
            .messagesLimit(20)
            .messagesIdLt("message-1")
            .messagesIdGte("message-2")
            .messagesIdAround("message-3")
            .internalBuild();

    var request =
        client().create(ChannelService.class).getChannel("messaging", "abc", data).request();

    var payload = request.url().queryParameter("payload");
    Assertions.assertNotNull(payload);
    Assertions.assertTrue(payload.contains("\"messages_id_lt\":\"message-1\""), payload);
    Assertions.assertTrue(payload.contains("\"messages_id_gte\":\"message-2\""), payload);
    Assertions.assertTrue(payload.contains("\"messages_id_around\":\"message-3\""), payload);
  }

  @Test
  @DisplayName("Get channel sends a payload query parameter even without options")
  void whenBuildingGetChannelRequestWithoutOptions_thenPayloadQueryParameterIsStillSent() {
    var data = Channel.getChannel("messaging", "abc").internalBuild();

    var request =
        client().create(ChannelService.class).getChannel("messaging", "abc", data).request();

    Assertions.assertNotNull(request.url().queryParameter("payload"));
  }

  private DefaultClient client() {
    var properties = new Properties();
    properties.put(DefaultClient.API_KEY_PROP_NAME, "test-key");
    properties.put(DefaultClient.API_SECRET_PROP_NAME, "test-secret");
    return new DefaultClient(properties);
  }
}
