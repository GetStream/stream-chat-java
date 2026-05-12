package io.getstream.chat.java.services.framework;

import io.getstream.chat.java.models.App;
import io.getstream.chat.java.models.Event;
import java.time.Duration;
import org.jetbrains.annotations.NotNull;

public interface Client {
  @NotNull
  <TService> TService create(Class<TService> svcClass);

  default @NotNull <TService> TService create(Class<TService> svcClass, String userToken) {
    return create(svcClass);
  }

  @NotNull
  String getApiKey();

  @NotNull
  String getApiSecret();

  void setTimeout(@NotNull Duration timeoutDuration);

  /**
   * Verify and parse an HTTP webhook event using this client's API secret.
   *
   * <p>Instance-method counterpart to {@link App#verifyAndParseWebhook(byte[], String, String)};
   * the call site stays a two-argument one-liner because the secret comes from the client.
   *
   * @param body raw HTTP request body bytes Stream signed
   * @param signature value of the {@code X-Signature} header
   * @return parsed {@link Event}
   */
  default @NotNull Event verifyAndParseWebhook(@NotNull byte[] body, @NotNull String signature) {
    return App.verifyAndParseWebhook(body, signature, getApiSecret());
  }

  /**
   * Parse an SQS-delivered webhook body (decode only).
   *
   * <p>Instance-method counterpart to {@link App#parseSqs(String)}.
   *
   * @param body SQS message {@code Body} string
   * @return parsed {@link Event}
   */
  default @NotNull Event parseSqs(@NotNull String body) {
    return App.parseSqs(body);
  }

  /**
   * Parse an SNS-delivered webhook body (unwrap envelope when needed).
   *
   * <p>Instance-method counterpart to {@link App#parseSns(String)}.
   *
   * @param notificationBody raw SNS POST body or pre-extracted {@code Message}
   * @return parsed {@link Event}
   */
  default @NotNull Event parseSns(@NotNull String notificationBody) {
    return App.parseSns(notificationBody);
  }

  static Client getInstance() {
    return DefaultClient.getInstance();
  }
}
