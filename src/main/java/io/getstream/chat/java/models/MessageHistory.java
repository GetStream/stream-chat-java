package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.Message.Attachment;
import io.getstream.chat.java.models.MessageHistory.MessageHistoryQueryRequestData.MessageHistoryQueryRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.MessageHistoryService;
import io.getstream.chat.java.services.framework.Client;
import java.util.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class MessageHistory {
  @Builder(
      builderClassName = "MessageHistoryQueryRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class MessageHistoryQueryRequestData {
    @Nullable
    @JsonProperty("filter")
    private Map<String, Object> filter;

    @Singular
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("next")
    private String next;

    @Nullable
    @JsonProperty("prev")
    private String prev;

    public static class MessageHistoryQueryRequest
        extends StreamRequest<MessageHistoryQueryResponse> {
      @Override
      protected Call<MessageHistoryQueryResponse> generateCall(Client client) {
        var data = this.internalBuild();
        return client.create(MessageHistoryService.class).query(data);
      }
    }
  }

  /**
   * Creates a query request
   *
   * @return the created request
   */
  @NotNull
  public static MessageHistoryQueryRequest query() {
    return new MessageHistoryQueryRequest();
  }

  @Data
  @NoArgsConstructor
  public static class MessageHistoryEntry {
    @NotNull
    @JsonProperty("message_id")
    private String messageId;

    @NotNull
    @JsonProperty("message_updated_at")
    private Date messageUpdatedAt;

    @Nullable
    @JsonProperty("attachments")
    private List<Attachment> attachments;

    @NotNull
    @JsonProperty("message_updated_by_id")
    private String messageUpdatedById;

    @Nullable
    @JsonProperty("text")
    private String text;

    @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
      this.additionalFields.put(name, value);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class MessageHistoryQueryResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("message_history")
    private List<MessageHistoryEntry> messageHistory;

    @NotNull
    @JsonProperty("next")
    private String next;

    @NotNull
    @JsonProperty("prev")
    private String prev;
  }
}
