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
  @DisplayName("DefaultClient uses configured connection pool properties")
  void givenConnectionPoolProperties_whenCreatingClient_thenUsesConfiguredPool() {
    var properties = baseProperties();
    properties.put(DefaultClient.CONNECTION_POOL_MAX_IDLE_CONNECTIONS_PROP_NAME, "20");
    properties.put(DefaultClient.CONNECTION_POOL_KEEP_ALIVE_DURATION_PROP_NAME, "120000");

    var client = new DefaultClient(properties);
    var pool = getConnectionPool(client);

    Assertions.assertEquals(20, readIntField(poolDelegate(pool), "maxIdleConnections"));
    Assertions.assertEquals(
        Duration.ofMinutes(2).toNanos(), readLongField(poolDelegate(pool), "keepAliveDurationNs"));
  }

  @Test
  @DisplayName("DefaultClient can update connection pool at runtime")
  void whenSettingConnectionPool_thenRebuildsClientWithNewPool() {
    var client = new DefaultClient(baseProperties());

    client.setConnectionPool(15, Duration.ofSeconds(30));

    var pool = getConnectionPool(client);
    Assertions.assertEquals(15, readIntField(poolDelegate(pool), "maxIdleConnections"));
    Assertions.assertEquals(
        Duration.ofSeconds(30).toNanos(), readLongField(poolDelegate(pool), "keepAliveDurationNs"));
  }

  private static Properties baseProperties() {
    var properties = new Properties();
    properties.put(DefaultClient.API_KEY_PROP_NAME, "test-key");
    properties.put(DefaultClient.API_SECRET_PROP_NAME, "test-secret");
    return properties;
  }

  private static ConnectionPool getConnectionPool(DefaultClient client) {
    Retrofit retrofit = (Retrofit) readField(client, "retrofit");
    OkHttpClient okHttpClient = (OkHttpClient) readField(retrofit, "callFactory");
    return okHttpClient.connectionPool();
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
