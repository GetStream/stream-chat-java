package io.getstream.services;

import org.jetbrains.annotations.NotNull;
import io.getstream.models.Reaction.ReactionDeleteResponse;
import io.getstream.models.Reaction.ReactionListResponse;
import io.getstream.models.Reaction.ReactionSendRequestData;
import io.getstream.models.Reaction.ReactionSendResponse;
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
      @NotNull @Path("id") String messageId,
      @NotNull @Body ReactionSendRequestData reactionSendRequestData);

  @DELETE("messages/{id}/reaction/{type}")
  Call<ReactionDeleteResponse> delete(
      @NotNull @Path("id") String messageId,
      @NotNull @Path("type") String type,
      @NotNull @Query("user_id") String userId);

  @GET("messages/{id}/reactions")
  Call<ReactionListResponse> list(@NotNull @Path("id") String messageId);
}
