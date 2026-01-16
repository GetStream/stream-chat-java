package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.Campaign.CampaignCreateRequestData.CampaignCreateRequest;
import io.getstream.chat.java.models.Campaign.CampaignQueryRequestData.CampaignQueryRequest;
import io.getstream.chat.java.models.Campaign.CampaignStartRequestData.CampaignStartRequest;
import io.getstream.chat.java.models.Campaign.CampaignUpdateRequestData.CampaignUpdateRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.CampaignService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Campaign {
  @NotNull
  @JsonProperty("id")
  private String id;

  @Nullable
  @JsonProperty("name")
  private String name;

  @Nullable
  @JsonProperty("description")
  private String description;

  @NotNull
  @JsonProperty("sender_id")
  private String senderId;

  @Nullable
  @JsonProperty("status")
  private String status;

  @Nullable
  @JsonProperty("segment_ids")
  private List<String> segmentIds;

  @Nullable
  @JsonProperty("user_ids")
  private List<String> userIds;

  @Nullable
  @JsonProperty("message_template")
  private MessageTemplate messageTemplate;

  @Nullable
  @JsonProperty("channel_template")
  private ChannelTemplate channelTemplate;

  @Nullable
  @JsonProperty("create_channels")
  private Boolean createChannels;

  @Nullable
  @JsonProperty("skip_push")
  private Boolean skipPush;

  @Nullable
  @JsonProperty("skip_webhook")
  private Boolean skipWebhook;

  @Nullable
  @JsonProperty("scheduled_for")
  private Date scheduledFor;

  @Nullable
  @JsonProperty("stop_at")
  private Date stopAt;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

  @Data
  @NoArgsConstructor
  public static class MessageTemplate {
    @Nullable
    @JsonProperty("text")
    private String text;

    @Nullable
    @JsonProperty("attachments")
    private List<Map<String, Object>> attachments;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;
  }

  @Data
  @NoArgsConstructor
  public static class MemberTemplate {
    @NotNull
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("channel_role")
    private String channelRole;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;
  }

  @Data
  @NoArgsConstructor
  public static class ChannelTemplate {
    @NotNull
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("members")
    private List<String> members;

    @Nullable
    @JsonProperty("members_template")
    private List<MemberTemplate> membersTemplate;

    @Nullable
    @JsonProperty("custom")
    private Map<String, Object> custom;
  }

  @Builder(
      builderClassName = "CampaignCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class CampaignCreateRequestData {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("description")
    private String description;

    @Nullable
    @JsonProperty("sender_id")
    private String senderId;

    @Nullable
    @JsonProperty("segment_ids")
    private List<String> segmentIds;

    @Nullable
    @JsonProperty("user_ids")
    private List<String> userIds;

    @Nullable
    @JsonProperty("message_template")
    private MessageTemplate messageTemplate;

    @Nullable
    @JsonProperty("channel_template")
    private ChannelTemplate channelTemplate;

    @Nullable
    @JsonProperty("create_channels")
    private Boolean createChannels;

    @Nullable
    @JsonProperty("skip_push")
    private Boolean skipPush;

    @Nullable
    @JsonProperty("skip_webhook")
    private Boolean skipWebhook;

    public static class CampaignCreateRequest extends StreamRequest<CampaignCreateResponse> {
      @Override
      protected Call<CampaignCreateResponse> generateCall(Client client) {
        return client.create(CampaignService.class).create(this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class CampaignGetRequest extends StreamRequest<CampaignGetResponse> {
    @NotNull private String id;

    @Override
    protected Call<CampaignGetResponse> generateCall(Client client) {
      return client.create(CampaignService.class).get(id);
    }
  }

  @Builder(
      builderClassName = "CampaignUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class CampaignUpdateRequestData {
    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("description")
    private String description;

    @Nullable
    @JsonProperty("sender_id")
    private String senderId;

    @Nullable
    @JsonProperty("segment_ids")
    private List<String> segmentIds;

    @Nullable
    @JsonProperty("user_ids")
    private List<String> userIds;

    @Nullable
    @JsonProperty("message_template")
    private MessageTemplate messageTemplate;

    @Nullable
    @JsonProperty("channel_template")
    private ChannelTemplate channelTemplate;

    @Nullable
    @JsonProperty("create_channels")
    private Boolean createChannels;

    @Nullable
    @JsonProperty("skip_push")
    private Boolean skipPush;

    @Nullable
    @JsonProperty("skip_webhook")
    private Boolean skipWebhook;

    public static class CampaignUpdateRequest extends StreamRequest<CampaignUpdateResponse> {
      @NotNull private String id;

      private CampaignUpdateRequest(@NotNull String id) {
        this.id = id;
      }

      @Override
      protected Call<CampaignUpdateResponse> generateCall(Client client) {
        return client.create(CampaignService.class).update(id, this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class CampaignDeleteRequest extends StreamRequest<CampaignDeleteResponse> {
    @NotNull private String id;

    @Override
    protected Call<CampaignDeleteResponse> generateCall(Client client) {
      return client.create(CampaignService.class).delete(id);
    }
  }

  @Builder(
      builderClassName = "CampaignStartRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class CampaignStartRequestData {
    @Nullable
    @JsonProperty("scheduled_for")
    private Date scheduledFor;

    @Nullable
    @JsonProperty("stop_at")
    private Date stopAt;

    public static class CampaignStartRequest extends StreamRequest<CampaignStartResponse> {
      @NotNull private String id;

      private CampaignStartRequest(@NotNull String id) {
        this.id = id;
      }

      @Override
      protected Call<CampaignStartResponse> generateCall(Client client) {
        return client.create(CampaignService.class).start(id, this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode(callSuper = false)
  @RequiredArgsConstructor
  public static class CampaignStopRequest extends StreamRequest<CampaignStopResponse> {
    @NotNull private String id;

    @Override
    protected Call<CampaignStopResponse> generateCall(Client client) {
      return client.create(CampaignService.class).stop(id);
    }
  }

  @Builder(
      builderClassName = "CampaignQueryRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode(callSuper = false)
  public static class CampaignQueryRequestData {
    @Nullable
    @JsonProperty("filter")
    private Map<String, Object> filter;

    @Singular("sort")
    @Nullable
    @JsonProperty("sort")
    private List<Sort> sorts;

    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    @Nullable
    @JsonProperty("offset")
    private Integer offset;

    public static class CampaignQueryRequest extends StreamRequest<CampaignQueryResponse> {
      @Override
      protected Call<CampaignQueryResponse> generateCall(Client client) {
        return client.create(CampaignService.class).query(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CampaignCreateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("campaign")
    private Campaign campaign;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CampaignGetResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("campaign")
    private Campaign campaign;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CampaignUpdateResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("campaign")
    private Campaign campaign;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CampaignDeleteResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("campaign")
    private Campaign campaign;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CampaignStartResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("campaign")
    private Campaign campaign;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CampaignStopResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("campaign")
    private Campaign campaign;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CampaignQueryResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("campaigns")
    private List<Campaign> campaigns;
  }

  /**
   * Creates a create request
   *
   * @return the created request
   */
  @NotNull
  public static CampaignCreateRequest create() {
    return new CampaignCreateRequest();
  }

  /**
   * Creates a get request
   *
   * @param id the campaign id
   * @return the created request
   */
  @NotNull
  public static CampaignGetRequest get(@NotNull String id) {
    return new CampaignGetRequest(id);
  }

  /**
   * Creates an update request
   *
   * @param id the campaign id
   * @return the created request
   */
  @NotNull
  public static CampaignUpdateRequest update(@NotNull String id) {
    return new CampaignUpdateRequest(id);
  }

  /**
   * Creates a delete request
   *
   * @param id the campaign id
   * @return the created request
   */
  @NotNull
  public static CampaignDeleteRequest delete(@NotNull String id) {
    return new CampaignDeleteRequest(id);
  }

  /**
   * Creates a start request
   *
   * @param id the campaign id
   * @return the created request
   */
  @NotNull
  public static CampaignStartRequest start(@NotNull String id) {
    return new CampaignStartRequest(id);
  }

  /**
   * Creates a stop request
   *
   * @param id the campaign id
   * @return the created request
   */
  @NotNull
  public static CampaignStopRequest stop(@NotNull String id) {
    return new CampaignStopRequest(id);
  }

  /**
   * Creates a query request
   *
   * @return the created request
   */
  @NotNull
  public static CampaignQueryRequest query() {
    return new CampaignQueryRequest();
  }
}
