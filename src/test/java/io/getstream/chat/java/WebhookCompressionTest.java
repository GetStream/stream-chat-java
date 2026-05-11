package io.getstream.chat.java;

import io.getstream.chat.java.models.App;
import io.getstream.chat.java.models.Event;
import io.getstream.chat.java.services.framework.Client;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class WebhookCompressionTest {

  private static final String API_SECRET = "tsec2";
  private static final String JSON_BODY =
      "{\"type\":\"message.new\",\"message\":{\"text\":\"the quick brown fox\"}}";

  private static byte[] gzip(byte[] raw) throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try (GZIPOutputStream gz = new GZIPOutputStream(out)) {
      gz.write(raw);
    }
    return out.toByteArray();
  }

  private static String base64(byte[] raw) {
    return Base64.getEncoder().encodeToString(raw);
  }

  private static String hmacSHA256Hex(String secret, byte[] body) throws Exception {
    javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
    mac.init(
        new javax.crypto.spec.SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
    byte[] hmac = mac.doFinal(body);
    StringBuilder hex = new StringBuilder(2 * hmac.length);
    for (byte b : hmac) {
      String h = Integer.toHexString(0xff & b);
      if (h.length() == 1) {
        hex.append('0');
      }
      hex.append(h);
    }
    return hex.toString();
  }

  @Test
  @DisplayName("gunzipPayload passes through plain bytes unchanged")
  void gunzipPayload_passthroughPlainBytes() {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    Assertions.assertArrayEquals(raw, App.gunzipPayload(raw));
  }

  @Test
  @DisplayName("gunzipPayload inflates gzip-magic bytes")
  void gunzipPayload_inflatesGzip() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    Assertions.assertArrayEquals(raw, App.gunzipPayload(gzip(raw)));
  }

  @Test
  @DisplayName("gunzipPayload returns empty input unchanged")
  void gunzipPayload_emptyInput() {
    Assertions.assertArrayEquals(new byte[0], App.gunzipPayload(new byte[0]));
  }

  @Test
  @DisplayName("gunzipPayload throws on truncated gzip with magic")
  void gunzipPayload_truncatedGzipThrows() {
    byte[] bad = new byte[] {0x1f, (byte) 0x8b, 0x08, 0, 0, 0};
    Assertions.assertThrows(IllegalStateException.class, () -> App.gunzipPayload(bad));
  }

  @Test
  @DisplayName("decodeSqsPayload decodes base64 only when no compression")
  void decodeSqsPayload_base64Only() {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    Assertions.assertArrayEquals(raw, App.decodeSqsPayload(base64(raw)));
  }

  @Test
  @DisplayName("decodeSqsPayload decodes base64 + gzip")
  void decodeSqsPayload_base64Gzip() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    Assertions.assertArrayEquals(raw, App.decodeSqsPayload(base64(gzip(raw))));
  }

  @Test
  @DisplayName("decodeSqsPayload throws on malformed base64")
  void decodeSqsPayload_malformedBase64() {
    Assertions.assertThrows(
        IllegalStateException.class, () -> App.decodeSqsPayload("!!!not-base64!!!"));
  }

  @Test
  @DisplayName("decodeSqsPayload decodes Tommaso's plain helloworld fixture")
  void decodeSqsPayload_helloworldBase64Fixture() {
    Assertions.assertArrayEquals(
        "helloworld".getBytes(StandardCharsets.UTF_8), App.decodeSqsPayload("aGVsbG93b3JsZA=="));
  }

  @Test
  @DisplayName("decodeSqsPayload decodes Tommaso's gzipped helloworld fixture")
  void decodeSqsPayload_helloworldBase64GzipFixture() {
    Assertions.assertArrayEquals(
        "helloworld".getBytes(StandardCharsets.UTF_8),
        App.decodeSqsPayload("H4sIAGrYAWoAA8tIzcnJL88vykkBAK0g6/kKAAAA"));
  }

  @Test
  @DisplayName("decodeSnsPayload treats pre-extracted Message identically to decodeSqsPayload")
  void decodeSnsPayload_preExtractedMessage() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String wrapped = base64(gzip(raw));
    Assertions.assertArrayEquals(App.decodeSqsPayload(wrapped), App.decodeSnsPayload(wrapped));
  }

  @Test
  @DisplayName("decodeSnsPayload unwraps a full SNS HTTP notification envelope")
  void decodeSnsPayload_fullEnvelope() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String wrapped = base64(gzip(raw));
    String envelope = snsEnvelope(wrapped);
    Assertions.assertArrayEquals(raw, App.decodeSnsPayload(envelope));
  }

  @Test
  @DisplayName("decodeSnsPayload handles whitespace before envelope JSON")
  void decodeSnsPayload_envelopeWithLeadingWhitespace() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String wrapped = base64(gzip(raw));
    String envelope = "\n  " + snsEnvelope(wrapped);
    Assertions.assertArrayEquals(raw, App.decodeSnsPayload(envelope));
  }

  private static String snsEnvelope(String innerMessage) {
    return "{"
        + "\"Type\":\"Notification\","
        + "\"MessageId\":\"22b80b92-fdea-4c2c-8f9d-bdfb0c7bf324\","
        + "\"TopicArn\":\"arn:aws:sns:us-east-1:123456789012:stream-webhooks\","
        + "\"Message\":\""
        + innerMessage
        + "\","
        + "\"Timestamp\":\"2026-05-11T10:00:00.000Z\","
        + "\"SignatureVersion\":\"1\","
        + "\"MessageAttributes\":{\"X-Signature\":{\"Type\":\"String\",\"Value\":\"placeholder\"}}"
        + "}";
  }

  @Test
  @DisplayName("verifySignature returns true for matching HMAC")
  void verifySignature_matching() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    Assertions.assertTrue(App.verifySignature(raw, sig, API_SECRET));
  }

  @Test
  @DisplayName("verifySignature returns false for mismatched signature")
  void verifySignature_mismatched() {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    Assertions.assertFalse(App.verifySignature(raw, "0".repeat(64), API_SECRET));
  }

  @Test
  @DisplayName("verifySignature returns false when computed over compressed bytes")
  void verifySignature_overCompressedRejected() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    byte[] compressed = gzip(raw);
    String sigOverCompressed = hmacSHA256Hex(API_SECRET, compressed);
    Assertions.assertFalse(App.verifySignature(raw, sigOverCompressed, API_SECRET));
  }

  @Test
  @DisplayName("parseEvent parses known event type into typed Event")
  void parseEvent_known() {
    Event ev = App.parseEvent(JSON_BODY.getBytes(StandardCharsets.UTF_8));
    Assertions.assertEquals("message.new", ev.getType());
    Assertions.assertNotNull(ev.getMessage());
    Assertions.assertEquals("the quick brown fox", ev.getMessage().getText());
  }

  @Test
  @DisplayName("parseEvent handles unknown event types")
  void parseEvent_unknownType() {
    Event ev =
        App.parseEvent(
            "{\"type\":\"a.future.event\",\"custom\":42}".getBytes(StandardCharsets.UTF_8));
    Assertions.assertEquals("a.future.event", ev.getType());
  }

  @Test
  @DisplayName("parseEvent throws on malformed JSON")
  void parseEvent_malformed() {
    Assertions.assertThrows(
        IllegalStateException.class,
        () -> App.parseEvent("not json".getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  @DisplayName("verifyAndParseWebhook parses plain JSON body with valid signature")
  void verifyAndParseWebhook_plain() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    Event ev = App.verifyAndParseWebhook(raw, sig, API_SECRET);
    Assertions.assertEquals("message.new", ev.getType());
  }

  @Test
  @DisplayName("verifyAndParseWebhook parses gzip-compressed body")
  void verifyAndParseWebhook_gzip() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    Event ev = App.verifyAndParseWebhook(gzip(raw), sig, API_SECRET);
    Assertions.assertEquals("message.new", ev.getType());
  }

  @Test
  @DisplayName("verifyAndParseWebhook throws SecurityException on signature mismatch")
  void verifyAndParseWebhook_signatureMismatch() {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    Assertions.assertThrows(
        SecurityException.class, () -> App.verifyAndParseWebhook(raw, "0".repeat(64), API_SECRET));
  }

  @Test
  @DisplayName("verifyAndParseWebhook rejects signature computed over compressed bytes")
  void verifyAndParseWebhook_signatureOverCompressed() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    byte[] compressed = gzip(raw);
    String sigOverCompressed = hmacSHA256Hex(API_SECRET, compressed);
    Assertions.assertThrows(
        SecurityException.class,
        () -> App.verifyAndParseWebhook(compressed, sigOverCompressed, API_SECRET));
  }

  @Test
  @DisplayName("verifyAndParseSqs parses base64-only message body")
  void verifyAndParseSqs_base64Only() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    Event ev = App.verifyAndParseSqs(base64(raw), sig, API_SECRET);
    Assertions.assertEquals("message.new", ev.getType());
  }

  @Test
  @DisplayName("verifyAndParseSqs parses base64 + gzip message body")
  void verifyAndParseSqs_base64Gzip() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    Event ev = App.verifyAndParseSqs(base64(gzip(raw)), sig, API_SECRET);
    Assertions.assertEquals("message.new", ev.getType());
  }

  @Test
  @DisplayName("verifyAndParseSqs rejects signature over wrapped bytes")
  void verifyAndParseSqs_signatureOverWrapped() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String wrapped = base64(gzip(raw));
    String sigOverWrapped = hmacSHA256Hex(API_SECRET, wrapped.getBytes(StandardCharsets.UTF_8));
    Assertions.assertThrows(
        SecurityException.class, () -> App.verifyAndParseSqs(wrapped, sigOverWrapped, API_SECRET));
  }

  @Test
  @DisplayName("verifyAndParseSns parses base64 + gzip notification")
  void verifyAndParseSns_base64Gzip() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    Event ev = App.verifyAndParseSns(base64(gzip(raw)), sig, API_SECRET);
    Assertions.assertEquals("message.new", ev.getType());
  }

  @Test
  @DisplayName(
      "verifyAndParseSns and verifyAndParseSqs return identical events for pre-extracted Message")
  void verifyAndParseSns_matchesSqs() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    String wrapped = base64(gzip(raw));
    Event sns = App.verifyAndParseSns(wrapped, sig, API_SECRET);
    Event sqs = App.verifyAndParseSqs(wrapped, sig, API_SECRET);
    Assertions.assertEquals(sqs.getType(), sns.getType());
  }

  @Test
  @DisplayName("verifyAndParseSns parses a full SNS HTTP notification envelope")
  void verifyAndParseSns_fullEnvelope() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    String envelope = snsEnvelope(base64(gzip(raw)));
    Event ev = App.verifyAndParseSns(envelope, sig, API_SECRET);
    Assertions.assertEquals("message.new", ev.getType());
  }

  @Test
  @DisplayName("verifyAndParseSns rejects signature computed over the envelope JSON")
  void verifyAndParseSns_rejectsSignatureOverEnvelope() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String envelope = snsEnvelope(base64(gzip(raw)));
    String sigOverEnvelope = hmacSHA256Hex(API_SECRET, envelope.getBytes(StandardCharsets.UTF_8));
    Assertions.assertThrows(
        SecurityException.class,
        () -> App.verifyAndParseSns(envelope, sigOverEnvelope, API_SECRET));
  }

  @Test
  @DisplayName("verifyWebhookSignature backward compatibility still validates HMAC")
  void verifyWebhookSignature_backwardCompat() throws Exception {
    String sig = hmacSHA256Hex(API_SECRET, JSON_BODY.getBytes(StandardCharsets.UTF_8));
    Assertions.assertTrue(App.verifyWebhookSignature(API_SECRET, JSON_BODY, sig));
    Assertions.assertFalse(App.verifyWebhookSignature(API_SECRET, JSON_BODY, "0".repeat(64)));
  }

  private static final class StubClient implements Client {
    private final String apiSecret;

    StubClient(String apiSecret) {
      this.apiSecret = apiSecret;
    }

    @Override
    public @NotNull <TService> TService create(Class<TService> svcClass) {
      throw new UnsupportedOperationException("stub client does not create services");
    }

    @Override
    public @NotNull String getApiKey() {
      return "stub-key";
    }

    @Override
    public @NotNull String getApiSecret() {
      return apiSecret;
    }

    @Override
    public void setTimeout(@NotNull Duration timeoutDuration) {}
  }

  @Test
  @DisplayName("Client.verifyAndParseWebhook delegates to static helper with client secret")
  void clientInstance_verifyAndParseWebhook() throws Exception {
    Client client = new StubClient(API_SECRET);
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    Event viaInstance = client.verifyAndParseWebhook(gzip(raw), sig);
    Event viaStatic = App.verifyAndParseWebhook(gzip(raw), sig, API_SECRET);
    Assertions.assertEquals(viaStatic.getType(), viaInstance.getType());
    Assertions.assertEquals("message.new", viaInstance.getType());
  }

  @Test
  @DisplayName("Client.verifyAndParseSqs delegates to static helper with client secret")
  void clientInstance_verifyAndParseSqs() throws Exception {
    Client client = new StubClient(API_SECRET);
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    Event ev = client.verifyAndParseSqs(base64(gzip(raw)), sig);
    Assertions.assertEquals("message.new", ev.getType());
  }

  @Test
  @DisplayName("Client.verifyAndParseSns delegates to static helper with client secret")
  void clientInstance_verifyAndParseSns() throws Exception {
    Client client = new StubClient(API_SECRET);
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    Event ev = client.verifyAndParseSns(base64(gzip(raw)), sig);
    Assertions.assertEquals("message.new", ev.getType());
  }

  @Test
  @DisplayName("Client.verifyAndParseWebhook rejects mismatched signature")
  void clientInstance_verifyAndParseWebhook_rejectsMismatch() {
    Client client = new StubClient(API_SECRET);
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    Assertions.assertThrows(
        SecurityException.class, () -> client.verifyAndParseWebhook(raw, "0".repeat(64)));
  }

  @Nested
  @DisplayName("gunzipPayload golden fixtures")
  class GunzipPayloadTest {

    @Test
    @DisplayName("gunzipPayload inflates Tommaso's helloworld gzip fixture")
    void gunzipPayload_helloworldFixture() {
      Assertions.assertArrayEquals(
          "helloworld".getBytes(StandardCharsets.UTF_8),
          App.gunzipPayload(
              Base64.getDecoder().decode("H4sIAGrYAWoAA8tIzcnJL88vykkBAK0g6/kKAAAA")));
    }
  }
}
