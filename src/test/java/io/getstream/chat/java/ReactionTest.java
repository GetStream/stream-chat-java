package io.getstream.chat.java;

import io.getstream.chat.java.models.Reaction;
import io.getstream.chat.java.models.Reaction.ReactionRequestObject;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReactionTest extends BasicTest {

  @DisplayName("Can send reaction")
  @Test
  void whenSendingReaction_thenNoException() {
    String reactionType = RandomStringUtils.randomAlphabetic(10);
    Reaction reaction =
        Assertions.assertDoesNotThrow(
                () ->
                    Reaction.send(testMessage.getId())
                        .reaction(
                            ReactionRequestObject.builder()
                                .type(reactionType)
                                .user(testUserRequestObject)
                                .build())
                        .request())
            .getReaction();
    Assertions.assertEquals(reactionType, reaction.getType());
  }

  @DisplayName("Cannot send multiple reactions if enforce unique")
  @Test
  void whenSendingMultipleReactionAndEnforceUnique_thenDoesNotCreateSecond() {
    String reactionType = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
            () ->
                Reaction.send(testMessage.getId())
                    .reaction(
                        ReactionRequestObject.builder()
                            .type(reactionType)
                            .user(testUserRequestObject)
                            .build())
                    .request())
        .getReaction();
    Assertions.assertDoesNotThrow(
        () ->
            Reaction.send(testMessage.getId())
                .reaction(
                    ReactionRequestObject.builder()
                        .type(reactionType)
                        .user(testUserRequestObject)
                        .build())
                .enforceUnique(true)
                .request());
    List<Reaction> reactions =
        Assertions.assertDoesNotThrow(() -> Reaction.list(testMessage.getId()).request())
            .getReactions();
    Assertions.assertEquals(
        1,
        reactions.stream()
            .filter(consideredReaction -> consideredReaction.getType().equals(reactionType))
            .count());
  }

  @DisplayName("Can delete reaction")
  @Test
  void whenDeletingReaction_thenIsDeleted() {
    String reactionType = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
        () ->
            Reaction.send(testMessage.getId())
                .reaction(
                    ReactionRequestObject.builder()
                        .type(reactionType)
                        .user(testUserRequestObject)
                        .build())
                .request());
    Assertions.assertDoesNotThrow(
        () ->
            Reaction.delete(testMessage.getId(), reactionType)
                .userId(testUserRequestObject.getId())
                .request());
    List<Reaction> reactions =
        Assertions.assertDoesNotThrow(() -> Reaction.list(testMessage.getId()).request())
            .getReactions();
    Assertions.assertEquals(
        0,
        reactions.stream()
            .filter(consideredReaction -> consideredReaction.getType().equals(reactionType))
            .count());
  }

  @DisplayName("Can list reactions")
  @Test
  void whenListingReactions_thenCanRetrieve() {
    String reactionType = RandomStringUtils.randomAlphabetic(10);
    Assertions.assertDoesNotThrow(
            () ->
                Reaction.send(testMessage.getId())
                    .reaction(
                        ReactionRequestObject.builder()
                            .type(reactionType)
                            .user(testUserRequestObject)
                            .build())
                    .request())
        .getReaction();
    List<Reaction> reactions =
        Assertions.assertDoesNotThrow(() -> Reaction.list(testMessage.getId()).request())
            .getReactions();
    Assertions.assertTrue(
        reactions.stream()
            .anyMatch(consideredReaction -> consideredReaction.getType().equals(reactionType)));
  }
}
