package io.getstream.chat.java;

import io.getstream.chat.java.models.TeamUsageStats.QueryTeamUsageStatsResponse;
import io.getstream.chat.java.models.TeamUsageStats;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Basic tests for Team Usage Stats API using regular (non-multi-tenant) app credentials.
 * Since the regular app doesn't have multi-tenant enabled, teams will always be empty.
 * Full data verification is done in TeamUsageStatsIntegrationTest with multi-tenant credentials.
 */
public class TeamUsageStatsTest {

  @DisplayName("Can query team usage stats with default options")
  @Test
  void whenQueryingTeamUsageStatsWithDefaultOptions_thenNoException() {
    QueryTeamUsageStatsResponse response =
        Assertions.assertDoesNotThrow(() -> TeamUsageStats.queryTeamUsageStats().request());

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getTeams());
    // Regular app doesn't have multi-tenant, so teams is empty
    Assertions.assertTrue(response.getTeams().isEmpty());
  }

  @DisplayName("Can query team usage stats with month parameter")
  @Test
  void whenQueryingTeamUsageStatsWithMonth_thenNoException() {
    String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

    QueryTeamUsageStatsResponse response =
        Assertions.assertDoesNotThrow(
            () -> TeamUsageStats.queryTeamUsageStats().month(currentMonth).request());

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getTeams());
    Assertions.assertTrue(response.getTeams().isEmpty());
  }

  @DisplayName("Can query team usage stats with date range")
  @Test
  void whenQueryingTeamUsageStatsWithDateRange_thenNoException() {
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
    Assertions.assertTrue(response.getTeams().isEmpty());
  }

  @DisplayName("Can query team usage stats with pagination")
  @Test
  void whenQueryingTeamUsageStatsWithPagination_thenNoException() {
    QueryTeamUsageStatsResponse response =
        Assertions.assertDoesNotThrow(
            () -> TeamUsageStats.queryTeamUsageStats().limit(10).request());

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getTeams());
    Assertions.assertTrue(response.getTeams().isEmpty());
    // No next cursor when teams is empty
    Assertions.assertTrue(response.getNext() == null || response.getNext().isEmpty());
  }
}
