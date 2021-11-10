package io.getstream.chat.java.models.framework;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.services.framework.Client;
import io.getstream.chat.java.services.framework.StreamServiceHandler;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

public abstract class StreamRequest<T extends StreamResponse> {
  protected abstract Call<T> generateCall(Client client) throws StreamException;

  private Client client;

  /**
   * Executes the request
   *
   * @return the channel update response
   * @throws StreamException when IO problem occurs or the stream API return an error
   */
  @NotNull
  public T request() throws StreamException {
    return new StreamServiceHandler().handle(generateCall(getClient()));
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
      var client = getClient();
      new StreamServiceHandler().handleAsync(generateCall(client), onSuccess, onError);
    } catch (StreamException e) {
      if (onError != null) {
        onError.accept(e);
      }
    }
  }

  /**
   * Use custom client implementation to execute requests
   *
   * @param client the client implementation
   */
  public StreamRequest<T> withClient(Client client) {
    this.client = client;
    return this;
  }

  @NotNull
  protected Client getClient() {
    return (client == null) ? Client.getInstance() : client;
  }
}
