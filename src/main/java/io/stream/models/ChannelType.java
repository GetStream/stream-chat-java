package io.stream.models;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.exceptions.StreamException;
import io.stream.models.ChannelConfig.ChannelConfigWithCommands;
import io.stream.models.ChannelType.ChannelTypeRequestData.ChannelTypeRequest;
import io.stream.models.framework.StreamResponse;
import io.stream.services.ChannelTypeService;
import io.stream.services.framework.StreamServiceGenerator;
import io.stream.services.framework.StreamServiceHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChannelType extends ChannelConfigWithCommands {
  public ChannelType() {}

  @Nullable
  @JsonProperty("roles")
  private Map<String, List<Right>> roles;

  @Nullable
  @JsonProperty("permissions")
  private List<Permission> permissions;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

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

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ChannelTypeRequestData extends ChannelConfigWithStringCommands {
    private static final boolean DEFAULT_PUSH_NOTIFICATIONS = true;

    private static final AutoModBehavior DEFAULT_MOD_BEHAVIOR = AutoModBehavior.FLAG;

    private static final AutoMod DEFAULT_AUTOMOD = AutoMod.DISABLED;

    private static final String DEFAULT_MESSAGE_RETENTION = "infinite";

    private static final int DEFAULT_MAX_MESSAGE_LENGTH = 5000;

    @Nullable
    @JsonProperty("permissions")
    private List<Permission> permissions;

    public ChannelTypeRequestData() {}

    private ChannelTypeRequestData(ChannelTypeRequest channelTypeRequest) {
      this.setName(channelTypeRequest.name);
      this.setTypingEvents(channelTypeRequest.typingEvents);
      this.setReadEvents(channelTypeRequest.readEvents);
      this.setConnectEvents(channelTypeRequest.connectEvents);
      this.setSearch(channelTypeRequest.search);
      this.setReactions(channelTypeRequest.reactions);
      this.setReplies(channelTypeRequest.replies);
      this.setUploads(channelTypeRequest.uploads);
      this.setUrlEnrichment(channelTypeRequest.urlEnrichment);
      this.setCustomEvents(channelTypeRequest.customEvents);
      this.setMutes(channelTypeRequest.mutes);
      this.setPushNotifications(channelTypeRequest.pushNotifications);
      this.setMessageRetention(channelTypeRequest.messageRetention);
      this.setMaxMessageLength(channelTypeRequest.maxMessageLength);
      this.setAutomod(channelTypeRequest.automod);
      this.setAutomodBehavior(channelTypeRequest.automodBehavior);
      this.setCreatedAt(channelTypeRequest.createdAt);
      this.setUpdatedAt(channelTypeRequest.updatedAt);
      this.setCommands(channelTypeRequest.commands);
      this.setBlocklist(channelTypeRequest.blocklist);
      this.setBlocklistBehavior(channelTypeRequest.blocklistBehavior);
      this.setAutomodThresholds(channelTypeRequest.automodThresholds);
      this.permissions = channelTypeRequest.permissions;
    }

    /** Builder to build {@link ChannelTypeRequestData}. */
    public static final class ChannelTypeRequest {
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
      private Date createdAt;
      private Date updatedAt;
      private List<String> commands = Collections.emptyList();
      private String blocklist;
      private BlocklistBehavior blocklistBehavior;
      private Map<String, LabelThreshold> automodThresholds = Collections.emptyMap();
      private List<Permission> permissions = Collections.emptyList();
      private Mode mode;

      private ChannelTypeRequest(Mode mode) {
        this.mode = mode;
      }

      private enum Mode {
        CREATE,
        UPDATE
      }

      @NotNull
      public ChannelTypeRequest withName(@NotNull String name) {
        this.name = name;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withTypingEvents(@NotNull Boolean typingEvents) {
        this.typingEvents = typingEvents;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withReadEvents(@NotNull Boolean readEvents) {
        this.readEvents = readEvents;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withConnectEvents(@NotNull Boolean connectEvents) {
        this.connectEvents = connectEvents;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withSearch(@NotNull Boolean search) {
        this.search = search;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withReactions(@NotNull Boolean reactions) {
        this.reactions = reactions;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withReplies(@NotNull Boolean replies) {
        this.replies = replies;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withUploads(@NotNull Boolean uploads) {
        this.uploads = uploads;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withUrlEnrichment(@NotNull Boolean urlEnrichment) {
        this.urlEnrichment = urlEnrichment;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withCustomEvents(@NotNull Boolean customEvents) {
        this.customEvents = customEvents;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withMutes(@NotNull Boolean mutes) {
        this.mutes = mutes;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withPushNotifications(@NotNull Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withMessageRetention(@NotNull String messageRetention) {
        this.messageRetention = messageRetention;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withMaxMessageLength(@NotNull Integer maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withAutomod(@NotNull AutoMod automod) {
        this.automod = automod;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withAutomodBehavior(@NotNull AutoModBehavior automodBehavior) {
        this.automodBehavior = automodBehavior;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withCreatedAt(@NotNull Date createdAt) {
        this.createdAt = createdAt;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withUpdatedAt(@NotNull Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withCommands(@NotNull List<String> commands) {
        this.commands = commands;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withBlocklist(@NotNull String blocklist) {
        this.blocklist = blocklist;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withBlocklistBehavior(
          @NotNull BlocklistBehavior blocklistBehavior) {
        this.blocklistBehavior = blocklistBehavior;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withAutomodThresholds(
          @NotNull Map<String, LabelThreshold> automodThresholds) {
        this.automodThresholds = automodThresholds;
        return this;
      }

      @NotNull
      public ChannelTypeRequest withPermissions(@NotNull List<Permission> permissions) {
        this.permissions = permissions;
        return this;
      }

      @NotNull
      /**
       * Executes the request
       *
       * @return the request data
       * @throws StreamException when IO problem occurs or the stream API return an error
       */
      public StreamResponse request() throws StreamException {
        switch (mode) {
          case CREATE:
            return new StreamServiceHandler()
                .handle(
                    StreamServiceGenerator.createService(ChannelTypeService.class)
                        .create(new ChannelTypeRequestData(this)));
          case UPDATE:
            ChannelTypeRequestDataWithoutNameSerialization channelTypeRequestData =
                new ChannelTypeRequestDataWithoutNameSerialization(this);
            return new StreamServiceHandler()
                .handle(
                    StreamServiceGenerator.createService(ChannelTypeService.class)
                        .update(channelTypeRequestData.getName(), channelTypeRequestData));
          default:
            throw StreamException.build("Should not happen, unsupported mode");
        }
      }

      public ChannelTypeRequest withDefaultConfig() {
        return this.withAutomod(DEFAULT_AUTOMOD)
            .withAutomodBehavior(DEFAULT_MOD_BEHAVIOR)
            .withMaxMessageLength(DEFAULT_MAX_MESSAGE_LENGTH)
            .withMessageRetention(DEFAULT_MESSAGE_RETENTION)
            .withPushNotifications(DEFAULT_PUSH_NOTIFICATIONS);
      }
    }
  }

  @JsonIgnoreProperties({"name"})
  public static class ChannelTypeRequestDataWithoutNameSerialization
      extends ChannelTypeRequestData {
    private ChannelTypeRequestDataWithoutNameSerialization(ChannelTypeRequest channelTypeRequest) {
      super(channelTypeRequest);
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ListChannelTypeResponse extends StreamResponse {
    public ListChannelTypeResponse() {}

    @NotNull
    @JsonProperty("channel_types")
    private Map<String, ChannelType> channelTypes;
  }

  /**
   * Creates an update request.
   *
   * @return the created request
   */
  public static ChannelTypeRequest update() {
    return new ChannelTypeRequest(ChannelTypeRequest.Mode.UPDATE);
  }

  /**
   * Creates an create request.
   *
   * @return the created request
   */
  public static ChannelTypeRequest create() {
    return new ChannelTypeRequest(ChannelTypeRequest.Mode.CREATE);
  }

  /**
   * Retrieves a channel type by name.
   *
   * @param name the channel type name
   * @return the retrieved channel type
   * @throws StreamException when IO problem occurs or the stream API return an error
   */
  public static ChannelType get(String name) throws StreamException {
    return new StreamServiceHandler()
        .handle(StreamServiceGenerator.createService(ChannelTypeService.class).get(name));
  }

  /**
   * Lists all channel types
   *
   * @return the channel types in a map
   * @throws StreamException when IO problem occurs or the stream API return an error
   */
  public static ListChannelTypeResponse list() throws StreamException {
    return new StreamServiceHandler()
        .handle(StreamServiceGenerator.createService(ChannelTypeService.class).list());
  }

  /**
   * Deletes a channel type by name.
   *
   * @param name the channel type name
   * @return the rate limit information
   * @throws StreamException when IO problem occurs or the stream API return an error
   */
  public static StreamResponse delete(String name) throws StreamException {
    return new StreamServiceHandler()
        .handle(StreamServiceGenerator.createService(ChannelTypeService.class).delete(name));
  }
}
