package io.getstream.models.framework;

import io.getstream.exceptions.StreamException;
import io.getstream.services.framework.StreamServiceHandler;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

public abstract class StreamRequest<T extends StreamResponse> {
  protected abstract Call<T> generateCall() throws StreamException;

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
  public void requestAsync(
      @Nullable Consumer<T> onSuccess, @Nullable Consumer<StreamException> onError) {
    try {
      new StreamServiceHandler().handleAsync(generateCall(), onSuccess, onError);
    } catch (StreamException e) {
      if (onError != null) {
        onError.accept(e);
      }
    }
  }
}
