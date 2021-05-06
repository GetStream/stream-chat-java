package io.stream.services.framework;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class QueryConverterFactory extends Converter.Factory {
  public static QueryConverterFactory create() {
    return new QueryConverterFactory();
  }

  @Override
  public Converter<?, String> stringConverter(
      Type type, Annotation[] annotations, Retrofit retrofit) {
    if (type == Date.class) {
      return value -> {
        return DateTimeFormatter.ISO_INSTANT.format(((Date) value).toInstant());
      };
    }
    if (!hasToJson(annotations)) {
      return super.stringConverter(type, annotations, retrofit);
    }
    return value -> {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      new ObjectMapper().writeValue(baos, value);
      return baos.toString("UTF-8");
    };
  }

  private boolean hasToJson(final Annotation[] annotations) {
    for (final Annotation annotation : annotations) {
      if (annotation instanceof ToJson) {
        return true;
      }
    }
    return false;
  }
}
