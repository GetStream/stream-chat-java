package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.TaskStatusService;
import io.getstream.chat.java.services.framework.ServiceFactory;
import java.util.Date;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class TaskStatus {
  public static TaskStatusGetRequest get(@NotNull String taskId) {
    return new TaskStatusGetRequest(taskId);
  }

  @RequiredArgsConstructor
  public static class TaskStatusGetRequest extends StreamRequest<TaskStatusGetResponse> {
    @NotNull private String id;

    @Override
    protected Call<TaskStatusGetResponse> generateCall(ServiceFactory serviceFactory)
        throws StreamException {
      return serviceFactory.create(TaskStatusService.class).get(this.id);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class TaskStatusGetResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("task_id")
    private String id;

    @NotNull
    @JsonProperty("status")
    private String status;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @NotNull
    @JsonProperty("updated_at")
    private Date updatedAt;

    @NotNull @JsonProperty private Map<String, Object> result;
  }
}
