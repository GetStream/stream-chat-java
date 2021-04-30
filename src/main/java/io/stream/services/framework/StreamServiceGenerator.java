package io.stream.services.framework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.stream.exceptions.StreamException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.Key;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.java.Log;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Log
public class StreamServiceGenerator {

  private static Retrofit retrofit;

  /** Turn this flag to enable logging of http requests */
  public static boolean logEnabled = false;

  private static boolean failOnUnknownProperties = false;

  public static @NotNull <S> S createService(@NotNull Class<S> serviceClass) {
    if (getApiKey() == null) {
      StreamException.build(
          "Missing Stream API key. Please set STREAM_KEY environment variable or System property");
    }
    if (getApiSecret() == null) {
      StreamException.build(
          "Missing Stream API secret. Please set STREAM_SECRET environment variable or System"
              + " property");
    }
    if (retrofit == null) {
      int streamChatTimeout =
          System.getenv("STREAM_CHAT_URL") != null
              ? Integer.parseInt(System.getenv("STREAM_CHAT_TIMEOUT"))
              : Integer.getInteger("STREAM_CHAT_TIMEOUT", 10000);
      OkHttpClient.Builder httpClient =
          new OkHttpClient.Builder().connectTimeout(streamChatTimeout, TimeUnit.MILLISECONDS);
      httpClient.interceptors().clear();
      if (logEnabled) {
        HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);
      }
      httpClient.addInterceptor(
          chain -> {
            Request original = chain.request();
            HttpUrl url =
                original.url().newBuilder().addQueryParameter("api_key", getApiKey()).build();
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
      String baseUrl =
          System.getenv("STREAM_CHAT_URL") != null
              ? System.getenv("STREAM_CHAT_URL")
              : System.getProperty("STREAM_CHAT_URL", "https://chat-us-east-1.stream-io-api.com/");
      Retrofit.Builder builder =
          new Retrofit.Builder()
              .baseUrl(baseUrl)
              .addConverterFactory(
                  new Converter.Factory() {
                    @Override
                    public Converter<?, String> stringConverter(
                        final Type type, final Annotation[] annotations, final Retrofit retrofit) {
                      if (!hasToJson(annotations)) {
                        return super.stringConverter(type, annotations, retrofit);
                      }

                      return value -> {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        mapper.writeValue(baos, value);
                        return baos.toString("UTF-8");
                      };
                    }

                    private boolean hasToJson(final Annotation[] annotations) {
                      for (final Annotation annotation : annotations) {
                        if (annotation instanceof ToJson) {
                          return true;
                        }
                      }
                      return false;
                    }
                  })
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
          StreamServiceGenerator.class.getClassLoader().getResourceAsStream("stream.properties"));
    } catch (IOException e) {
      log.severe("Missing stream.properties, should not happen");
    }
    return properties.getProperty("version");
  }

  private static @NotNull String jwtToken() {
    Key signingKey;
    try {
      signingKey =
          new SecretKeySpec(
              getApiSecret().getBytes("UTF-8"), SignatureAlgorithm.HS256.getJcaName());
      return Jwts.builder()
          .setIssuedAt(new Date())
          .setIssuer("Stream Chat Java SDK")
          .setSubject("Stream Chat Java SDK")
          .claim("server", true)
          .claim("scope", "admins")
          .setIssuedAt(new Date())
          .signWith(signingKey, SignatureAlgorithm.HS256)
          .compact();
    } catch (UnsupportedEncodingException e) {
      log.severe("Should not happen: UTF-8 is not supported");
      return "";
    }
  }

  private static @Nullable String getApiSecret() {
    return System.getenv("STREAM_SECRET") != null
        ? System.getenv("STREAM_SECRET")
        : System.getProperty("STREAM_SECRET");
  }

  private static @Nullable String getApiKey() {
    return System.getenv("STREAM_KEY") != null
        ? System.getenv("STREAM_KEY")
        : System.getProperty("STREAM_KEY");
  }
}
