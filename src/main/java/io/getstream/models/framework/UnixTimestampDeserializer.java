package io.getstream.models.framework;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Date;

public class UnixTimestampDeserializer extends JsonDeserializer<Date> {
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
