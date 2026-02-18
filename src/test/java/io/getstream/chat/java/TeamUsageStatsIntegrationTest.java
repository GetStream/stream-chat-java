package io.getstream.chat.java;

import static org.junit.jupiter.api.Assertions.*;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.TeamUsageStats;
import io.getstream.chat.java.models.TeamUsageStats.MetricStats;
import io.getstream.chat.java.models.TeamUsageStats.QueryTeamUsageStatsResponse;
import io.getstream.chat.java.services.framework.DefaultClient;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for Team Usage Stats API. Uses dedicated test credentials from
 * STREAM_USAGE_STATS_KEY and STREAM_USAGE_STATS_SECRET environment variables.
 *
 * <p>These tests verify that the SDK correctly parses all response data from the backend.
 */
public class TeamUsageStatsIntegrationTest {

  @BeforeAll
  static void setup() {
    // Use dedicated credentials for usage stats testing
    String apiKey = System.getenv("STREAM_USAGE_STATS_KEY");
    String apiSecret = System.getenv("STREAM_USAGE_STATS_SECRET");

    // Fall back to standard credentials if usage stats specific ones aren't set
    if (apiKey == null || apiKey.isEmpty()) {
      apiKey = System.getenv("STREAM_KEY");
    }
    if (apiSecret == null || apiSecret.isEmpty()) {
      apiSecret = System.getenv("STREAM_SECRET");
    }

    if (apiKey == null || apiSecret == null) {
      throw new IllegalStateException(
          "Missing credentials. Set STREAM_USAGE_STATS_KEY/STREAM_USAGE_STATS_SECRET or"
              + " STREAM_KEY/STREAM_SECRET");
    }

    Properties props = new Properties();
    props.setProperty("io.getstream.chat.apiKey", apiKey);
    props.setProperty("io.getstream.chat.apiSecret", apiSecret);

    DefaultClient.setInstance(new DefaultClient(props));
  }

  @Nested
  @DisplayName("Basic Queries")
  class BasicQueries {

    @Test
    @DisplayName("No parameters returns teams")
    void noParametersReturnsTeams() throws StreamException {
      QueryTeamUsageStatsResponse response = TeamUsageStats.queryTeamUsageStats().request();

      assertNotNull(response);
      assertNotNull(response.getTeams());
      assertTrue(response.getTeams().size() > 0, "Should return at least one team");
      assertNotNull(response.getDuration());
    }

    @Test
    @DisplayName("Empty request returns teams")
    void emptyRequestReturnsTeams() throws StreamException {
      QueryTeamUsageStatsResponse response = TeamUsageStats.queryTeamUsageStats().request();

      assertNotNull(response.getTeams());
    }
  }

  @Nested
  @DisplayName("Month Parameter")
  class MonthParameter {

    @Test
    @DisplayName("Valid month format works")
    void validMonthWorks() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats().month("2026-02").request();

      assertNotNull(response.getTeams());
      assertTrue(response.getTeams().size() > 0);
    }

    @Test
    @DisplayName("Past month with no data returns empty")
    void pastMonthReturnsEmpty() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats().month("2025-01").request();

      assertNotNull(response.getTeams());
      assertEquals(0, response.getTeams().size());
    }

    @Test
    @DisplayName("Invalid month format throws error")
    void invalidMonthThrows() {
      assertThrows(
          StreamException.class,
          () -> TeamUsageStats.queryTeamUsageStats().month("invalid").request());
    }

    @Test
    @DisplayName("Wrong length month throws error")
    void wrongLengthMonthThrows() {
      assertThrows(
          StreamException.class,
          () -> TeamUsageStats.queryTeamUsageStats().month("2026").request());
    }
  }

  @Nested
  @DisplayName("Date Range Parameters")
  class DateRangeParameters {

    @Test
    @DisplayName("Valid date range works")
    void validDateRangeWorks() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats()
              .startDate("2026-02-01")
              .endDate("2026-02-17")
              .request();

      assertNotNull(response.getTeams());
      assertTrue(response.getTeams().size() > 0);
    }

    @Test
    @DisplayName("Single day range works")
    void singleDayRangeWorks() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats()
              .startDate("2026-02-17")
              .endDate("2026-02-17")
              .request();

      assertNotNull(response.getTeams());
    }

    @Test
    @DisplayName("Invalid start_date throws error")
    void invalidStartDateThrows() {
      assertThrows(
          StreamException.class,
          () -> TeamUsageStats.queryTeamUsageStats().startDate("bad").request());
    }

    @Test
    @DisplayName("end_date before start_date throws error")
    void endBeforeStartThrows() {
      assertThrows(
          StreamException.class,
          () ->
              TeamUsageStats.queryTeamUsageStats()
                  .startDate("2026-02-20")
                  .endDate("2026-02-10")
                  .request());
    }
  }

  @Nested
  @DisplayName("Pagination")
  class Pagination {

    @Test
    @DisplayName("limit=3 returns exactly 3 teams")
    void limitReturnsCorrectCount() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats().limit(3).request();

      assertEquals(3, response.getTeams().size());
    }

    @Test
    @DisplayName("limit returns next cursor when more data exists")
    void limitReturnsNextCursor() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats().limit(3).request();

      assertNotNull(response.getNext());
      assertFalse(response.getNext().isEmpty());
    }

    @Test
    @DisplayName("Pagination with next cursor returns different teams")
    void paginationReturnsDifferentTeams() throws StreamException {
      QueryTeamUsageStatsResponse page1 = TeamUsageStats.queryTeamUsageStats().limit(3).request();
      QueryTeamUsageStatsResponse page2 =
          TeamUsageStats.queryTeamUsageStats().limit(3).next(page1.getNext()).request();

      // Verify no overlap between pages
      for (var t1 : page1.getTeams()) {
        for (var t2 : page2.getTeams()) {
          assertNotEquals(
              t1.getTeam(), t2.getTeam(), "Pages should not have overlapping teams");
        }
      }
    }

    @Test
    @DisplayName("limit=30 (max) works")
    void maxLimitWorks() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats().limit(30).request();

      assertNotNull(response.getTeams());
    }

    @Test
    @DisplayName("limit > 30 throws error")
    void overMaxLimitThrows() {
      assertThrows(
          StreamException.class, () -> TeamUsageStats.queryTeamUsageStats().limit(31).request());
    }

    @Test
    @DisplayName("limit + month combined works")
    void limitWithMonthWorks() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats().limit(2).month("2026-02").request();

      assertEquals(2, response.getTeams().size());
    }

    @Test
    @DisplayName("limit + date range combined works")
    void limitWithDateRangeWorks() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats()
              .limit(2)
              .startDate("2026-02-01")
              .endDate("2026-02-17")
              .request();

      assertEquals(2, response.getTeams().size());
    }
  }

  @Nested
  @DisplayName("Response Structure Validation")
  class ResponseStructure {

    @Test
    @DisplayName("Response has duration field")
    void responseHasDuration() throws StreamException {
      QueryTeamUsageStatsResponse response = TeamUsageStats.queryTeamUsageStats().request();

      assertNotNull(response.getDuration());
    }

    @Test
    @DisplayName("Teams have team field")
    void teamsHaveTeamField() throws StreamException {
      QueryTeamUsageStatsResponse response = TeamUsageStats.queryTeamUsageStats().request();

      // team field exists (may be empty string for default team)
      assertDoesNotThrow(() -> response.getTeams().get(0).getTeam());
    }

    @Test
    @DisplayName("All 16 metrics are present and parseable")
    void allMetricsPresent() throws StreamException {
      QueryTeamUsageStatsResponse response = TeamUsageStats.queryTeamUsageStats().request();
      var team = response.getTeams().get(0);

      // Daily activity metrics
      assertNotNull(team.getUsersDaily(), "users_daily should be present");
      assertNotNull(team.getMessagesDaily(), "messages_daily should be present");
      assertNotNull(team.getTranslationsDaily(), "translations_daily should be present");
      assertNotNull(team.getImageModerationDaily(), "image_moderations_daily should be present");

      // Peak metrics
      assertNotNull(team.getConcurrentUsers(), "concurrent_users should be present");
      assertNotNull(team.getConcurrentConnections(), "concurrent_connections should be present");

      // Rolling/cumulative metrics
      assertNotNull(team.getUsersTotal(), "users_total should be present");
      assertNotNull(team.getUsersLast24Hours(), "users_last_24_hours should be present");
      assertNotNull(team.getUsersLast30Days(), "users_last_30_days should be present");
      assertNotNull(team.getUsersMonthToDate(), "users_month_to_date should be present");
      assertNotNull(team.getUsersEngagedLast30Days(), "users_engaged_last_30_days should be present");
      assertNotNull(
          team.getUsersEngagedMonthToDate(), "users_engaged_month_to_date should be present");
      assertNotNull(team.getMessagesTotal(), "messages_total should be present");
      assertNotNull(team.getMessagesLast24Hours(), "messages_last_24_hours should be present");
      assertNotNull(team.getMessagesLast30Days(), "messages_last_30_days should be present");
      assertNotNull(team.getMessagesMonthToDate(), "messages_month_to_date should be present");
    }

    @Test
    @DisplayName("Metrics have total field with valid value")
    void metricsHaveTotal() throws StreamException {
      QueryTeamUsageStatsResponse response = TeamUsageStats.queryTeamUsageStats().request();
      var team = response.getTeams().get(0);

      // Verify total field is present and non-null
      assertNotNull(team.getMessagesTotal().getTotal());
      assertNotNull(team.getUsersDaily().getTotal());
      assertNotNull(team.getConcurrentUsers().getTotal());
    }

    @Test
    @DisplayName("MetricStats total values are non-negative")
    void metricTotalsNonNegative() throws StreamException {
      QueryTeamUsageStatsResponse response = TeamUsageStats.queryTeamUsageStats().request();

      for (var team : response.getTeams()) {
        assertTrue(team.getMessagesTotal().getTotal() >= 0, "messages_total should be >= 0");
        assertTrue(team.getUsersDaily().getTotal() >= 0, "users_daily should be >= 0");
        assertTrue(team.getConcurrentUsers().getTotal() >= 0, "concurrent_users should be >= 0");
      }
    }
  }

  @Nested
  @DisplayName("Data Correctness")
  class DataCorrectness {

    /**
     * Verifies exact metric values for sdk-test-team-1, sdk-test-team-2, sdk-test-team-3.
     *
     * <p>This test queries a fixed date range (2026-02-17 to 2026-02-18) and asserts exact values
     * for all 16 metrics. This ensures the SDK correctly parses the API response and catches any
     * API/SDK regressions.
     *
     * <p>Expected values for each sdk-test-team-N:
     * <ul>
     *   <li>users_daily: 0</li>
     *   <li>messages_daily: 100</li>
     *   <li>translations_daily: 0</li>
     *   <li>image_moderations_daily: 0</li>
     *   <li>concurrent_users: 0</li>
     *   <li>concurrent_connections: 0</li>
     *   <li>users_total: 5</li>
     *   <li>users_last_24_hours: 5</li>
     *   <li>users_last_30_days: 5</li>
     *   <li>users_month_to_date: 5</li>
     *   <li>users_engaged_last_30_days: 0</li>
     *   <li>users_engaged_month_to_date: 0</li>
     *   <li>messages_total: 100</li>
     *   <li>messages_last_24_hours: 100</li>
     *   <li>messages_last_30_days: 100</li>
     *   <li>messages_month_to_date: 100</li>
     * </ul>
     */
    @Test
    @DisplayName("sdk-test-team-1 has exact expected values for all 16 metrics")
    void sdkTestTeam1HasExactValues() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats()
              .startDate("2026-02-17")
              .endDate("2026-02-18")
              .request();

      TeamUsageStats team = findTeamByName(response, "sdk-test-team-1");
      assertNotNull(team, "sdk-test-team-1 should exist");

      assertAllMetricsExact(team, "sdk-test-team-1");
    }

    @Test
    @DisplayName("sdk-test-team-2 has exact expected values for all 16 metrics")
    void sdkTestTeam2HasExactValues() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats()
              .startDate("2026-02-17")
              .endDate("2026-02-18")
              .request();

      TeamUsageStats team = findTeamByName(response, "sdk-test-team-2");
      assertNotNull(team, "sdk-test-team-2 should exist");

      assertAllMetricsExact(team, "sdk-test-team-2");
    }

    @Test
    @DisplayName("sdk-test-team-3 has exact expected values for all 16 metrics")
    void sdkTestTeam3HasExactValues() throws StreamException {
      QueryTeamUsageStatsResponse response =
          TeamUsageStats.queryTeamUsageStats()
              .startDate("2026-02-17")
              .endDate("2026-02-18")
              .request();

      TeamUsageStats team = findTeamByName(response, "sdk-test-team-3");
      assertNotNull(team, "sdk-test-team-3 should exist");

      assertAllMetricsExact(team, "sdk-test-team-3");
    }

    private TeamUsageStats findTeamByName(QueryTeamUsageStatsResponse response, String teamName) {
      for (TeamUsageStats team : response.getTeams()) {
        if (teamName.equals(team.getTeam())) {
          return team;
        }
      }
      return null;
    }

    private void assertAllMetricsExact(TeamUsageStats team, String teamName) {
      // Daily activity metrics
      assertEquals(0, team.getUsersDaily().getTotal(), teamName + " users_daily");
      assertEquals(100, team.getMessagesDaily().getTotal(), teamName + " messages_daily");
      assertEquals(0, team.getTranslationsDaily().getTotal(), teamName + " translations_daily");
      assertEquals(
          0, team.getImageModerationDaily().getTotal(), teamName + " image_moderations_daily");

      // Peak metrics
      assertEquals(0, team.getConcurrentUsers().getTotal(), teamName + " concurrent_users");
      assertEquals(
          0, team.getConcurrentConnections().getTotal(), teamName + " concurrent_connections");

      // User rolling/cumulative metrics
      assertEquals(5, team.getUsersTotal().getTotal(), teamName + " users_total");
      assertEquals(5, team.getUsersLast24Hours().getTotal(), teamName + " users_last_24_hours");
      assertEquals(5, team.getUsersLast30Days().getTotal(), teamName + " users_last_30_days");
      assertEquals(5, team.getUsersMonthToDate().getTotal(), teamName + " users_month_to_date");
      assertEquals(
          0, team.getUsersEngagedLast30Days().getTotal(), teamName + " users_engaged_last_30_days");
      assertEquals(
          0,
          team.getUsersEngagedMonthToDate().getTotal(),
          teamName + " users_engaged_month_to_date");

      // Message rolling/cumulative metrics
      assertEquals(100, team.getMessagesTotal().getTotal(), teamName + " messages_total");
      assertEquals(
          100, team.getMessagesLast24Hours().getTotal(), teamName + " messages_last_24_hours");
      assertEquals(
          100, team.getMessagesLast30Days().getTotal(), teamName + " messages_last_30_days");
      assertEquals(
          100, team.getMessagesMonthToDate().getTotal(), teamName + " messages_month_to_date");
    }
  }
}
