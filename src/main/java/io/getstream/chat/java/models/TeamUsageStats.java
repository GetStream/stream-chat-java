package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.TeamUsageStats.QueryTeamUsageStatsRequestData.QueryTeamUsageStatsRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.StatsService;
import io.getstream.chat.java.services.framework.Client;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

/** Team-level usage statistics for multi-tenant apps. */
@Data
@NoArgsConstructor
public class TeamUsageStats {

  /** Team identifier (empty string for users not assigned to any team). */
  @NotNull
  @JsonProperty("team")
  private String team;

  // Daily activity metrics (total = SUM of daily values)

  /** Daily active users. */
  @NotNull
  @JsonProperty("users_daily")
  private MetricStats usersDaily;

  /** Daily messages sent. */
  @NotNull
  @JsonProperty("messages_daily")
  private MetricStats messagesDaily;

  /** Daily translations. */
  @NotNull
  @JsonProperty("translations_daily")
  private MetricStats translationsDaily;

  /** Daily image moderations. */
  @NotNull
  @JsonProperty("image_moderations_daily")
  private MetricStats imageModerationDaily;

  // Peak metrics (total = MAX of daily values)

  /** Peak concurrent users. */
  @NotNull
  @JsonProperty("concurrent_users")
  private MetricStats concurrentUsers;

  /** Peak concurrent connections. */
  @NotNull
  @JsonProperty("concurrent_connections")
  private MetricStats concurrentConnections;

  // Rolling/cumulative metrics (total = LATEST daily value)

  /** Total users. */
  @NotNull
  @JsonProperty("users_total")
  private MetricStats usersTotal;

  /** Users active in last 24 hours. */
  @NotNull
  @JsonProperty("users_last_24_hours")
  private MetricStats usersLast24Hours;

  /** MAU - users active in last 30 days. */
  @NotNull
  @JsonProperty("users_last_30_days")
  private MetricStats usersLast30Days;

  /** Users active this month. */
  @NotNull
  @JsonProperty("users_month_to_date")
  private MetricStats usersMonthToDate;

  /** Engaged MAU. */
  @NotNull
  @JsonProperty("users_engaged_last_30_days")
  private MetricStats usersEngagedLast30Days;

  /** Engaged users this month. */
  @NotNull
  @JsonProperty("users_engaged_month_to_date")
  private MetricStats usersEngagedMonthToDate;

  /** Total messages. */
  @NotNull
  @JsonProperty("messages_total")
  private MetricStats messagesTotal;

  /** Messages in last 24 hours. */
  @NotNull
  @JsonProperty("messages_last_24_hours")
  private MetricStats messagesLast24Hours;

  /** Messages in last 30 days. */
  @NotNull
  @JsonProperty("messages_last_30_days")
  private MetricStats messagesLast30Days;

  /** Messages this month. */
  @NotNull
  @JsonProperty("messages_month_to_date")
  private MetricStats messagesMonthToDate;

  /** Statistics for a single metric with optional daily breakdown. */
  @Data
  @NoArgsConstructor
  public static class MetricStats {
    /** Per-day values (only present in daily mode). */
    @Nullable
    @JsonProperty("daily")
    private List<DailyValue> daily;

    /** Aggregated total value. */
    @NotNull
    @JsonProperty("total")
    private Long total;
  }

  /** Represents a metric value for a specific date. */
  @Data
  @NoArgsConstructor
  public static class DailyValue {
    /** Date in YYYY-MM-DD format. */
    @NotNull
    @JsonProperty("date")
    private String date;

    /** Metric value for this date. */
    @NotNull
    @JsonProperty("value")
    private Long value;
  }

  @Builder(
      builderClassName = "QueryTeamUsageStatsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class QueryTeamUsageStatsRequestData {
    /**
     * Month in YYYY-MM format (e.g., '2026-01'). Mutually exclusive with start_date/end_date.
     * Returns aggregated monthly values.
     */
    @Nullable
    @JsonProperty("month")
    private String month;

    /**
     * Start date in YYYY-MM-DD format. Used with end_date for custom date range. Returns daily
     * breakdown.
     */
    @Nullable
    @JsonProperty("start_date")
    private String startDate;

    /**
     * End date in YYYY-MM-DD format. Used with start_date for custom date range. Returns daily
     * breakdown.
     */
    @Nullable
    @JsonProperty("end_date")
    private String endDate;

    /** Maximum number of teams to return per page (default: 30, max: 30). */
    @Nullable
    @JsonProperty("limit")
    private Integer limit;

    /** Cursor for pagination to fetch next page of teams. */
    @Nullable
    @JsonProperty("next")
    private String next;

    public static class QueryTeamUsageStatsRequest
        extends StreamRequest<QueryTeamUsageStatsResponse> {
      @Override
      protected Call<QueryTeamUsageStatsResponse> generateCall(Client client) {
        return client.create(StatsService.class).queryTeamUsageStats(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class QueryTeamUsageStatsResponse extends StreamResponseObject {
    /** Array of team usage statistics. */
    @NotNull
    @JsonProperty("teams")
    private List<TeamUsageStats> teams;

    /** Cursor for pagination to fetch next page. */
    @Nullable
    @JsonProperty("next")
    private String next;
  }

  /**
   * Queries team-level usage statistics from the warehouse database.
   *
   * <p>Returns all 16 metrics grouped by team with cursor-based pagination.
   *
   * <p>Date Range Options (mutually exclusive):
   *
   * <ul>
   *   <li>Use 'month' parameter (YYYY-MM format) for monthly aggregated values
   *   <li>Use 'startDate'/'endDate' parameters (YYYY-MM-DD format) for daily breakdown
   *   <li>If neither provided, defaults to current month (monthly mode)
   * </ul>
   *
   * <p>This endpoint is server-side only.
   *
   * @return the created request
   */
  @NotNull
  public static QueryTeamUsageStatsRequest queryTeamUsageStats() {
    return new QueryTeamUsageStatsRequest();
  }
}
