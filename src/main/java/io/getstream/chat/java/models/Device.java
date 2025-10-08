package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.App.PushProviderType;
import io.getstream.chat.java.models.Device.DeviceCreateRequestData.DeviceCreateRequest;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.framework.RequestObjectBuilder;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.DeviceService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Device {
  @Nullable
  @JsonProperty("push_provider")
  private PushProviderType pushProvider;

  @NotNull
  @JsonProperty("id")
  private String id;

  @Nullable
  @JsonProperty("push_provider_name")
  private String pushProviderName;

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

  @Builder
  @Setter
  @Getter
  @EqualsAndHashCode
  public static class DeviceRequestObject {
    @Nullable
    @JsonProperty("push_provider")
    private PushProviderType pushProvider;

    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("push_provider_name")
    private String pushProviderName;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("disabled")
    private Boolean disabled;

    @Nullable
    @JsonProperty("disabled_reason")
    private String disabledReason;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    public static DeviceRequestObject buildFrom(@Nullable Device device) {
      return RequestObjectBuilder.build(DeviceRequestObject.class, device);
    }
  }

  @Builder(
      builderClassName = "DeviceCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class DeviceCreateRequestData {
    @Nullable
    @JsonProperty("push_provider")
    private PushProviderType pushProvider;

    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("push_provider_name")
    private String pushProviderName;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class DeviceCreateRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall(Client client) {
        return client.create(DeviceService.class).create(this.internalBuild());
      }
    }
  }

  @Getter
  @EqualsAndHashCode
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
    protected Call<StreamResponseObject> generateCall(Client client) {
      return client.create(DeviceService.class).delete(id, userId);
    }
  }

  @Getter
  @EqualsAndHashCode
  @RequiredArgsConstructor
  public static class DeviceListRequest extends StreamRequest<DeviceListResponse> {
    @NotNull private String userId;

    @Override
    protected Call<DeviceListResponse> generateCall(Client client) {
      return client.create(DeviceService.class).list(userId);
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
