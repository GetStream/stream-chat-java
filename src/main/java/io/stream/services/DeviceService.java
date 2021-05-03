package io.stream.services;

import org.jetbrains.annotations.NotNull;
import io.stream.models.Device.DeviceCreateRequestData;
import io.stream.models.Device.DeviceListResponse;
import io.stream.models.framework.StreamResponseObject;
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
