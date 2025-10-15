package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Builder
@Getter
@EqualsAndHashCode
public class Sort {
  @NotNull
  @JsonProperty("field")
  private String field;

  @NotNull
  @JsonProperty("direction")
  private Direction direction;

  @JsonFormat(shape = JsonFormat.Shape.NUMBER)
  public enum Direction {
    ASC,
    DESC;

    @JsonValue
    public int toValue() {
      return this == ASC ? 1 : -1;
    }
  }
}
