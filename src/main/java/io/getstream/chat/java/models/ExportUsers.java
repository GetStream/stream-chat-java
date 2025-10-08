package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.ExportUsersService;
import io.getstream.chat.java.services.framework.Client;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class ExportUsers {

  @Builder(
      builderClassName = "ExportUsersRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class ExportUsersRequestData {
    @JsonProperty("user_ids")
    private List<String> userIds;

    public static class ExportUsersRequest extends StreamRequest<ExportUsers.ExportUsersResponse> {
      @Override
      protected Call<ExportUsers.ExportUsersResponse> generateCall(Client client) {
        return client.create(ExportUsersService.class).exportUsers(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ExportUsersResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("task_id")
    private String taskId;
  }

  /**
   * Creates a export users request
   *
   * @param userIds list of user IDs to be exported
   * @return the created request
   */
  @NotNull
  public static ExportUsers.ExportUsersRequestData.ExportUsersRequest exportUsers(
      @NotNull List<String> userIds) {
    return new ExportUsers.ExportUsersRequestData.ExportUsersRequest().userIds(userIds);
  }
}
