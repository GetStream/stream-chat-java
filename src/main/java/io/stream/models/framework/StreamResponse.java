package io.stream.models.framework;

import lombok.Data;

public interface StreamResponse {

  RateLimitData getRateLimitData();

  void setRateLimitData(RateLimitData rateLimitData);

  @Data
  public static final class RateLimitData {
    private int ratelimitLimit;

    private int ratelimitRemaining;

    private int ratelimitReset;
  }
}
