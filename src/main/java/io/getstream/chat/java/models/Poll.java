package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.Poll.PollCreateRequestData.PollCreateRequest;
import io.getstream.chat.java.models.Poll.PollOptionCreateRequestData.PollOptionCreateRequest;
import io.getstream.chat.java.models.Poll.PollOptionUpdateRequestData.PollOptionUpdateRequest;
import io.getstream.chat.java.models.Poll.PollQueryRequestData.PollQueryRequest;
import io.getstream.chat.java.models.Poll.PollUpdatePartialRequestData.PollUpdatePartialRequest;
import io.getstream.chat.java.models.Poll.PollUpdateRequestData.PollUpdateRequest;
import io.getstream.chat.java.models.Poll.PollVoteCastRequestData.PollVoteCastRequest;
import io.getstream.chat.java.models.Poll.PollVoteQueryRequestData.PollVoteQueryRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.PollService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Poll {
  @NotNull
  @JsonProperty("id")
  private String id;

  @NotNull
  @JsonProperty("name")
  private String name;

  @Nullable
  @JsonProperty("description")
  private String description;

  @Nullable
  @JsonProperty("voting_visibility")
  private VotingVisibility votingVisibility;

  @NotNull
  @JsonProperty("enforce_unique_vote")
  private Boolean enforceUniqueVote;

  @Nullable
  @JsonProperty("max_votes_allowed")
  private Integer maxVotesAllowed;

  @NotNull
  @JsonProperty("allow_user_suggested_options")
  private Boolean allowUserSuggestedOptions;

  @NotNull
  @JsonProperty("allow_answers")
  private Boolean allowAnswers;

  @NotNull
  @JsonProperty("is_closed")
  private Boolean isClosed;

  @Nullable
  @JsonProperty("options")
  private List<PollOption> options;

  @Nullable
  @JsonProperty("vote_count")
  private Integer voteCount;

  @Nullable
  @JsonProperty("vote_counts_by_option")
  private Map<String, Integer> voteCountsByOption;

  @Nullable
  @JsonProperty("answers_count")
  private Integer answersCount;

  @Nullable
  @JsonProperty("latest_votes_by_option")
  private Map<String, List<PollVote>> latestVotesByOption;

  @Nullable
  @JsonProperty("latest_answers")
  private List<PollVote> latestAnswers;

  @Nullable
  @JsonProperty("own_votes")
  private List<PollVote> ownVotes;

  @NotNull
  @JsonProperty("created_by_id")
  private String createdById;

  @Nullable
  @JsonProperty("created_by")
  private User createdBy;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

  @Nullable
  @JsonProperty("custom")
  private Map<String, Object> custom;

  public enum VotingVisibility {
    @JsonProperty("public")
    PUBLIC,
    @JsonProperty("anonymous")
    ANONYMOUS,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  @Data
  @NoArgsConstructor
  public static class PollOption {
    @NotNull
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;
  }

  @Data
  @NoArgsConstructor
  public static class PollVote {
    @NotNull
    @JsonProperty("poll_id")
    private String pollId;

    @NotNull
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("option_id")
    private String optionId;

    @NotNull
    @JsonProperty("is_answer")
    private Boolean isAnswer;

    @Nullable
    @JsonProperty("answer_text")
    private String answerText;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private User user;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @NotNull
    @JsonProperty("updated_at")
    private Date updatedAt;
  }

  @Builder(
      builderClassName = "PollCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class PollCreateRequestData {
    @Nullable
    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("description")
    private String description;

    @Singular
    @Nullable
    @JsonProperty("options")
    private List<PollOptionInput> options;

    @Nullable
    @JsonProperty("voting_visibility")
    private VotingVisibility votingVisibility;

    @Nullable
    @JsonProperty("enforce_unique_vote")
    private Boolean enforceUniqueVote;

    @Nullable
    @JsonProperty("max_votes_allowed")
    private Integer maxVotesAllowed;

    @Nullable
    @JsonProperty("allow_user_suggested_options")
    private Boolean allowUserSuggestedOptions;

    @Nullable
    @JsonProperty("allow_answers")
    private Boolean allowAnswers;

    @Nullable
    @JsonProperty("is_closed")
    private Boolean isClosed;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;

    @Data
    @NoArgsConstructor
    public static class PollOptionInput {
      @Nullable
      @JsonProperty("id")
      private String id;

      @Nullable
      @JsonProperty("text")
      private String text;

      @Nullable
      @JsonProperty("custom")
      private Map<String, Object> custom;
    }

    public static class PollCreateRequest extends StreamRequest<PollCreateResponse> {
      @Override
      protected Call<PollCreateResponse> generateCall(Client client) {
        return client.create(PollService.class).create(this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class PollGetRequest extends StreamRequest<PollGetResponse> {
    @NotNull private String pollId;

    @Override
    protected Call<PollGetResponse> generateCall(Client client) {
      return client.create(PollService.class).get(pollId);
    }
  }

  @Builder(
      builderClassName = "PollUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class PollUpdateRequestData {
    @NotNull
    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("description")
    private String description;

    @Singular
    @Nullable
    @JsonProperty("options")
    private List<PollOptionRequest> options;

    @Nullable
    @JsonProperty("voting_visibility")
    private VotingVisibility votingVisibility;

    @Nullable
    @JsonProperty("enforce_unique_vote")
    private Boolean enforceUniqueVote;

    @Nullable
    @JsonProperty("max_votes_allowed")
    private Integer maxVotesAllowed;

    @Nullable
    @JsonProperty("allow_user_suggested_options")
    private Boolean allowUserSuggestedOptions;

    @Nullable
    @JsonProperty("allow_answers")
    private Boolean allowAnswers;

    @Nullable
    @JsonProperty("is_closed")
    private Boolean isClosed;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;

    @Data
    @NoArgsConstructor
    public static class PollOptionRequest {
      @NotNull
      @JsonProperty("id")
      private String id;

      @Nullable
      @JsonProperty("text")
      private String text;

      @Nullable
      @JsonProperty("custom")
      private Map<String, Object> custom;
    }

    public static class PollUpdateRequest extends StreamRequest<PollUpdateResponse> {
      @NotNull private String pollId;

      private PollUpdateRequest(@NotNull String pollId) {
        this.pollId = pollId;
      }

      @Override
      protected Call<PollUpdateResponse> generateCall(Client client) {
        PollUpdateRequestData data = this.internalBuild();
        data.setId(pollId);
        return client.create(PollService.class).update(data);
      }
    }
  }

  @Builder(
      builderClassName = "PollUpdatePartialRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class PollUpdatePartialRequestData {
    @Nullable
    @JsonProperty("set")
    private Map<String, Object> set;

    @Singular("unset")
    @Nullable
    @JsonProperty("unset")
    private List<String> unset;

    public static class PollUpdatePartialRequest extends StreamRequest<PollUpdatePartialResponse> {
      @NotNull private String pollId;

      private PollUpdatePartialRequest(@NotNull String pollId) {
        this.pollId = pollId;
      }

      @Override
      protected Call<PollUpdatePartialResponse> generateCall(Client client) {
        return client.create(PollService.class).updatePartial(pollId, this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class PollDeleteRequest extends StreamRequest<PollDeleteResponse> {
    @NotNull private String pollId;

    @Override
    protected Call<PollDeleteResponse> generateCall(Client client) {
      return client.create(PollService.class).delete(pollId);
    }
  }

  @Builder(
      builderClassName = "PollQueryRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class PollQueryRequestData {
    @Nullable
    @JsonProperty("filter")
    private Map<String, Object> filter;

    @Singular("sort")
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    public static class PollQueryRequest extends StreamRequest<PollQueryResponse> {
      @Override
      protected Call<PollQueryResponse> generateCall(Client client) {
        return client.create(PollService.class).query(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "PollOptionCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class PollOptionCreateRequestData {
    @NotNull
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;

    public static class PollOptionCreateRequest extends StreamRequest<PollOptionCreateResponse> {
      @NotNull private String pollId;

      private PollOptionCreateRequest(@NotNull String pollId) {
        this.pollId = pollId;
      }

      @Override
      protected Call<PollOptionCreateResponse> generateCall(Client client) {
        return client.create(PollService.class).createOption(pollId, this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class PollOptionGetRequest extends StreamRequest<PollOptionGetResponse> {
    @NotNull private String pollId;
    @NotNull private String optionId;

    @Override
    protected Call<PollOptionGetResponse> generateCall(Client client) {
      return client.create(PollService.class).getOption(pollId, optionId);
    }
  }

  @Builder(
      builderClassName = "PollOptionUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class PollOptionUpdateRequestData {
    @NotNull
    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;

    public static class PollOptionUpdateRequest extends StreamRequest<PollOptionUpdateResponse> {
      @NotNull private String pollId;

      private PollOptionUpdateRequest(@NotNull String pollId) {
        this.pollId = pollId;
      }

      @Override
      protected Call<PollOptionUpdateResponse> generateCall(Client client) {
        return client.create(PollService.class).updateOption(pollId, this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class PollOptionDeleteRequest extends StreamRequest<PollOptionDeleteResponse> {
    @NotNull private String pollId;
    @NotNull private String optionId;

    @Override
    protected Call<PollOptionDeleteResponse> generateCall(Client client) {
      return client.create(PollService.class).deleteOption(pollId, optionId);
    }
  }

  @Builder(
      builderClassName = "PollVoteQueryRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class PollVoteQueryRequestData {
    @Nullable
    @JsonProperty("filter")
    private Map<String, Object> filter;

    @Singular("sort")
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    public static class PollVoteQueryRequest extends StreamRequest<PollVoteQueryResponse> {
      @NotNull private String pollId;

      private PollVoteQueryRequest(@NotNull String pollId) {
        this.pollId = pollId;
      }

      @Override
      protected Call<PollVoteQueryResponse> generateCall(Client client) {
        return client.create(PollService.class).queryVotes(pollId, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "PollVoteCastRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class PollVoteCastRequestData {
    @Nullable
    @JsonProperty("vote")
    private VoteData vote;

    @Data
    @NoArgsConstructor
    public static class VoteData {
      @Nullable
      @JsonProperty("option_id")
      private String optionId;

      @Nullable
      @JsonProperty("answer_text")
      private String answerText;
    }

    public static class PollVoteCastRequest extends StreamRequest<PollVoteCastResponse> {
      @NotNull private String messageId;
      @NotNull private String pollId;

      private PollVoteCastRequest(@NotNull String messageId, @NotNull String pollId) {
        this.messageId = messageId;
        this.pollId = pollId;
      }

      @Override
      protected Call<PollVoteCastResponse> generateCall(Client client) {
        return client.create(PollService.class).castVote(messageId, pollId, this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class PollVoteDeleteRequest extends StreamRequest<PollVoteDeleteResponse> {
    @NotNull private String messageId;
    @NotNull private String pollId;
    @NotNull private String voteId;

    @Override
    protected Call<PollVoteDeleteResponse> generateCall(Client client) {
      return client.create(PollService.class).deleteVote(messageId, pollId, voteId);
    }
  }

  // Response classes
  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollCreateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll")
    private Poll poll;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollGetResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll")
    private Poll poll;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll")
    private Poll poll;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollUpdatePartialResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll")
    private Poll poll;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll")
    private Poll poll;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollQueryResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("polls")
    private List<Poll> polls;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollOptionCreateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll_option")
    private PollOption pollOption;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollOptionGetResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll_option")
    private PollOption pollOption;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollOptionUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll_option")
    private PollOption pollOption;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollOptionDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll_option")
    private PollOption pollOption;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollVoteQueryResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("votes")
    private List<PollVote> votes;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollVoteCastResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("vote")
    private PollVote vote;

    @Nullable
    @JsonProperty("poll")
    private Poll poll;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class PollVoteDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll")
    private Poll poll;
  }

  // Factory methods
  /**
   * Creates a create request
   *
   * @return the created request
   */
  @NotNull
  public static PollCreateRequest create() {
    return new PollCreateRequest();
  }

  /**
   * Creates a get request
   *
   * @param pollId the poll id
   * @return the created request
   */
  @NotNull
  public static PollGetRequest get(@NotNull String pollId) {
    return new PollGetRequest(pollId);
  }

  /**
   * Creates an update request
   *
   * @param pollId the poll id
   * @return the created request
   */
  @NotNull
  public static PollUpdateRequest update(@NotNull String pollId) {
    return new PollUpdateRequest(pollId);
  }

  /**
   * Creates a partial update request
   *
   * @param pollId the poll id
   * @return the created request
   */
  @NotNull
  public static PollUpdatePartialRequest updatePartial(@NotNull String pollId) {
    return new PollUpdatePartialRequest(pollId);
  }

  /**
   * Creates a delete request
   *
   * @param pollId the poll id
   * @return the created request
   */
  @NotNull
  public static PollDeleteRequest delete(@NotNull String pollId) {
    return new PollDeleteRequest(pollId);
  }

  /**
   * Creates a query request
   *
   * @return the created request
   */
  @NotNull
  public static PollQueryRequest query() {
    return new PollQueryRequest();
  }

  /**
   * Creates a create option request
   *
   * @param pollId the poll id
   * @return the created request
   */
  @NotNull
  public static PollOptionCreateRequest createOption(@NotNull String pollId) {
    return new PollOptionCreateRequest(pollId);
  }

  /**
   * Creates a get option request
   *
   * @param pollId the poll id
   * @param optionId the option id
   * @return the created request
   */
  @NotNull
  public static PollOptionGetRequest getOption(@NotNull String pollId, @NotNull String optionId) {
    return new PollOptionGetRequest(pollId, optionId);
  }

  /**
   * Creates an update option request
   *
   * @param pollId the poll id
   * @return the created request
   */
  @NotNull
  public static PollOptionUpdateRequest updateOption(@NotNull String pollId) {
    return new PollOptionUpdateRequest(pollId);
  }

  /**
   * Creates a delete option request
   *
   * @param pollId the poll id
   * @param optionId the option id
   * @return the created request
   */
  @NotNull
  public static PollOptionDeleteRequest deleteOption(
      @NotNull String pollId, @NotNull String optionId) {
    return new PollOptionDeleteRequest(pollId, optionId);
  }

  /**
   * Creates a query votes request
   *
   * @param pollId the poll id
   * @return the created request
   */
  @NotNull
  public static PollVoteQueryRequest queryVotes(@NotNull String pollId) {
    return new PollVoteQueryRequest(pollId);
  }

  /**
   * Creates a cast vote request
   *
   * @param messageId the message id
   * @param pollId the poll id
   * @return the created request
   */
  @NotNull
  public static PollVoteCastRequest castVote(@NotNull String messageId, @NotNull String pollId) {
    return new PollVoteCastRequest(messageId, pollId);
  }

  /**
   * Creates a delete vote request
   *
   * @param messageId the message id
   * @param pollId the poll id
   * @param voteId the vote id
   * @return the created request
   */
  @NotNull
  public static PollVoteDeleteRequest deleteVote(
      @NotNull String messageId, @NotNull String pollId, @NotNull String voteId) {
    return new PollVoteDeleteRequest(messageId, pollId, voteId);
  }
}
