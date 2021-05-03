package io.stream.models;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.exceptions.StreamException;
import io.stream.models.App.AppUpdateRequestData.AppUpdateRequest;
import io.stream.models.ChannelType.ChannelTypeWithStringCommands;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.AppService;
import io.stream.services.framework.StreamServiceGenerator;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    private String enforceUniqueUsernames;

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

    @NotNull
    @JsonProperty("permission_version")
    private String permissionVersion;

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

    @NotNull
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

    private AppUpdateRequestData(AppUpdateRequest appUpdateRequest) {
      this.disableAuth = appUpdateRequest.disableAuth;
      this.disablePermissions = appUpdateRequest.disablePermissions;
      this.aPNConfig = appUpdateRequest.aPNConfig;
      this.firebaseConfig = appUpdateRequest.firebaseConfig;
      this.pushConfig = appUpdateRequest.pushConfig;
      this.userSearchDisallowedRoles = appUpdateRequest.userSearchDisallowedRoles;
      this.customActionHandlerUrl = appUpdateRequest.customActionHandlerUrl;
      this.enforceUniqueUsernames = appUpdateRequest.enforceUniqueUsernames;
      this.permissionVersion = appUpdateRequest.permissionVersion;
      this.fileUploadConfig = appUpdateRequest.fileUploadConfig;
      this.imageUploadConfig = appUpdateRequest.imageUploadConfig;
      this.beforeMessageSendHookUrl = appUpdateRequest.beforeMessageSendHookUrl;
      this.autoTranslationEnabled = appUpdateRequest.autoTranslationEnabled;
      this.imageModerationEnabled = appUpdateRequest.imageModerationEnabled;
      this.imageModerationLabels = appUpdateRequest.imageModerationLabels;
      this.sqsUrl = appUpdateRequest.sqsUrl;
      this.sqsKey = appUpdateRequest.sqsKey;
      this.sqsSecret = appUpdateRequest.sqsSecret;
      this.webhookURL = appUpdateRequest.webhookURL;
      this.multiTenantEnabled = appUpdateRequest.multiTenantEnabled;
    }

    public static class AppUpdateRequest extends StreamRequest<StreamResponseObject> {
      private Boolean disableAuth;
      private Boolean disablePermissions;
      private APNConfigRequestObject aPNConfig;
      private FirebaseConfigRequestObject firebaseConfig;
      private PushConfigRequestObject pushConfig;
      private List<String> userSearchDisallowedRoles;
      private String customActionHandlerUrl;
      private String enforceUniqueUsernames;
      private String permissionVersion;
      private FileUploadConfigRequestObject fileUploadConfig;
      private FileUploadConfigRequestObject imageUploadConfig;
      private String beforeMessageSendHookUrl;
      private Boolean autoTranslationEnabled;
      private Boolean imageModerationEnabled;
      private List<String> imageModerationLabels;
      private String sqsUrl;
      private String sqsKey;
      private String sqsSecret;
      private String webhookURL;
      private Boolean multiTenantEnabled;

      private AppUpdateRequest() {}

      @NotNull
      public AppUpdateRequest disableAuth(@NotNull Boolean disableAuth) {
        this.disableAuth = disableAuth;
        return this;
      }

      @NotNull
      public AppUpdateRequest disablePermissions(@NotNull Boolean disablePermissions) {
        this.disablePermissions = disablePermissions;
        return this;
      }

      @NotNull
      public AppUpdateRequest aPNConfig(@NotNull APNConfigRequestObject aPNConfig) {
        this.aPNConfig = aPNConfig;
        return this;
      }

      @NotNull
      public AppUpdateRequest firebaseConfig(@NotNull FirebaseConfigRequestObject firebaseConfig) {
        this.firebaseConfig = firebaseConfig;
        return this;
      }

      @NotNull
      public AppUpdateRequest pushConfig(@NotNull PushConfigRequestObject pushConfig) {
        this.pushConfig = pushConfig;
        return this;
      }

      @NotNull
      public AppUpdateRequest userSearchDisallowedRoles(
          @NotNull List<String> userSearchDisallowedRoles) {
        this.userSearchDisallowedRoles = userSearchDisallowedRoles;
        return this;
      }

      @NotNull
      public AppUpdateRequest customActionHandlerUrl(@NotNull String customActionHandlerUrl) {
        this.customActionHandlerUrl = customActionHandlerUrl;
        return this;
      }

      @NotNull
      public AppUpdateRequest enforceUniqueUsernames(@NotNull String enforceUniqueUsernames) {
        this.enforceUniqueUsernames = enforceUniqueUsernames;
        return this;
      }

      @NotNull
      public AppUpdateRequest permissionVersion(@NotNull String permissionVersion) {
        this.permissionVersion = permissionVersion;
        return this;
      }

      @NotNull
      public AppUpdateRequest fileUploadConfig(
          @NotNull FileUploadConfigRequestObject fileUploadConfig) {
        this.fileUploadConfig = fileUploadConfig;
        return this;
      }

      @NotNull
      public AppUpdateRequest imageUploadConfig(
          @NotNull FileUploadConfigRequestObject imageUploadConfig) {
        this.imageUploadConfig = imageUploadConfig;
        return this;
      }

      @NotNull
      public AppUpdateRequest beforeMessageSendHookUrl(@NotNull String beforeMessageSendHookUrl) {
        this.beforeMessageSendHookUrl = beforeMessageSendHookUrl;
        return this;
      }

      @NotNull
      public AppUpdateRequest autoTranslationEnabled(@NotNull Boolean autoTranslationEnabled) {
        this.autoTranslationEnabled = autoTranslationEnabled;
        return this;
      }

      @NotNull
      public AppUpdateRequest imageModerationEnabled(@NotNull Boolean imageModerationEnabled) {
        this.imageModerationEnabled = imageModerationEnabled;
        return this;
      }

      @NotNull
      public AppUpdateRequest imageModerationLabels(@NotNull List<String> imageModerationLabels) {
        this.imageModerationLabels = imageModerationLabels;
        return this;
      }

      @NotNull
      public AppUpdateRequest sqsUrl(@NotNull String sqsUrl) {
        this.sqsUrl = sqsUrl;
        return this;
      }

      @NotNull
      public AppUpdateRequest sqsKey(@NotNull String sqsKey) {
        this.sqsKey = sqsKey;
        return this;
      }

      @NotNull
      public AppUpdateRequest sqsSecret(@NotNull String sqsSecret) {
        this.sqsSecret = sqsSecret;
        return this;
      }

      @NotNull
      public AppUpdateRequest webhookURL(@NotNull String webhookURL) {
        this.webhookURL = webhookURL;
        return this;
      }

      @NotNull
      public AppUpdateRequest multiTenantEnabled(@NotNull Boolean multiTenantEnabled) {
        this.multiTenantEnabled = multiTenantEnabled;
        return this;
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(AppService.class)
            .update(new AppUpdateRequestData(this));
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
