package io.stream.models.framework;

import io.stream.exceptions.StreamException;
import io.stream.services.framework.StreamServiceHandler;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;

public abstract class StreamRequest<T extends StreamResponse> {
  protected abstract Call<T> generateCall();

  /**
   * Executes the request
   *
   * @return the channel update response
   * @throws StreamException when IO problem occurs or the stream API return an error
   */
  @NotNull
  public T request() throws StreamException {
    return new StreamServiceHandler().handle(generateCall());
  }

  /**
   * Executes the request asynchronously
   *
   * @param onSuccess executed when the request is successful
   * @param onError executed when IO problem occurs or the stream API return an error
   */
  public void requestAsync(Consumer<T> onSuccess, Consumer<StreamException> onError) {
    new StreamServiceHandler().handleAsync(generateCall(), onSuccess, onError);
  }
}
