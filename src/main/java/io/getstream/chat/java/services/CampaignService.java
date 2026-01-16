package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Campaign.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.*;

public interface CampaignService {

  @POST("campaigns")
  @NotNull
  Call<CampaignCreateResponse> create(
      @NotNull @Body CampaignCreateRequestData campaignCreateRequestData);

  @GET("campaigns/{id}")
  @NotNull
  Call<CampaignGetResponse> get(@NotNull @Path("id") String id);

  @PUT("campaigns/{id}")
  @NotNull
  Call<CampaignUpdateResponse> update(
      @NotNull @Path("id") String id,
      @NotNull @Body CampaignUpdateRequestData campaignUpdateRequestData);

  @DELETE("campaigns/{id}")
  @NotNull
  Call<CampaignDeleteResponse> delete(@NotNull @Path("id") String id);

  @POST("campaigns/{id}/start")
  @NotNull
  Call<CampaignStartResponse> start(
      @NotNull @Path("id") String id,
      @Nullable @Body CampaignStartRequestData campaignStartRequestData);

  @POST("campaigns/{id}/stop")
  @NotNull
  Call<CampaignStopResponse> stop(@NotNull @Path("id") String id);

  @POST("campaigns/query")
  @NotNull
  Call<CampaignQueryResponse> query(
      @NotNull @Body CampaignQueryRequestData campaignQueryRequestData);
}
