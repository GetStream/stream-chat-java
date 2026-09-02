package io.getstream.chat.java;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.Language;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LanguageTest {

  @DisplayName("Every language returns its declared wire value")
  @Test
  void whenReadingValue_thenItMatchesTheDeclaredJsonProperty() throws Exception {
    for (Language language : Language.values()) {
      JsonProperty jsonProperty =
          Language.class.getField(language.name()).getAnnotation(JsonProperty.class);
      if (language == Language.UNKNOWN) {
        Assertions.assertNull(jsonProperty);
        continue;
      }
      Assertions.assertNotNull(jsonProperty, language.name() + " is missing @JsonProperty");
      Assertions.assertEquals(jsonProperty.value(), language.getValue());
    }
  }
}
