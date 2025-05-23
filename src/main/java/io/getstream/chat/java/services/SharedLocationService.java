package io.getstream.chat.java.services;

import io.getstream.chat.java.models.SharedLocation;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.*;

public interface SharedLocationService {
  @GET("users/locations")
  Call<SharedLocation.ActiveLiveLocationsResponse> getLiveLocations();

  @PUT("users/location")
  Call<SharedLocation.SharedLocationResponse> updateLiveLocation(
      @NotNull @Body SharedLocation.SharedLocationRequest sharedLocationRequest);
}
