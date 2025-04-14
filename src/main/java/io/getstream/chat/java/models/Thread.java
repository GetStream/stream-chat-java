package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.*;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.ThreadService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

/** Represents thread functionality in Stream Chat. */
@Data
public class Thread {
  /** A thread participant. */
  @Data
  @NoArgsConstructor
  public static class ThreadParticipant {
    @Nullable
    @JsonProperty("app_pk")
    private Integer appPk;

    @Nullable
    @JsonProperty("channel_cid")
    private String channelCid;

    @Nullable
    @JsonProperty("last_thread_message_at")
    private Date lastThreadMessageAt;

    @Nullable
    @JsonProperty("thread_id")
    private String threadId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private User user;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("left_thread_at")
    private Date leftThreadAt;

    @Nullable
    @JsonProperty("last_read_at")
    private Date lastReadAt;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;
  }

  /** A thread object containing thread data and metadata. */
  @Data
  @NoArgsConstructor
  public static class ThreadObject {
    @Nullable
    @JsonProperty("app_pk")
    private Integer appPk;

    @NotNull
    @JsonProperty("channel_cid")
    private String channelCid;

    @Nullable
    @JsonProperty("channel")
    private Channel channel;

    @NotNull
    @JsonProperty("parent_message_id")
    private String parentMessageId;

    @Nullable
    @JsonProperty("parent_message")
    private Message parentMessage;

    @NotNull
    @JsonProperty("created_by_user_id")
    private String createdByUserId;

    @Nullable
    @JsonProperty("created_by")
    private User createdBy;

    @Nullable
    @JsonProperty("reply_count")
    private Integer replyCount;

    @Nullable
    @JsonProperty("participant_count")
    private Integer participantCount;

    @Nullable
    @JsonProperty("active_participant_count")
    private Integer activeParticipantCount;

    @Nullable
    @JsonProperty("thread_participants")
    private List<ThreadParticipant> participants;

    @Nullable
    @JsonProperty("last_message_at")
    private Date lastMessageAt;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @NotNull
    @JsonProperty("updated_at")
    private Date updatedAt;

    @Nullable
    @JsonProperty("deleted_at")
    private Date deletedAt;

    @NotNull
    @JsonProperty("title")
    private String title;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;

    @Nullable
    @JsonProperty("latest_replies")
    private List<Message> latestReplies;

    @Nullable
    @JsonProperty("read")
    private List<Channel.ChannelRead> read;

    @Nullable
    @JsonProperty("draft")
    private Draft.DraftObject draft;
  }

  /** Response for querying threads. */
  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryThreadsResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("threads")
    private List<ThreadObject> threads;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;
  }

  /** Request data for querying threads. */
  @Builder(
      builderClassName = "QueryThreadsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class QueryThreadsRequestData {
    @Nullable
    @JsonProperty("filter")
    private Map<String, Object> filter;

    @Singular
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

    @Nullable
    @JsonProperty("watch")
    private Boolean watch;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private User.UserRequestObject user;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    public static class QueryThreadsRequest extends StreamRequest<QueryThreadsResponse> {
      public QueryThreadsRequest() {}

      @Override
      protected Call<QueryThreadsResponse> generateCall(Client client) throws StreamException {
        return client.create(ThreadService.class).queryThreads(this.internalBuild());
      }
    }
  }

  /**
   * Queries threads based on the provided parameters
   *
   * @return the created request
   */
  @NotNull
  public static QueryThreadsRequestData.QueryThreadsRequest queryThreads() {
    return new QueryThreadsRequestData.QueryThreadsRequest();
  }
}
