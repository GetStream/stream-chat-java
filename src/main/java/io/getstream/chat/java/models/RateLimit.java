package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public class RateLimit {
  @NotNull
  @JsonProperty("limit")
  private Integer limit;

  @NotNull
  @JsonProperty("remaining")
  private Integer remaining;

  @NotNull
  @JsonProperty("reset")
  @JsonDeserialize(using = UnixTimestampDeserializer.class)
  private Date reset;

  public static class UnixTimestampDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {
      String jsonString = jsonParser.readValueAs(String.class);
      try {
        return new Date(Long.parseLong(jsonString) * 1000);
      } catch (NumberFormatException e) {
        throw deserializationContext.instantiationException(
            Date.class, "Unparseable date for unix timestamp: " + jsonString);
      }
    }
  }
}
