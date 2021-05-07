package io.stream.models.framework;

import org.jetbrains.annotations.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StreamResponseObject implements StreamResponseWithRateLimit {
  private RateLimit rateLimit;

  @NotNull
  @JsonProperty("duration")
  private String duration;
}
