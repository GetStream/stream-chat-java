package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.ChannelType.ChannelTypeCreateRequestData.ChannelTypeCreateRequest;
import io.getstream.chat.java.models.ChannelType.ChannelTypeUpdateRequestData.ChannelTypeUpdateRequest;
import io.getstream.chat.java.models.framework.RequestObjectBuilder;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponse;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.ChannelTypeService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class ChannelType {
  @NotNull
  @JsonProperty("name")
  private String name;

  @Nullable
  @JsonProperty("typing_events")
  private Boolean typingEvents;

  @Nullable
  @JsonProperty("read_events")
  private Boolean readEvents;

  @Nullable
  @JsonProperty("reminders")
  private Boolean reminders;

  @Nullable
  @JsonProperty("user_message_reminders")
  private Boolean userMessageReminders;

  @Nullable
  @JsonProperty("connect_events")
  private Boolean connectEvents;

  @Nullable
  @JsonProperty("search")
  private Boolean search;

  @Nullable
  @JsonProperty("reactions")
  private Boolean reactions;

  @Nullable
  @JsonProperty("replies")
  private Boolean replies;

  @Nullable
  @JsonProperty("quotes")
  private Boolean quotes;

  @Nullable
  @JsonProperty("uploads")
  private Boolean uploads;

  @Nullable
  @JsonProperty("url_enrichment")
  private Boolean urlEnrichment;

  @Nullable
  @JsonProperty("custom_events")
  private Boolean customEvents;

  @Nullable
  @JsonProperty("mutes")
  private Boolean mutes;

  @Nullable
  @JsonProperty("push_notifications")
  private Boolean pushNotifications;

  @Nullable
  @JsonProperty("message_retention")
  private String messageRetention;

  @Nullable
  @JsonProperty("max_message_length")
  private Integer maxMessageLength;

  @Nullable
  @JsonProperty("mark_messages_pending")
  private Boolean markMessagesPending;

  @Nullable
  @JsonProperty("automod")
  private AutoMod automod;

  @Nullable
  @JsonProperty("automod_behavior")
  private AutoModBehavior automodBehavior;

  @Nullable
  @JsonProperty("created_at")
  private Date createdAt;

  @Nullable
  @JsonProperty("updated_at")
  private Date updatedAt;

  @Nullable
  @JsonProperty("blocklist")
  private String blocklist;

  @Nullable
  @JsonProperty("blocklist_behavior")
  private BlocklistBehavior blocklistBehavior;

  @Nullable
  @JsonProperty("automod_thresholds")
  private Map<String, Threshold> automodThresholds;

  @Nullable
  @JsonProperty("roles")
  private Map<String, List<Right>> roles;

  @Nullable
  @JsonProperty("permissions")
  private List<Policy> permissions;

  @Nullable
  @JsonProperty("grants")
  private Map<String, List<String>> grants;

  @Nullable
  @JsonProperty("count_messages")
  private Boolean countMessages;

  @Data
  @NoArgsConstructor
  public static class Threshold {
    @Nullable
    @JsonProperty("flag")
    private Integer flag;

    @Nullable
    @JsonProperty("block")
    private Integer block;
  }

  @Data
  @NoArgsConstructor
  public static class Policy {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("action")
    private Action action;

    @NotNull
    @JsonProperty("roles")
    private List<String> roles;

    @NotNull
    @JsonProperty("owner")
    private Boolean owner;

    @NotNull
    @JsonProperty("priority")
    private Integer priority;

    @NotNull
    @JsonProperty("resources")
    private List<String> resources;
  }

  public enum Action {
    @JsonProperty("Deny")
    DENY,
    @JsonProperty("Allow")
    ALLOW,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  @Data
  @NoArgsConstructor
  public static class Right {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("owner")
    private Boolean owner;

    @NotNull
    @JsonProperty("same_team")
    private Boolean sameTeam;

    @NotNull
    @JsonProperty("custom")
    private Boolean custom;
  }

  public enum AutoMod {
    @JsonProperty("disabled")
    DISABLED,
    @JsonProperty("simple")
    SIMPLE,
    @JsonProperty("AI")
    AI,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  public enum AutoModBehavior {
    @JsonProperty("flag")
    FLAG,
    @JsonProperty("block")
    BLOCK,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  public enum BlocklistBehavior {
    @JsonProperty("flag")
    FLAG,
    @JsonProperty("block")
    BLOCK,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelTypeWithStringCommands extends ChannelType {

    @Nullable
    @JsonProperty("commands")
    private List<String> commands;
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelTypeWithCommands extends ChannelType {

    @Nullable
    @JsonProperty("commands")
    private List<Command> commands;
  }

  @Builder
  @Setter
  @Getter
  @EqualsAndHashCode
  public static class ThresholdRequestObject {
    @Nullable
    @JsonProperty("flag")
    private Integer flag;

    @Nullable
    @JsonProperty("block")
    private Integer block;

    @Nullable
    public static ThresholdRequestObject buildFrom(@Nullable Threshold threshold) {
      return RequestObjectBuilder.build(ThresholdRequestObject.class, threshold);
    }
  }

  @Builder
  @Setter
  @Getter
  @EqualsAndHashCode
  public static class PermissionRequestObject {
    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("action")
    private Action action;

    @Nullable
    @JsonProperty("roles")
    private List<String> roles;

    @Nullable
    @JsonProperty("owner")
    private Boolean owner;

    @Nullable
    @JsonProperty("priority")
    private Integer priority;

    @Nullable
    @JsonProperty("resources")
    private List<String> resources;

    @Nullable
    public static PermissionRequestObject buildFrom(@Nullable Policy policy) {
      return RequestObjectBuilder.build(PermissionRequestObject.class, policy);
    }
  }

  @Builder(
      builderClassName = "ChannelTypeCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class ChannelTypeCreateRequestData {
    @Nullable
    @JsonProperty("typing_events")
    protected Boolean typingEvents;

    @Nullable
    @JsonProperty("read_events")
    protected Boolean readEvents;

    @Nullable
    @JsonProperty("reminders")
    protected Boolean reminders;

    @Nullable
    @JsonProperty("user_message_reminders")
    protected Boolean userMessageReminders;

    @Nullable
    @JsonProperty("connect_events")
    protected Boolean connectEvents;

    @Nullable
    @JsonProperty("search")
    protected Boolean search;

    @Nullable
    @JsonProperty("reactions")
    protected Boolean reactions;

    @Nullable
    @JsonProperty("replies")
    protected Boolean replies;

    @Nullable
    @JsonProperty("quotes")
    protected Boolean quotes;

    @Nullable
    @JsonProperty("uploads")
    protected Boolean uploads;

    @Nullable
    @JsonProperty("url_enrichment")
    protected Boolean urlEnrichment;

    @Nullable
    @JsonProperty("custom_events")
    protected Boolean customEvents;

    @Nullable
    @JsonProperty("mutes")
    protected Boolean mutes;

    @Nullable
    @JsonProperty("push_notifications")
    protected Boolean pushNotifications;

    @Nullable
    @JsonProperty("message_retention")
    protected String messageRetention;

    @Nullable
    @JsonProperty("max_message_length")
    protected Integer maxMessageLength;

    @Nullable
    @JsonProperty("mark_messages_pending")
    protected Boolean markMessagesPending;

    @Nullable
    @JsonProperty("automod")
    protected AutoMod automod;

    @Nullable
    @JsonProperty("automod_behavior")
    protected AutoModBehavior automodBehavior;

    @Nullable
    @JsonProperty("blocklist")
    protected String blocklist;

    @Nullable
    @JsonProperty("blocklist_behavior")
    protected BlocklistBehavior blocklistBehavior;

    @Nullable
    @JsonProperty("commands")
    protected List<String> commands;

    @Singular
    @Nullable
    @JsonProperty("permissions")
    protected List<PermissionRequestObject> permissions;

    @Nullable
    @JsonProperty("grants")
    protected Map<String, List<String>> grants;

    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("count_messages")
    private Boolean countMessages;

    public static class ChannelTypeCreateRequest extends StreamRequest<ChannelTypeCreateResponse> {

      private static final boolean DEFAULT_PUSH_NOTIFICATIONS = true;

      private static final AutoModBehavior DEFAULT_MOD_BEHAVIOR = AutoModBehavior.FLAG;

      private static final AutoMod DEFAULT_AUTOMOD = AutoMod.DISABLED;

      private static final String DEFAULT_MESSAGE_RETENTION = "infinite";

      private static final int DEFAULT_MAX_MESSAGE_LENGTH = 5000;

      public ChannelTypeCreateRequest withDefaultConfig() {
        return this.automod(DEFAULT_AUTOMOD)
            .automodBehavior(DEFAULT_MOD_BEHAVIOR)
            .maxMessageLength(DEFAULT_MAX_MESSAGE_LENGTH)
            .messageRetention(DEFAULT_MESSAGE_RETENTION)
            .pushNotifications(DEFAULT_PUSH_NOTIFICATIONS);
      }

      @Override
      protected Call<ChannelTypeCreateResponse> generateCall(Client client) {
        return client.create(ChannelTypeService.class).create(this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  public static class ChannelTypeGetRequest extends StreamRequest<ChannelTypeGetResponse> {
    @NotNull private String name;

    @Override
    protected Call<ChannelTypeGetResponse> generateCall(Client client) {
      return client.create(ChannelTypeService.class).get(name);
    }
  }

  @Builder(
      builderClassName = "ChannelTypeUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class ChannelTypeUpdateRequestData {
    @Nullable
    @JsonProperty("typing_events")
    protected Boolean typingEvents;

    @Nullable
    @JsonProperty("read_events")
    protected Boolean readEvents;

    @Nullable
    @JsonProperty("reminders")
    protected Boolean reminders;

    @Nullable
    @JsonProperty("user_message_reminders")
    protected Boolean userMessageReminders;

    @Nullable
    @JsonProperty("connect_events")
    protected Boolean connectEvents;

    @Nullable
    @JsonProperty("search")
    protected Boolean search;

    @Nullable
    @JsonProperty("reactions")
    protected Boolean reactions;

    @Nullable
    @JsonProperty("replies")
    protected Boolean replies;

    @Nullable
    @JsonProperty("quotes")
    protected Boolean quotes;

    @Nullable
    @JsonProperty("uploads")
    protected Boolean uploads;

    @Nullable
    @JsonProperty("url_enrichment")
    protected Boolean urlEnrichment;

    @Nullable
    @JsonProperty("custom_events")
    protected Boolean customEvents;

    @Nullable
    @JsonProperty("mutes")
    protected Boolean mutes;

    @Nullable
    @JsonProperty("push_notifications")
    protected Boolean pushNotifications;

    @Nullable
    @JsonProperty("message_retention")
    protected String messageRetention;

    @Nullable
    @JsonProperty("max_message_length")
    protected Integer maxMessageLength;

    @Nullable
    @JsonProperty("mark_messages_pending")
    protected Boolean markMessagesPending;

    @Nullable
    @JsonProperty("automod")
    protected AutoMod automod;

    @Nullable
    @JsonProperty("automod_behavior")
    protected AutoModBehavior automodBehavior;

    @Nullable
    @JsonProperty("blocklist")
    protected String blocklist;

    @Nullable
    @JsonProperty("blocklist_behavior")
    protected BlocklistBehavior blocklistBehavior;

    @Singular
    @Nullable
    @JsonProperty("automod_thresholds")
    protected Map<String, ThresholdRequestObject> automodThresholds;

    @Nullable
    @JsonProperty("commands")
    protected List<String> commands;

    @Singular
    @Nullable
    @JsonProperty("permissions")
    protected List<PermissionRequestObject> permissions;

    @Nullable
    @JsonProperty("grants")
    protected Map<String, List<String>> grants;

    @Nullable
    @JsonProperty("count_messages")
    private Boolean countMessages;

    public static class ChannelTypeUpdateRequest extends StreamRequest<ChannelTypeUpdateResponse> {
      @NotNull private String name;

      private ChannelTypeUpdateRequest(@NotNull String name) {
        this.name = name;
      }

      @Override
      protected Call<ChannelTypeUpdateResponse> generateCall(Client client) {
        return client.create(ChannelTypeService.class).update(name, this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  public static class ChannelTypeDeleteRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String name;

    @Override
    protected Call<StreamResponseObject> generateCall(Client client) {
      return client.create(ChannelTypeService.class).delete(name);
    }
  }

  public static class ChannelTypeListRequest extends StreamRequest<ChannelTypeListResponse> {
    @Override
    protected Call<ChannelTypeListResponse> generateCall(Client client) {
      return client.create(ChannelTypeService.class).list();
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelTypeCreateResponse extends ChannelTypeWithStringCommands
      implements StreamResponse {
    private RateLimit rateLimit;

    @NotNull
    @JsonProperty("duration")
    private String duration;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelTypeGetResponse extends ChannelTypeWithCommands
      implements StreamResponse {
    private RateLimit rateLimit;

    @NotNull
    @JsonProperty("duration")
    private String duration;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelTypeUpdateResponse extends ChannelTypeWithStringCommands
      implements StreamResponse {
    private RateLimit rateLimit;

    @NotNull
    @JsonProperty("duration")
    private String duration;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ChannelTypeListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("channel_types")
    private Map<String, ChannelTypeWithCommands> channelTypes;
  }

  /**
   * Creates an create request
   *
   * @return the created request
   */
  @NotNull
  public static ChannelTypeCreateRequest create() {
    return new ChannelTypeCreateRequest();
  }

  /**
   * Creates a get request
   *
   * @param name the channel type name
   * @return the created request
   */
  @NotNull
  public static ChannelTypeGetRequest get(String name) {
    return new ChannelTypeGetRequest(name);
  }

  /**
   * Creates an update request
   *
   * @param name the name of the channel type to update
   * @return the created request
   */
  @NotNull
  public static ChannelTypeUpdateRequest update(@NotNull String name) {
    return new ChannelTypeUpdateRequest(name);
  }

  /**
   * Creates a delete request
   *
   * @param name the channel type name
   * @return the created request
   */
  @NotNull
  public static ChannelTypeDeleteRequest delete(String name) {
    return new ChannelTypeDeleteRequest(name);
  }

  /**
   * Creates a list request
   *
   * @return the channel types in a map
   * @throws StreamException when IO problem occurs or the stream API return an error
   */
  @NotNull
  public static ChannelTypeListRequest list() throws StreamException {
    return new ChannelTypeListRequest();
  }
}
