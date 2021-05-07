package io.stream.models.framework;

public interface StreamResponseWithRateLimit extends StreamResponse {
  RateLimit getRateLimit();

  void setRateLimit(RateLimit rateLimit);
}
