package io.getstream.chat.java;

import io.getstream.chat.java.models.Poll;
import io.getstream.chat.java.models.Poll.CreatePollResponse;
import io.getstream.chat.java.models.Poll.PollOptionRequestObject;
import io.getstream.chat.java.models.Poll.VotingVisibility;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for Poll functionality. */
public class PollTest extends BasicTest {

  @DisplayName("Can create a basic poll")
  @Test
  void whenCreatingPoll_thenNoException() {
    String pollName = "Test Poll " + UUID.randomUUID();

    CreatePollResponse response =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.create()
                    .name(pollName)
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("Option 1").build())
                    .option(PollOptionRequestObject.builder().text("Option 2").build())
                    .request());

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getPoll());
    Assertions.assertEquals(pollName, response.getPoll().getName());
    Assertions.assertNotNull(response.getPoll().getId());
    Assertions.assertEquals(2, response.getPoll().getOptions().size());
  }

  @DisplayName("Can create a poll with all options")
  @Test
  void whenCreatingPollWithAllOptions_thenNoException() {
    String pollName = "Comprehensive Poll " + UUID.randomUUID();

    CreatePollResponse response =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.create()
                    .name(pollName)
                    .description("A test poll with all options")
                    .votingVisibility(VotingVisibility.PUBLIC)
                    .enforceUniqueVote(true)
                    .maxVotesAllowed(3)
                    .allowUserSuggestedOptions(true)
                    .allowAnswers(true)
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("Option A").position(0).build())
                    .option(PollOptionRequestObject.builder().text("Option B").position(1).build())
                    .option(PollOptionRequestObject.builder().text("Option C").position(2).build())
                    .request());

    Assertions.assertNotNull(response);
    Poll poll = response.getPoll();
    Assertions.assertEquals(pollName, poll.getName());
    Assertions.assertEquals("A test poll with all options", poll.getDescription());
    Assertions.assertEquals(VotingVisibility.PUBLIC, poll.getVotingVisibility());
    Assertions.assertEquals(true, poll.getEnforceUniqueVote());
    Assertions.assertEquals(3, poll.getMaxVotesAllowed());
    Assertions.assertEquals(true, poll.getAllowUserSuggestedOptions());
    Assertions.assertEquals(true, poll.getAllowAnswers());
    Assertions.assertEquals(3, poll.getOptions().size());
  }

  @DisplayName("Can create poll with anonymous voting")
  @Test
  void whenCreatingAnonymousPoll_thenNoException() {
    CreatePollResponse response =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.create()
                    .name("Anonymous Poll " + UUID.randomUUID())
                    .votingVisibility(VotingVisibility.ANONYMOUS)
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("Choice A").build())
                    .option(PollOptionRequestObject.builder().text("Choice B").build())
                    .request());

    Assertions.assertNotNull(response);
    Assertions.assertEquals(VotingVisibility.ANONYMOUS, response.getPoll().getVotingVisibility());
  }
}
