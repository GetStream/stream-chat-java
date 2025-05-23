package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.StreamClient;
import io.getstream.chat.java.exceptions.StreamException;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Response;

@Data
@Builder
public class SharedLocation {
  @JsonProperty("channel_cid")
  private String channelCid;

  @JsonProperty("created_at")
  private Date createdAt;

  @JsonProperty("created_by_device_id")
  private String createdByDeviceId;

  @JsonProperty("end_at")
  private Date endAt;

  private Double latitude;
  private Double longitude;

  @JsonProperty("message_id")
  private String messageId;

  @JsonProperty("updated_at")
  private Date updatedAt;

  @JsonProperty("user_id")
  private String userId;

  @Data
  @Builder
  public static class SharedLocationRequest {
    @JsonProperty("created_by_device_id")
    private String createdByDeviceId;

    @Nullable
    @JsonProperty("end_at")
    private String endAt;

    @Nullable private Double latitude;

    @Nullable private Double longitude;
  }

  @Data
  @Builder
  public static class SharedLocationResponse {
    @JsonProperty("created_by_device_id")
    private String createdByDeviceId;

    @JsonProperty("end_at")
    private String endAt;

    private Double latitude;
    private Double longitude;
  }

  @Data
  @Builder
  public static class ActiveLiveLocationsResponse {
    @JsonProperty("active_live_locations")
    private List<SharedLocation> activeLiveLocations;
  }

  public static UpdateLocationRequest updateLocation() {
    return new UpdateLocationRequest();
  }

  public static GetLocationsRequest getLocations() {
    return new GetLocationsRequest();
  }

  public static class UpdateLocationRequest {
    private SharedLocationRequest request;

    public UpdateLocationRequest request(@NotNull SharedLocationRequest request) {
      this.request = request;
      return this;
    }

    public SharedLocationResponse request() throws StreamException {
      Response<SharedLocationResponse> response =
          StreamClient.getInstance()
              .getSharedLocationService()
              .updateLiveLocation(request)
              .execute();

      if (!response.isSuccessful()) {
        throw new StreamException("Failed to update live location: " + response.code());
      }

      return response.body();
    }
  }

  public static class GetLocationsRequest {
    public ActiveLiveLocationsResponse request() throws StreamException {
      Response<ActiveLiveLocationsResponse> response =
          StreamClient.getInstance().getSharedLocationService().getLiveLocations().execute();

      if (!response.isSuccessful()) {
        throw new StreamException("Failed to get live locations: " + response.code());
      }

      return response.body();
    }
  }
}
