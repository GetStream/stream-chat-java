package io.getstream.chat.java.services.framework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DefaultServiceFactory implements ServiceFactory {
  private static final String API_KEY_PROP_NAME = "io.getstream.chat.apiKey";
  private static final String API_SECRET_PROP_NAME = "io.getstream.chat.apiSecret";
  private static final String API_TIMEOUT_PROP_NAME = "io.getstream.chat.timeout";

  private final Retrofit retrofit;

  private static final HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.NONE;

  private static final boolean failOnUnknownProperties = false;

  private static volatile ServiceFactory defaultInstance;

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
    this(new Properties());
  }

  public DefaultServiceFactory(Properties properties) {
    properties = mergeSystemProperties(properties);
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
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(logLevel);
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
                  .header("X-Stream-Client", "stream-java-client-" + sdkVersion())
                  .header("Stream-Auth-Type", "jwt")
                  .header("Authorization", jwtToken(apiSecret.toString()))
                  .build();
          return chain.proceed(request);
        });
    final ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
    mapper.setDateFormat(
        new StdDateFormat().withColonInTimeZone(true).withTimeZone(TimeZone.getTimeZone("UTC")));
    mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

    Retrofit.Builder builder =
        new Retrofit.Builder()
            .baseUrl(getStreamChatBaseUrl())
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

  private static Properties mergeSystemProperties(Properties properties) {
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

    mergedProperties.putAll(System.getProperties());
    mergedProperties.putAll(properties);
    return mergedProperties;
  }

  private static long getStreamChatTimeout(Properties properties) {
    return 10000;
  }

  private static String getStreamChatBaseUrl() {
    return "https://chat.stream-io-api.com";
  }

  private static @NotNull String sdkVersion() {
    final Properties properties = new Properties();
    StreamServiceGenerator.class.getClassLoader().getResourceAsStream("version.properties");
    return properties.getProperty("version");
  }
}
