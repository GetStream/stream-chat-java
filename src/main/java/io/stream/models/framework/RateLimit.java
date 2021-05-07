package io.stream.models.framework;

import java.util.Date;
import org.jetbrains.annotations.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private Date reset;
}
