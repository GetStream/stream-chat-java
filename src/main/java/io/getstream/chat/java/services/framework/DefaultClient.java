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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DefaultClient implements Client {
  public static final String API_KEY_PROP_NAME = "io.getstream.chat.apiKey";
  public static final String API_SECRET_PROP_NAME = "io.getstream.chat.apiSecret";
  public static final String API_TIMEOUT_PROP_NAME = "io.getstream.chat.timeout";
  public static final String API_CONNECT_TIMEOUT_PROP_NAME = "io.getstream.chat.connectTimeout";
  public static final String API_READ_TIMEOUT_PROP_NAME = "io.getstream.chat.readTimeout";
  public static final String API_WRITE_TIMEOUT_PROP_NAME = "io.getstream.chat.writeTimeout";
  public static final String API_URL_PROP_NAME = "io.getstream.chat.url";
  public static final String X_STREAM_EXT_PROP_NAME = "io.getstream.chat.xStreamExt";
  public static final String CONNECTION_POOL_MAX_IDLE_CONNECTIONS_PROP_NAME =
      "io.getstream.chat.connectionPool.maxIdleConnections";
  public static final String CONNECTION_POOL_KEEP_ALIVE_DURATION_PROP_NAME =
      "io.getstream.chat.connectionPool.keepAliveDurationMs";
  public static final String DISPATCHER_MAX_REQUESTS_PROP_NAME =
      "io.getstream.chat.dispatcher.maxRequests";
  public static final String DISPATCHER_MAX_REQUESTS_PER_HOST_PROP_NAME =
      "io.getstream.chat.dispatcher.maxRequestsPerHost";

  private static final String API_DEFAULT_URL = "https://chat.stream-io-api.com";
  private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 10;
  private static final long DEFAULT_KEEP_ALIVE_DURATION_MS = 118_000L;
  private static final int DEFAULT_DISPATCHER_MAX_REQUESTS = 128;
  private static final int DEFAULT_DISPATCHER_MAX_REQUESTS_PER_HOST = 10;
  private static final long DEFAULT_TIMEOUT_MS = 20_000L;
  private static volatile DefaultClient defaultInstance;
  @NotNull private final String apiSecret;
  @NotNull private final String apiKey;
  @NotNull private final Properties extendedProperties;
  @NotNull private final Function<Retrofit, UserServiceFactory> serviceFactoryBuilder;

  @NotNull Retrofit retrofit;
  @NotNull UserServiceFactory serviceFactory;

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
    this(properties, HttpClientOptions.builder().build());
  }

  public DefaultClient(Properties properties, @NotNull HttpClientOptions httpClientOptions) {
    this(properties, UserServiceFactorySelector::new, httpClientOptions);
  }

  public DefaultClient(
      @NotNull Properties properties,
      @NotNull HttpClientOptions httpClientOptions,
      @NotNull Function<Retrofit, UserServiceFactory> serviceFactoryBuilder) {
    this(properties, serviceFactoryBuilder, httpClientOptions);
  }

  public DefaultClient(
      @NotNull Properties properties,
      @NotNull Function<Retrofit, UserServiceFactory> serviceFactoryBuilder) {
    this(properties, serviceFactoryBuilder, HttpClientOptions.builder().build());
  }

  private DefaultClient(
      @NotNull Properties properties,
      @NotNull Function<Retrofit, UserServiceFactory> serviceFactoryBuilder,
      @NotNull HttpClientOptions httpClientOptions) {
    extendedProperties = extendProperties(properties, httpClientOptions);
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
    this.serviceFactoryBuilder = serviceFactoryBuilder;

    this.retrofit = buildRetrofitClient(buildOkHttpClient());
    this.serviceFactory = serviceFactoryBuilder.apply(retrofit);
  }

  private OkHttpClient buildOkHttpClient() {
    OkHttpClient.Builder httpClient =
        new OkHttpClient.Builder()
            .dispatcher(buildDispatcher(extendedProperties))
            .connectionPool(buildConnectionPool(extendedProperties))
            .connectTimeout(getStreamChatConnectTimeout(extendedProperties), TimeUnit.MILLISECONDS)
            .readTimeout(getStreamChatReadTimeout(extendedProperties), TimeUnit.MILLISECONDS)
            .writeTimeout(getStreamChatWriteTimeout(extendedProperties), TimeUnit.MILLISECONDS)
            .callTimeout(getStreamChatTimeout(extendedProperties), TimeUnit.MILLISECONDS);
    httpClient.interceptors().clear();

    HttpLoggingInterceptor loggingInterceptor =
        new HttpLoggingInterceptor().setLevel(getLogLevel(extendedProperties));
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

          // Add x-stream-ext header if configured
          String xStreamExt = getXStreamExt(extendedProperties);
          if (xStreamExt != null && !xStreamExt.isEmpty()) {
            builder.header("X-Stream-Ext", xStreamExt);
          }

          if (userToken != null) {
            // User token present - use user auth
            builder.header("Authorization", userToken.value());
          } else {
            // Server-side auth
            builder.header("Authorization", jwtToken(apiSecret));
          }

          return chain.proceed(builder.build());
        });
    return httpClient.build();
  }

  private Retrofit buildRetrofitClient(OkHttpClient okHttpClient) {
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

    Retrofit.Builder builder =
        new Retrofit.Builder()
            .baseUrl(getStreamChatBaseUrl(extendedProperties))
            .client(okHttpClient)
            .addConverterFactory(new QueryConverterFactory())
            .addConverterFactory(JacksonConverterFactory.create(mapper));
    return builder.build();
  }

  @NotNull
  @Override
  public <TService> TService create(Class<TService> svcClass) {
    return retrofit.create(svcClass);
  }

  @Override
  @NotNull
  public <TService> TService create(Class<TService> svcClass, String userToken) {
    return serviceFactory.create(svcClass, new UserToken(userToken));
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
    this.retrofit = buildRetrofitClient(buildOkHttpClient());
    this.serviceFactory = serviceFactoryBuilder.apply(retrofit);
  }

  public void setConnectionPool(int maxIdleConnections, @NotNull Duration keepAliveDuration) {
    if (maxIdleConnections < 0) {
      throw new IllegalArgumentException("maxIdleConnections must be >= 0");
    }
    if (keepAliveDuration.isNegative()) {
      throw new IllegalArgumentException("keepAliveDuration must be >= 0");
    }

    extendedProperties.setProperty(
        CONNECTION_POOL_MAX_IDLE_CONNECTIONS_PROP_NAME, Integer.toString(maxIdleConnections));
    extendedProperties.setProperty(
        CONNECTION_POOL_KEEP_ALIVE_DURATION_PROP_NAME, Long.toString(keepAliveDuration.toMillis()));
    this.retrofit = buildRetrofitClient(buildOkHttpClient());
    this.serviceFactory = serviceFactoryBuilder.apply(retrofit);
  }

  public void setDispatcher(int maxRequests, int maxRequestsPerHost) {
    if (maxRequests < 1) {
      throw new IllegalArgumentException("maxRequests must be >= 1");
    }
    if (maxRequestsPerHost < 1) {
      throw new IllegalArgumentException("maxRequestsPerHost must be >= 1");
    }
    if (maxRequestsPerHost > maxRequests) {
      throw new IllegalArgumentException("maxRequestsPerHost must be <= maxRequests");
    }

    extendedProperties.setProperty(
        DISPATCHER_MAX_REQUESTS_PROP_NAME, Integer.toString(maxRequests));
    extendedProperties.setProperty(
        DISPATCHER_MAX_REQUESTS_PER_HOST_PROP_NAME, Integer.toString(maxRequestsPerHost));
    this.retrofit = buildRetrofitClient(buildOkHttpClient());
    this.serviceFactory = serviceFactoryBuilder.apply(retrofit);
  }

  public void setTimeouts(
      @NotNull Duration connectTimeout,
      @NotNull Duration readTimeout,
      @NotNull Duration writeTimeout,
      @NotNull Duration callTimeout) {
    validateTimeout(API_CONNECT_TIMEOUT_PROP_NAME, connectTimeout);
    validateTimeout(API_READ_TIMEOUT_PROP_NAME, readTimeout);
    validateTimeout(API_WRITE_TIMEOUT_PROP_NAME, writeTimeout);
    validateTimeout(API_TIMEOUT_PROP_NAME, callTimeout);

    extendedProperties.setProperty(
        API_CONNECT_TIMEOUT_PROP_NAME, Long.toString(connectTimeout.toMillis()));
    extendedProperties.setProperty(
        API_READ_TIMEOUT_PROP_NAME, Long.toString(readTimeout.toMillis()));
    extendedProperties.setProperty(
        API_WRITE_TIMEOUT_PROP_NAME, Long.toString(writeTimeout.toMillis()));
    extendedProperties.setProperty(API_TIMEOUT_PROP_NAME, Long.toString(callTimeout.toMillis()));
    this.retrofit = buildRetrofitClient(buildOkHttpClient());
    this.serviceFactory = serviceFactoryBuilder.apply(retrofit);
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
  private static Properties extendProperties(
      Properties properties, @NotNull HttpClientOptions httpClientOptions) {
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

    var envConnectTimeout =
        env.getOrDefault(
            "STREAM_CHAT_CONNECT_TIMEOUT", System.getProperty("STREAM_CHAT_CONNECT_TIMEOUT"));
    if (envConnectTimeout != null) {
      canformedProperties.put(API_CONNECT_TIMEOUT_PROP_NAME, envConnectTimeout);
    }

    var envReadTimeout =
        env.getOrDefault(
            "STREAM_CHAT_READ_TIMEOUT", System.getProperty("STREAM_CHAT_READ_TIMEOUT"));
    if (envReadTimeout != null) {
      canformedProperties.put(API_READ_TIMEOUT_PROP_NAME, envReadTimeout);
    }

    var envWriteTimeout =
        env.getOrDefault(
            "STREAM_CHAT_WRITE_TIMEOUT", System.getProperty("STREAM_CHAT_WRITE_TIMEOUT"));
    if (envWriteTimeout != null) {
      canformedProperties.put(API_WRITE_TIMEOUT_PROP_NAME, envWriteTimeout);
    }

    var envConnectionPoolMaxIdleConnections =
        env.getOrDefault(
            "STREAM_CHAT_CONNECTION_POOL_MAX_IDLE_CONNECTIONS",
            System.getProperty("STREAM_CHAT_CONNECTION_POOL_MAX_IDLE_CONNECTIONS"));
    if (envConnectionPoolMaxIdleConnections != null) {
      canformedProperties.put(
          CONNECTION_POOL_MAX_IDLE_CONNECTIONS_PROP_NAME, envConnectionPoolMaxIdleConnections);
    }

    var envConnectionPoolKeepAliveDuration =
        env.getOrDefault(
            "STREAM_CHAT_CONNECTION_POOL_KEEP_ALIVE_DURATION_MS",
            System.getProperty("STREAM_CHAT_CONNECTION_POOL_KEEP_ALIVE_DURATION_MS"));
    if (envConnectionPoolKeepAliveDuration != null) {
      canformedProperties.put(
          CONNECTION_POOL_KEEP_ALIVE_DURATION_PROP_NAME, envConnectionPoolKeepAliveDuration);
    }

    var envDispatcherMaxRequests =
        env.getOrDefault(
            "STREAM_CHAT_DISPATCHER_MAX_REQUESTS",
            System.getProperty("STREAM_CHAT_DISPATCHER_MAX_REQUESTS"));
    if (envDispatcherMaxRequests != null) {
      canformedProperties.put(DISPATCHER_MAX_REQUESTS_PROP_NAME, envDispatcherMaxRequests);
    }

    var envDispatcherMaxRequestsPerHost =
        env.getOrDefault(
            "STREAM_CHAT_DISPATCHER_MAX_REQUESTS_PER_HOST",
            System.getProperty("STREAM_CHAT_DISPATCHER_MAX_REQUESTS_PER_HOST"));
    if (envDispatcherMaxRequestsPerHost != null) {
      canformedProperties.put(
          DISPATCHER_MAX_REQUESTS_PER_HOST_PROP_NAME, envDispatcherMaxRequestsPerHost);
    }

    var envApiUrl = env.getOrDefault("STREAM_CHAT_URL", System.getProperty("STREAM_CHAT_URL"));
    if (envApiUrl != null) {
      canformedProperties.put(API_URL_PROP_NAME, envApiUrl);
    }

    var envXStreamExt =
        env.getOrDefault(
            "STREAM_CHAT_X_STREAM_EXT", System.getProperty("STREAM_CHAT_X_STREAM_EXT"));
    if (envXStreamExt != null) {
      canformedProperties.put(X_STREAM_EXT_PROP_NAME, envXStreamExt);
    }

    canformedProperties.putAll(System.getProperties());
    canformedProperties.putAll(properties);
    httpClientOptions.applyTo(canformedProperties);
    return canformedProperties;
  }

  private static long getStreamChatTimeout(@NotNull Properties properties) {
    var timeout = properties.getOrDefault(API_TIMEOUT_PROP_NAME, DEFAULT_TIMEOUT_MS);
    return parseTimeout(API_TIMEOUT_PROP_NAME, timeout);
  }

  private static long getStreamChatConnectTimeout(@NotNull Properties properties) {
    var timeout = properties.getOrDefault(API_CONNECT_TIMEOUT_PROP_NAME, DEFAULT_TIMEOUT_MS);
    return parseTimeout(API_CONNECT_TIMEOUT_PROP_NAME, timeout);
  }

  private static long getStreamChatReadTimeout(@NotNull Properties properties) {
    var timeout = properties.getOrDefault(API_READ_TIMEOUT_PROP_NAME, DEFAULT_TIMEOUT_MS);
    return parseTimeout(API_READ_TIMEOUT_PROP_NAME, timeout);
  }

  private static long getStreamChatWriteTimeout(@NotNull Properties properties) {
    var timeout = properties.getOrDefault(API_WRITE_TIMEOUT_PROP_NAME, DEFAULT_TIMEOUT_MS);
    return parseTimeout(API_WRITE_TIMEOUT_PROP_NAME, timeout);
  }

  private static long parseTimeout(@NotNull String propName, Object timeout) {
    long parsedTimeout = Long.parseLong(timeout.toString());
    if (parsedTimeout < 0) {
      throw new IllegalArgumentException(propName + " must be >= 0");
    }
    return parsedTimeout;
  }

  private static @NotNull ConnectionPool buildConnectionPool(@NotNull Properties properties) {
    return new ConnectionPool(
        getConnectionPoolMaxIdleConnections(properties),
        getConnectionPoolKeepAliveDurationMs(properties),
        TimeUnit.MILLISECONDS);
  }

  private static @NotNull okhttp3.Dispatcher buildDispatcher(@NotNull Properties properties) {
    okhttp3.Dispatcher dispatcher = new okhttp3.Dispatcher();
    dispatcher.setMaxRequests(getDispatcherMaxRequests(properties));
    dispatcher.setMaxRequestsPerHost(getDispatcherMaxRequestsPerHost(properties));
    return dispatcher;
  }

  private static int getConnectionPoolMaxIdleConnections(@NotNull Properties properties) {
    var maxIdleConnections =
        properties.getOrDefault(
            CONNECTION_POOL_MAX_IDLE_CONNECTIONS_PROP_NAME, DEFAULT_MAX_IDLE_CONNECTIONS);
    int parsedMaxIdleConnections = Integer.parseInt(maxIdleConnections.toString());
    if (parsedMaxIdleConnections < 0) {
      throw new IllegalArgumentException(
          CONNECTION_POOL_MAX_IDLE_CONNECTIONS_PROP_NAME + " must be >= 0");
    }
    return parsedMaxIdleConnections;
  }

  private static long getConnectionPoolKeepAliveDurationMs(@NotNull Properties properties) {
    var keepAliveDuration =
        properties.getOrDefault(
            CONNECTION_POOL_KEEP_ALIVE_DURATION_PROP_NAME, DEFAULT_KEEP_ALIVE_DURATION_MS);
    long parsedKeepAliveDuration = Long.parseLong(keepAliveDuration.toString());
    if (parsedKeepAliveDuration < 0) {
      throw new IllegalArgumentException(
          CONNECTION_POOL_KEEP_ALIVE_DURATION_PROP_NAME + " must be >= 0");
    }
    return parsedKeepAliveDuration;
  }

  private static int getDispatcherMaxRequests(@NotNull Properties properties) {
    var maxRequests =
        properties.getOrDefault(DISPATCHER_MAX_REQUESTS_PROP_NAME, DEFAULT_DISPATCHER_MAX_REQUESTS);
    int parsedMaxRequests = Integer.parseInt(maxRequests.toString());
    if (parsedMaxRequests < 1) {
      throw new IllegalArgumentException(DISPATCHER_MAX_REQUESTS_PROP_NAME + " must be >= 1");
    }
    return parsedMaxRequests;
  }

  private static int getDispatcherMaxRequestsPerHost(@NotNull Properties properties) {
    var maxRequestsPerHost =
        properties.getOrDefault(
            DISPATCHER_MAX_REQUESTS_PER_HOST_PROP_NAME, DEFAULT_DISPATCHER_MAX_REQUESTS_PER_HOST);
    int parsedMaxRequestsPerHost = Integer.parseInt(maxRequestsPerHost.toString());
    if (parsedMaxRequestsPerHost < 1) {
      throw new IllegalArgumentException(
          DISPATCHER_MAX_REQUESTS_PER_HOST_PROP_NAME + " must be >= 1");
    }
    return parsedMaxRequestsPerHost;
  }

  private static String getStreamChatBaseUrl(@NotNull Properties properties) {
    var url = properties.getOrDefault(API_URL_PROP_NAME, API_DEFAULT_URL);
    return url.toString();
  }

  private static String getXStreamExt(@NotNull Properties properties) {
    var xStreamExt = properties.get(X_STREAM_EXT_PROP_NAME);
    return xStreamExt != null ? xStreamExt.toString() : null;
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

  public static final class HttpClientOptions {
    private final Integer dispatcherMaxRequests;
    private final Integer dispatcherMaxRequestsPerHost;
    private final Integer connectionPoolMaxIdleConnections;
    private final Duration connectionPoolKeepAliveDuration;
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private final Duration writeTimeout;
    private final Duration callTimeout;

    private HttpClientOptions(Builder builder) {
      this.dispatcherMaxRequests = builder.dispatcherMaxRequests;
      this.dispatcherMaxRequestsPerHost = builder.dispatcherMaxRequestsPerHost;
      this.connectionPoolMaxIdleConnections = builder.connectionPoolMaxIdleConnections;
      this.connectionPoolKeepAliveDuration = builder.connectionPoolKeepAliveDuration;
      this.connectTimeout = builder.connectTimeout;
      this.readTimeout = builder.readTimeout;
      this.writeTimeout = builder.writeTimeout;
      this.callTimeout = builder.callTimeout;
    }

    public static Builder builder() {
      return new Builder();
    }

    private void applyTo(@NotNull Properties properties) {
      if (dispatcherMaxRequests != null) {
        properties.setProperty(
            DISPATCHER_MAX_REQUESTS_PROP_NAME, Integer.toString(dispatcherMaxRequests));
      }
      if (dispatcherMaxRequestsPerHost != null) {
        properties.setProperty(
            DISPATCHER_MAX_REQUESTS_PER_HOST_PROP_NAME,
            Integer.toString(dispatcherMaxRequestsPerHost));
      }
      if (connectionPoolMaxIdleConnections != null) {
        properties.setProperty(
            CONNECTION_POOL_MAX_IDLE_CONNECTIONS_PROP_NAME,
            Integer.toString(connectionPoolMaxIdleConnections));
      }
      if (connectionPoolKeepAliveDuration != null) {
        properties.setProperty(
            CONNECTION_POOL_KEEP_ALIVE_DURATION_PROP_NAME,
            Long.toString(connectionPoolKeepAliveDuration.toMillis()));
      }
      if (connectTimeout != null) {
        properties.setProperty(
            API_CONNECT_TIMEOUT_PROP_NAME, Long.toString(connectTimeout.toMillis()));
      }
      if (readTimeout != null) {
        properties.setProperty(API_READ_TIMEOUT_PROP_NAME, Long.toString(readTimeout.toMillis()));
      }
      if (writeTimeout != null) {
        properties.setProperty(API_WRITE_TIMEOUT_PROP_NAME, Long.toString(writeTimeout.toMillis()));
      }
      if (callTimeout != null) {
        properties.setProperty(API_TIMEOUT_PROP_NAME, Long.toString(callTimeout.toMillis()));
      }
    }

    public static final class Builder {
      private Integer dispatcherMaxRequests;
      private Integer dispatcherMaxRequestsPerHost;
      private Integer connectionPoolMaxIdleConnections;
      private Duration connectionPoolKeepAliveDuration;
      private Duration connectTimeout;
      private Duration readTimeout;
      private Duration writeTimeout;
      private Duration callTimeout;

      public Builder dispatcher(int maxRequests, int maxRequestsPerHost) {
        if (maxRequests < 1) {
          throw new IllegalArgumentException("maxRequests must be >= 1");
        }
        if (maxRequestsPerHost < 1) {
          throw new IllegalArgumentException("maxRequestsPerHost must be >= 1");
        }
        if (maxRequestsPerHost > maxRequests) {
          throw new IllegalArgumentException("maxRequestsPerHost must be <= maxRequests");
        }
        this.dispatcherMaxRequests = maxRequests;
        this.dispatcherMaxRequestsPerHost = maxRequestsPerHost;
        return this;
      }

      public Builder connectionPool(int maxIdleConnections, @NotNull Duration keepAliveDuration) {
        if (maxIdleConnections < 0) {
          throw new IllegalArgumentException("maxIdleConnections must be >= 0");
        }
        if (keepAliveDuration.isNegative()) {
          throw new IllegalArgumentException("keepAliveDuration must be >= 0");
        }
        this.connectionPoolMaxIdleConnections = maxIdleConnections;
        this.connectionPoolKeepAliveDuration = keepAliveDuration;
        return this;
      }

      public Builder connectTimeout(@NotNull Duration connectTimeout) {
        validateTimeout(API_CONNECT_TIMEOUT_PROP_NAME, connectTimeout);
        this.connectTimeout = connectTimeout;
        return this;
      }

      public Builder readTimeout(@NotNull Duration readTimeout) {
        validateTimeout(API_READ_TIMEOUT_PROP_NAME, readTimeout);
        this.readTimeout = readTimeout;
        return this;
      }

      public Builder writeTimeout(@NotNull Duration writeTimeout) {
        validateTimeout(API_WRITE_TIMEOUT_PROP_NAME, writeTimeout);
        this.writeTimeout = writeTimeout;
        return this;
      }

      public Builder callTimeout(@NotNull Duration callTimeout) {
        validateTimeout(API_TIMEOUT_PROP_NAME, callTimeout);
        this.callTimeout = callTimeout;
        return this;
      }

      public HttpClientOptions build() {
        return new HttpClientOptions(this);
      }
    }
  }

  private static void validateTimeout(@NotNull String propName, @NotNull Duration timeout) {
    if (timeout.isNegative()) {
      throw new IllegalArgumentException(propName + " must be >= 0");
    }
  }
}
