package io.getstream.chat.java;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.chat.java.models.Channel.ChannelRequestObject;
import io.getstream.chat.java.models.Language;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChannelAutoTranslationLanguageTest {

  // Mirrors the visibility configuration of DefaultClient's mapper.
  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
          .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

  @DisplayName("Multiple languages serialize as a comma separated list")
  @Test
  void whenSettingLanguages_thenSerializedAsCommaSeparatedList() throws Exception {
    ChannelRequestObject channel =
        ChannelRequestObject.builder()
            .autoTranslationEnabled(true)
            .autoTranslationLanguages(Arrays.asList(Language.IT, Language.FR, Language.ES_MX))
            .build();

    Assertions.assertEquals("it,fr,es-MX", channel.getAutoTranslationLanguageValue());
    Assertions.assertTrue(
        MAPPER
            .writeValueAsString(channel)
            .contains("\"auto_translation_language\":\"it,fr,es-MX\""));
  }

  @DisplayName("Single language setter keeps working")
  @Test
  void whenSettingSingleLanguage_thenSerializedAsSingleValue() throws Exception {
    @SuppressWarnings("deprecation")
    ChannelRequestObject channel =
        ChannelRequestObject.builder().autoTranslationLanguage(Language.IT).build();

    Assertions.assertTrue(
        MAPPER.writeValueAsString(channel).contains("\"auto_translation_language\":\"it\""));
  }

  @DisplayName("The language list wins over the single language")
  @Test
  void whenSettingBoth_thenListWins() {
    @SuppressWarnings("deprecation")
    ChannelRequestObject channel =
        ChannelRequestObject.builder()
            .autoTranslationLanguage(Language.IT)
            .autoTranslationLanguages(Collections.singletonList(Language.FR))
            .build();

    Assertions.assertEquals("fr", channel.getAutoTranslationLanguageValue());
  }

  @DisplayName("No language set serializes as null")
  @Test
  void whenSettingNoLanguage_thenSerializedAsNull() throws Exception {
    ChannelRequestObject channel = ChannelRequestObject.builder().build();

    Assertions.assertNull(channel.getAutoTranslationLanguageValue());
    Assertions.assertTrue(
        MAPPER.writeValueAsString(channel).contains("\"auto_translation_language\":null"));
  }
}
