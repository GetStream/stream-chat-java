package io.getstream.chat.java.models;

public class ResourceAction {
  public static final String ALL = "*";
  public static final String CREATE_CHANNEL = "CreateChannel";
  public static final String CREATE_DISTINCT_CHANNEL_FOR_OTHERS = "CreateDistinctChannelsForOthers";
  public static final String READ_CHANNEL = "ReadChannel";
  public static final String UPDATE_CHANNEL_MEMBERS = "UpdateChannelMembers";
  public static final String REMOVE_OWN_CHANNEL_MEMBERSHIP = "RemoveOwnChannelMembership";
  public static final String UPDATE_CHANNEL = "UpdateChannel";
  public static final String DELETE_CHANNEL = "DeleteChannel";
  public static final String CREATE_MESSAGE = "CreateMessage";
  public static final String UPDATE_MESSAGE = "UpdateMessage";
  public static final String PIN_MESSAGE = "PinMessage";
  public static final String DELETE_MESSAGE = "DeleteMessage";
  public static final String RUN_MESSAGE_ACTION = "RunMessageAction";
  public static final String UPLOAD_ATTACHMENT = "UploadAttachment";
  public static final String DELETE_ATTACHMENT = "DeleteAttachment";
  public static final String ADD_LINKS = "AddLinks";
  public static final String CREATE_REACTION = "CreateReaction";
  public static final String DELETE_REACTION = "DeleteReaction";
  public static final String USE_FROZEN_CHANNEL = "UseFrozenChannel";
  public static final String SEND_CUSTOM_EVENT = "SendCustomEvent";
  public static final String SKIP_MESSAGE_MODERATION = "SkipMessageModeration";
  public static final String READ_MESSAGE_FLAGS = "ReadMessageFlags";
  public static final String UPDATE_CHANNEL_FROZEN = "UpdateChannelFrozen";
  public static final String UPDATE_CHANNEL_COOLDOWN = "UpdateChannelCooldown";
  public static final String SKIP_CHANNEL_COOLDOWN = "SkipChannelCooldown";
  public static final String TRUNCATE_CHANNEL = "TruncateChannel";
  public static final String FLAG_MESSAGE = "FlagMessage";
  public static final String MUTE_CHANNEL = "MuteChannel";
  public static final String BAN_USER = "BanUser";
  public static final String UPDATE_USER = "UpdateUser";
  public static final String UPDATE_USER_ROLE = "UpdateUserRole";
  public static final String UPDATE_USER_TEAMS = "UpdateUserTeams";
  public static final String SEARCH_USER = "SearchUser";
  public static final String FLAG_USER = "FlagUser";
  public static final String MUTE_USER = "MuteUser";

  private ResourceAction() {}
}
