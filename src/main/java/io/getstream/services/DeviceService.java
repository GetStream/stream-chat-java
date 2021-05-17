package io.getstream.services;

import io.getstream.models.Device.DeviceCreateRequestData;
import io.getstream.models.Device.DeviceListResponse;
import io.getstream.models.framework.StreamResponseObject;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DeviceService {

  @POST("devices")
  Call<StreamResponseObject> create(@NotNull @Body DeviceCreateRequestData deviceCreateRequestData);

  @DELETE("devices")
  Call<StreamResponseObject> delete(
      @NotNull @Query("id") String id, @NotNull @Query("user_id") String userId);

  @GET("devices")
  Call<DeviceListResponse> list(@NotNull @Query("user_id") String userId);
}
