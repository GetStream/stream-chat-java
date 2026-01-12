package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.*;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Poll.CreatePollRequestData.CreatePollRequest;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.framework.RequestObjectBuilder;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.PollService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

/** Represents poll functionality in Stream Chat. */
@Data
@NoArgsConstructor
public class Poll {
  @Nullable
  @JsonProperty("id")
  private String id;

  @Nullable
  @JsonProperty("name")
  private String name;

  @Nullable
  @JsonProperty("description")
  private String description;

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
  @JsonProperty("created_by_id")
  private String createdById;

  @Nullable
  @JsonProperty("created_by")
  private User createdBy;

  @Nullable
  @JsonProperty("created_at")
  private Date createdAt;

  @Nullable
  @JsonProperty("updated_at")
  private Date updatedAt;

  @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return this.additionalFields;
  }

  @JsonAnySetter
  public void setAdditionalField(String name, Object value) {
    this.additionalFields.put(name, value);
  }

  /** Voting visibility options for a poll. */
  public enum VotingVisibility {
    @JsonProperty("public")
    PUBLIC,
    @JsonProperty("anonymous")
    ANONYMOUS,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  /** A poll option. */
  @Data
  @NoArgsConstructor
  public static class PollOption {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("position")
    private Integer position;

    @Nullable
    @JsonProperty("vote_count")
    private Integer voteCount;

    @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
      this.additionalFields.put(name, value);
    }
  }

  /** Request object for poll options. */
  @Builder
  @Setter
  @Getter
  @EqualsAndHashCode
  public static class PollOptionRequestObject {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("position")
    private Integer position;

    @Singular @Nullable @JsonIgnore private Map<String, Object> additionalFields;

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
      this.additionalFields.put(name, value);
    }

    @Nullable
    public static PollOptionRequestObject buildFrom(@Nullable PollOption pollOption) {
      return RequestObjectBuilder.build(PollOptionRequestObject.class, pollOption);
    }
  }

  /** Request object for polls. */
  @Builder
  @Setter
  @Getter
  @EqualsAndHashCode
  public static class PollRequestObject {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("description")
    private String description;

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

    @Singular
    @Nullable
    @JsonProperty("options")
    private List<PollOptionRequestObject> options;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    @Singular @Nullable @JsonIgnore private Map<String, Object> additionalFields;

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
      this.additionalFields.put(name, value);
    }

    @Nullable
    public static PollRequestObject buildFrom(@Nullable Poll poll) {
      return RequestObjectBuilder.build(PollRequestObject.class, poll);
    }
  }

  /** Response for poll creation. */
  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CreatePollResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("poll")
    private Poll poll;
  }

  /** Request data for creating a poll. */
  @Builder(
      builderClassName = "CreatePollRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class CreatePollRequestData {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("description")
    private String description;

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

    @Singular
    @Nullable
    @JsonProperty("options")
    private List<PollOptionRequestObject> options;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class CreatePollRequest extends StreamRequest<CreatePollResponse> {
      public CreatePollRequest() {}

      @Override
      protected Call<CreatePollResponse> generateCall(Client client) throws StreamException {
        return client.create(PollService.class).create(this.internalBuild());
      }
    }
  }

  /**
   * Creates a poll creation request.
   *
   * @return the created request
   */
  @NotNull
  public static CreatePollRequest create() {
    return new CreatePollRequest();
  }
}
