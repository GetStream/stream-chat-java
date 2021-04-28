package io.stream.services.framework;

import java.io.IOException;
import io.stream.exceptions.StreamException;
import io.stream.models.framework.StreamResponse;
import io.stream.models.framework.StreamResponse.RateLimitData;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;

public class StreamServiceHandler {
  public <T extends StreamResponse> T handle(Call<T> call) throws StreamException {
    try {
      Response<T> response = call.execute();
      if (response.isSuccessful()) {
        T result = response.body();
        Headers headers = response.headers();
        RateLimitData rateLimitData = new RateLimitData();
        rateLimitData.setRatelimitLimit(Integer.parseInt(headers.get("X-Ratelimit-Limit")));
        rateLimitData.setRatelimitRemaining(Integer.parseInt(headers.get("X-Ratelimit-Remaining")));
        rateLimitData.setRatelimitReset(Integer.parseInt(headers.get("X-Ratelimit-Reset")));
        result.setRateLimitData(rateLimitData);
        return result;
      }
      throw StreamException.build(response.errorBody());
    } catch (IOException e) {
      throw StreamException.build(e);
    }
  }
}
