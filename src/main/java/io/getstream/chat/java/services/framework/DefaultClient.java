package io.getstream.chat.java.services.framework;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DefaultClient implements Client {
  public static final String API_KEY_PROP_NAME = "io.getstream.chat.apiKey";
  public static final String API_SECRET_PROP_NAME = "io.getstream.chat.apiSecret";
  public static final String API_TIMEOUT_PROP_NAME = "io.getstream.chat.timeout";
  public static final String API_URL_PROP_NAME = "io.getstream.chat.url";

  private static final String API_DEFAULT_URL = "https://chat.stream-io-api.com";
  private static volatile DefaultClient defaultInstance;
  @NotNull private OkHttpClient okHttpClient;
  @NotNull private Retrofit retrofit;
  @NotNull private UserServiceFactory serviceFactory;
  @NotNull private UserServiceFactory serviceFactory2;
  @NotNull private UserServiceFactory serviceFactory3;
  @NotNull private final String apiSecret;
  @NotNull private final String apiKey;
  @NotNull private final Properties extendedProperties;

  public static DefaultClient getInstance() {
    if (defaultInstance == null) {
      synchronized (DefaultClient.class) {
        if (defaultInstance == null) {
          defaultInstance = new DefaultClient();
        }
      }
    }

    return defaultInstance;
  }

  public static void setInstance(@NotNull DefaultClient instance) {
    defaultInstance = instance;
  }

  public DefaultClient() {
    this(System.getProperties());
  }

  public DefaultClient(Properties properties) {
    extendedProperties = extendProperties(properties);
    var apiKey = extendedProperties.get(API_KEY_PROP_NAME);
    var apiSecret = extendedProperties.get(API_SECRET_PROP_NAME);

    if (apiSecret == null) {
      throw new IllegalStateException(
          "Missing Stream API secret. Please set STREAM_SECRET environment variable or System"
              + " property");
    }

    if (apiKey == null) {
      throw new IllegalStateException(
          "Missing Stream API key. Please set STREAM_KEY environment variable or System"
              + " property");
    }

    this.apiSecret = apiSecret.toString();
    this.apiKey = apiKey.toString();
    this.retrofit = buildRetrofitClient();
    this.serviceFactory = new UserServiceFactorySelector(retrofit);
    this.serviceFactory2 = new UserServiceFactoryTagging(retrofit);
    this.serviceFactory3 = new UserServiceFactoryCall(retrofit);
  }

  private Retrofit buildRetrofitClient() {
    OkHttpClient.Builder httpClient =
        new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(5, 59, TimeUnit.SECONDS))
            .callTimeout(getStreamChatTimeout(extendedProperties), TimeUnit.MILLISECONDS);
    httpClient.interceptors().clear();

    HttpLoggingInterceptor loggingInterceptor =
        new HttpLoggingInterceptor(s -> System.out.printf("OkHttp: %s%n", s)).setLevel(getLogLevel(extendedProperties));
    httpClient.addInterceptor(loggingInterceptor);

    httpClient.addInterceptor(
        chain -> {
          Request original = chain.request();
          
          // Check for user token tag
          UserToken userToken = original.tag(UserToken.class);
          
          HttpUrl url = original.url().newBuilder().addQueryParameter("api_key", apiKey).build();
          Request.Builder builder =
              original
                  .newBuilder()
                  .url(url)
                  .header("Content-Type", "application/json")
                  .header("X-Stream-Client", "stream-java-client-" + sdkVersion)
                  .header("Stream-Auth-Type", "jwt");
          
          if (userToken != null) {
            System.out.println("!.!.! Client-Side");
            // User token present - use user auth
            builder.header("Authorization", userToken.value());
          } else {
            System.out.println("!.!.! Server-Side");
            // Server-side auth
            builder.header("Authorization", jwtToken(apiSecret));
          }
          
          return chain.proceed(builder.build());
        });
    final ObjectMapper mapper = new ObjectMapper();
    // Use field-based serialization but respect @JsonProperty and @JsonAnyGetter annotations
    mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    mapper.configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        hasFailOnUnknownProperties(extendedProperties));
    mapper.setDateFormat(
        new StdDateFormat().withColonInTimeZone(true).withTimeZone(TimeZone.getTimeZone("UTC")));
    mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

    this.okHttpClient = httpClient.build();
    Retrofit.Builder builder =
        new Retrofit.Builder()
            .baseUrl(getStreamChatBaseUrl(extendedProperties))
            .client(okHttpClient)
            .addConverterFactory(new QueryConverterFactory())
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .callFactory(new Call.Factory() {
              @Override
              public @NotNull Call newCall(@NotNull Request request) {
                return okHttpClient.newCall(request);
              }
            });
//    builder.client(httpClient.build());

    return builder.build();
  }

  @NotNull
  @Override
  public <TService> TService create(Class<TService> svcClass) {
    return retrofit.create(svcClass);
  }

//  @Override
  @NotNull
  public <TService> TService create(Class<TService> svcClass, String userToken) {
    return serviceFactory.create(svcClass, new UserToken(userToken));
  }

  @NotNull
  public <TService> TService create2(Class<TService> svcClass, String userToken) {
    return serviceFactory2.create(svcClass, new UserToken(userToken));
  }

  @NotNull
  public <TService> TService create3(Class<TService> svcClass, String userToken) {
    return serviceFactory3.create(svcClass, new UserToken(userToken));
  }

  @NotNull
  public String getApiSecret() {
    return apiSecret;
  }

  @NotNull
  public String getApiKey() {
    return apiKey;
  }

  public void setTimeout(@NotNull Duration timeoutDuration) {
    extendedProperties.setProperty(
        API_TIMEOUT_PROP_NAME, Long.toString(timeoutDuration.toMillis()));
    this.retrofit = buildRetrofitClient();
  }

  private static @NotNull String jwtToken(String apiSecret) {
    Key signingKey =
        new SecretKeySpec(
            apiSecret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    // We set issued at 5 seconds ago to avoid problems like JWTAuth error: token
    // used before
    // issue
    // at (iat)
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.add(Calendar.SECOND, -5);
    return Jwts.builder()
        .setIssuedAt(new Date())
        .setIssuer("Stream Chat Java SDK")
        .setSubject("Stream Chat Java SDK")
        .claim("server", true)
        .claim("scope", "admins")
        .setIssuedAt(calendar.getTime())
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  @NotNull
  private static Properties extendProperties(Properties properties) {
    var canformedProperties = new Properties();
    var env = System.getenv();

    var envApiSecret = env.getOrDefault("STREAM_SECRET", System.getProperty("STREAM_SECRET"));
    if (envApiSecret != null) {
      canformedProperties.put(API_SECRET_PROP_NAME, envApiSecret);
    }

    var envApiKey = env.getOrDefault("STREAM_KEY", System.getProperty("STREAM_KEY"));
    if (envApiKey != null) {
      canformedProperties.put(API_KEY_PROP_NAME, envApiKey);
    }

    var envTimeout =
        env.getOrDefault("STREAM_CHAT_TIMEOUT", System.getProperty("STREAM_CHAT_TIMEOUT"));
    if (envTimeout != null) {
      canformedProperties.put(API_TIMEOUT_PROP_NAME, envTimeout);
    }

    var envApiUrl = env.getOrDefault("STREAM_CHAT_URL", System.getProperty("STREAM_CHAT_URL"));
    if (envApiUrl != null) {
      canformedProperties.put(API_URL_PROP_NAME, envApiUrl);
    }

    canformedProperties.putAll(System.getProperties());
    canformedProperties.putAll(properties);
    return canformedProperties;
  }

  private static long getStreamChatTimeout(@NotNull Properties properties) {
    var timeout = properties.getOrDefault(API_TIMEOUT_PROP_NAME, 10000);
    return Long.parseLong(timeout.toString());
  }

  private static String getStreamChatBaseUrl(@NotNull Properties properties) {
    var url = properties.getOrDefault(API_URL_PROP_NAME, API_DEFAULT_URL);
    return url.toString();
  }

  private static final @NotNull String sdkVersion = getSdkVersion();

  private static @NotNull String getSdkVersion() {
    var clsLoader = DefaultClient.class.getClassLoader();
    try (var inputStream = clsLoader.getResourceAsStream("version.properties")) {
      var properties = new Properties();
      properties.load(inputStream);
      return properties.getProperty("version");
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private static @NotNull HttpLoggingInterceptor.Level getLogLevel(@NotNull Properties properties) {
    final var propName = "io.getstream.chat.debug.logLevel";
    var logLevel = properties.getOrDefault(propName, "NONE").toString();
    return HttpLoggingInterceptor.Level.valueOf(logLevel);
  }

  private static boolean hasFailOnUnknownProperties(@NotNull Properties properties) {
    final var propName = "io.getstream.chat.debug.failOnUnknownProperties";
    var hasEnabled = properties.getOrDefault(propName, "false");
    return Boolean.parseBoolean(hasEnabled.toString());
  }
}
