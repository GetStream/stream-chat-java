package io.stream.models.framework;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

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
  @JsonDeserialize(using = UnixTimestampDeserializer.class)
  private Date reset;
}
