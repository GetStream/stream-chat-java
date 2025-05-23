package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.*;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.SharedLocationService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
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
  @NoArgsConstructor
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
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class SharedLocationResponse extends StreamResponseObject {
    @JsonProperty("created_by_device_id")
    private String createdByDeviceId;

    @JsonProperty("end_at")
    private String endAt;

    private Double latitude;
    private Double longitude;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ActiveLiveLocationsResponse extends StreamResponseObject {
    @JsonProperty("active_live_locations")
    private List<SharedLocation> activeLiveLocations;
  }

  @Builder(
      builderClassName = "UpdateLocationRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class UpdateLocationRequestData {
    @NotNull
    @JsonProperty("request")
    private SharedLocationRequest request;

    public static class UpdateLocationRequest extends StreamRequest<SharedLocationResponse> {
      @Override
      protected Call<SharedLocationResponse> generateCall(Client client) {
        return client.create(SharedLocationService.class).updateLiveLocation(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "GetLocationsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class GetLocationsRequestData {
    public static class GetLocationsRequest extends StreamRequest<ActiveLiveLocationsResponse> {
      @Override
      protected Call<ActiveLiveLocationsResponse> generateCall(Client client) {
        return client.create(SharedLocationService.class).getLiveLocations();
      }
    }
  }

  /**
   * Creates an update location request
   *
   * @return the created request
   */
  @NotNull
  public static UpdateLocationRequest updateLocation() {
    return new UpdateLocationRequest();
  }

  /**
   * Creates a get locations request
   *
   * @return the created request
   */
  @NotNull
  public static GetLocationsRequest getLocations() {
    return new GetLocationsRequest();
  }
}
