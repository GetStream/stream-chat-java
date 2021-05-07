package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class LanguageDeserializer extends JsonDeserializer<Language> {
  @Override
  public Language deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    String jsonString = jsonParser.readValueAs(String.class);
    if (jsonString == null || jsonString.equals("")) {
      return null;
    }
    for (Language enumValue : Language.values()) {
      try {
        if (jsonString.equals(
            Language.class.getField(enumValue.name()).getAnnotation(JsonProperty.class).value())) {
          return enumValue;
        }
      } catch (NoSuchFieldException e) {
        return null;
      } catch (SecurityException e) {
        throw deserializationContext.instantiationException(Language.class, "Should not happen");
      }
    }
    throw deserializationContext.instantiationException(
        Language.class, "Unparseable value for Language: " + jsonString);
  }
}
