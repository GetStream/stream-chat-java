package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.stream.exceptions.StreamException;
import io.stream.models.App.AppCheckPushRequestData.AppCheckPushRequest;
import io.stream.models.App.AppCheckSqsRequestData.AppCheckSqsRequest;
import io.stream.models.App.AppUpdateRequestData.AppUpdateRequest;
import io.stream.models.ChannelType.ChannelTypeWithStringCommands;
import io.stream.models.Permission.Resource;
import io.stream.models.User.UserRequestObject;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponse;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.AppService;
import io.stream.services.framework.StreamServiceGenerator;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class App extends StreamResponseObject {
  @Nullable
  @JsonProperty("app")
  private AppConfig app;

  @Data
  @NoArgsConstructor
  public static class APNConfig {
    @NotNull
    @JsonProperty("enabled")
    private Boolean enabled;

    @NotNull
    @JsonProperty("development")
    private Boolean development;

    @Nullable
    @JsonProperty("auth_type")
    private AuthenticationType authType;

    @Nullable
    @JsonProperty("auth_key")
    private byte[] authKey;

    @NotNull
    @JsonProperty("notification_template")
    private String notificationTemplate;

    @Nullable
    @JsonProperty("host")
    private String host;

    @Nullable
    @JsonProperty("bundle_Id")
    private String bundleId;

    @Nullable
    @JsonProperty("team_Id")
    private String teamId;

    @Nullable
    @JsonProperty("key_Id")
    private String keyId;
  }

  @Data
  @NoArgsConstructor
  public static class FirebaseConfig {
    @NotNull
    @JsonProperty("enabled")
    private Boolean enabled;

    @NotNull
    @JsonProperty("notification_template")
    private String notificationTemplate;

    @NotNull
    @JsonProperty("data_template")
    private String dataTemplate;
  }

  @Data
  @NoArgsConstructor
  public static class PushNotificationFields {
    @NotNull
    @JsonProperty("version")
    private PushVersion version;

    @NotNull
    @JsonProperty("apn")
    private APNConfig aPNConfig;

    @NotNull
    @JsonProperty("firebase")
    private FirebaseConfig firebaseConfig;
  }

  @Data
  @NoArgsConstructor
  public static class Policy {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("resources")
    private List<Resource> resources;

    @NotNull
    @JsonProperty("roles")
    private List<String> roles;

    @NotNull
    @JsonProperty("action")
    private Integer action;

    @NotNull
    @JsonProperty("owner")
    private Boolean owner;

    @NotNull
    @JsonProperty("priority")
    private Integer priority;

    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @NotNull
    @JsonProperty("updated_at")
    private Date updatedAt;
  }

  @Data
  @NoArgsConstructor
  public static class AppConfig {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("organization")
    private String organizationName;

    @NotNull
    @JsonProperty("webhook_url")
    private String webhookURL;

    @NotNull
    @JsonProperty("suspended_explanation")
    private String suspendedExplanation;

    @NotNull
    @JsonProperty("push_notifications")
    private PushNotificationFields pushNotifications;

    @NotNull
    @JsonProperty("channel_configs")
    private Map<String, ChannelTypeWithStringCommands> channelConfigs;

    @NotNull
    @JsonProperty("policies")
    private Map<String, Policy[]> policies;

    @NotNull
    @JsonProperty("suspended")
    private Boolean suspended;

    @NotNull
    @JsonProperty("disable_auth_checks")
    private Boolean disableAuth;

    @NotNull
    @JsonProperty("disable_permissions_checks")
    private Boolean disablePermissions;

    @NotNull
    @JsonProperty("multi_tenant_enabled")
    private Boolean multiTenantEnabled;

    @NotNull
    @JsonProperty("permission_version")
    private String permissionVersion;

    @Nullable
    @JsonProperty("user_search_disallowed_roles")
    private List<String> userSearchDisallowedRoles;

    @NotNull
    @JsonProperty("image_moderation_enabled")
    private Boolean imageModerationEnabled;

    @Nullable
    @JsonProperty("image_moderation_labels")
    private List<String> imageModerationLabels;

    @Nullable
    @JsonProperty("custom_action_handler_url")
    private String customActionHandlerUrl;

    @Nullable
    @JsonProperty("enforce_unique_usernames")
    @JsonDeserialize(using = EnforceUniqueUsernamesDeserializer.class)
    private EnforceUniqueUsernames enforceUniqueUsernames;

    @Nullable
    @JsonProperty("sqs_url")
    private String sqsUrl;

    @Nullable
    @JsonProperty("sqs_key")
    private String sqsKey;

    @Nullable
    @JsonProperty("sqs_secret")
    private String sqsSecret;

    @Nullable
    @JsonProperty("file_upload_config")
    private FileUploadConfig fileUploadConfig;

    @Nullable
    @JsonProperty("image_upload_config")
    private FileUploadConfig imageUploadConfig;

    @Nullable
    @JsonProperty("before_message_send_hook_url")
    private String beforeMessageSendHookUrl;

    @Nullable
    @JsonProperty("auto_translation_enabled")
    private Boolean autoTranslationEnabled;
  }

  public enum PermissionVersion {
    @JsonProperty("v1")
    V1,
    @JsonProperty("v2")
    V2
  }

  public enum PushVersion {
    @JsonProperty("v1")
    V1,
    @JsonProperty("v2")
    V2
  }

  public enum AuthenticationType {
    @JsonProperty("certificate")
    CERTIFICATE,
    @JsonProperty("token")
    TOKEN
  }

  public enum EnforceUniqueUsernames {
    @JsonProperty("no")
    NO,
    @JsonProperty("app")
    APP,
    @JsonProperty("team")
    TEAM
  }

  public static class EnforceUniqueUsernamesDeserializer
      extends JsonDeserializer<EnforceUniqueUsernames> {
    @Override
    public EnforceUniqueUsernames deserialize(
        JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {
      String jsonString = jsonParser.readValueAs(String.class);
      if (jsonString == null || jsonString.equals("")) {
        return null;
      }
      for (EnforceUniqueUsernames enumValue : EnforceUniqueUsernames.values()) {
        try {
          if (jsonString.equals(
              EnforceUniqueUsernames.class
                  .getField(enumValue.name())
                  .getAnnotation(JsonProperty.class)
                  .value())) {
            return enumValue;
          }
        } catch (NoSuchFieldException | SecurityException e) {
          throw deserializationContext.instantiationException(
              EnforceUniqueUsernames.class, "Should not happen");
        }
      }
      throw deserializationContext.instantiationException(
          EnforceUniqueUsernames.class,
          "Unparseable value for EnforceUniqueUsernames: " + jsonString);
    }
  }

  @Data
  @NoArgsConstructor
  public static class FileUploadConfig {
    @Nullable
    @JsonProperty("allowed_file_extensions")
    private List<String> allowedFileExtensions;

    @Nullable
    @JsonProperty("blocked_file_extensions")
    private List<String> blockedFileExtensions;

    @Nullable
    @JsonProperty("allowed_mime_types")
    private List<String> allowedMimeTypes;

    @Nullable
    @JsonProperty("blocked_mime_types")
    private List<String> blockedMimeTypes;
  }

  @Data
  @NoArgsConstructor
  public static class DeviceError {
    @NotNull
    @JsonProperty("provider")
    private String provider;

    @NotNull
    @JsonProperty("error_message")
    private String errorMessage;
  }

  @Builder
  public static class FileUploadConfigRequestObject {

    @Nullable
    @JsonProperty("allowed_file_extensions")
    private List<String> allowedFileExtensions;

    @Nullable
    @JsonProperty("blocked_file_extensions")
    private List<String> blockedFileExtensions;

    @Nullable
    @JsonProperty("allowed_mime_types")
    private List<String> allowedMimeTypes;

    @Nullable
    @JsonProperty("blocked_mime_types")
    private List<String> blockedMimeTypes;
  }

  @Builder
  public static class APNConfigRequestObject {
    @Nullable
    @JsonProperty("enabled")
    private Boolean enabled;

    @Nullable
    @JsonProperty("development")
    private Boolean development;

    @Nullable
    @JsonProperty("auth_type")
    private AuthenticationType authType;

    @Nullable
    @JsonProperty("auth_key")
    private byte[] authKey;

    @Nullable
    @JsonProperty("notification_template")
    private String notificationTemplate;

    @Nullable
    @JsonProperty("host")
    private String host;

    @Nullable
    @JsonProperty("bundle_Id")
    private String bundleId;

    @Nullable
    @JsonProperty("team_Id")
    private String teamId;

    @Nullable
    @JsonProperty("key_Id")
    private String keyId;
  }

  @Builder
  public static class FirebaseConfigRequestObject {

    @Nullable
    @JsonProperty("enabled")
    private Boolean enabled;

    @Nullable
    @JsonProperty("notification_template")
    private String notificationTemplate;

    @Nullable
    @JsonProperty("data_template")
    private String dataTemplate;
  }

  @Builder
  public static class PushConfigRequestObject {
    @Nullable
    @JsonProperty("version")
    private PushVersion version;
  }

  public static class AppGetRequest extends StreamRequest<App> {
    @Override
    protected Call<App> generateCall() {
      return StreamServiceGenerator.createService(AppService.class).get();
    }
  }

  @Builder(
      builderClassName = "AppUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class AppUpdateRequestData {
    @Nullable
    @JsonProperty("disable_auth_checks")
    private Boolean disableAuth;

    @Nullable
    @JsonProperty("disable_permissions_checks")
    private Boolean disablePermissions;

    @Nullable
    @JsonProperty("apn_config")
    private APNConfigRequestObject aPNConfig;

    @Nullable
    @JsonProperty("firebase_config")
    private FirebaseConfigRequestObject firebaseConfig;

    @Nullable
    @JsonProperty("push_config")
    private PushConfigRequestObject pushConfig;

    @Nullable
    @JsonProperty("user_search_disallowed_roles")
    private List<String> userSearchDisallowedRoles;

    @Nullable
    @JsonProperty("custom_action_handler_url")
    private String customActionHandlerUrl;

    @Nullable
    @JsonProperty("enforce_unique_usernames")
    private String enforceUniqueUsernames;

    @Nullable
    @JsonProperty("permission_version")
    private PermissionVersion permissionVersion;

    @Nullable
    @JsonProperty("file_upload_config")
    private FileUploadConfigRequestObject fileUploadConfig;

    @Nullable
    @JsonProperty("image_upload_config")
    private FileUploadConfigRequestObject imageUploadConfig;

    @Nullable
    @JsonProperty("before_message_send_hook_url")
    private String beforeMessageSendHookUrl;

    @Nullable
    @JsonProperty("auto_translation_enabled")
    private Boolean autoTranslationEnabled;

    @Nullable
    @JsonProperty("image_moderation_enabled")
    private Boolean imageModerationEnabled;

    @Nullable
    @JsonProperty("image_moderation_labels")
    private List<String> imageModerationLabels;

    @Nullable
    @JsonProperty("sqs_url")
    private String sqsUrl;

    @Nullable
    @JsonProperty("sqs_key")
    private String sqsKey;

    @Nullable
    @JsonProperty("sqs_secret")
    private String sqsSecret;

    @Nullable
    @JsonProperty("webhook_url")
    private String webhookURL;

    @Nullable
    @JsonProperty("multi_tenant_enabled")
    private Boolean multiTenantEnabled;

    public static class AppUpdateRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(AppService.class).update(this.internalBuild());
      }
    }
  }

  public static class AppGetRateLimitsRequest extends StreamRequest<AppGetRateLimitsResponse> {
    @Nullable private Boolean serverSide;

    @Nullable private Boolean android;

    @Nullable private Boolean ios;

    @Nullable private Boolean web;

    @Nullable private List<String> endpoints;

    @Override
    protected Call<AppGetRateLimitsResponse> generateCall() {
      return StreamServiceGenerator.createService(AppService.class)
          .getRateLimits(
              serverSide,
              android,
              ios,
              web,
              endpoints == null ? null : String.join(",", endpoints));
    }
  }

  @Builder(
      builderClassName = "AppCheckSqsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class AppCheckSqsRequestData {
    @NotNull
    @JsonProperty("sqs_url")
    private String sqsUrl;

    @NotNull
    @JsonProperty("sqs_key")
    private String sqsKey;

    @NotNull
    @JsonProperty("sqs_secret")
    private String sqsSecret;

    public static class AppCheckSqsRequest extends StreamRequest<AppCheckSqsResponse> {
      @Override
      protected Call<AppCheckSqsResponse> generateCall() {
        return StreamServiceGenerator.createService(AppService.class)
            .checkSqs(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "AppCheckPushRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class AppCheckPushRequestData {
    @Nullable
    @JsonProperty("message_id")
    private String messageId;

    @Nullable
    @JsonProperty("apn_template")
    private String apnTemplate;

    @Nullable
    @JsonProperty("firebase_template")
    private String firebaseTemplate;

    @Nullable
    @JsonProperty("firebase_data_template")
    private String firebaseDataTemplate;

    @Nullable
    @JsonProperty("skip_devices")
    private Boolean skipDevices;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class AppCheckPushRequest extends StreamRequest<AppCheckPushResponse> {
      @Override
      protected Call<AppCheckPushResponse> generateCall() {
        return StreamServiceGenerator.createService(AppService.class)
            .checkPush(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  public static class AppGetRateLimitsResponse implements StreamResponse {
    @NotNull
    @JsonProperty("server_side")
    private Map<String, RateLimit> serverSide;

    @NotNull
    @JsonProperty("android")
    private Map<String, RateLimit> android;

    @NotNull
    @JsonProperty("ios")
    private Map<String, RateLimit> ios;

    @NotNull
    @JsonProperty("web")
    private Map<String, RateLimit> web;

    @NotNull
    @JsonProperty("duration")
    private String duration;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class AppCheckSqsResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("status")
    private Status status;

    @Nullable
    @JsonProperty("error")
    private String error;

    @Nullable
    @JsonProperty("data")
    private Map<String, Object> data;

    public enum Status {
      @JsonProperty("ok")
      OK,
      @JsonProperty("error")
      ERROR
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class AppCheckPushResponse extends StreamResponseObject {
    @Nullable
    @JsonProperty("device_errors")
    private Map<String, DeviceError> deviceErrors;

    @Nullable
    @JsonProperty("general_errors")
    private List<String> generalErrors;

    @Nullable
    @JsonProperty("skip_devices")
    private Boolean skipDevices;

    @NotNull
    @JsonProperty("rendered_apn_template")
    private String renderedApnTemplate;

    @NotNull
    @JsonProperty("rendered_firebase_template")
    private String renderedFirebaseTemplate;

    @NotNull
    @JsonProperty("rendered_message")
    private Map<String, String> renderedMessage;
  }

  /**
   * Creates a get request.
   *
   * @return the created request
   */
  @NotNull
  public static AppGetRequest get() throws StreamException {
    return new AppGetRequest();
  }

  /**
   * Creates an update request.
   *
   * @return the created request
   */
  @NotNull
  public static AppUpdateRequest update() {
    return new AppUpdateRequest();
  }

  /**
   * Creates a get rate limits request.
   *
   * @return the created request
   */
  @NotNull
  public static AppGetRateLimitsRequest getRateLimits() throws StreamException {
    return new AppGetRateLimitsRequest();
  }

  /**
   * Creates a check SQS request.
   *
   * @return the created request
   */
  @NotNull
  public static AppCheckSqsRequest checkSqs() throws StreamException {
    return new AppCheckSqsRequest();
  }

  /**
   * Creates a check push request.
   *
   * @return the created request
   */
  @NotNull
  public static AppCheckPushRequest checkPush() throws StreamException {
    return new AppCheckPushRequest();
  }
}
