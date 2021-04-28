package io.stream.models;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChannelConfig {
  public ChannelConfig() {}

  @NotNull
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("typing_events")
  private Boolean typingEvents;

  @NotNull
  @JsonProperty("read_events")
  private Boolean readEvents;

  @NotNull
  @JsonProperty("connect_events")
  private Boolean connectEvents;

  @NotNull
  @JsonProperty("search")
  private Boolean search;

  @NotNull
  @JsonProperty("reactions")
  private Boolean reactions;

  @NotNull
  @JsonProperty("replies")
  private Boolean replies;

  @NotNull
  @JsonProperty("uploads")
  private Boolean uploads;

  @NotNull
  @JsonProperty("url_enrichment")
  private Boolean urlEnrichment;

  @NotNull
  @JsonProperty("custom_events")
  private Boolean customEvents;

  @NotNull
  @JsonProperty("mutes")
  private Boolean mutes;

  @NotNull
  @JsonProperty("push_notifications")
  private Boolean pushNotifications;

  @NotNull
  @JsonProperty("message_retention")
  private String messageRetention;

  @NotNull
  @JsonProperty("max_message_length")
  private Integer maxMessageLength;

  @NotNull
  @JsonProperty("automod")
  private String automod;

  @NotNull
  @JsonProperty("automod_behavior")
  private String modBehavior;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

  @Nullable
  @JsonProperty("commands")
  private List<String> commands;

  @Nullable
  @JsonProperty("blocklist")
  private String blocklist;

  @Nullable
  @JsonProperty("blocklist_behavior")
  private String blocklistBehavior;

  @Nullable
  @JsonProperty("automod_thresholds")
  private Map<String, LabelThreshold> automodThresholds;

  @Data
  static class LabelThreshold {
    public LabelThreshold() {}

    @Nullable
    @JsonProperty("flag")
    private Integer flag;

    @Nullable
    @JsonProperty("block")
    private Integer block;
  }
}
