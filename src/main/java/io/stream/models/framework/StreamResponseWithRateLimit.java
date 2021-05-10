package io.stream.models.framework;

import io.stream.models.RateLimit;

public interface StreamResponseWithRateLimit extends StreamResponse {
  RateLimit getRateLimit();

  void setRateLimit(RateLimit rateLimit);
}
