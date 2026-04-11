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
  @DisplayName("README and DOCS property snippet configures the documented settings")
  void docsPropertySnippetConfiguresDocumentedSettings() {
    var originalInstance = getDefaultClientInstance();
    try {
      var properties = new Properties();
      properties.put(DefaultClient.API_KEY_PROP_NAME, "test-key");
      properties.put(DefaultClient.API_SECRET_PROP_NAME, "test-secret");
      properties.put(DefaultClient.DISPATCHER_MAX_REQUESTS_PROP_NAME, "128");
      properties.put(DefaultClient.DISPATCHER_MAX_REQUESTS_PER_HOST_PROP_NAME, "32");
      properties.put(DefaultClient.CONNECTION_POOL_MAX_IDLE_CONNECTIONS_PROP_NAME, "20");
      properties.put(DefaultClient.CONNECTION_POOL_KEEP_ALIVE_DURATION_PROP_NAME, "59000");
      properties.put(DefaultClient.API_CONNECT_TIMEOUT_PROP_NAME, "10000");
      properties.put(DefaultClient.API_READ_TIMEOUT_PROP_NAME, "30000");
      properties.put(DefaultClient.API_WRITE_TIMEOUT_PROP_NAME, "30000");
      properties.put(DefaultClient.API_TIMEOUT_PROP_NAME, "30000");

      var client = new DefaultClient(properties);
      client.setDispatcher(128, 32);
      client.setConnectionPool(20, Duration.ofSeconds(59));
      client.setTimeouts(
          Duration.ofSeconds(10),
          Duration.ofSeconds(30),
          Duration.ofSeconds(30),
          Duration.ofSeconds(30));
      DefaultClient.setInstance(client);

      Assertions.assertSame(client, DefaultClient.getInstance());
      assertConfiguredHttpClient(client, 128, 32, 20, 59, 10_000, 30_000, 30_000, 30_000);
    } finally {
      setDefaultClientInstance(originalInstance);
    }
  }

  @Test
  @DisplayName("README and DOCS options plus client snippet configures the documented settings")
  void docsOptionsAndClientSnippetConfiguresDocumentedSettings() {
    var properties = baseProperties();
    var options =
        DefaultClient.HttpClientOptions.builder()
            .dispatcher(128, 32)
            .connectionPool(20, Duration.ofSeconds(59))
            .connectTimeout(Duration.ofSeconds(10))
            .readTimeout(Duration.ofSeconds(30))
            .writeTimeout(Duration.ofSeconds(30))
            .callTimeout(Duration.ofSeconds(30))
            .build();

    var client = new DefaultClient(properties, options);

    assertConfiguredHttpClient(client, 128, 32, 20, 59, 10_000, 30_000, 30_000, 30_000);
  }

  @Test
  @DisplayName("README and DOCS options builder snippet builds the documented settings")
  void docsOptionsBuilderSnippetBuildsDocumentedSettings() {
    var options =
        DefaultClient.HttpClientOptions.builder()
            .dispatcher(128, 32)
            .connectionPool(20, Duration.ofSeconds(59))
            .connectTimeout(Duration.ofSeconds(10))
            .readTimeout(Duration.ofSeconds(30))
            .writeTimeout(Duration.ofSeconds(30))
            .callTimeout(Duration.ofSeconds(30))
            .build();

    var client = new DefaultClient(baseProperties(), options);

    assertConfiguredHttpClient(client, 128, 32, 20, 59, 10_000, 30_000, 30_000, 30_000);
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

    assertConfiguredHttpClient(client, 72, 16, 15, 30, 4_000, 14_000, 16_000, 22_000);
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

  private static DefaultClient getDefaultClientInstance() {
    return (DefaultClient) readStaticField(DefaultClient.class, "defaultInstance");
  }

  private static void setDefaultClientInstance(DefaultClient client) {
    writeStaticField(DefaultClient.class, "defaultInstance", client);
  }

  private static void assertConfiguredHttpClient(
      DefaultClient client,
      int maxRequests,
      int maxRequestsPerHost,
      int maxIdleConnections,
      long keepAliveSeconds,
      int connectTimeoutMillis,
      int readTimeoutMillis,
      int writeTimeoutMillis,
      int callTimeoutMillis) {
    var okHttpClient = getOkHttpClient(client);
    var pool = okHttpClient.connectionPool();

    Assertions.assertEquals(
        maxIdleConnections, readIntField(poolDelegate(pool), "maxIdleConnections"));
    Assertions.assertEquals(
        Duration.ofSeconds(keepAliveSeconds).toNanos(),
        readLongField(poolDelegate(pool), "keepAliveDurationNs"));
    Assertions.assertEquals(maxRequests, readIntField(okHttpClient.dispatcher(), "maxRequests"));
    Assertions.assertEquals(
        maxRequestsPerHost, readIntField(okHttpClient.dispatcher(), "maxRequestsPerHost"));
    Assertions.assertEquals(connectTimeoutMillis, okHttpClient.connectTimeoutMillis());
    Assertions.assertEquals(readTimeoutMillis, okHttpClient.readTimeoutMillis());
    Assertions.assertEquals(writeTimeoutMillis, okHttpClient.writeTimeoutMillis());
    Assertions.assertEquals(callTimeoutMillis, okHttpClient.callTimeoutMillis());
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

  private static Object readStaticField(Class<?> type, String fieldName) {
    try {
      Field field = type.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(null);
    } catch (NoSuchFieldException | IllegalAccessException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private static void writeStaticField(Class<?> type, String fieldName, Object value) {
    try {
      Field field = type.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(null, value);
    } catch (NoSuchFieldException | IllegalAccessException ex) {
      throw new IllegalStateException(ex);
    }
  }
}
