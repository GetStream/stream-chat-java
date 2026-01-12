package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Poll.CreatePollOptionRequestData;
import io.getstream.chat.java.models.Poll.CreatePollOptionResponse;
import io.getstream.chat.java.models.Poll.CreatePollRequestData;
import io.getstream.chat.java.models.Poll.CreatePollResponse;
import io.getstream.chat.java.models.Poll.GetPollResponse;
import io.getstream.chat.java.models.Poll.PartialUpdatePollRequestData;
import io.getstream.chat.java.models.Poll.UpdatePollRequestData;
import io.getstream.chat.java.models.Poll.UpdatePollResponse;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/** Service interface for Poll API endpoints. */
public interface PollService {

  /**
   * Creates a new poll.
   *
   * @param request The poll creation request data
   * @return A response with the created poll
   */
  @POST("polls")
  Call<CreatePollResponse> create(@NotNull @Body CreatePollRequestData request);

  /**
   * Gets a poll by ID.
   *
   * @param pollId The poll ID
   * @param userId Optional user ID
   * @return A response with the poll
   */
  @GET("polls/{poll_id}")
  Call<GetPollResponse> get(
      @NotNull @Path("poll_id") String pollId, @Nullable @Query("user_id") String userId);

  /**
   * Updates a poll (full update).
   *
   * @param request The poll update request data
   * @return A response with the updated poll
   */
  @PUT("polls")
  Call<UpdatePollResponse> update(@NotNull @Body UpdatePollRequestData request);

  /**
   * Partially updates a poll.
   *
   * @param pollId The poll ID
   * @param request The partial update request data
   * @return A response with the updated poll
   */
  @PATCH("polls/{poll_id}")
  Call<UpdatePollResponse> partialUpdate(
      @NotNull @Path("poll_id") String pollId, @NotNull @Body PartialUpdatePollRequestData request);

  /**
   * Deletes a poll.
   *
   * @param pollId The poll ID
   * @param userId Optional user ID
   * @return A response indicating success
   */
  @DELETE("polls/{poll_id}")
  Call<StreamResponseObject> delete(
      @NotNull @Path("poll_id") String pollId, @Nullable @Query("user_id") String userId);

  /**
   * Creates a poll option.
   *
   * @param pollId The poll ID
   * @param request The poll option creation request data
   * @return A response with the created poll option
   */
  @POST("polls/{poll_id}/options")
  Call<CreatePollOptionResponse> createOption(
      @NotNull @Path("poll_id") String pollId, @NotNull @Body CreatePollOptionRequestData request);
}
