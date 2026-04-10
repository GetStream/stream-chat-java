package io.getstream.chat.java;

import io.getstream.chat.java.services.framework.DefaultClient;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Properties;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;

public class DefaultClientConfigurationTest {

  @Test
  @DisplayName("DefaultClient uses doubled throughput defaults")
  void whenCreatingClientWithoutOverrides_thenUsesDoubledDefaults() {
    var client = new DefaultClient(baseProperties());
    var okHttpClient = getOkHttpClient(client);
    var pool = okHttpClient.connectionPool();

    Assertions.assertEquals(10, readIntField(poolDelegate(pool), "maxIdleConnections"));
    Assertions.assertEquals(
        Duration.ofSeconds(118).toNanos(),
        readLongField(poolDelegate(pool), "keepAliveDurationNs"));
    Assertions.assertEquals(128, readIntField(okHttpClient.dispatcher(), "maxRequests"));
    Assertions.assertEquals(10, readIntField(okHttpClient.dispatcher(), "maxRequestsPerHost"));
    Assertions.assertEquals(20_000, okHttpClient.connectTimeoutMillis());
    Assertions.assertEquals(20_000, okHttpClient.readTimeoutMillis());
    Assertions.assertEquals(20_000, okHttpClient.writeTimeoutMillis());
    Assertions.assertEquals(20_000, okHttpClient.callTimeoutMillis());
  }

  @Test
  @DisplayName("DefaultClient uses configured HTTP properties")
  void givenHttpProperties_whenCreatingClient_thenUsesConfiguredValues() {
    var properties = baseProperties();
    properties.put(DefaultClient.DISPATCHER_MAX_REQUESTS_PROP_NAME, "80");
    properties.put(DefaultClient.DISPATCHER_MAX_REQUESTS_PER_HOST_PROP_NAME, "24");
    properties.put(DefaultClient.CONNECTION_POOL_MAX_IDLE_CONNECTIONS_PROP_NAME, "20");
    properties.put(DefaultClient.CONNECTION_POOL_KEEP_ALIVE_DURATION_PROP_NAME, "120000");
    properties.put(DefaultClient.API_CONNECT_TIMEOUT_PROP_NAME, "5000");
    properties.put(DefaultClient.API_READ_TIMEOUT_PROP_NAME, "15000");
    properties.put(DefaultClient.API_WRITE_TIMEOUT_PROP_NAME, "25000");
    properties.put(DefaultClient.API_TIMEOUT_PROP_NAME, "30000");

    var client = new DefaultClient(properties);
    var okHttpClient = getOkHttpClient(client);
    var pool = okHttpClient.connectionPool();

    Assertions.assertEquals(20, readIntField(poolDelegate(pool), "maxIdleConnections"));
    Assertions.assertEquals(
        Duration.ofMinutes(2).toNanos(), readLongField(poolDelegate(pool), "keepAliveDurationNs"));
    Assertions.assertEquals(80, readIntField(okHttpClient.dispatcher(), "maxRequests"));
    Assertions.assertEquals(24, readIntField(okHttpClient.dispatcher(), "maxRequestsPerHost"));
    Assertions.assertEquals(5_000, okHttpClient.connectTimeoutMillis());
    Assertions.assertEquals(15_000, okHttpClient.readTimeoutMillis());
    Assertions.assertEquals(25_000, okHttpClient.writeTimeoutMillis());
    Assertions.assertEquals(30_000, okHttpClient.callTimeoutMillis());
  }

  @Test
  @DisplayName("DefaultClient uses option-based HTTP configuration")
  void givenHttpOptions_whenCreatingClient_thenUsesConfiguredValues() {
    var options =
        DefaultClient.HttpClientOptions.builder()
            .dispatcher(96, 32)
            .connectionPool(30, Duration.ofSeconds(90))
            .connectTimeout(Duration.ofSeconds(3))
            .readTimeout(Duration.ofSeconds(12))
            .writeTimeout(Duration.ofSeconds(18))
            .callTimeout(Duration.ofSeconds(25))
            .build();

    var client = new DefaultClient(baseProperties(), options);
    var okHttpClient = getOkHttpClient(client);
    var pool = okHttpClient.connectionPool();

    Assertions.assertEquals(30, readIntField(poolDelegate(pool), "maxIdleConnections"));
    Assertions.assertEquals(
        Duration.ofSeconds(90).toNanos(), readLongField(poolDelegate(pool), "keepAliveDurationNs"));
    Assertions.assertEquals(96, readIntField(okHttpClient.dispatcher(), "maxRequests"));
    Assertions.assertEquals(32, readIntField(okHttpClient.dispatcher(), "maxRequestsPerHost"));
    Assertions.assertEquals(3_000, okHttpClient.connectTimeoutMillis());
    Assertions.assertEquals(12_000, okHttpClient.readTimeoutMillis());
    Assertions.assertEquals(18_000, okHttpClient.writeTimeoutMillis());
    Assertions.assertEquals(25_000, okHttpClient.callTimeoutMillis());
  }

  @Test
  @DisplayName("DefaultClient can update connection pool dispatcher and timeouts at runtime")
  void whenSettingHttpConfiguration_thenRebuildsClientWithNewValues() {
    var client = new DefaultClient(baseProperties());

    client.setConnectionPool(15, Duration.ofSeconds(30));
    client.setDispatcher(72, 16);
    client.setTimeouts(
        Duration.ofSeconds(4),
        Duration.ofSeconds(14),
        Duration.ofSeconds(16),
        Duration.ofSeconds(22));

    var okHttpClient = getOkHttpClient(client);
    var pool = okHttpClient.connectionPool();
    Assertions.assertEquals(15, readIntField(poolDelegate(pool), "maxIdleConnections"));
    Assertions.assertEquals(
        Duration.ofSeconds(30).toNanos(), readLongField(poolDelegate(pool), "keepAliveDurationNs"));
    Assertions.assertEquals(72, readIntField(okHttpClient.dispatcher(), "maxRequests"));
    Assertions.assertEquals(16, readIntField(okHttpClient.dispatcher(), "maxRequestsPerHost"));
    Assertions.assertEquals(4_000, okHttpClient.connectTimeoutMillis());
    Assertions.assertEquals(14_000, okHttpClient.readTimeoutMillis());
    Assertions.assertEquals(16_000, okHttpClient.writeTimeoutMillis());
    Assertions.assertEquals(22_000, okHttpClient.callTimeoutMillis());
  }

  private static Properties baseProperties() {
    var properties = new Properties();
    properties.put(DefaultClient.API_KEY_PROP_NAME, "test-key");
    properties.put(DefaultClient.API_SECRET_PROP_NAME, "test-secret");
    return properties;
  }

  private static OkHttpClient getOkHttpClient(DefaultClient client) {
    Retrofit retrofit = (Retrofit) readField(client, "retrofit");
    return (OkHttpClient) readField(retrofit, "callFactory");
  }

  private static Object poolDelegate(ConnectionPool pool) {
    return readField(pool, "delegate");
  }

  private static int readIntField(Object target, String fieldName) {
    return (int) readField(target, fieldName);
  }

  private static long readLongField(Object target, String fieldName) {
    return (long) readField(target, fieldName);
  }

  private static Object readField(Object target, String fieldName) {
    Class<?> type = target.getClass();
    while (type != null) {
      try {
        Field field = type.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
      } catch (NoSuchFieldException ignored) {
        type = type.getSuperclass();
      } catch (IllegalAccessException ex) {
        throw new IllegalStateException(ex);
      }
    }

    throw new IllegalStateException(
        String.format("Field '%s' not found on %s", fieldName, target.getClass().getName()));
  }
}
