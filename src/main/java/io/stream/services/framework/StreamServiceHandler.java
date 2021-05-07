package io.stream.services.framework;

import java.io.IOException;
import java.util.function.Consumer;
import io.stream.exceptions.StreamException;
import io.stream.models.App.AppGetRateLimitsResponse;
import io.stream.models.framework.StreamResponse;
import io.stream.models.framework.StreamResponse.RateLimitData;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreamServiceHandler {
  public <T extends StreamResponse> T handle(Call<T> call) throws StreamException {
    try {
      Response<T> response = call.execute();
      if (response.isSuccessful()) {
        return enrichResponse(response);
      }
      throw StreamException.build(response.errorBody());
    } catch (IOException e) {
      throw StreamException.build(e);
    }
  }

  public <T extends StreamResponse> void handleAsync(
      Call<T> call, Consumer<T> onSuccess, Consumer<StreamException> onError) {
    call.enqueue(
        new Callback<T>() {
          @Override
          public void onResponse(Call<T> call, Response<T> response) {
            if (response.isSuccessful()) {
              onSuccess.accept(enrichResponse(response));
            } else {
              onError.accept(StreamException.build(response.errorBody()));
            }
          }

          @Override
          public void onFailure(Call<T> call, Throwable throwable) {
            onError.accept(StreamException.build(throwable));
          }
        });
  }

  private <T extends StreamResponse> T enrichResponse(Response<T> response) {
    T result = response.body();
    if (result instanceof AppGetRateLimitsResponse) {
      return result;
    }
    Headers headers = response.headers();
    RateLimitData rateLimitData = new RateLimitData();
    rateLimitData.setRatelimitLimit(Integer.parseInt(headers.get("X-Ratelimit-Limit")));
    rateLimitData.setRatelimitRemaining(Integer.parseInt(headers.get("X-Ratelimit-Remaining")));
    rateLimitData.setRatelimitReset(Integer.parseInt(headers.get("X-Ratelimit-Reset")));
    result.setRateLimitData(rateLimitData);
    return result;
  }
}
