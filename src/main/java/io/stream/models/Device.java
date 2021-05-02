package io.stream.models;

import java.util.Date;
import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Device {
  @Nullable
  @JsonProperty("push_provider")
  private PushProvider pushProvider;

  @Nullable
  @JsonProperty("id")
  private String id;

  @Nullable
  @JsonProperty("created_at")
  private Date created_at;

  @Nullable
  @JsonProperty("disabled")
  private Boolean disabled;

  @Nullable
  @JsonProperty("disabled_reason")
  private String disabled_reason;

  @Nullable
  @JsonProperty("user_id")
  private String user_id;

  public Device() {}

  public enum PushProvider {
    @JsonProperty("firebase")
    FIREBASE,
    @JsonProperty("apn")
    APN
  }
}
