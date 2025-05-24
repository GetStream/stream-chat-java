package io.getstream.chat.java.services;

import io.getstream.chat.java.models.SharedLocation;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.*;

public interface SharedLocationService {
  @GET("users/locations")
  Call<SharedLocation.ActiveLiveLocationsResponse> getLiveLocations(
      @NotNull @Query("user_id") String userId);

  @PUT("users/location")
  Call<SharedLocation.SharedLocationResponse> updateLiveLocation(
      @NotNull @Query("user_id") String userId,
      @NotNull @Body SharedLocation.SharedLocationRequest sharedLocationRequest);
}
