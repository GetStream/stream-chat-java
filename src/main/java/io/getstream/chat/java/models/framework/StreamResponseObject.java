package io.getstream.chat.java.models.framework;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.RateLimit;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public class StreamResponseObject implements StreamResponseWithRateLimit {
  private RateLimit rateLimit;

  @NotNull
  @JsonProperty("duration")
  private String duration;
}
