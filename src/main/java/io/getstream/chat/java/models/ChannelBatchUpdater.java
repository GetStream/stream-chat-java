package io.getstream.chat.java.models;

import io.getstream.chat.java.models.Channel.*;
import java.util.List;
import org.jetbrains.annotations.NotNull;

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
    options.setMembers(members);
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
    options.setMembers(members);
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
    options.setMembers(members);
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
    options.setMembers(members);
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
    options.setMembers(members);
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
    options.setMembers(members);
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
    options.setMembers(members);
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
    options.setMembers(members);
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
    options.setMembers(members);
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
    options.setMembers(members);
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
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.UPDATE_DATA);
    options.setFilter(filter);
    options.setData(data);
    return Channel.updateBatch(options);
  }

  /**
   * Adds filter tags to channels matching the filter.
   *
   * @param filter the filter to match channels
   * @param tags list of filter tags to add
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest addFilterTags(
      @NotNull ChannelsBatchFilters filter, @NotNull List<String> tags) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.ADD_FILTER_TAGS);
    options.setFilter(filter);
    options.setFilterTagsUpdate(tags);
    return Channel.updateBatch(options);
  }

  /**
   * Removes filter tags from channels matching the filter.
   *
   * @param filter the filter to match channels
   * @param tags list of filter tags to remove
   * @return the batch update request
   */
  @NotNull
  public ChannelsBatchUpdateRequest removeFilterTags(
      @NotNull ChannelsBatchFilters filter, @NotNull List<String> tags) {
    ChannelsBatchOptions options = new ChannelsBatchOptions();
    options.setOperation(ChannelBatchOperation.REMOVE_FILTER_TAGS);
    options.setFilter(filter);
    options.setFilterTagsUpdate(tags);
    return Channel.updateBatch(options);
  }
}
