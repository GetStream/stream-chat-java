package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Reminder.ReminderCreateRequestData.ReminderCreateRequest;
import io.getstream.chat.java.models.Reminder.ReminderQueryRequestData.ReminderQueryRequest;
import io.getstream.chat.java.models.Reminder.ReminderUpdateRequestData.ReminderUpdateRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.ReminderService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Reminder {
  @NotNull
  @JsonProperty("id")
  private String id;

  @NotNull
  @JsonProperty("message_id")
  private String messageId;

  @Nullable
  @JsonProperty("message")
  private Message message;

  @NotNull
  @JsonProperty("user_id")
  private String userId;

  @Nullable
  @JsonProperty("user")
  private User user;

  @NotNull
  @JsonProperty("channel_cid")
  private String channelCid;

  @Nullable
  @JsonProperty("remind_at")
  private Date remindAt;

  @Nullable
  @JsonProperty("created_at")
  private Date createdAt;

  @Nullable
  @JsonProperty("updated_at")
  private Date updatedAt;

  @NotNull @JsonIgnore private Map<String, Object> additionalFields = new HashMap<>();

  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return additionalFields;
  }

  @JsonAnySetter
  public void setAdditionalField(String name, Object value) {
    additionalFields.put(name, value);
  }

  @Builder(
      builderClassName = "ReminderCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ReminderCreateRequestData {
    @NotNull
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("remind_at")
    private Date remindAt;

    public static class ReminderCreateRequest extends StreamRequest<ReminderCreateResponse> {
      @NotNull private String messageId;

      private ReminderCreateRequest(@NotNull String messageId) {
        this.messageId = messageId;
      }

      @Override
      protected Call<ReminderCreateResponse> generateCall(Client client) {
        return client.create(ReminderService.class).create(messageId, this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "ReminderUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ReminderUpdateRequestData {
    @NotNull
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("remind_at")
    private Date remindAt;

    public static class ReminderUpdateRequest extends StreamRequest<ReminderUpdateResponse> {
      @NotNull private String messageId;

      private ReminderUpdateRequest(@NotNull String messageId) {
        this.messageId = messageId;
      }

      @Override
      protected Call<ReminderUpdateResponse> generateCall(Client client) {
        return client.create(ReminderService.class).update(messageId, this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class ReminderDeleteRequest extends StreamRequest<ReminderDeleteResponse> {
    @NotNull private String messageId;
    @NotNull private String userId;

    @Override
    protected Call<ReminderDeleteResponse> generateCall(Client client) {
      return client.create(ReminderService.class).delete(messageId, userId);
    }
  }

  @Builder(
      builderClassName = "ReminderQueryRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class ReminderQueryRequestData {
    @NotNull
    @JsonProperty("user_id")
    private String userId;

    @Singular
    @Nullable
    @JsonProperty("filter_conditions")
    private Map<String, Object> filterConditions;

    @Singular
    @Nullable
    @JsonProperty("sort")
    private List<Map<String, Object>> sorts;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    public static class ReminderQueryRequest extends StreamRequest<ReminderQueryResponse> {
      @Override
      protected Call<ReminderQueryResponse> generateCall(Client client) {
        return client.create(ReminderService.class).query(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ReminderCreateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("reminder")
    private Reminder reminder;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ReminderUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("reminder")
    private Reminder reminder;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ReminderDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("reminder")
    private Reminder reminder;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ReminderQueryResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("reminders")
    private List<Reminder> reminders;
    
    @Nullable
    @JsonProperty("prev")
    private String prev;
    
    @Nullable
    @JsonProperty("next")
    private String next;
  }

  /**
   * Creates a reminder for a message.
   *
   * @param messageId The ID of the message to create a reminder for
   * @return A request builder for creating a reminder
   */
  @NotNull
  public static ReminderCreateRequest createReminder(@NotNull String messageId) {
    return new ReminderCreateRequest(messageId);
  }

  /**
   * Updates a reminder for a message.
   *
   * @param messageId The ID of the message with the reminder
   * @return A request builder for updating a reminder
   */
  @NotNull
  public static ReminderUpdateRequest updateReminder(@NotNull String messageId) {
    return new ReminderUpdateRequest(messageId);
  }

  /**
   * Deletes a reminder for a message.
   *
   * @param messageId The ID of the message with the reminder
   * @param userId The ID of the user who owns the reminder
   * @return A request for deleting a reminder
   */
  @NotNull
  public static ReminderDeleteRequest deleteReminder(
      @NotNull String messageId, @NotNull String userId) {
    return new ReminderDeleteRequest(messageId, userId);
  }

  /**
   * Queries reminders based on filter conditions.
   *
   * @return A request builder for querying reminders
   */
  @NotNull
  public static ReminderQueryRequest queryReminders() {
    return new ReminderQueryRequest();
  }
} 