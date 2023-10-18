package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.getstream.chat.java.models.App.AppCheckPushRequestData.AppCheckPushRequest;
import io.getstream.chat.java.models.App.AppCheckSqsRequestData.AppCheckSqsRequest;
import io.getstream.chat.java.models.App.AppCheckSnsRequestData.AppCheckSnsRequest;
import io.getstream.chat.java.models.App.AppUpdateRequestData.AppUpdateRequest;
import io.getstream.chat.java.models.App.PushProviderRequestData.PushProviderRequest;
import io.getstream.chat.java.models.ChannelType.ChannelTypeWithStringCommands;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.framework.RequestObjectBuilder;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponse;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.AppService;
import io.getstream.chat.java.services.framework.Client;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.*;
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

    @NotNull
    @JsonProperty("apn_template")
    private String apnTemplate;
  }

  @Data
  @NoArgsConstructor
  public static class HuaweiConfig {
    @NotNull
    @JsonProperty("enabled")
    private Boolean enabled;
  }

  public enum PushProviderType {
    @JsonProperty("firebase")
    Firebase,
    @JsonProperty("apn")
    Apn,
    @JsonProperty("xiaomi")
    Xiaomi,
    @JsonProperty("huawei")
    Huawei,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  @Data
  @NoArgsConstructor
  public static class PushNotificationFields {
    @NotNull
    @JsonProperty("version")
    private PushVersion version;

    @NotNull
    @JsonProperty("offline_only")
    private Boolean offlineOnly;

    @NotNull
    @JsonProperty("apn")
    private APNConfig aPNConfig;

    @NotNull
    @JsonProperty("firebase")
    private FirebaseConfig firebaseConfig;

    @NotNull
    @JsonProperty("huawei")
    private HuaweiConfig huaweiConfig;

    @NotNull
    @JsonProperty("providers")
    private List<PushProvider> providers;
  }

  @Data
  @NoArgsConstructor
  public static class Policy {
    @NotNull
    @JsonProperty("name")
    private String name;

    @Nullable
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

    @NotNull
    @JsonProperty("async_url_enrich_enabled")
    private Boolean asyncURLEnrichEnabled;

    @NotNull
    @JsonProperty("reminders_interval")
    private int remindersInterval;

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
    @JsonProperty("sns_topic_arn")
    private String snsTopicArn;

    @Nullable
    @JsonProperty("sns_key")
    private String snsKey;

    @Nullable
    @JsonProperty("sns_secret")
    private String snsSecret;

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

    @Nullable
    @JsonProperty("revoke_tokens_issued_before")
    private Date revokeTokensIssuedBefore;

    @Nullable
    @JsonProperty("grants")
    private Map<String, List<String>> grants;

    @Nullable
    @JsonProperty("webhook_events")
    private List<String> webhookEvents;
  }

  public enum PermissionVersion {
    @JsonProperty("v1")
    V1,
    @JsonProperty("v2")
    V2,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  public enum PushVersion {
    @JsonProperty("v1")
    V1,
    @JsonProperty("v2")
    V2,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  public enum AuthenticationType {
    @JsonProperty("certificate")
    CERTIFICATE,
    @JsonProperty("token")
    TOKEN,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  public enum EnforceUniqueUsernames {
    @JsonProperty("no")
    NO,
    @JsonProperty("app")
    APP,
    @JsonProperty("team")
    TEAM,
    @JsonEnumDefaultValue
    UNKNOWN
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
    @JsonProperty("provider_name")
    private String providerName;

    @NotNull
    @JsonProperty("error_message")
    private String errorMessage;
  }

  @Builder
  @Setter
  public static class AsyncModerationCallback {
    @Nullable
    @JsonProperty("mode")
    private String mode;

    @Nullable
    @JsonProperty("server_url")
    private String serverUrl;
  }

  @Builder
  @Setter
  public static class AsyncModerationConfigRequestObject {
    @Nullable
    @JsonProperty("callback")
    private AsyncModerationCallback callback;

    @Nullable
    @JsonProperty("timeout_ms")
    private Number timeoutMs;
  }

  @Builder
  @Setter
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

    @Nullable
    public static FileUploadConfigRequestObject buildFrom(
        @Nullable FileUploadConfig fileUploadConfig) {
      return RequestObjectBuilder.build(FileUploadConfigRequestObject.class, fileUploadConfig);
    }
  }

  @Builder
  @Setter
  public static class APNConfigRequestObject {
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

    @Nullable
    @JsonProperty("p12_cert")
    private String p12Cert;

    @Nullable
    public static APNConfigRequestObject buildFrom(@Nullable APNConfig aPNConfig) {
      return RequestObjectBuilder.build(APNConfigRequestObject.class, aPNConfig);
    }
  }

  @Builder
  @Setter
  public static class FirebaseConfigRequestObject {

    @Nullable
    @JsonProperty("server_key")
    private String serverKey;

    @Nullable
    @JsonProperty("credentials_json")
    private String credentialsJson;

    @Nullable
    @JsonProperty("notification_template")
    private String notificationTemplate;

    @Nullable
    @JsonProperty("data_template")
    private String dataTemplate;

    @Nullable
    @JsonProperty("apn_template")
    private String apnTemplate;

    @Nullable
    public static FirebaseConfigRequestObject buildFrom(@Nullable FirebaseConfig firebaseConfig) {
      return RequestObjectBuilder.build(FirebaseConfigRequestObject.class, firebaseConfig);
    }
  }

  @Builder
  @Setter
  public static class HuaweiConfigRequestObject {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("secret")
    private String secret;
  }

  @Builder
  @Setter
  public static class PushConfigRequestObject {
    @Nullable
    @JsonProperty("version")
    private PushVersion version;

    @Nullable
    @JsonProperty("offline_only")
    private Boolean offlineOnly;

    @Nullable
    public static PushConfigRequestObject buildFrom(@Nullable PushVersion pushVersion) {
      return builder().version(pushVersion).build();
    }
  }

  public static class AppGetRequest extends StreamRequest<App> {
    @Override
    protected Call<App> generateCall(Client client) {
      return client.create(AppService.class).get();
    }
  }

  public static class ListPushProvidersRequest extends StreamRequest<ListPushProviderResponse> {
    @Override
    protected Call<ListPushProviderResponse> generateCall(Client client) {
      return client.create(AppService.class).listPushProviders();
    }
  }

  public static class DeletePushProviderRequest extends StreamRequest<StreamResponseObject> {
    private String providerType;
    private String name;

    public DeletePushProviderRequest(@NotNull String providerType, @NotNull String name) {
      this.providerType = providerType;
      this.name = name;
    }

    @Override
    protected Call<StreamResponseObject> generateCall(Client client) {
      return client.create(AppService.class).deletePushProvider(this.providerType, this.name);
    }
  }

  @Builder(
      builderClassName = "AppUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class AppUpdateRequestData {
    @Nullable
    @JsonProperty("disable_auth_checks")
    @JsonInclude(Include.NON_NULL)
    private Boolean disableAuthChecks;

    @Nullable
    @JsonProperty("disable_permissions_checks")
    @JsonInclude(Include.NON_NULL)
    private Boolean disablePermissionsChecks;

    @Nullable
    @JsonProperty("apn_config")
    @JsonInclude(Include.NON_NULL)
    private APNConfigRequestObject aPNConfig;

    @Nullable
    @JsonProperty("firebase_config")
    @JsonInclude(Include.NON_NULL)
    private FirebaseConfigRequestObject firebaseConfig;

    @Nullable
    @JsonProperty("huawei_config")
    @JsonInclude(Include.NON_NULL)
    private HuaweiConfigRequestObject huaweiConfig;

    @Nullable
    @JsonProperty("push_config")
    @JsonInclude(Include.NON_NULL)
    private PushConfigRequestObject pushConfig;

    @Nullable
    @JsonProperty("user_search_disallowed_roles")
    @JsonInclude(Include.NON_NULL)
    private List<String> userSearchDisallowedRoles;

    @Nullable
    @JsonProperty("custom_action_handler_url")
    @JsonInclude(Include.NON_NULL)
    private String customActionHandlerUrl;

    @NotNull
    @JsonProperty("reminders_interval")
    private int remindersInterval;

    @Nullable
    @JsonProperty("enforce_unique_usernames")
    @JsonInclude(Include.NON_NULL)
    private String enforceUniqueUsernames;

    @Nullable
    @JsonProperty("permission_version")
    @JsonInclude(Include.NON_NULL)
    private PermissionVersion permissionVersion;

    @Nullable
    @JsonProperty("file_upload_config")
    @JsonInclude(Include.NON_NULL)
    private FileUploadConfigRequestObject fileUploadConfig;

    @Nullable
    @JsonProperty("image_upload_config")
    @JsonInclude(Include.NON_NULL)
    private FileUploadConfigRequestObject imageUploadConfig;

    @Nullable
    @JsonProperty("before_message_send_hook_url")
    @JsonInclude(Include.NON_NULL)
    private String beforeMessageSendHookUrl;

    @Nullable
    @JsonProperty("auto_translation_enabled")
    @JsonInclude(Include.NON_NULL)
    private Boolean autoTranslationEnabled;

    @Nullable
    @JsonProperty("image_moderation_enabled")
    @JsonInclude(Include.NON_NULL)
    private Boolean imageModerationEnabled;

    @Nullable
    @JsonProperty("image_moderation_labels")
    @JsonInclude(Include.NON_NULL)
    private List<String> imageModerationLabels;

    @Nullable
    @JsonProperty("async_url_enrich_enabled")
    @JsonInclude(Include.NON_NULL)
    private Boolean asyncURLEnrichEnabled;

    @Nullable
    @JsonProperty("async_moderation_config")
    @JsonInclude(Include.NON_NULL)
    private AsyncModerationConfigRequestObject asyncModerationConfig;

    @Nullable
    @JsonProperty("sqs_url")
    @JsonInclude(Include.NON_NULL)
    private String sqsUrl;

    @Nullable
    @JsonProperty("sqs_key")
    @JsonInclude(Include.NON_NULL)
    private String sqsKey;

    @Nullable
    @JsonProperty("sqs_secret")
    @JsonInclude(Include.NON_NULL)
    private String sqsSecret;

    @Nullable
    @JsonProperty("sns_topic_arn")
    @JsonInclude(Include.NON_NULL)
    private String snsTopicArn;

    @Nullable
    @JsonProperty("sns_key")
    @JsonInclude(Include.NON_NULL)
    private String snsKey;

    @Nullable
    @JsonProperty("sns_secret")
    @JsonInclude(Include.NON_NULL)
    private String snsSecret;

    @Nullable
    @JsonProperty("webhook_url")
    @JsonInclude(Include.NON_NULL)
    private String webhookURL;

    @Nullable
    @JsonProperty("webhook_events")
    private List<String> webhookEvents;

    @Nullable
    @JsonProperty("multi_tenant_enabled")
    @JsonInclude(Include.NON_NULL)
    private Boolean multiTenantEnabled;

    @Nullable
    @JsonProperty("revoke_tokens_issued_before")
    // This field can be sent as null
    private Date revokeTokensIssuedBefore;

    @Nullable
    @JsonProperty("channel_hide_members_only")
    @JsonInclude(Include.NON_NULL)
    private Boolean channelHideMembersOnly;

    @Nullable
    @JsonProperty("migrate_permissions_to_v2")
    @JsonInclude(Include.NON_NULL)
    private Boolean migratePermissionsToV2;

    @Nullable
    @JsonProperty("grants")
    private Map<String, List<String>> grants;

    public static class AppUpdateRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall(Client client) {
        return client.create(AppService.class).update(this.internalBuild());
      }
    }
  }

  public static class AppGetRateLimitsRequest extends StreamRequest<AppGetRateLimitsResponse> {
    @Nullable private Boolean serverSide;

    @Nullable private Boolean android;

    @Nullable private Boolean ios;

    @Nullable private Boolean web;

    @Nullable private List<String> endpoints = new ArrayList<>();

    @NotNull
    public AppGetRateLimitsRequest serverSide(@NotNull Boolean serverSide) {
      this.serverSide = serverSide;
      return this;
    }

    @NotNull
    public AppGetRateLimitsRequest android(@NotNull Boolean android) {
      this.android = android;
      return this;
    }

    @NotNull
    public AppGetRateLimitsRequest ios(@NotNull Boolean ios) {
      this.ios = ios;
      return this;
    }

    @NotNull
    public AppGetRateLimitsRequest web(@NotNull Boolean web) {
      this.web = web;
      return this;
    }

    @NotNull
    public AppGetRateLimitsRequest endpoints(@NotNull List<String> endpoints) {
      this.endpoints = endpoints;
      return this;
    }

    @NotNull
    public AppGetRateLimitsRequest endpoint(@NotNull String endpoint) {
      this.endpoints.add(endpoint);
      return this;
    }

    @Override
    protected Call<AppGetRateLimitsResponse> generateCall(Client client) {
      return client
          .create(AppService.class)
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
    @Nullable
    @JsonProperty("sqs_url")
    private String sqsUrl;

    @Nullable
    @JsonProperty("sqs_key")
    private String sqsKey;

    @Nullable
    @JsonProperty("sqs_secret")
    private String sqsSecret;

    public static class AppCheckSqsRequest extends StreamRequest<AppCheckSqsResponse> {
      @Override
      protected Call<AppCheckSqsResponse> generateCall(Client client) {
        return client.create(AppService.class).checkSqs(this.internalBuild());
      }
    }
  }

  @Builder(
      builderClassName = "AppCheckSnsRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class AppCheckSnsRequestData {
    @Nullable
    @JsonProperty("sns_topic_arn")
    private String snsTopicArn;

    @Nullable
    @JsonProperty("sns_key")
    private String snsKey;

    @Nullable
    @JsonProperty("sns_secret")
    private String snsSecret;

    public static class AppCheckSnsRequest extends StreamRequest<AppCheckSnsResponse> {
      @Override
      protected Call<AppCheckSnsResponse> generateCall(Client client) {
        return client.create(AppService.class).checkSns(this.internalBuild());
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
    @JsonProperty("push_provider_name")
    private String pushProviderName;

    @Nullable
    @JsonProperty("push_provider_type")
    private PushProviderType pushProviderType;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    public static class AppCheckPushRequest extends StreamRequest<AppCheckPushResponse> {
      @Override
      protected Call<AppCheckPushResponse> generateCall(Client client) {
        return client.create(AppService.class).checkPush(this.internalBuild());
      }
    }
  }

  @AllArgsConstructor
  public static class AppRevokeTokensRequest extends StreamRequest<StreamResponseObject> {
    @Nullable private Date revokeTokensIssuedBefore;

    @Override
    protected Call<StreamResponseObject> generateCall(Client client) {
      return new AppUpdateRequest()
          .revokeTokensIssuedBefore(revokeTokensIssuedBefore)
          .generateCall(client);
    }
  }

  @Data
  @NoArgsConstructor
  public static class PushProvider {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("type")
    private PushProviderType type;

    @Nullable
    @JsonProperty("description")
    private String description;

    @Nullable
    @JsonProperty("disabled_at")
    private Date disabledAt;

    @Nullable
    @JsonProperty("disabled_reason")
    private String disabledReason;

    @Nullable
    @JsonProperty("apn_auth_key")
    private String apnAuthKey;

    @Nullable
    @JsonProperty("apn_key_id")
    private String apnKeyId;

    @Nullable
    @JsonProperty("apn_team_id")
    private String apnTeamId;

    @Nullable
    @JsonProperty("apn_topic")
    private String apnTopic;

    @Nullable
    @JsonProperty("firebase_credentials")
    private String firebaseCredentials;

    @Nullable
    @JsonProperty("firebase_apn_template")
    private String firebaseApnTemplate;

    @Nullable
    @JsonProperty("huawei_app_id")
    private String huaweiAppId;

    @Nullable
    @JsonProperty("huawei_app_secret")
    private String huaweiAppSecret;

    @Nullable
    @JsonProperty("xiaomi_package_name")
    private String xiaomiPackageName;

    @Nullable
    @JsonProperty("xiaomi_app_secret")
    private String xiaomiAppSecret;

    @Nullable
    @JsonProperty("created_at")
    private Date createdAt;

    @Nullable
    @JsonProperty("updated_at")
    private Date updatedAt;
  }

  @Builder(
      builderClassName = "PushProviderRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class PushProviderRequestData {
    @JsonProperty("push_provider")
    private PushProvider pushProvider;

    public static class PushProviderRequest extends StreamRequest<UpsertPushProviderResponse> {
      @Override
      protected Call<UpsertPushProviderResponse> generateCall(Client client) {
        return client.create(AppService.class).upsertPushProvider(this.internalBuild());
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
      ERROR,
      @JsonEnumDefaultValue
      UNKNOWN
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class AppCheckSnsResponse extends StreamResponseObject {
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
      ERROR,
      @JsonEnumDefaultValue
      UNKNOWN
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

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class UpsertPushProviderResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("push_provider")
    private PushProvider pushProvider;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ListPushProviderResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("push_providers")
    private List<PushProvider> pushProviders;
  }

  /**
   * Creates a get request.
   *
   * @return the created request
   */
  @NotNull
  public static AppGetRequest get() {
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
  public static AppGetRateLimitsRequest getRateLimits() {
    return new AppGetRateLimitsRequest();
  }

  /**
   * Creates a check SQS request.
   *
   * @return the created request
   */
  @NotNull
  public static AppCheckSqsRequest checkSqs() {
    return new AppCheckSqsRequest();
  }

  /**
   * Creates a check SNS request.
   *
   * @return the created request
   */
  @NotNull
  public static AppCheckSnsRequest checkSns() {
    return new AppCheckSnsRequest();
  }

  /**
   * Creates a check push request.
   *
   * @return the created request
   */
  @NotNull
  public static AppCheckPushRequest checkPush() {
    return new AppCheckPushRequest();
  }

  /**
   * Creates a revoke tokens request
   *
   * @param revokeTokensIssuedBefore the limit date to revoke tokens
   * @return the created request
   */
  @NotNull
  public static AppRevokeTokensRequest revokeTokens(@Nullable Date revokeTokensIssuedBefore) {
    return new AppRevokeTokensRequest(revokeTokensIssuedBefore);
  }

  /**
   * Creates an upsert push provider request
   *
   * @return the created request
   */
  @NotNull
  public static PushProviderRequest upsertPushProvider() {
    return new PushProviderRequest();
  }

  /**
   * Creates a list push providers request
   *
   * @return the created request
   */
  @NotNull
  public static ListPushProvidersRequest listPushProviders() {
    return new ListPushProvidersRequest();
  }

  /**
   * Creates a delete push provider request
   *
   * @param providerType push provider type
   * @param name push provider name
   * @return the created request
   */
  @NotNull
  public static DeletePushProviderRequest deletePushProvider(
      @NotNull String providerType, @NotNull String name) {
    return new DeletePushProviderRequest(providerType, name);
  }

  /**
   * Validates if hmac signature is correct for message body.
   *
   * @param body raw body from http request converted to a string.
   * @param signature the signature provided in X-Signature header
   * @return true if the signature is valid
   */
  public boolean verifyWebhook(@NotNull String body, @NotNull String signature) {
    return verifyWebhookSignature(body, signature);
  }

  /**
   * Validates if hmac signature is correct for message body.
   *
   * @param apiSecret the secret key
   * @param body raw body from http request converted to a string.
   * @param signature the signature provided in X-Signature header
   * @return true if the signature is valid
   */
  public static boolean verifyWebhookSignature(
      @NotNull String apiSecret, @NotNull String body, @NotNull String signature) {
    try {
      Key sk = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256");
      Mac mac = Mac.getInstance(sk.getAlgorithm());
      mac.init(sk);
      final byte[] hmac = mac.doFinal(body.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(hmac).equals(signature);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Should not happen. Could not find HmacSHA256", e);
    } catch (InvalidKeyException e) {
      throw new IllegalStateException("error building signature, invalid key", e);
    }
  }

  /**
   * Validates if hmac signature is correct for message body.
   *
   * @param body the message body
   * @param signature the signature provided in X-Signature header
   * @return true if the signature is valid
   */
  public static boolean verifyWebhookSignature(@NotNull String body, @NotNull String signature) {
    String apiSecret = Client.getInstance().getApiSecret();
    return verifyWebhookSignature(apiSecret, body, signature);
  }

  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder(2 * hash.length);
    for (byte b : hash) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }
}
