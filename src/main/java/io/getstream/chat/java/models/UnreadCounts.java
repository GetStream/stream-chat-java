package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.*;
import io.getstream.chat.java.models.UnreadCounts.UnreadCountsBatchRequestData.UnreadCountsBatchRequest;
import io.getstream.chat.java.models.UnreadCounts.UnreadCountsGetRequestData.UnreadCountsGetRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.UnreadCountsService;
import io.getstream.chat.java.services.framework.Client;
import java.util.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class UnreadCounts {
  @Data
  @NoArgsConstructor
  public static class UnreadCountsChannel {
    @NotNull
    @JsonProperty("channel_id")
    private String channelId;

    @NotNull
    @JsonProperty("unread_count")
    private Integer unreadCount;

    @NotNull
    @JsonProperty("last_read")
    private Date lastRead;
  }

  @Data
  @NoArgsConstructor
  public static class UnreadCountsChannelType {
    @NotNull
    @JsonProperty("channel_type")
    private String channelType;

    @NotNull
    @JsonProperty("channel_count")
    private String channelCount;

    @NotNull
    @JsonProperty("unread_count")
    private Integer unreadCount;
  }

  @Data
  @NoArgsConstructor
  public static class UnreadCountsThread {
    @NotNull
    @JsonProperty("unread_count")
    private Integer unreadCount;

    @NotNull
    @JsonProperty("last_read")
    private Date lastRead;

    @Nullable
    @JsonProperty("last_read_message_id")
    private String lastReadMessageId;

    @Nullable
    @JsonProperty("parent_message_id")
    private String parentMessageId;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UnreadCountsResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("total_unread_count")
    private Integer totalUnreadCount;

    @NotNull
    @JsonProperty("total_unread_threads_count")
    private Integer totalUnreadThreadsCount;

    @Nullable
    @JsonProperty("channels")
    private List<UnreadCountsChannel> channels;

    @Nullable
    @JsonProperty("channel_type")
    private List<UnreadCountsChannelType> channelType;

    @Nullable
    @JsonProperty("threads")
    private List<UnreadCountsThread> threads;
  }

  @Builder(
      builderClassName = "UnreadCountsGetRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UnreadCountsGetRequestData {
    @Nullable
    @JsonProperty("user_id")
    private String userId;

    public static class UnreadCountsGetRequest extends StreamRequest<UnreadCountsResponse> {
      private UnreadCountsGetRequest(@NotNull String userId) {
        this.userId = userId;
      }

      @Override
      protected Call<UnreadCountsResponse> generateCall(Client client) {
        return client.create(UnreadCountsService.class).get(userId);
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UnreadCountsBatchResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("counts_by_user")
    private Map<String, UnreadCountsResponse> countsByUser;
  }

  @Builder(
      builderClassName = "UnreadCountsBatchRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UnreadCountsBatchRequestData {
    @NotNull
    @JsonProperty("user_ids")
    private List<String> userIds;

    public static class UnreadCountsBatchRequest extends StreamRequest<UnreadCountsBatchResponse> {
      private UnreadCountsBatchRequest() {}

      @Override
      protected Call<UnreadCountsBatchResponse> generateCall(Client client) {
        return client.create(UnreadCountsService.class).batch(this.internalBuild());
      }
    }
  }

  /**
   * Creates a get request.
   *
   * @return the created request
   */
  @NotNull
  public static UnreadCountsGetRequest get(@NotNull String userId) {
    return new UnreadCountsGetRequest(userId);
  }

  /**
   * Creates a batch request.
   *
   * @return the created request
   */
  @NotNull
  public static UnreadCountsBatchRequest batch() {
    return new UnreadCountsBatchRequest();
  }
}
