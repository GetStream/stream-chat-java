package io.getstream.chat.java.services.framework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class DefaultServiceFactory implements ServiceFactory {
  public static final String API_KEY_PROP_NAME = "io.getstream.chat.apiKey";

  public static final String API_SECRET_PROP_NAME = "io.getstream.chat.apiSecret";

  public static final String API_TIMEOUT_PROP_NAME = "io.getstream.chat.timeout";

  public static final String API_URL_PROP_NAME = "io.getstream.chat.url";

  private static final String API_DEFAULT_URL = "https://chat.stream-io-api.com";

  private static volatile ServiceFactory defaultInstance;

  @NotNull private final Retrofit retrofit;

  public static ServiceFactory getInstance() {
    if (defaultInstance == null) {
      synchronized (StreamServiceGenerator.class) {
        if (defaultInstance == null) {
          defaultInstance = new DefaultServiceFactory();
        }
      }
    }

    return defaultInstance;
  }

  public static void setInstance(@NotNull ServiceFactory instance) {
    defaultInstance = instance;
  }

  public DefaultServiceFactory() {
    this(System.getProperties());
  }

  public DefaultServiceFactory(Properties properties) {
    properties = conformProperties(properties);
    var apiKey = properties.get(API_KEY_PROP_NAME);
    var apiSecret = properties.get(API_SECRET_PROP_NAME);

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

    OkHttpClient.Builder httpClient =
        new OkHttpClient.Builder()
            .callTimeout(getStreamChatTimeout(properties), TimeUnit.MILLISECONDS);
    httpClient.interceptors().clear();

    HttpLoggingInterceptor loggingInterceptor =
        new HttpLoggingInterceptor().setLevel(getLogLevel(properties));
    httpClient.addInterceptor(loggingInterceptor);

    httpClient.addInterceptor(
        chain -> {
          Request original = chain.request();
          HttpUrl url =
              original.url().newBuilder().addQueryParameter("api_key", apiKey.toString()).build();
          Request request =
              original
                  .newBuilder()
                  .url(url)
                  .header("Content-Type", "application/json")
                  .header("X-Stream-Client", "stream-java-client-" + getSdkVersion())
                  .header("Stream-Auth-Type", "jwt")
                  .header("Authorization", jwtToken(apiSecret.toString()))
                  .build();
          return chain.proceed(request);
        });
    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, hasFailOnUnknownProperties(properties));
    mapper.setDateFormat(
        new StdDateFormat().withColonInTimeZone(true).withTimeZone(TimeZone.getTimeZone("UTC")));
    mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

    Retrofit.Builder builder =
        new Retrofit.Builder()
            .baseUrl(getStreamChatBaseUrl(properties))
            .addConverterFactory(new QueryConverterFactory())
            .addConverterFactory(JacksonConverterFactory.create(mapper));
    builder.client(httpClient.build());
    retrofit = builder.build();
  }

  @NotNull
  @Override
  public <TService> TService create(Class<TService> svcClass) {
    return retrofit.create(svcClass);
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
  private static Properties conformProperties(Properties properties) {
    var mergedProperties = new Properties();
    var env = System.getenv();

    var envApiSecret = env.getOrDefault("STREAM_SECRET", System.getProperty("STREAM_SECRET"));
    if (envApiSecret != null) {
      mergedProperties.put(API_SECRET_PROP_NAME, envApiSecret);
    }

    var envApiKey = env.getOrDefault("STREAM_KEY", System.getProperty("STREAM_KEY"));
    if (envApiKey != null) {
      mergedProperties.put(API_KEY_PROP_NAME, envApiKey);
    }

    var envTimeout =
        env.getOrDefault("STREAM_CHAT_TIMEOUT", System.getProperty("STREAM_CHAT_TIMEOUT"));
    if (envTimeout != null) {
      mergedProperties.put(API_TIMEOUT_PROP_NAME, envTimeout);
    }

    var envApiUrl = env.getOrDefault("STREAM_CHAT_URL", System.getProperty("STREAM_CHAT_URL"));
    if (envApiUrl != null) {
      mergedProperties.put(API_URL_PROP_NAME, envApiUrl);
    }

    mergedProperties.putAll(System.getProperties());
    mergedProperties.putAll(properties);
    return mergedProperties;
  }

  private static long getStreamChatTimeout(@NotNull Properties properties) {
    var timeout = properties.getOrDefault(API_TIMEOUT_PROP_NAME, 10000);
    return Long.parseLong(timeout.toString());
  }

  private static String getStreamChatBaseUrl(@NotNull Properties properties) {
    var url = properties.getOrDefault(API_URL_PROP_NAME, API_DEFAULT_URL);
    return url.toString();
  }

  private static @NotNull String getSdkVersion() {
    var clsLoader = DefaultServiceFactory.class.getClassLoader();
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
