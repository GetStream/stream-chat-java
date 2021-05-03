package io.stream.models;

import org.jetbrains.annotations.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class Sort {
  @NotNull
  @JsonProperty("field")
  private String field;

  @NotNull
  @JsonProperty("direction")
  private Direction direction;

  private Sort(Builder builder) {
    this.field = builder.field;
    this.direction = builder.direction;
  }

  /**
   * Creates builder to build {@link Sort}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder to build {@link Sort}. */
  public static final class Builder {
    private String field;
    private Direction direction;

    private Builder() {}

    @NotNull
    public Builder field(@NotNull String field) {
      this.field = field;
      return this;
    }

    @NotNull
    public Builder direction(@NotNull Direction direction) {
      this.direction = direction;
      return this;
    }

    @NotNull
    public Sort build() {
      return new Sort(this);
    }
  }

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
