package io.getstream.chat.java.services.framework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.java.Log;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Log
public class StreamServiceGenerator {

  private static Retrofit retrofit;

  /** Change this to enable logging of http requests */
  public static HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.NONE;

  private static boolean failOnUnknownProperties = false;

  private static String apiKey;

  private static String apiSecret;

  private static void initKeys() {
    apiKey =
        System.getenv("STREAM_KEY") != null
            ? System.getenv("STREAM_KEY")
            : System.getProperty("STREAM_KEY");
    apiSecret =
        System.getenv("STREAM_SECRET") != null
            ? System.getenv("STREAM_SECRET")
            : System.getProperty("STREAM_SECRET");
  }

  public static @NotNull <S> S createService(@NotNull Class<S> serviceClass) {
    if (retrofit == null) {
      initKeys();
      if (apiKey == null) {
        throw new IllegalStateException(
            "Missing Stream API key. Please set STREAM_KEY environment variable or System"
                + " property");
      }
      if (apiSecret == null) {
        throw new IllegalStateException(
            "Missing Stream API secret. Please set STREAM_SECRET environment variable or System"
                + " property");
      }
      int streamChatTimeout =
          System.getenv("STREAM_CHAT_TIMEOUT") != null
              ? Integer.parseInt(System.getenv("STREAM_CHAT_TIMEOUT"))
              : Integer.getInteger("STREAM_CHAT_TIMEOUT", 10000);

      OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
          .callTimeout(streamChatTimeout, TimeUnit.MILLISECONDS);

      httpClient.interceptors().clear();
      HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(logLevel);
      httpClient.addInterceptor(loggingInterceptor);
      httpClient.addInterceptor(
          chain -> {
            Request original = chain.request();
            HttpUrl url = original.url().newBuilder().addQueryParameter("api_key", apiKey).build();
            Request request =
                original
                    .newBuilder()
                    .url(url)
                    .header("Content-Type", "application/json")
                    .header("X-Stream-Client", "stream-java-client-" + sdkVersion())
                    .header("Stream-Auth-Type", "jwt")
                    .header("Authorization", jwtToken())
                    .build();
            return chain.proceed(request);
          });
      final ObjectMapper mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
      mapper.setDateFormat(
          new StdDateFormat().withColonInTimeZone(true).withTimeZone(TimeZone.getTimeZone("UTC")));
      mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
      String baseUrl =
          System.getenv("STREAM_CHAT_URL") != null
              ? System.getenv("STREAM_CHAT_URL")
              : System.getProperty("STREAM_CHAT_URL", "https://chat.stream-io-api.com");
      Retrofit.Builder builder =
          new Retrofit.Builder()
              .baseUrl(baseUrl)
              .addConverterFactory(new QueryConverterFactory())
              .addConverterFactory(JacksonConverterFactory.create(mapper));
      builder.client(httpClient.build());
      retrofit = builder.build();
    }
    return retrofit.create(serviceClass);
  }

  private static @NotNull String sdkVersion() {
    final Properties properties = new Properties();
    try {
      properties.load(
          StreamServiceGenerator.class.getClassLoader().getResourceAsStream("version.properties"));
    } catch (IOException e) {
      log.severe("Missing version.properties, should not happen");
    }
    return properties.getProperty("version");
  }

  private static @NotNull String jwtToken() {
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
}
