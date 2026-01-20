package io.getstream.chat.java;

import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.ChannelType;
import io.getstream.chat.java.models.FilterCondition;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Poll;
import io.getstream.chat.java.models.Poll.PollCreateRequestData.PollOptionInput;
import io.getstream.chat.java.models.Poll.PollUpdateRequestData.PollOptionRequest;
import io.getstream.chat.java.models.Sort;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PollTest extends BasicTest {

  @BeforeAll
  static void setupPolls() {
    // Enable polls on the channel type
    Assertions.assertDoesNotThrow(
        () -> ChannelType.update(testChannel.getType()).polls(true).request());

    // Also enable polls on the channel via config_overrides
    Map<String, Object> configOverrides = new HashMap<>();
    configOverrides.put("polls", true);

    Assertions.assertDoesNotThrow(
        () ->
            Channel.partialUpdate(testChannel.getType(), testChannel.getId())
                .setValue("config_overrides", configOverrides)
                .request());

    // Wait for changes to propagate
    try {
      Thread.sleep(6000);
    } catch (InterruptedException e) {
      // Do nothing
    }
  }

  @DisplayName("Can create poll")
  @Test
  void whenCreatingPoll_thenCorrectName() {
    String pollName = "test poll";
    PollOptionInput option1 = new PollOptionInput();
    option1.setText("Option 1");
    PollOptionInput option2 = new PollOptionInput();
    option2.setText("Option 2");

    Poll poll =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.create()
                        .userId(testUserRequestObject.getId())
                        .name(pollName)
                        .description("Test description")
                        .options(List.of(option1, option2))
                        .votingVisibility(Poll.VotingVisibility.PUBLIC)
                        .enforceUniqueVote(false)
                        .request())
            .getPoll();
    Assertions.assertEquals(pollName, poll.getName());
    Assertions.assertNotNull(poll.getId());
    Assertions.assertNotNull(poll.getOptions());
    Assertions.assertEquals(2, poll.getOptions().size());

    // Cleanup
    Assertions.assertDoesNotThrow(
        () -> Poll.delete(poll.getId()).userId(testUserRequestObject.getId()).request());
  }

  @DisplayName("Can perform poll CRUD operations")
  @Test
  void whenPerformingPollCRUD_thenCorrectOperations() {
    PollOptionInput option1 = new PollOptionInput();
    option1.setText("Option 1");
    PollOptionInput option2 = new PollOptionInput();
    option2.setText("Option 2");

    // Create
    String originalName = "original poll name";
    Poll created =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.create()
                        .userId(testUserRequestObject.getId())
                        .name(originalName)
                        .description("Original description")
                        .options(List.of(option1, option2))
                        .votingVisibility(Poll.VotingVisibility.PUBLIC)
                        .enforceUniqueVote(false)
                        .request())
            .getPoll();
    Assertions.assertEquals(originalName, created.getName());
    String pollId = created.getId();

    pause();

    // Read
    Poll retrieved = Assertions.assertDoesNotThrow(() -> Poll.get(pollId).request()).getPoll();
    Assertions.assertEquals(pollId, retrieved.getId());
    Assertions.assertEquals(originalName, retrieved.getName());

    // Update
    String updatedName = "updated poll name";
    PollOptionRequest updatedOption1 = new PollOptionRequest();
    updatedOption1.setId(created.getOptions().get(0).getId());
    updatedOption1.setText("Updated Option 1");
    PollOptionRequest updatedOption2 = new PollOptionRequest();
    updatedOption2.setId(created.getOptions().get(1).getId());
    updatedOption2.setText("Updated Option 2");

    Poll updated =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.update(pollId)
                        .userId(testUserRequestObject.getId())
                        .name(updatedName)
                        .description("Updated description")
                        .options(List.of(updatedOption1, updatedOption2))
                        .votingVisibility(Poll.VotingVisibility.PUBLIC)
                        .enforceUniqueVote(false)
                        .request())
            .getPoll();
    Assertions.assertEquals(updatedName, updated.getName());

    pause();

    // Update Partial
    Map<String, Object> setFields = new HashMap<>();
    setFields.put("name", "partially updated name");
    Poll partiallyUpdated =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.updatePartial(pollId)
                        .userId(testUserRequestObject.getId())
                        .set(setFields)
                        .request())
            .getPoll();
    Assertions.assertEquals("partially updated name", partiallyUpdated.getName());

    pause();

    // Delete
    Assertions.assertDoesNotThrow(
        () -> Poll.delete(pollId).userId(testUserRequestObject.getId()).request());
  }

  @DisplayName("Can perform poll option CRUD operations")
  @Test
  void whenPerformingPollOptionCRUD_thenCorrectOperations() {
    PollOptionInput option1 = new PollOptionInput();
    option1.setText("Option 1");

    Poll created =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.create()
                        .userId(testUserRequestObject.getId())
                        .name("poll with options")
                        .description("Test poll")
                        .options(List.of(option1))
                        .votingVisibility(Poll.VotingVisibility.PUBLIC)
                        .enforceUniqueVote(false)
                        .request())
            .getPoll();
    String pollId = created.getId();

    pause();

    // Create option
    Poll.PollOption newOption =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.createOption(pollId)
                        .userId(testUserRequestObject.getId())
                        .text("New Option")
                        .request())
            .getPollOption();
    Assertions.assertNotNull(newOption.getId());
    Assertions.assertEquals("New Option", newOption.getText());

    pause();

    // Get option
    Poll.PollOption retrievedOption =
        Assertions.assertDoesNotThrow(() -> Poll.getOption(pollId, newOption.getId()).request())
            .getPollOption();
    Assertions.assertEquals(newOption.getId(), retrievedOption.getId());

    // Update option
    Poll.PollOption updatedOption =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.updateOption(pollId)
                        .userId(testUserRequestObject.getId())
                        .id(newOption.getId())
                        .text("Updated Option Text")
                        .request())
            .getPollOption();
    Assertions.assertEquals("Updated Option Text", updatedOption.getText());

    pause();

    // Delete option
    Assertions.assertDoesNotThrow(
        () ->
            Poll.deleteOption(pollId, updatedOption.getId())
                .userId(testUserRequestObject.getId())
                .request());

    // Cleanup
    Assertions.assertDoesNotThrow(
        () -> Poll.delete(pollId).userId(testUserRequestObject.getId()).request());
  }

  @DisplayName("Can query polls")
  @Test
  void whenQueryingPolls_thenCorrectResults() {
    PollOptionInput option1 = new PollOptionInput();
    option1.setText("Option 1");

    Poll created =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.create()
                        .userId(testUserRequestObject.getId())
                        .name("query test poll")
                        .description("Test poll for query")
                        .options(List.of(option1))
                        .votingVisibility(Poll.VotingVisibility.PUBLIC)
                        .enforceUniqueVote(false)
                        .request())
            .getPoll();
    String pollId = created.getId();

    pause();

    // Query by ID
    List<Poll> polls =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.query()
                        .userId(testUserRequestObject.getId())
                        .filter(FilterCondition.eq("id", pollId))
                        .sorts(
                            List.of(
                                Sort.builder()
                                    .field("created_at")
                                    .direction(Sort.Direction.DESC)
                                    .build()))
                        .limit(10)
                        .request())
            .getPolls();
    Assertions.assertTrue(
        polls.stream().anyMatch(p -> p.getId().equals(pollId)),
        "Created poll should be found in query results");

    // Cleanup
    Assertions.assertDoesNotThrow(
        () -> Poll.delete(pollId).userId(testUserRequestObject.getId()).request());
  }

  @DisplayName("Can create poll without ID")
  @Test
  void whenCreatingPollWithoutId_thenIdIsGenerated() {
    PollOptionInput option1 = new PollOptionInput();
    option1.setText("Option 1");

    Poll poll =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.create()
                        .userId(testUserRequestObject.getId())
                        .name("auto id poll")
                        .description("Test poll")
                        .options(List.of(option1))
                        .votingVisibility(Poll.VotingVisibility.PUBLIC)
                        .enforceUniqueVote(false)
                        .request())
            .getPoll();
    Assertions.assertNotNull(poll.getId());
    Assertions.assertFalse(poll.getId().isEmpty());

    // Cleanup
    Assertions.assertDoesNotThrow(
        () -> Poll.delete(poll.getId()).userId(testUserRequestObject.getId()).request());
  }

  @DisplayName("Can query poll votes")
  @Test
  void whenQueryingPollVotes_thenCorrectResults() {
    PollOptionInput option1 = new PollOptionInput();
    option1.setText("Option 1");
    PollOptionInput option2 = new PollOptionInput();
    option2.setText("Option 2");

    Poll created =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.create()
                        .userId(testUserRequestObject.getId())
                        .name("poll for votes")
                        .description("Test poll")
                        .options(List.of(option1, option2))
                        .votingVisibility(Poll.VotingVisibility.PUBLIC)
                        .enforceUniqueVote(false)
                        .request())
            .getPoll();
    String pollId = created.getId();

    pause();

    // Attach poll to a message (required before querying votes)
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text("Message with poll")
            .userId(testUserRequestObject.getId())
            .additionalField("poll_id", pollId)
            .build();
    Assertions.assertDoesNotThrow(
        () ->
            Message.send(testChannel.getType(), testChannel.getId())
                .message(messageRequest)
                .request());

    pause();

    // Query votes (should be empty initially)
    List<Poll.PollVote> votes =
        Assertions.assertDoesNotThrow(
                () ->
                    Poll.queryVotes(pollId)
                        .userId(testUserRequestObject.getId())
                        .limit(10)
                        .request())
            .getVotes();
    Assertions.assertNotNull(votes);

    // Cleanup
    Assertions.assertDoesNotThrow(
        () -> Poll.delete(pollId).userId(testUserRequestObject.getId()).request());
  }
}
