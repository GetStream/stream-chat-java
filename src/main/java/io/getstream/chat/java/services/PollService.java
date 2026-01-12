package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Poll.CreatePollRequestData;
import io.getstream.chat.java.models.Poll.CreatePollResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
}
