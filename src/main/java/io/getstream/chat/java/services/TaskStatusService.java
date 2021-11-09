package io.getstream.chat.java.services;

import io.getstream.chat.java.models.TaskStatus.TaskStatusGetResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TaskStatusService {
  @GET("tasks/{id}")
  Call<TaskStatusGetResponse> get(@NotNull @Path("id") String id);
}
