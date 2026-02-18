package io.getstream.chat.java.services;

import io.getstream.chat.java.models.TeamUsageStats.QueryTeamUsageStatsRequestData;
import io.getstream.chat.java.models.TeamUsageStats.QueryTeamUsageStatsResponse;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface StatsService {
  @POST("stats/team_usage")
  Call<QueryTeamUsageStatsResponse> queryTeamUsageStats(
      @NotNull @Body QueryTeamUsageStatsRequestData queryTeamUsageStatsRequestData);
}
