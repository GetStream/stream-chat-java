package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.ChannelService;
import io.getstream.chat.java.services.framework.Client;
import java.util.List;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class MarkDeliveredOptions {
  @NotNull
  @JsonProperty("latest_delivered_messages")
  private List<DeliveredMessageConfirmation> latestDeliveredMessages;

  @Nullable
  @JsonProperty("user")
  private User user;

  @Nullable
  @JsonProperty("user_id")
  private String userId;

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MarkDeliveredResponse extends StreamResponseObject {}

  public static class MarkDeliveredRequest extends StreamRequest<MarkDeliveredResponse> {
    private MarkDeliveredOptions options;
    private String userId;

    public MarkDeliveredRequest() {}

    public MarkDeliveredRequest(MarkDeliveredOptions options) {
      this.options = options;
    }

    public MarkDeliveredRequest options(MarkDeliveredOptions options) {
      this.options = options;
      return this;
    }

    public MarkDeliveredRequest userId(String userId) {
      this.userId = userId;
      return this;
    }

    @Override
    protected Call<MarkDeliveredResponse> generateCall(Client client) {
      return client.create(ChannelService.class).markChannelsDelivered(options, userId);
    }
  }

  /**
   * Creates a mark channels delivered request
   *
   * @return the created request
   */
  @NotNull
  public static MarkDeliveredRequest markChannelsDelivered() {
    return new MarkDeliveredRequest();
  }

  /**
   * Creates a mark channels delivered request with options
   *
   * @param options the mark delivered options
   * @return the created request
   */
  @NotNull
  public static MarkDeliveredRequest markChannelsDelivered(MarkDeliveredOptions options) {
    return new MarkDeliveredRequest(options);
  }

  /**
   * Creates a mark channels delivered request with a list of delivered message confirmations
   *
   * @param latestDeliveredMessages the list of delivered message confirmations
   * @return the created request
   */
  @NotNull
  public static MarkDeliveredRequest markChannelsDelivered(
      List<DeliveredMessageConfirmation> latestDeliveredMessages) {
    MarkDeliveredOptions options = new MarkDeliveredOptions();
    options.setLatestDeliveredMessages(latestDeliveredMessages);
    return new MarkDeliveredRequest(options);
  }
}
