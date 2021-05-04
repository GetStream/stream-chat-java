package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.exceptions.StreamException;
import io.stream.models.App.AppUpdateRequestData.AppUpdateRequest;
import io.stream.models.ChannelType.ChannelTypeWithStringCommands;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.AppService;
import io.stream.services.framework.StreamServiceGenerator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@EqualsAndHashCode(callSuper = true)
public class App extends StreamResponseObject {
  @Nullable
  @JsonProperty("app")
  private AppConfig app;

  public App() {}

  @Data
  public static class APNConfig {
    @NotNull
    @JsonProperty("enabled")
    private Boolean enabled;

    @NotNull
    @JsonProperty("development")
    private Boolean development;

    @Nullable
    @JsonProperty("auth_type")
    private String authType;

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

    public APNConfig() {}
  }

  @Data
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

    public FirebaseConfig() {}
  }

  @Data
  public static class PushNotificationFields {
    @NotNull private String version;

    @NotNull
    @JsonProperty("apn")
    private APNConfig aPNConfig;

    @NotNull
    @JsonProperty("firebase")
    private FirebaseConfig firebaseConfig;

    public PushNotificationFields() {}
  }

  @Data
  public static class Policy {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("resources")
    private List<String> resources;

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

    public Policy() {}
  }

  @Data
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

    public AppConfig() {}
  }

  public enum PermissionVersion {
    @JsonProperty("v1")
    V1,
    @JsonProperty("v2")
    V2
  }

  public enum EnforceUniqueUsernames {
    @JsonProperty("no")
    NO,
    @JsonProperty("app")
    APP,
    @JsonProperty("team")
    TEAM
  }

  @Data
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

    public FileUploadConfig() {}
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
    @NotNull
    @JsonProperty("enabled")
    private Boolean enabled;

    @NotNull
    @JsonProperty("development")
    private Boolean development;

    @Nullable
    @JsonProperty("auth_type")
    private String authType;

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

  @Builder
  public static class FirebaseConfigRequestObject {

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

  @Builder
  public static class PushConfigRequestObject {
    @Nullable
    @JsonProperty("version")
    private String version;
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
}
