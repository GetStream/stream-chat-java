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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
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

  public Device() {}

  public enum PushProvider {
    @JsonProperty("firebase")
    FIREBASE,
    @JsonProperty("apn")
    APN
  }

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

    private DeviceCreateRequestData(DeviceCreateRequest deviceCreateRequest) {
      this.pushProvider = deviceCreateRequest.pushProvider;
      this.id = deviceCreateRequest.id;
      this.userId = deviceCreateRequest.userId;
      this.user = deviceCreateRequest.user;
    }

    public static final class DeviceCreateRequest extends StreamRequest<StreamResponseObject> {
      private PushProvider pushProvider;
      private String id;
      private String userId;
      private UserRequestObject user;

      private DeviceCreateRequest() {}

      @NotNull
      public DeviceCreateRequest pushProvider(@NotNull PushProvider pushProvider) {
        this.pushProvider = pushProvider;
        return this;
      }

      @NotNull
      public DeviceCreateRequest id(@NotNull String id) {
        this.id = id;
        return this;
      }

      @NotNull
      public DeviceCreateRequest userId(@NotNull String userId) {
        this.userId = userId;
        return this;
      }

      @NotNull
      public DeviceCreateRequest user(@NotNull UserRequestObject user) {
        this.user = user;
        return this;
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(DeviceService.class)
            .create(new DeviceCreateRequestData(this));
      }
    }
  }

  public static final class DeviceDeleteRequest extends StreamRequest<StreamResponseObject> {
    private String id;
    private String userId;

    private DeviceDeleteRequest(@NotNull String id, @NotNull String userId) {
      this.id = id;
      this.userId = userId;
    }

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

  public static class DeviceListRequest extends StreamRequest<DeviceListResponse> {
    private String userId;

    private DeviceListRequest(@NotNull String userId) {
      this.userId = userId;
    }

    @Override
    protected Call<DeviceListResponse> generateCall() {
      return StreamServiceGenerator.createService(DeviceService.class).list(userId);
    }
  }

  @Data
  @EqualsAndHashCode(callSuper = true)
  public static class DeviceListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("devices")
    private List<Device> devices;

    public DeviceListResponse() {}
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
