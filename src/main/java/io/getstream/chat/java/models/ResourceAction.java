package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ResourceAction implements Comparable<ResourceAction> {
  public static final ResourceAction ALL = new ResourceAction("*");
  public static final ResourceAction CREATE_CHANNEL = new ResourceAction("CreateChannel");
  public static final ResourceAction CREATE_DISTINCT_CHANNEL_FOR_OTHERS =
      new ResourceAction("CreateDistinctChannelsForOthers");
  public static final ResourceAction READ_CHANNEL = new ResourceAction("ReadChannel");
  public static final ResourceAction UPDATE_CHANNEL_MEMBERS =
      new ResourceAction("UpdateChannelMembers");
  public static final ResourceAction REMOVE_OWN_CHANNEL_MEMBERSHIP =
      new ResourceAction("RemoveOwnChannelMembership");
  public static final ResourceAction UPDATE_CHANNEL = new ResourceAction("UpdateChannel");
  public static final ResourceAction DELETE_CHANNEL = new ResourceAction("DeleteChannel");
  public static final ResourceAction CREATE_MESSAGE = new ResourceAction("CreateMessage");
  public static final ResourceAction UPDATE_MESSAGE = new ResourceAction("UpdateMessage");
  public static final ResourceAction PIN_MESSAGE = new ResourceAction("PinMessage");
  public static final ResourceAction DELETE_MESSAGE = new ResourceAction("DeleteMessage");
  public static final ResourceAction RUN_MESSAGE_ACTION = new ResourceAction("RunMessageAction");
  public static final ResourceAction UPLOAD_ATTACHMENT = new ResourceAction("UploadAttachment");
  public static final ResourceAction DELETE_ATTACHMENT = new ResourceAction("DeleteAttachment");
  public static final ResourceAction ADD_LINKS = new ResourceAction("AddLinks");
  public static final ResourceAction CREATE_REACTION = new ResourceAction("CreateReaction");
  public static final ResourceAction DELETE_REACTION = new ResourceAction("DeleteReaction");
  public static final ResourceAction USE_FROZEN_CHANNEL = new ResourceAction("UseFrozenChannel");
  public static final ResourceAction SEND_CUSTOM_EVENT = new ResourceAction("SendCustomEvent");
  public static final ResourceAction SKIP_MESSAGE_MODERATION =
      new ResourceAction("SkipMessageModeration");
  public static final ResourceAction READ_MESSAGE_FLAGS = new ResourceAction("ReadMessageFlags");
  public static final ResourceAction UPDATE_CHANNEL_FROZEN =
      new ResourceAction("UpdateChannelFrozen");
  public static final ResourceAction UPDATE_CHANNEL_COOLDOWN =
      new ResourceAction("UpdateChannelCooldown");
  public static final ResourceAction SKIP_CHANNEL_COOLDOWN =
      new ResourceAction("SkipChannelCooldown");
  public static final ResourceAction TRUNCATE_CHANNEL = new ResourceAction("TruncateChannel");
  public static final ResourceAction FLAG_MESSAGE = new ResourceAction("FlagMessage");
  public static final ResourceAction MUTE_CHANNEL = new ResourceAction("MuteChannel");
  public static final ResourceAction BAN_USER = new ResourceAction("BanUser");
  public static final ResourceAction UPDATE_USER = new ResourceAction("UpdateUser");
  public static final ResourceAction UPDATE_USER_ROLE = new ResourceAction("UpdateUserRole");
  public static final ResourceAction UPDATE_USER_TEAMS = new ResourceAction("UpdateUserTeams");
  public static final ResourceAction SEARCH_USER = new ResourceAction("SearchUser");
  public static final ResourceAction FLAG_USER = new ResourceAction("FlagUser");
  public static final ResourceAction MUTE_USER = new ResourceAction("MuteUser");

  @NotNull private final String resourceAction;

  public ResourceAction(@NotNull String resourceAction) {
    this.resourceAction = resourceAction;
  }

  @Override
  @JsonValue
  public String toString() {
    return resourceAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResourceAction that = (ResourceAction) o;
    return Objects.equals(resourceAction, that.resourceAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceAction);
  }

  @Override
  public int compareTo(@NotNull ResourceAction other) {
    return Objects.compare(resourceAction, other.resourceAction, String::compareTo);
  }
}
