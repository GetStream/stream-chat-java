package io.getstream.chat.java;

import io.getstream.chat.java.models.TeamUsageStats;
import io.getstream.chat.java.models.TeamUsageStats.QueryTeamUsageStatsResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TeamUsageStatsTest {

  @DisplayName("Can query team usage stats with default options")
  @Test
  void whenQueryingTeamUsageStatsWithDefaultOptions_thenNoException() {
    QueryTeamUsageStatsResponse response =
        Assertions.assertDoesNotThrow(() -> TeamUsageStats.queryTeamUsageStats().request());

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getTeams());
    // Teams list might be empty if there's no usage data
  }

  @DisplayName("Can query team usage stats with month parameter")
  @Test
  void whenQueryingTeamUsageStatsWithMonth_thenNoException() {
    // Use current month
    String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

    QueryTeamUsageStatsResponse response =
        Assertions.assertDoesNotThrow(
            () -> TeamUsageStats.queryTeamUsageStats().month(currentMonth).request());

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getTeams());
  }

  @DisplayName("Can query team usage stats with date range")
  @Test
  void whenQueryingTeamUsageStatsWithDateRange_thenNoException() {
    // Use last 7 days
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusDays(7);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    QueryTeamUsageStatsResponse response =
        Assertions.assertDoesNotThrow(
            () ->
                TeamUsageStats.queryTeamUsageStats()
                    .startDate(startDate.format(formatter))
                    .endDate(endDate.format(formatter))
                    .request());

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getTeams());

    // If there are teams with data, verify the daily breakdown is present
    if (!response.getTeams().isEmpty()) {
      TeamUsageStats team = response.getTeams().get(0);
      Assertions.assertNotNull(team.getTeam());
      Assertions.assertNotNull(team.getUsersDaily());
      Assertions.assertNotNull(team.getMessagesDaily());
    }
  }

  @DisplayName("Can query team usage stats with pagination")
  @Test
  void whenQueryingTeamUsageStatsWithPagination_thenNoException() {
    // First page with limit
    QueryTeamUsageStatsResponse firstPage =
        Assertions.assertDoesNotThrow(
            () -> TeamUsageStats.queryTeamUsageStats().limit(10).request());

    Assertions.assertNotNull(firstPage);
    Assertions.assertNotNull(firstPage.getTeams());

    // If there's a next cursor, fetch the next page
    if (firstPage.getNext() != null && !firstPage.getNext().isEmpty()) {
      QueryTeamUsageStatsResponse secondPage =
          Assertions.assertDoesNotThrow(
              () ->
                  TeamUsageStats.queryTeamUsageStats()
                      .limit(10)
                      .next(firstPage.getNext())
                      .request());

      Assertions.assertNotNull(secondPage);
      Assertions.assertNotNull(secondPage.getTeams());
    }
  }

  @DisplayName("Can query team usage stats for last year and verify response structure")
  @Test
  void whenQueryingTeamUsageStats_thenResponseStructureIsCorrect() {
    // Query last year to maximize chance of getting data
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusYears(1);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    QueryTeamUsageStatsResponse response =
        Assertions.assertDoesNotThrow(
            () ->
                TeamUsageStats.queryTeamUsageStats()
                    .startDate(startDate.format(formatter))
                    .endDate(endDate.format(formatter))
                    .request());

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getTeams());

    // Verify response structure
    List<TeamUsageStats> teams = response.getTeams();
    if (!teams.isEmpty()) {
      TeamUsageStats team = teams.get(0);

      // Verify all 16 metrics are present
      Assertions.assertNotNull(team.getTeam(), "Team identifier should not be null");

      // Daily activity metrics
      Assertions.assertNotNull(team.getUsersDaily(), "users_daily should not be null");
      Assertions.assertNotNull(team.getMessagesDaily(), "messages_daily should not be null");
      Assertions.assertNotNull(
          team.getTranslationsDaily(), "translations_daily should not be null");
      Assertions.assertNotNull(
          team.getImageModerationDaily(), "image_moderations_daily should not be null");

      // Peak metrics
      Assertions.assertNotNull(team.getConcurrentUsers(), "concurrent_users should not be null");
      Assertions.assertNotNull(
          team.getConcurrentConnections(), "concurrent_connections should not be null");

      // Rolling/cumulative metrics
      Assertions.assertNotNull(team.getUsersTotal(), "users_total should not be null");
      Assertions.assertNotNull(
          team.getUsersLast24Hours(), "users_last_24_hours should not be null");
      Assertions.assertNotNull(team.getUsersLast30Days(), "users_last_30_days should not be null");
      Assertions.assertNotNull(
          team.getUsersMonthToDate(), "users_month_to_date should not be null");
      Assertions.assertNotNull(
          team.getUsersEngagedLast30Days(), "users_engaged_last_30_days should not be null");
      Assertions.assertNotNull(
          team.getUsersEngagedMonthToDate(), "users_engaged_month_to_date should not be null");
      Assertions.assertNotNull(team.getMessagesTotal(), "messages_total should not be null");
      Assertions.assertNotNull(
          team.getMessagesLast24Hours(), "messages_last_24_hours should not be null");
      Assertions.assertNotNull(
          team.getMessagesLast30Days(), "messages_last_30_days should not be null");
      Assertions.assertNotNull(
          team.getMessagesMonthToDate(), "messages_month_to_date should not be null");

      // Verify MetricStats structure
      Assertions.assertNotNull(
          team.getUsersDaily().getTotal(), "MetricStats total should not be null");
    }
  }
}
