package io.stream.services;

import org.jetbrains.annotations.NotNull;
import io.stream.models.Reaction.ReactionDeleteResponse;
import io.stream.models.Reaction.ReactionListResponse;
import io.stream.models.Reaction.ReactionSendRequestData;
import io.stream.models.Reaction.ReactionSendResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReactionService {

  @POST("messages/{id}/reaction")
  Call<ReactionSendResponse> send(
      @NotNull @Path("id") String messageId, @NotNull @Body ReactionSendRequestData internalBuild);

  @DELETE("messages/{id}/reaction/{type}")
  Call<ReactionDeleteResponse> delete(
      @NotNull @Path("id") String messageId,
      @NotNull @Path("type") String type,
      @NotNull @Query("user_id") String userId);

  @GET("messages/{id}/reactions")
  Call<ReactionListResponse> list(@NotNull @Path("id") String messageId);
}
