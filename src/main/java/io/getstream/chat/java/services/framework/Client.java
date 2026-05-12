package io.getstream.chat.java.services.framework;

import io.getstream.chat.java.exceptions.InvalidWebhookException;
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
  default @NotNull Event verifyAndParseWebhook(@NotNull byte[] body, @NotNull String signature)
      throws InvalidWebhookException {
    return App.verifyAndParseWebhook(body, signature, getApiSecret());
  }

  /**
   * Verify and parse an SQS firehose webhook event using this client's API secret.
   *
   * <p>Instance-method counterpart to {@link App#verifyAndParseSqs(String, String, String)}.
   *
   * @param messageBody SQS message {@code Body} (UTF-8 string)
   * @param signature value of the {@code X-Signature} message attribute
   * @return parsed {@link Event}
   */
  default @NotNull Event verifyAndParseSqs(@NotNull String messageBody, @NotNull String signature)
      throws InvalidWebhookException {
    return App.verifyAndParseSqs(messageBody, signature, getApiSecret());
  }

  /**
   * Verify and parse an SNS firehose webhook event using this client's API secret.
   *
   * <p>Instance-method counterpart to {@link App#verifyAndParseSns(String, String, String)}.
   *
   * @param message SNS notification {@code Message} field (UTF-8 string)
   * @param signature value of the {@code X-Signature} message attribute
   * @return parsed {@link Event}
   */
  default @NotNull Event verifyAndParseSns(@NotNull String message, @NotNull String signature)
      throws InvalidWebhookException {
    return App.verifyAndParseSns(message, signature, getApiSecret());
  }

  static Client getInstance() {
    return DefaultClient.getInstance();
  }
}
