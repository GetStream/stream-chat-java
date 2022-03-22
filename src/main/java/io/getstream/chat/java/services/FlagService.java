package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Flag.FlagCreateRequestData;
import io.getstream.chat.java.models.Flag.FlagCreateResponse;
import io.getstream.chat.java.models.Flag.FlagDeleteRequestData;
import io.getstream.chat.java.models.Flag.FlagDeleteResponse;
import io.getstream.chat.java.models.Flag.FlagMessageQueryRequestData;
import io.getstream.chat.java.models.Flag.FlagMessageQueryResponse;
import io.getstream.chat.java.models.Flag.QueryFlagReportsRequestData;
import io.getstream.chat.java.models.Flag.QueryFlagReportsResponse;
import io.getstream.chat.java.models.Flag.ReviewFlagReportRequestData;
import io.getstream.chat.java.models.Flag.ReviewFlagReportResponse;
import io.getstream.chat.java.services.framework.ToJson;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.*;

public interface FlagService {

  @POST("moderation/flag")
  Call<FlagCreateResponse> create(@NotNull @Body FlagCreateRequestData flagCreateRequestData);

  @POST("moderation/unflag")
  Call<FlagDeleteResponse> delete(@NotNull @Body FlagDeleteRequestData flagDeleteRequestData);

  @GET("moderation/flags/message")
  Call<FlagMessageQueryResponse> messageQuery(
      @NotNull @ToJson @Query("payload") FlagMessageQueryRequestData flagMessageQueryRequestData);

  @POST("moderation/reports")
  Call<QueryFlagReportsResponse> queryFlagReports(
      @NotNull @Body QueryFlagReportsRequestData queryFlagReportsRequestData);

  @PATCH("moderation/reports/{id}")
  Call<ReviewFlagReportResponse> reviewFlagReport(
      @NotNull @Path("id") String id,
      @NotNull @Body ReviewFlagReportRequestData reviewFlagReportRequestData);
}
