package io.getstream.chat.java;

import io.getstream.chat.java.models.Poll;
import io.getstream.chat.java.models.Poll.CreatePollResponse;
import io.getstream.chat.java.models.Poll.GetPollResponse;
import io.getstream.chat.java.models.Poll.PollOptionRequestObject;
import io.getstream.chat.java.models.Poll.UpdatePollResponse;
import io.getstream.chat.java.models.Poll.VotingVisibility;
import io.getstream.chat.java.models.framework.StreamResponseObject;
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

  @DisplayName("Can get a poll by ID")
  @Test
  void whenGettingPoll_thenNoException() {
    // Create a poll first
    String pollName = "Get Test Poll " + UUID.randomUUID();
    CreatePollResponse createResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.create()
                    .name(pollName)
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("Option 1").build())
                    .option(PollOptionRequestObject.builder().text("Option 2").build())
                    .request());

    String pollId = createResponse.getPoll().getId();

    // Get the poll
    GetPollResponse getResponse = Assertions.assertDoesNotThrow(() -> Poll.get(pollId).request());

    Assertions.assertNotNull(getResponse);
    Assertions.assertNotNull(getResponse.getPoll());
    Assertions.assertEquals(pollId, getResponse.getPoll().getId());
    Assertions.assertEquals(pollName, getResponse.getPoll().getName());
  }

  @DisplayName("Can get a poll with user_id")
  @Test
  void whenGettingPollWithUserId_thenNoException() {
    // Create a poll first
    CreatePollResponse createResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.create()
                    .name("Poll with user " + UUID.randomUUID())
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("A").build())
                    .request());

    // Get the poll with user_id
    GetPollResponse getResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.get(createResponse.getPoll().getId())
                    .userId(testUserRequestObject.getId())
                    .request());

    Assertions.assertNotNull(getResponse.getPoll());
  }

  @DisplayName("Can update a poll")
  @Test
  void whenUpdatingPoll_thenNoException() {
    // Create a poll first
    CreatePollResponse createResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.create()
                    .name("Original Name")
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("Option 1").build())
                    .request());

    String pollId = createResponse.getPoll().getId();

    // Update the poll
    String newName = "Updated Name " + UUID.randomUUID();
    UpdatePollResponse updateResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.update()
                    .id(pollId)
                    .name(newName)
                    .description("Updated description")
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("New Option 1").build())
                    .option(PollOptionRequestObject.builder().text("New Option 2").build())
                    .request());

    Assertions.assertNotNull(updateResponse);
    Assertions.assertEquals(newName, updateResponse.getPoll().getName());
    Assertions.assertEquals("Updated description", updateResponse.getPoll().getDescription());
  }

  @DisplayName("Can close a poll via update")
  @Test
  void whenClosingPoll_thenNoException() {
    // Create a poll first
    CreatePollResponse createResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.create()
                    .name("Poll to close " + UUID.randomUUID())
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("A").build())
                    .request());

    String pollId = createResponse.getPoll().getId();

    // Close the poll
    UpdatePollResponse updateResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.update()
                    .id(pollId)
                    .name("Poll to close")
                    .isClosed(true)
                    .userId(testUserRequestObject.getId())
                    .request());

    Assertions.assertNotNull(updateResponse);
    Assertions.assertTrue(updateResponse.getPoll().getIsClosed());
  }

  @DisplayName("Can partially update a poll with set")
  @Test
  void whenPartiallyUpdatingPollWithSet_thenNoException() {
    // Create a poll first
    CreatePollResponse createResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.create()
                    .name("Original Partial " + UUID.randomUUID())
                    .description("Original description")
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("A").build())
                    .request());

    String pollId = createResponse.getPoll().getId();

    // Partial update the poll
    UpdatePollResponse updateResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.partialUpdate(pollId)
                    .setValue("name", "Partial Updated Name")
                    .userId(testUserRequestObject.getId())
                    .request());

    Assertions.assertNotNull(updateResponse);
    Assertions.assertEquals("Partial Updated Name", updateResponse.getPoll().getName());
    // Description should remain unchanged
    Assertions.assertEquals("Original description", updateResponse.getPoll().getDescription());
  }

  @DisplayName("Can partially update a poll with unset")
  @Test
  void whenPartiallyUpdatingPollWithUnset_thenNoException() {
    // Create a poll first
    CreatePollResponse createResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.create()
                    .name("Poll with description " + UUID.randomUUID())
                    .description("To be removed")
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("A").build())
                    .request());

    String pollId = createResponse.getPoll().getId();

    // Partial update to unset description
    UpdatePollResponse updateResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.partialUpdate(pollId)
                    .unsetValue("description")
                    .userId(testUserRequestObject.getId())
                    .request());

    Assertions.assertNotNull(updateResponse);
    Assertions.assertNull(updateResponse.getPoll().getDescription());
  }

  @DisplayName("Can delete a poll")
  @Test
  void whenDeletingPoll_thenNoException() {
    // Create a poll first
    CreatePollResponse createResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Poll.create()
                    .name("Poll to delete " + UUID.randomUUID())
                    .userId(testUserRequestObject.getId())
                    .option(PollOptionRequestObject.builder().text("A").build())
                    .request());

    String pollId = createResponse.getPoll().getId();

    // Delete the poll
    StreamResponseObject deleteResponse =
        Assertions.assertDoesNotThrow(
            () -> Poll.delete(pollId).userId(testUserRequestObject.getId()).request());

    Assertions.assertNotNull(deleteResponse);
  }
}
