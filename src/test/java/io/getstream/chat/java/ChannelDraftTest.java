package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.ChannelMemberRequestObject;
import io.getstream.chat.java.models.Channel.ChannelRequestObject;
import io.getstream.chat.java.models.Draft;
import io.getstream.chat.java.models.FilterCondition;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.Sort;
import io.getstream.chat.java.models.User;
import io.getstream.chat.java.models.User.UserRequestObject;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests for the draft message functionality. */
public class ChannelDraftTest extends BasicTest {

  @DisplayName("Can create/get/delete a draft message in a channel")
  @Test
  void whenCreatingDraft_thenNoException() throws StreamException {
    // Prepare a draft message request
    String text = UUID.randomUUID().toString();
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();

    // Create the draft
    Draft.CreateDraftResponse response =
        Draft.createDraft(testChannel.getType(), testChannel.getId())
            .message(messageRequest)
            .userId(testUserRequestObject.getId())
            .request();

    // Verify the response
    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getDraft());
    Assertions.assertEquals(text, response.getDraft().getMessage().getText());

    // Then get the draft - note that userId is passed as a query param
    Draft.GetDraftResponse getResponse =
        Draft.getDraft(testChannel.getType(), testChannel.getId())
            .userId(testUserRequestObject.getId())
            .request();

    // Verify the response
    Assertions.assertNotNull(getResponse);
    Assertions.assertNotNull(getResponse.getDraft());
    Assertions.assertEquals(text, getResponse.getDraft().getMessage().getText());

    // Then delete the draft - note that userId is passed as a query param
    Draft.deleteDraft(testChannel.getType(), testChannel.getId())
        .userId(testUserRequestObject.getId())
        .request();

    // Verify that the draft is deleted by trying to get it (should throw an exception)
    Assertions.assertThrows(
        Exception.class,
        () ->
            Draft.getDraft(testChannel.getType(), testChannel.getId())
                .userId(testUserRequestObject.getId())
                .request());
  }

  @DisplayName("Can create/get/delete a draft message with a parent message")
  @Test
  void whenCreatingDraftWithParent_thenNoException() throws StreamException {
    // Prepare a draft message request with a parent message
    String text = UUID.randomUUID().toString();
    MessageRequestObject messageRequest =
        MessageRequestObject.builder()
            .text(text)
            .userId(testUserRequestObject.getId())
            .parentId(testMessage.getId())
            .build();

    // Create the draft
    Draft.CreateDraftResponse response =
        Draft.createDraft(testChannel.getType(), testChannel.getId())
            .message(messageRequest)
            .userId(testUserRequestObject.getId())
            .request();

    // Verify the response
    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getDraft());
    Assertions.assertEquals(text, response.getDraft().getMessage().getText());
    Assertions.assertEquals(testMessage.getId(), response.getDraft().getMessage().getParentId());

    // Then get the draft with parent_id specified as a query param
    Draft.GetDraftResponse getResponse =
        Draft.getDraft(testChannel.getType(), testChannel.getId())
            .userId(testUserRequestObject.getId())
            .parentId(testMessage.getId())
            .request();

    // Verify the response
    Assertions.assertNotNull(getResponse);
    Assertions.assertNotNull(getResponse.getDraft());
    Assertions.assertEquals(text, getResponse.getDraft().getMessage().getText());
    Assertions.assertEquals(testMessage.getId(), getResponse.getDraft().getMessage().getParentId());

    // Then delete the draft with parent_id specified as a query param
    Draft.deleteDraft(testChannel.getType(), testChannel.getId())
        .userId(testUserRequestObject.getId())
        .parentId(testMessage.getId())
        .request();

    // Verify that the draft is deleted by trying to get it (should throw an exception)
    Assertions.assertThrows(
        Exception.class,
        () ->
            Draft.getDraft(testChannel.getType(), testChannel.getId())
                .userId(testUserRequestObject.getId())
                .parentId(testMessage.getId())
                .request());
  }

  @DisplayName("Can query all drafts for a user")
  @Test
  void whenQueryingDrafts_thenNoException() throws StreamException {
    Draft.QueryDraftsResponse queryResponse =
        Draft.queryDrafts().userId(testUserRequestObject.getId()).limit(10).request();

    // Verify the response
    Assertions.assertNotNull(queryResponse);
    Assertions.assertNotNull(queryResponse.getDrafts());

    // Create a draft in the first channel
    String text1 = "Draft " + UUID.randomUUID().toString();
    MessageRequestObject messageRequest1 =
        MessageRequestObject.builder().text(text1).userId(testUserRequestObject.getId()).build();

    Draft.createDraft(testChannel.getType(), testChannel.getId())
        .message(messageRequest1)
        .userId(testUserRequestObject.getId())
        .request();

    // Create a second channel and add a draft there
    Channel secondChannel = createRandomChannel().getChannel();

    String text2 = "Draft " + UUID.randomUUID().toString();
    MessageRequestObject messageRequest2 =
        MessageRequestObject.builder().text(text2).userId(testUserRequestObject.getId()).build();

    Draft.createDraft(secondChannel.getType(), secondChannel.getId())
        .message(messageRequest2)
        .userId(testUserRequestObject.getId())
        .request();

    // Query all drafts
    Draft.QueryDraftsResponse queryResponse2 =
        Draft.queryDrafts().userId(testUserRequestObject.getId()).limit(10).request();

    // Verify the response
    Assertions.assertNotNull(queryResponse2);
    Assertions.assertNotNull(queryResponse2.getDrafts());
    Assertions.assertEquals(2, queryResponse2.getDrafts().size());
  }

  @DisplayName("Can query drafts with filters and sort")
  @Test
  void whenQueryingDraftsWithFiltersAndSort_thenNoException() throws StreamException {
    // Create a user
    UserRequestObject user =
        UserRequestObject.builder()
            .id("user-" + RandomStringUtils.randomAlphabetic(10))
            .name("User 1")
            .build();
    User.upsert().user(user).request();
    // Create a channel with a draft
    String channel1Id = UUID.randomUUID().toString();
    Channel.ChannelGetResponse channel1 =
        Channel.getOrCreate("messaging", channel1Id)
            .data(
                ChannelRequestObject.builder()
                    .createdBy(user)
                    .members(List.of(ChannelMemberRequestObject.builder().user(user).build()))
                    .build())
            .request();

    // Create a draft in the test channel
    String text1 = "Draft in channel 1";
    MessageRequestObject messageRequest1 =
        MessageRequestObject.builder().text(text1).userId(user.getId()).build();

    Draft.createDraft(channel1.getChannel().getType(), channel1.getChannel().getId())
        .message(messageRequest1)
        .userId(user.getId())
        .request();

    // Create another channel with a draft
    String channel2Id = UUID.randomUUID().toString();
    Channel.ChannelGetResponse channel2 =
        Channel.getOrCreate("messaging", channel2Id)
            .data(
                ChannelRequestObject.builder()
                    .createdBy(user)
                    .members(List.of(ChannelMemberRequestObject.builder().user(user).build()))
                    .build())
            .request();

    String text2 = "Draft in channel 2";
    MessageRequestObject messageRequest2 =
        MessageRequestObject.builder().text(text2).userId(user.getId()).build();

    Draft.createDraft(channel2.getChannel().getType(), channel2.getChannel().getId())
        .message(messageRequest2)
        .userId(user.getId())
        .request();

    // Query all drafts for the user
    Draft.QueryDraftsResponse response = Draft.queryDrafts().userId(user.getId()).request();

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getDrafts());
    Assertions.assertEquals(2, response.getDrafts().size());

    // Query drafts for a specific channel
    Draft.QueryDraftsResponse channelResponse =
        Draft.queryDrafts()
            .userId(user.getId())
            .filter(FilterCondition.eq("channel_cid", channel2.getChannel().getCId()))
            .request();

    Assertions.assertNotNull(channelResponse);
    Assertions.assertNotNull(channelResponse.getDrafts());
    Assertions.assertEquals(1, channelResponse.getDrafts().size());
    Assertions.assertEquals(text2, channelResponse.getDrafts().get(0).getMessage().getText());
    Assertions.assertEquals(
        channel2.getChannel().getCId(), channelResponse.getDrafts().get(0).getChannelCid());

    // Query drafts with sort
    Draft.QueryDraftsResponse sortedResponse =
        Draft.queryDrafts()
            .userId(user.getId())
            .sort(Sort.builder().field("created_at").direction(Sort.Direction.ASC).build())
            .request();

    Assertions.assertNotNull(sortedResponse);
    Assertions.assertNotNull(sortedResponse.getDrafts());
    Assertions.assertEquals(2, sortedResponse.getDrafts().size());
    Assertions.assertEquals(
        channel1.getChannel().getCId(), sortedResponse.getDrafts().get(0).getChannelCid());
    Assertions.assertEquals(
        channel2.getChannel().getCId(), sortedResponse.getDrafts().get(1).getChannelCid());

    // Query drafts with pagination
    Draft.QueryDraftsResponse paginatedResponse =
        Draft.queryDrafts().userId(user.getId()).limit(1).request();

    Assertions.assertNotNull(paginatedResponse);
    Assertions.assertNotNull(paginatedResponse.getDrafts());
    Assertions.assertEquals(1, paginatedResponse.getDrafts().size());
    Assertions.assertEquals(
        channel2.getChannel().getCId(), paginatedResponse.getDrafts().get(0).getChannelCid());
    Assertions.assertNotNull(paginatedResponse.getNext());

    // Query drafts with next page
    Draft.QueryDraftsResponse nextPageResponse =
        Draft.queryDrafts()
            .userId(user.getId())
            .limit(1)
            .next(paginatedResponse.getNext())
            .request();

    Assertions.assertNotNull(nextPageResponse);
    Assertions.assertNotNull(nextPageResponse.getDrafts());
    Assertions.assertEquals(1, nextPageResponse.getDrafts().size());
    Assertions.assertEquals(
        channel1.getChannel().getCId(), nextPageResponse.getDrafts().get(0).getChannelCid());
  }
}
