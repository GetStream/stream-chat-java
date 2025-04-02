package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Draft.CreateDraftRequestData;
import io.getstream.chat.java.models.Draft.CreateDraftResponse;
import io.getstream.chat.java.models.Draft.GetDraftResponse;
import io.getstream.chat.java.models.Draft.QueryDraftsRequestData;
import io.getstream.chat.java.models.Draft.QueryDraftsResponse;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.*;

/** Service for managing draft messages in channels. */
public interface DraftService {
  /**
   * Creates a draft message in a channel.
   *
   * @param type The channel type
   * @param id The channel ID
   * @param request The draft creation request data
   * @return A response with the created draft
   */
  @POST("channels/{type}/{id}/draft")
  Call<CreateDraftResponse> createDraft(
      @Path("type") String type, @Path("id") String id, @Body CreateDraftRequestData request);

  /**
   * Deletes a draft message from a channel.
   *
   * @param type The channel type
   * @param id The channel ID
   * @param userId The user ID
   * @param parentId Optional parent message ID
   * @return A response indicating success
   */
  @DELETE("channels/{type}/{id}/draft")
  Call<StreamResponseObject> deleteDraft(
      @Path("type") String type,
      @Path("id") String id,
      @Query("user_id") String userId,
      @Query("parent_id") @Nullable String parentId);

  /**
   * Gets a draft message from a channel.
   *
   * @param type The channel type
   * @param id The channel ID
   * @param userId The user ID
   * @param parentId Optional parent message ID
   * @return A response with the draft
   */
  @GET("channels/{type}/{id}/draft")
  Call<GetDraftResponse> getDraft(
      @Path("type") String type,
      @Path("id") String id,
      @Query("user_id") String userId,
      @Query("parent_id") @Nullable String parentId);

  /**
   * Queries all drafts for a user.
   *
   * @param request The query parameters
   * @return A response with the matching drafts
   */
  @POST("drafts/query")
  Call<QueryDraftsResponse> queryDrafts(@Body QueryDraftsRequestData request);
}
