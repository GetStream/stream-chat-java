package io.getstream.chat.java.models.framework;

import io.getstream.chat.java.models.RateLimit;

public interface StreamResponseWithRateLimit extends StreamResponse {
  RateLimit getRateLimit();

  void setRateLimit(RateLimit rateLimit);
}
