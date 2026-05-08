package io.getstream.chat.java;

import io.getstream.chat.java.models.App;
import io.getstream.chat.java.models.Event;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("ungzipPayload passes through plain bytes unchanged")
  void ungzipPayload_passthroughPlainBytes() {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    Assertions.assertArrayEquals(raw, App.ungzipPayload(raw));
  }

  @Test
  @DisplayName("ungzipPayload inflates gzip-magic bytes")
  void ungzipPayload_inflatesGzip() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    Assertions.assertArrayEquals(raw, App.ungzipPayload(gzip(raw)));
  }

  @Test
  @DisplayName("ungzipPayload returns empty input unchanged")
  void ungzipPayload_emptyInput() {
    Assertions.assertArrayEquals(new byte[0], App.ungzipPayload(new byte[0]));
  }

  @Test
  @DisplayName("ungzipPayload throws on truncated gzip with magic")
  void ungzipPayload_truncatedGzipThrows() {
    byte[] bad = new byte[] {0x1f, (byte) 0x8b, 0x08, 0, 0, 0};
    Assertions.assertThrows(IllegalStateException.class, () -> App.ungzipPayload(bad));
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
  @DisplayName("decodeSnsPayload aliases decodeSqsPayload")
  void decodeSnsPayload_aliasesSqs() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String wrapped = base64(gzip(raw));
    Assertions.assertArrayEquals(App.decodeSqsPayload(wrapped), App.decodeSnsPayload(wrapped));
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
  @DisplayName("verifyAndParseSns and verifyAndParseSqs return identical events")
  void verifyAndParseSns_matchesSqs() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    String wrapped = base64(gzip(raw));
    Event sns = App.verifyAndParseSns(wrapped, sig, API_SECRET);
    Event sqs = App.verifyAndParseSqs(wrapped, sig, API_SECRET);
    Assertions.assertEquals(sqs.getType(), sns.getType());
  }

  @Test
  @DisplayName("verifyWebhookSignature backward compatibility still validates HMAC")
  void verifyWebhookSignature_backwardCompat() throws Exception {
    String sig = hmacSHA256Hex(API_SECRET, JSON_BODY.getBytes(StandardCharsets.UTF_8));
    Assertions.assertTrue(App.verifyWebhookSignature(API_SECRET, JSON_BODY, sig));
    Assertions.assertFalse(App.verifyWebhookSignature(API_SECRET, JSON_BODY, "0".repeat(64)));
  }
}
