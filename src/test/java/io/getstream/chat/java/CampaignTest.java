package io.getstream.chat.java;

import io.getstream.chat.java.models.Campaign;
import io.getstream.chat.java.models.Campaign.MessageTemplate;
import io.getstream.chat.java.models.FilterCondition;
import io.getstream.chat.java.models.Sort;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CampaignTest extends BasicTest {

  @DisplayName("Can create campaign")
  @Test
  void whenCreatingCampaign_thenCorrectName() {
    String campaignName = "test campaign";
    MessageTemplate messageTemplate = new MessageTemplate();
    messageTemplate.setText("Hello");

    Campaign campaign =
        Assertions.assertDoesNotThrow(
                () ->
                    Campaign.create()
                        .senderId(testUserRequestObject.getId())
                        .userIds(List.of(testUsersRequestObjects.get(1).getId()))
                        .messageTemplate(messageTemplate)
                        .name(campaignName)
                        .request())
            .getCampaign();
    Assertions.assertEquals(campaignName, campaign.getName());
    Assertions.assertNotNull(campaign.getId());

    // Cleanup
    Assertions.assertDoesNotThrow(() -> Campaign.delete(campaign.getId()).request());
  }

  @DisplayName("Can perform campaign CRUD operations")
  @Test
  void whenPerformingCampaignCRUD_thenCorrectOperations() {
    MessageTemplate messageTemplate = new MessageTemplate();
    messageTemplate.setText("Hello");

    // Create
    String originalName = "original name";
    Campaign created =
        Assertions.assertDoesNotThrow(
                () ->
                    Campaign.create()
                        .senderId(testUserRequestObject.getId())
                        .userIds(List.of(testUsersRequestObjects.get(1).getId()))
                        .messageTemplate(messageTemplate)
                        .name(originalName)
                        .request())
            .getCampaign();
    Assertions.assertEquals(originalName, created.getName());
    String campaignId = created.getId();

    pause();

    // Read
    Campaign retrieved =
        Assertions.assertDoesNotThrow(() -> Campaign.get(campaignId).request()).getCampaign();
    Assertions.assertEquals(campaignId, retrieved.getId());
    Assertions.assertEquals(originalName, retrieved.getName());

    // Update
    String updatedName = "updated name";
    Campaign updated =
        Assertions.assertDoesNotThrow(
                () ->
                    Campaign.update(campaignId)
                        .name(updatedName)
                        .messageTemplate(messageTemplate)
                        .senderId(testUserRequestObject.getId())
                        .userIds(List.of(testUsersRequestObjects.get(1).getId()))
                        .request())
            .getCampaign();
    Assertions.assertEquals(updatedName, updated.getName());

    pause();

    // Delete
    Assertions.assertDoesNotThrow(() -> Campaign.delete(campaignId).request());
  }

  @DisplayName("Can start and stop campaign")
  @Test
  void whenStartingAndStoppingCampaign_thenCorrectOperations() {
    MessageTemplate messageTemplate = new MessageTemplate();
    messageTemplate.setText("Hello");

    Campaign created =
        Assertions.assertDoesNotThrow(
                () ->
                    Campaign.create()
                        .senderId(testUserRequestObject.getId())
                        .userIds(List.of(testUsersRequestObjects.get(1).getId()))
                        .messageTemplate(messageTemplate)
                        .name("test campaign")
                        .request())
            .getCampaign();
    String campaignId = created.getId();

    pause();

    // Start with scheduling
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.HOUR, 1);
    Date scheduledFor = cal.getTime();
    cal.add(Calendar.HOUR, 1);
    Date stopAt = cal.getTime();

    Campaign started =
        Assertions.assertDoesNotThrow(
                () ->
                    Campaign.start(campaignId)
                        .scheduledFor(scheduledFor)
                        .stopAt(stopAt)
                        .request())
            .getCampaign();
    Assertions.assertNotNull(started.getScheduledFor());

    pause();

    // Stop
    Campaign stopped =
        Assertions.assertDoesNotThrow(() -> Campaign.stop(campaignId).request()).getCampaign();
    Assertions.assertNotNull(stopped);

    // Cleanup
    Assertions.assertDoesNotThrow(() -> Campaign.delete(campaignId).request());
  }

  @DisplayName("Can query campaigns")
  @Test
  void whenQueryingCampaigns_thenCorrectResults() {
    MessageTemplate messageTemplate = new MessageTemplate();
    messageTemplate.setText("Hello");

    Campaign created =
        Assertions.assertDoesNotThrow(
                () ->
                    Campaign.create()
                        .senderId(testUserRequestObject.getId())
                        .userIds(List.of(testUsersRequestObjects.get(1).getId()))
                        .messageTemplate(messageTemplate)
                        .name("query test campaign")
                        .request())
            .getCampaign();
    String campaignId = created.getId();

    pause();

    // Query by ID
    List<Campaign> campaigns =
        Assertions.assertDoesNotThrow(
                () ->
                    Campaign.query()
                        .filter(FilterCondition.eq("id", campaignId))
                        .sorts(
                            List.of(
                                Sort.builder()
                                    .field("created_at")
                                    .direction(Sort.Direction.DESC)
                                    .build()))
                        .limit(10)
                        .request())
            .getCampaigns();
    Assertions.assertTrue(
        campaigns.stream().anyMatch(c -> c.getId().equals(campaignId)),
        "Created campaign should be found in query results");

    // Cleanup
    Assertions.assertDoesNotThrow(() -> Campaign.delete(campaignId).request());
  }

  @DisplayName("Can create campaign without ID")
  @Test
  void whenCreatingCampaignWithoutId_thenIdIsGenerated() {
    MessageTemplate messageTemplate = new MessageTemplate();
    messageTemplate.setText("Hello");

    Campaign campaign =
        Assertions.assertDoesNotThrow(
                () ->
                    Campaign.create()
                        .senderId(testUserRequestObject.getId())
                        .userIds(List.of(testUsersRequestObjects.get(1).getId()))
                        .messageTemplate(messageTemplate)
                        .name("auto id campaign")
                        .request())
            .getCampaign();
    Assertions.assertNotNull(campaign.getId());
    Assertions.assertFalse(campaign.getId().isEmpty());

    // Cleanup
    Assertions.assertDoesNotThrow(() -> Campaign.delete(campaign.getId()).request());
  }
}
