package io.getstream.chat.java;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MessageSkipEnrichUrlTest {

  // Mirrors the visibility configuration of DefaultClient's mapper.
  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
          .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

  private static MessageRequestObject message() {
    return MessageRequestObject.builder().text("https://getstream.io").userId("gandalf").build();
  }

  @DisplayName("Send carries skip_enrich_url next to message")
  @Test
  void whenSendingWithSkipEnrichUrl_thenBodyCarriesIt() throws Exception {
    String body =
        MAPPER.writeValueAsString(
            Message.send("messaging", "general")
                .message(message())
                .skipEnrichUrl(true)
                .internalBuild());

    Assertions.assertTrue(body.contains("\"skip_enrich_url\":true"), body);
  }

  @DisplayName("Update carries skip_enrich_url next to message")
  @Test
  void whenUpdatingWithSkipEnrichUrl_thenBodyCarriesIt() throws Exception {
    String body =
        MAPPER.writeValueAsString(
            Message.update("msg-1").message(message()).skipEnrichUrl(true).internalBuild());

    Assertions.assertTrue(body.contains("\"skip_enrich_url\":true"), body);
  }

  @DisplayName("Partial update carries skip_enrich_url")
  @Test
  void whenPartialUpdatingWithSkipEnrichUrl_thenBodyCarriesIt() throws Exception {
    String body =
        MAPPER.writeValueAsString(
            Message.partialUpdate("msg-1")
                .userId("gandalf")
                .setValue("text", "https://getstream.io")
                .skipEnrichUrl(true)
                .internalBuild());

    Assertions.assertTrue(body.contains("\"skip_enrich_url\":true"), body);
  }
}
