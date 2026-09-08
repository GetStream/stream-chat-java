package io.getstream.chat.java.models;

import io.getstream.chat.java.models.Channel.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Provides convenience methods for batch channel operations. */
public class ChannelBatchUpdater {

  /**
   * Adds members to channels matching the filter.
   *
   * @param filter the filter to match channels
   * @param members list of members to add
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest addMembers(
      @NotNull ChannelsBatchFilters filter, @NotNull List<ChannelBatchMemberRequest> members) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.ADD_MEMBERS);
    options.setFilter(filter);
    options.setMembers(members != null ? new ArrayList<>(members) : null);
    return Channel.updateBatch(options);
  }

  /**
   * Removes members from channels matching the filter.
   *
   * @param filter the filter to match channels
   * @param members list of members to remove
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest removeMembers(
      @NotNull ChannelsBatchFilters filter, @NotNull List<ChannelBatchMemberRequest> members) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.REMOVE_MEMBERS);
    options.setFilter(filter);
    options.setMembers(members != null ? new ArrayList<>(members) : null);
    return Channel.updateBatch(options);
  }

  /**
   * Invites members to channels matching the filter.
   *
   * @param filter the filter to match channels
   * @param members list of members to invite
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest inviteMembers(
      @NotNull ChannelsBatchFilters filter, @NotNull List<ChannelBatchMemberRequest> members) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.INVITE_MEMBERS);
    options.setFilter(filter);
    options.setMembers(members != null ? new ArrayList<>(members) : null);
    return Channel.updateBatch(options);
  }

  /**
   * Adds moderators to channels matching the filter.
   *
   * @param filter the filter to match channels
   * @param members list of members to add as moderators
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest addModerators(
      @NotNull ChannelsBatchFilters filter, @NotNull List<ChannelBatchMemberRequest> members) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.ADD_MODERATORS);
    options.setFilter(filter);
    options.setMembers(members != null ? new ArrayList<>(members) : null);
    return Channel.updateBatch(options);
  }

  /**
   * Removes moderator role from members in channels matching the filter.
   *
   * @param filter the filter to match channels
   * @param members list of members to demote from moderators
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest demoteModerators(
      @NotNull ChannelsBatchFilters filter, @NotNull List<ChannelBatchMemberRequest> members) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.DEMOTE_MODERATORS);
    options.setFilter(filter);
    options.setMembers(members != null ? new ArrayList<>(members) : null);
    return Channel.updateBatch(options);
  }

  /**
   * Assigns roles to members in channels matching the filter.
   *
   * @param filter the filter to match channels
   * @param members list of members with roles to assign
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest assignRoles(
      @NotNull ChannelsBatchFilters filter, @NotNull List<ChannelBatchMemberRequest> members) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.ASSIGN_ROLES);
    options.setFilter(filter);
    options.setMembers(members != null ? new ArrayList<>(members) : null);
    return Channel.updateBatch(options);
  }

  /**
   * Hides channels matching the filter for the specified members.
   *
   * @param filter the filter to match channels
   * @param members list of members for whom to hide channels
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest hide(
      @NotNull ChannelsBatchFilters filter, @NotNull List<ChannelBatchMemberRequest> members) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.HIDE);
    options.setFilter(filter);
    options.setMembers(members != null ? new ArrayList<>(members) : null);
    return Channel.updateBatch(options);
  }

  /**
   * Shows channels matching the filter for the specified members.
   *
   * @param filter the filter to match channels
   * @param members list of members for whom to show channels
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest show(
      @NotNull ChannelsBatchFilters filter, @NotNull List<ChannelBatchMemberRequest> members) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.SHOW);
    options.setFilter(filter);
    options.setMembers(members != null ? new ArrayList<>(members) : null);
    return Channel.updateBatch(options);
  }

  /**
   * Archives channels matching the filter for the specified members.
   *
   * @param filter the filter to match channels
   * @param members list of members for whom to archive channels
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest archive(
      @NotNull ChannelsBatchFilters filter, @NotNull List<ChannelBatchMemberRequest> members) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.ARCHIVE);
    options.setFilter(filter);
    options.setMembers(members != null ? new ArrayList<>(members) : null);
    return Channel.updateBatch(options);
  }

  /**
   * Unarchives channels matching the filter for the specified members.
   *
   * @param filter the filter to match channels
   * @param members list of members for whom to unarchive channels
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest unarchive(
      @NotNull ChannelsBatchFilters filter, @NotNull List<ChannelBatchMemberRequest> members) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.UNARCHIVE);
    options.setFilter(filter);
    options.setMembers(members != null ? new ArrayList<>(members) : null);
    return Channel.updateBatch(options);
  }

  /**
   * Updates data on channels matching the filter.
   *
   * @param filter the filter to match channels
   * @param data channel data to update
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest updateData(
      @NotNull ChannelsBatchFilters filter, @NotNull ChannelDataUpdate data) {
    return updateData(filter, data, null, null);
  }

  /**
   * Updates data on channels matching the filter, patching individual custom keys instead of
   * replacing the whole custom object.
   *
   * <p>{@code customSet} merges its keys into each channel's existing custom object and {@code
   * customUnset} deletes its keys, both leaving every other custom key untouched. Keys are
   * dot-paths. They cannot be combined with {@code data.custom}, which replaces the whole object;
   * the backend validates that and the other combinations it rejects. Pass a null {@code data} to
   * send a custom patch on its own.
   *
   * @param filter the filter to match channels
   * @param data channel data to update, or null to only patch custom keys
   * @param customSet custom keys to merge in, or null
   * @param customUnset custom keys to delete, or null
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest updateData(
      @NotNull ChannelsBatchFilters filter,
      @Nullable ChannelDataUpdate data,
      @Nullable Map<String, Object> customSet,
      @Nullable List<String> customUnset) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.UPDATE_DATA);
    options.setFilter(filter);
    options.setData(data);
    options.setCustomSet(customSet != null ? new HashMap<>(customSet) : null);
    options.setCustomUnset(customUnset != null ? new ArrayList<>(customUnset) : null);
    return Channel.updateBatch(options);
  }
}
