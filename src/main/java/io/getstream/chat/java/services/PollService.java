package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Poll.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.*;

public interface PollService {

  // Poll CRUD endpoints
  @POST("polls")
  @NotNull
  Call<PollCreateResponse> create(@NotNull @Body PollCreateRequestData pollCreateRequestData);

  @GET("polls/{poll_id}")
  @NotNull
  Call<PollGetResponse> get(@NotNull @Path("poll_id") String pollId);

  @PUT("polls")
  @NotNull
  Call<PollUpdateResponse> update(@NotNull @Body PollUpdateRequestData pollUpdateRequestData);

  @PATCH("polls/{poll_id}")
  @NotNull
  Call<PollUpdatePartialResponse> updatePartial(
      @NotNull @Path("poll_id") String pollId,
      @NotNull @Body PollUpdatePartialRequestData pollUpdatePartialRequestData);

  @DELETE("polls/{poll_id}")
  @NotNull
  Call<PollDeleteResponse> delete(
      @NotNull @Path("poll_id") String pollId, @Nullable @Query("user_id") String userId);

  @POST("polls/query")
  @NotNull
  Call<PollQueryResponse> query(
      @NotNull @Body PollQueryRequestData pollQueryRequestData,
      @Nullable @Query("user_id") String userId);

  // PollOption CRUD endpoints
  @POST("polls/{poll_id}/options")
  @NotNull
  Call<PollOptionCreateResponse> createOption(
      @NotNull @Path("poll_id") String pollId,
      @NotNull @Body PollOptionCreateRequestData pollOptionCreateRequestData);

  @GET("polls/{poll_id}/options/{option_id}")
  @NotNull
  Call<PollOptionGetResponse> getOption(
      @NotNull @Path("poll_id") String pollId, @NotNull @Path("option_id") String optionId);

  @PUT("polls/{poll_id}/options")
  @NotNull
  Call<PollOptionUpdateResponse> updateOption(
      @NotNull @Path("poll_id") String pollId,
      @NotNull @Body PollOptionUpdateRequestData pollOptionUpdateRequestData);

  @DELETE("polls/{poll_id}/options/{option_id}")
  @NotNull
  Call<PollOptionDeleteResponse> deleteOption(
      @NotNull @Path("poll_id") String pollId,
      @NotNull @Path("option_id") String optionId,
      @Nullable @Query("user_id") String userId);

  // PollVote endpoints
  @POST("polls/{poll_id}/votes")
  @NotNull
  Call<PollVoteQueryResponse> queryVotes(
      @NotNull @Path("poll_id") String pollId,
      @NotNull @Body PollVoteQueryRequestData pollVoteQueryRequestData,
      @Nullable @Query("user_id") String userId);

  @POST("messages/{message_id}/polls/{poll_id}/vote")
  @NotNull
  Call<PollVoteCastResponse> castVote(
      @NotNull @Path("message_id") String messageId,
      @NotNull @Path("poll_id") String pollId,
      @NotNull @Body PollVoteCastRequestData pollVoteCastRequestData);

  @DELETE("messages/{message_id}/polls/{poll_id}/vote/{vote_id}")
  @NotNull
  Call<PollVoteDeleteResponse> deleteVote(
      @NotNull @Path("message_id") String messageId,
      @NotNull @Path("poll_id") String pollId,
      @NotNull @Path("vote_id") String voteId);
}
