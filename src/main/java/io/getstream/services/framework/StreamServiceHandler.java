package io.getstream.services.framework;

import io.getstream.exceptions.StreamException;
import io.getstream.models.RateLimit;
import io.getstream.models.framework.StreamResponse;
import io.getstream.models.framework.StreamResponseWithRateLimit;
import java.io.IOException;
import java.util.Date;
import java.util.function.Consumer;
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
              if (onSuccess != null) {
                onSuccess.accept(enrichResponse(response));
              }
            } else if (onError != null) {
              onError.accept(StreamException.build(response.errorBody()));
            }
          }

          @Override
          public void onFailure(Call<T> call, Throwable throwable) {
            if (onError != null) {
              onError.accept(StreamException.build(throwable));
            }
          }
        });
  }

  private <T extends StreamResponse> T enrichResponse(Response<T> response) {
    T result = response.body();
    if (result instanceof StreamResponseWithRateLimit) {
      Headers headers = response.headers();
      RateLimit rateLimit = new RateLimit();
      rateLimit.setLimit(Integer.parseInt(headers.get("X-Ratelimit-Limit")));
      rateLimit.setRemaining(Integer.parseInt(headers.get("X-Ratelimit-Remaining")));
      rateLimit.setReset(new Date(Long.parseLong(headers.get("X-Ratelimit-Reset")) * 1000));
      ((StreamResponseWithRateLimit) result).setRateLimit(rateLimit);
    }
    return result;
  }
}
