package io.stream.models.framework;

import lombok.Data;

@Data
public class StreamResponse {

  private RateLimitData rateLimitData;

  @Data
  public static final class RateLimitData {
    private int ratelimitLimit;

    private int ratelimitRemaining;

    private int ratelimitReset;
  }
}
