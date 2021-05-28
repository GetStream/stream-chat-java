package io.getstream.models.framework;

import io.getstream.models.RateLimit;

public interface StreamResponseWithRateLimit extends StreamResponse {
  RateLimit getRateLimit();

  void setRateLimit(RateLimit rateLimit);
}
