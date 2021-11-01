package io.getstream.chat.java.models.framework;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.services.framework.DefaultServiceFactory;
import io.getstream.chat.java.services.framework.ServiceFactory;
import io.getstream.chat.java.services.framework.StreamServiceHandler;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

public abstract class StreamRequest<T extends StreamResponse> {
  protected abstract Call<T> generateCall(ServiceFactory svcFactory) throws StreamException;

  /**
   * Executes the request
   *
   * @return the channel update response
   * @throws StreamException when IO problem occurs or the stream API return an error
   */
  @NotNull
  public T request() throws StreamException {
    return request(DefaultServiceFactory.getInstance());
  }

  /**
   * Executes the request
   *
   * @param svcFactory service factory
   * @return the channel update response
   * @throws StreamException when IO problem occurs or the stream API return an error
   */
  @NotNull
  public T request(ServiceFactory svcFactory) throws StreamException {
    return new StreamServiceHandler().handle(generateCall(svcFactory));
  }

  /**
   * Executes the request asynchronously
   *
   * @param onSuccess executed when the request is successful
   * @param onError executed when IO problem occurs or the stream API return an error
   */
  public void requestAsync(
      @Nullable Consumer<T> onSuccess, @Nullable Consumer<StreamException> onError) {
    requestAsync(DefaultServiceFactory.getInstance(), onSuccess, onError);
  }

  /**
   * Executes the request asynchronously
   * @param svcFactory service factory
   * @param onSuccess executed when the request is successful
   * @param onError executed when IO problem occurs or the stream API return an error
   */
  public void requestAsync(
      @NotNull ServiceFactory svcFactory,
      @Nullable Consumer<T> onSuccess, @Nullable Consumer<StreamException> onError) {
    try {
      new StreamServiceHandler().handleAsync(generateCall(svcFactory), onSuccess, onError);
    } catch (StreamException e) {
      if (onError != null) {
        onError.accept(e);
      }
    }
  }
}
