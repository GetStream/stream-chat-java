package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Device.DeviceCreateRequestData.DeviceCreateRequest;
import io.stream.models.User.UserRequestObject;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.DeviceService;
import io.stream.services.framework.StreamServiceGenerator;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Device {
  @Nullable
  @JsonProperty("push_provider")
  private PushProvider pushProvider;

  @NotNull
  @JsonProperty("id")
  private String id;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @Nullable
  @JsonProperty("disabled")
  private Boolean disabled;

  @Nullable
  @JsonProperty("disabled_reason")
  private String disabledReason;

  @NotNull
  @JsonProperty("user_id")
  private String userId;

  public enum PushProvider {
    @JsonProperty("firebase")
    FIREBASE,
    @JsonProperty("apn")
    APN
  }

  @Builder
  public static class DeviceRequestObject {
    @Nullable
    @JsonProperty("push_provider")
    private PushProvider pushProvider;

    @NotNull
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("disabled")
    private Boolean disabled;

    @Nullable
    @JsonProperty("disabled_reason")
    private String disabledReason;

    @NotNull
    @JsonProperty("user_id")
    private String userId;
  }

  @Builder(
      builderClassName = "DeviceCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class DeviceCreateRequestData {
    @NotNull
    @JsonProperty("push_provider")
    private PushProvider pushProvider;

    @NotNull
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class DeviceCreateRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(DeviceService.class)
            .create(this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class DeviceDeleteRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String id;

    @NotNull private String userId;

    @NotNull
    public DeviceDeleteRequest id(@NotNull String id) {
      this.id = id;
      return this;
    }

    @NotNull
    public DeviceDeleteRequest userId(@NotNull String userId) {
      this.userId = userId;
      return this;
    }

    @Override
    protected Call<StreamResponseObject> generateCall() {
      return StreamServiceGenerator.createService(DeviceService.class).delete(id, userId);
    }
  }

  @RequiredArgsConstructor
  public static class DeviceListRequest extends StreamRequest<DeviceListResponse> {
    @NotNull private String userId;

    @Override
    protected Call<DeviceListResponse> generateCall() {
      return StreamServiceGenerator.createService(DeviceService.class).list(userId);
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class DeviceListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("devices")
    private List<Device> devices;
  }

  /**
   * Creates a create request
   *
   * @return the created request
   */
  @NotNull
  public static DeviceCreateRequest create() {
    return new DeviceCreateRequest();
  }

  /**
   * Creates a delete request
   *
   * @param id the device id
   * @param userId the user id
   * @return the created request
   */
  @NotNull
  public static DeviceDeleteRequest delete(@NotNull String id, @NotNull String userId) {
    return new DeviceDeleteRequest(id, userId);
  }

  /**
   * Creates a list request
   *
   * @param userId the user id
   * @return the created request
   */
  @NotNull
  public static DeviceListRequest list(@NotNull String userId) {
    return new DeviceListRequest(userId);
  }
}
