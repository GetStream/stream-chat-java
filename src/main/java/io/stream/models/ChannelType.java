package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.exceptions.StreamException;
import io.stream.models.ChannelType.ChannelTypeCreateRequestData.ChannelTypeCreateRequest;
import io.stream.models.ChannelType.ChannelTypeUpdateRequestData.ChannelTypeUpdateRequest;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponse;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.ChannelTypeService;
import io.stream.services.framework.StreamServiceGenerator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChannelType {
  @Nullable
  @JsonProperty("name")
  private String name;

  @Nullable
  @JsonProperty("typing_events")
  private Boolean typingEvents;

  @Nullable
  @JsonProperty("read_events")
  private Boolean readEvents;

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
  private List<Permission> permissions;

  public ChannelType() {}

  @Data
  public static class Threshold {
    @Nullable
    @JsonProperty("flag")
    private Integer flag;

    @Nullable
    @JsonProperty("block")
    private Integer block;

    public Threshold() {}
  }

  @Data
  public static class Permission {
    public Permission() {}

    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("action")
    private String action;

    @NotNull
    @JsonProperty("resources")
    private List<String> resources;

    @NotNull
    @JsonProperty("roles")
    private List<String> roles;

    @NotNull
    @JsonProperty("owner")
    private Boolean owner;

    @NotNull
    @JsonProperty("priority")
    private Integer priority;
  }

  public static class Right {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("resource")
    private String resource;

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
    AI
  }

  public enum AutoModBehavior {
    @JsonProperty("flag")
    FLAG,
    @JsonProperty("block")
    BLOCK
  }

  public enum BlocklistBehavior {
    @JsonProperty("flag")
    FLAG,
    @JsonProperty("block")
    BLOCK
  }

  public static class ThresholdRequestObject {
    @Nullable
    @JsonProperty("flag")
    private Integer flag;

    @Nullable
    @JsonProperty("block")
    private Integer block;

    private ThresholdRequestObject(Builder builder) {
      this.flag = builder.flag;
      this.block = builder.block;
    }

    /**
     * Creates builder to build {@link ThresholdRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link ThresholdRequestObject}. */
    public static final class Builder {
      private Integer flag;
      private Integer block;

      private Builder() {}

      @NotNull
      public Builder withFlag(@NotNull Integer flag) {
        this.flag = flag;
        return this;
      }

      @NotNull
      public Builder withBlock(@NotNull Integer block) {
        this.block = block;
        return this;
      }

      @NotNull
      public ThresholdRequestObject build() {
        return new ThresholdRequestObject(this);
      }
    }
  }

  public static class PermissionRequestObject {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("action")
    private String action;

    @NotNull
    @JsonProperty("resources")
    private List<String> resources;

    @NotNull
    @JsonProperty("roles")
    private List<String> roles;

    @NotNull
    @JsonProperty("owner")
    private Boolean owner;

    @NotNull
    @JsonProperty("priority")
    private Integer priority;

    private PermissionRequestObject(Builder builder) {
      this.name = builder.name;
      this.action = builder.action;
      this.resources = builder.resources;
      this.roles = builder.roles;
      this.owner = builder.owner;
      this.priority = builder.priority;
    }

    /**
     * Creates builder to build {@link PermissionRequestObject}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /** Builder to build {@link PermissionRequestObject}. */
    public static final class Builder {
      private String name;
      private String action;
      private List<String> resources;
      private List<String> roles;
      private Boolean owner;
      private Integer priority;

      private Builder() {}

      @NotNull
      public Builder withName(@NotNull String name) {
        this.name = name;
        return this;
      }

      @NotNull
      public Builder withAction(@NotNull String action) {
        this.action = action;
        return this;
      }

      @NotNull
      public Builder withResources(@NotNull List<String> resources) {
        this.resources = resources;
        return this;
      }

      @NotNull
      public Builder withRoles(@NotNull List<String> roles) {
        this.roles = roles;
        return this;
      }

      @NotNull
      public Builder withOwner(@NotNull Boolean owner) {
        this.owner = owner;
        return this;
      }

      @NotNull
      public Builder withPriority(@NotNull Integer priority) {
        this.priority = priority;
        return this;
      }

      @NotNull
      public PermissionRequestObject build() {
        return new PermissionRequestObject(this);
      }
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelTypeWithStringCommands extends ChannelType {

    @Nullable
    @JsonProperty("commands")
    private List<String> commands;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelTypeWithCommands extends ChannelType {

    @Nullable
    @JsonProperty("commands")
    private List<Command> commands;
  }

  public static class ChannelTypeCreateRequestData extends ChannelTypeUpdateRequestData {

    @Nullable
    @JsonProperty("name")
    private String name;

    private ChannelTypeCreateRequestData() {} // This is necessary for inheritance

    private ChannelTypeCreateRequestData(ChannelTypeCreateRequest channelTypeCreateRequest) {
      this.name = channelTypeCreateRequest.name;
      this.typingEvents = channelTypeCreateRequest.typingEvents;
      this.readEvents = channelTypeCreateRequest.readEvents;
      this.connectEvents = channelTypeCreateRequest.connectEvents;
      this.search = channelTypeCreateRequest.search;
      this.reactions = channelTypeCreateRequest.reactions;
      this.replies = channelTypeCreateRequest.replies;
      this.uploads = channelTypeCreateRequest.uploads;
      this.urlEnrichment = channelTypeCreateRequest.urlEnrichment;
      this.customEvents = channelTypeCreateRequest.customEvents;
      this.mutes = channelTypeCreateRequest.mutes;
      this.pushNotifications = channelTypeCreateRequest.pushNotifications;
      this.messageRetention = channelTypeCreateRequest.messageRetention;
      this.maxMessageLength = channelTypeCreateRequest.maxMessageLength;
      this.automod = channelTypeCreateRequest.automod;
      this.automodBehavior = channelTypeCreateRequest.automodBehavior;
      this.blocklist = channelTypeCreateRequest.blocklist;
      this.blocklistBehavior = channelTypeCreateRequest.blocklistBehavior;
      this.automodThresholds = channelTypeCreateRequest.automodThresholds;
      this.commands = channelTypeCreateRequest.commands;
      this.permissions = channelTypeCreateRequest.permissions;
    }

    public static class ChannelTypeCreateRequest extends StreamRequest<ChannelTypeCreateResponse> {
      private String name;
      private Boolean typingEvents;
      private Boolean readEvents;
      private Boolean connectEvents;
      private Boolean search;
      private Boolean reactions;
      private Boolean replies;
      private Boolean uploads;
      private Boolean urlEnrichment;
      private Boolean customEvents;
      private Boolean mutes;
      private Boolean pushNotifications;
      private String messageRetention;
      private Integer maxMessageLength;
      private AutoMod automod;
      private AutoModBehavior automodBehavior;
      private String blocklist;
      private BlocklistBehavior blocklistBehavior;
      private Map<String, ThresholdRequestObject> automodThresholds;
      private List<String> commands;
      private List<PermissionRequestObject> permissions;

      private static final boolean DEFAULT_PUSH_NOTIFICATIONS = true;

      private static final AutoModBehavior DEFAULT_MOD_BEHAVIOR = AutoModBehavior.FLAG;

      private static final AutoMod DEFAULT_AUTOMOD = AutoMod.DISABLED;

      private static final String DEFAULT_MESSAGE_RETENTION = "infinite";

      private static final int DEFAULT_MAX_MESSAGE_LENGTH = 5000;

      public ChannelTypeCreateRequest withDefaultConfig() {
        return this.withAutomod(DEFAULT_AUTOMOD)
            .withAutomodBehavior(DEFAULT_MOD_BEHAVIOR)
            .withMaxMessageLength(DEFAULT_MAX_MESSAGE_LENGTH)
            .withMessageRetention(DEFAULT_MESSAGE_RETENTION)
            .withPushNotifications(DEFAULT_PUSH_NOTIFICATIONS);
      }

      @NotNull
      public ChannelTypeCreateRequest withName(@NotNull String name) {
        this.name = name;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withTypingEvents(@NotNull Boolean typingEvents) {
        this.typingEvents = typingEvents;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withReadEvents(@NotNull Boolean readEvents) {
        this.readEvents = readEvents;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withConnectEvents(@NotNull Boolean connectEvents) {
        this.connectEvents = connectEvents;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withSearch(@NotNull Boolean search) {
        this.search = search;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withReactions(@NotNull Boolean reactions) {
        this.reactions = reactions;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withReplies(@NotNull Boolean replies) {
        this.replies = replies;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withUploads(@NotNull Boolean uploads) {
        this.uploads = uploads;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withUrlEnrichment(@NotNull Boolean urlEnrichment) {
        this.urlEnrichment = urlEnrichment;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withCustomEvents(@NotNull Boolean customEvents) {
        this.customEvents = customEvents;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withMutes(@NotNull Boolean mutes) {
        this.mutes = mutes;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withPushNotifications(@NotNull Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withMessageRetention(@NotNull String messageRetention) {
        this.messageRetention = messageRetention;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withMaxMessageLength(@NotNull Integer maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withAutomod(@NotNull AutoMod automod) {
        this.automod = automod;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withAutomodBehavior(
          @NotNull AutoModBehavior automodBehavior) {
        this.automodBehavior = automodBehavior;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withBlocklist(@NotNull String blocklist) {
        this.blocklist = blocklist;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withBlocklistBehavior(
          @NotNull BlocklistBehavior blocklistBehavior) {
        this.blocklistBehavior = blocklistBehavior;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withAutomodThresholds(
          @NotNull Map<String, ThresholdRequestObject> automodThresholds) {
        this.automodThresholds = automodThresholds;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withCommands(@NotNull List<String> commands) {
        this.commands = commands;
        return this;
      }

      @NotNull
      public ChannelTypeCreateRequest withPermissions(
          @NotNull List<PermissionRequestObject> permissions) {
        this.permissions = permissions;
        return this;
      }

      @Override
      protected Call<ChannelTypeCreateResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelTypeService.class)
            .create(new ChannelTypeCreateRequestData(this));
      }
    }
  }

  public static class ChannelTypeGetRequest extends StreamRequest<ChannelTypeGetResponse> {
    private String name;

    private ChannelTypeGetRequest(String name) {
      this.name = name;
    }

    @Override
    protected Call<ChannelTypeGetResponse> generateCall() {
      return StreamServiceGenerator.createService(ChannelTypeService.class).get(name);
    }
  }

  public static class ChannelTypeUpdateRequestData {

    private ChannelTypeUpdateRequestData() {} // This is necessary for inheritance

    private ChannelTypeUpdateRequestData(ChannelTypeUpdateRequest channelTypeUpdateRequest) {
      this.typingEvents = channelTypeUpdateRequest.typingEvents;
      this.readEvents = channelTypeUpdateRequest.readEvents;
      this.connectEvents = channelTypeUpdateRequest.connectEvents;
      this.search = channelTypeUpdateRequest.search;
      this.reactions = channelTypeUpdateRequest.reactions;
      this.replies = channelTypeUpdateRequest.replies;
      this.uploads = channelTypeUpdateRequest.uploads;
      this.urlEnrichment = channelTypeUpdateRequest.urlEnrichment;
      this.customEvents = channelTypeUpdateRequest.customEvents;
      this.mutes = channelTypeUpdateRequest.mutes;
      this.pushNotifications = channelTypeUpdateRequest.pushNotifications;
      this.messageRetention = channelTypeUpdateRequest.messageRetention;
      this.maxMessageLength = channelTypeUpdateRequest.maxMessageLength;
      this.automod = channelTypeUpdateRequest.automod;
      this.automodBehavior = channelTypeUpdateRequest.automodBehavior;
      this.blocklist = channelTypeUpdateRequest.blocklist;
      this.blocklistBehavior = channelTypeUpdateRequest.blocklistBehavior;
      this.automodThresholds = channelTypeUpdateRequest.automodThresholds;
      this.commands = channelTypeUpdateRequest.commands;
      this.permissions = channelTypeUpdateRequest.permissions;
    }

    @Nullable
    @JsonProperty("typing_events")
    protected Boolean typingEvents;

    @Nullable
    @JsonProperty("read_events")
    protected Boolean readEvents;

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
    @JsonProperty("automod_thresholds")
    protected Map<String, ThresholdRequestObject> automodThresholds;

    @Nullable
    @JsonProperty("commands")
    protected List<String> commands;

    @Nullable
    @JsonProperty("permissions")
    protected List<PermissionRequestObject> permissions;

    public static class ChannelTypeUpdateRequest extends StreamRequest<ChannelTypeUpdateResponse> {
      private String name;
      private Boolean typingEvents;
      private Boolean readEvents;
      private Boolean connectEvents;
      private Boolean search;
      private Boolean reactions;
      private Boolean replies;
      private Boolean uploads;
      private Boolean urlEnrichment;
      private Boolean customEvents;
      private Boolean mutes;
      private Boolean pushNotifications;
      private String messageRetention;
      private Integer maxMessageLength;
      private AutoMod automod;
      private AutoModBehavior automodBehavior;
      private String blocklist;
      private BlocklistBehavior blocklistBehavior;
      private Map<String, ThresholdRequestObject> automodThresholds;
      private List<String> commands;
      private List<PermissionRequestObject> permissions;

      private ChannelTypeUpdateRequest(String name) {
        this.name = name;
      }

      @NotNull
      public ChannelTypeUpdateRequest withTypingEvents(@NotNull Boolean typingEvents) {
        this.typingEvents = typingEvents;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withReadEvents(@NotNull Boolean readEvents) {
        this.readEvents = readEvents;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withConnectEvents(@NotNull Boolean connectEvents) {
        this.connectEvents = connectEvents;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withSearch(@NotNull Boolean search) {
        this.search = search;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withReactions(@NotNull Boolean reactions) {
        this.reactions = reactions;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withReplies(@NotNull Boolean replies) {
        this.replies = replies;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withUploads(@NotNull Boolean uploads) {
        this.uploads = uploads;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withUrlEnrichment(@NotNull Boolean urlEnrichment) {
        this.urlEnrichment = urlEnrichment;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withCustomEvents(@NotNull Boolean customEvents) {
        this.customEvents = customEvents;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withMutes(@NotNull Boolean mutes) {
        this.mutes = mutes;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withPushNotifications(@NotNull Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withMessageRetention(@NotNull String messageRetention) {
        this.messageRetention = messageRetention;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withMaxMessageLength(@NotNull Integer maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withAutomod(@NotNull AutoMod automod) {
        this.automod = automod;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withAutomodBehavior(
          @NotNull AutoModBehavior automodBehavior) {
        this.automodBehavior = automodBehavior;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withBlocklist(@NotNull String blocklist) {
        this.blocklist = blocklist;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withBlocklistBehavior(
          @NotNull BlocklistBehavior blocklistBehavior) {
        this.blocklistBehavior = blocklistBehavior;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withAutomodThresholds(
          @NotNull Map<String, ThresholdRequestObject> automodThresholds) {
        this.automodThresholds = automodThresholds;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withCommands(@NotNull List<String> commands) {
        this.commands = commands;
        return this;
      }

      @NotNull
      public ChannelTypeUpdateRequest withPermissions(
          @NotNull List<PermissionRequestObject> permissions) {
        this.permissions = permissions;
        return this;
      }

      @Override
      protected Call<ChannelTypeUpdateResponse> generateCall() {
        return StreamServiceGenerator.createService(ChannelTypeService.class)
            .update(name, new ChannelTypeUpdateRequestData(this));
      }
    }
  }

  public static class ChannelTypeDeleteRequest extends StreamRequest<StreamResponseObject> {
    private String name;

    private ChannelTypeDeleteRequest(String name) {
      this.name = name;
    }

    @Override
    protected Call<StreamResponseObject> generateCall() {
      return StreamServiceGenerator.createService(ChannelTypeService.class).delete(name);
    }
  }

  public static class ChannelTypeListRequest extends StreamRequest<ChannelTypeListResponse> {

    private ChannelTypeListRequest() {}

    @Override
    protected Call<ChannelTypeListResponse> generateCall() {
      return StreamServiceGenerator.createService(ChannelTypeService.class).list();
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelTypeCreateResponse extends ChannelTypeCreateRequestData
      implements StreamResponse {
    private RateLimitData rateLimitData;

    public ChannelTypeCreateResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelTypeGetResponse extends ChannelType implements StreamResponse {
    private RateLimitData rateLimitData;
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelTypeUpdateResponse extends ChannelTypeCreateRequestData
      implements StreamResponse {
    private RateLimitData rateLimitData;

    public ChannelTypeUpdateResponse() {}
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelTypeListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("channel_types")
    private Map<String, ChannelTypeWithCommands> channelTypes;

    public ChannelTypeListResponse() {}
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
  public static ChannelTypeGetRequest get(String name) throws StreamException {
    return new ChannelTypeGetRequest(name);
  }

  /**
   * Creates an update request
   *
   * @param the name of the channel type to update
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
  public static ChannelTypeDeleteRequest delete(String name) throws StreamException {
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
