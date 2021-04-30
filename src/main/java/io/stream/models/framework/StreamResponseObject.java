package io.stream.models.framework;

import lombok.Data;

@Data
public class StreamResponseObject implements StreamResponse {

  private RateLimitData rateLimitData;
}
