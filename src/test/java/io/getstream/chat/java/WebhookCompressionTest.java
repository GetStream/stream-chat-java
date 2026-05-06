package io.getstream.chat.java;

import io.getstream.chat.java.models.App;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
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
  @DisplayName("decompressWebhookBody returns body unchanged when Content-Encoding is empty")
  void decompressWebhookBody_passthroughWhenEncodingEmpty() {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    Assertions.assertArrayEquals(raw, App.decompressWebhookBody(raw, null));
    Assertions.assertArrayEquals(raw, App.decompressWebhookBody(raw, ""));
  }

  @Test
  @DisplayName("decompressWebhookBody round-trips gzip bytes")
  void decompressWebhookBody_gzipRoundTrip() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    byte[] compressed = gzip(raw);
    Assertions.assertTrue(
        compressed.length > 0 && compressed.length != raw.length,
        "fixture sanity: gzipped bytes should differ from raw");
    Assertions.assertArrayEquals(raw, App.decompressWebhookBody(compressed, "gzip"));
  }

  @Test
  @DisplayName("decompressWebhookBody handles Content-Encoding case-insensitively")
  void decompressWebhookBody_caseInsensitiveEncoding() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    byte[] compressed = gzip(raw);
    Assertions.assertArrayEquals(raw, App.decompressWebhookBody(compressed, "GZIP"));
    Assertions.assertArrayEquals(raw, App.decompressWebhookBody(compressed, "  gzip  "));
  }

  @Test
  @DisplayName("decompressWebhookBody rejects every non-gzip Content-Encoding")
  void decompressWebhookBody_nonGzipRejected() {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    for (String encoding : new String[] {"br", "brotli", "zstd", "deflate", "compress", "lz4"}) {
      IllegalStateException ex =
          Assertions.assertThrows(
              IllegalStateException.class,
              () -> App.decompressWebhookBody(raw, encoding),
              "encoding " + encoding + " should be rejected");
      Assertions.assertTrue(
          ex.getMessage().contains("unsupported"),
          "error for " + encoding + " should mention 'unsupported'; got: " + ex.getMessage());
      Assertions.assertTrue(
          ex.getMessage().contains("gzip"),
          "error for "
              + encoding
              + " should point operators back to gzip; got: "
              + ex.getMessage());
    }
  }

  @Test
  @DisplayName("decompressWebhookBody throws when the payload is not actually gzip")
  void decompressWebhookBody_invalidGzipBytes() {
    byte[] notGzip = "not actually gzip".getBytes(StandardCharsets.UTF_8);
    IllegalStateException ex =
        Assertions.assertThrows(
            IllegalStateException.class, () -> App.decompressWebhookBody(notGzip, "gzip"));
    Assertions.assertTrue(ex.getMessage().contains("failed to decompress"));
  }

  @Test
  @DisplayName("verifyWebhookSignature accepts byte[] body and matches the string overload")
  void verifyWebhookSignature_bytesOverload() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);
    Assertions.assertTrue(App.verifyWebhookSignature(API_SECRET, raw, sig));
    Assertions.assertTrue(App.verifyWebhookSignature(API_SECRET, JSON_BODY, sig));
    Assertions.assertFalse(App.verifyWebhookSignature(API_SECRET, raw, "deadbeef"));
  }

  @Test
  @DisplayName(
      "verifyAndDecodeWebhook decompresses gzip and returns raw JSON when signature matches")
  void verifyAndDecodeWebhook_gzipHappyPath() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    byte[] compressed = gzip(raw);
    String sig = hmacSHA256Hex(API_SECRET, raw);

    byte[] decoded = App.verifyAndDecodeWebhook(API_SECRET, compressed, sig, "gzip");
    Assertions.assertArrayEquals(raw, decoded);
  }

  @Test
  @DisplayName("verifyAndDecodeWebhook works for uncompressed bodies (no Content-Encoding)")
  void verifyAndDecodeWebhook_passthroughHappyPath() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    String sig = hmacSHA256Hex(API_SECRET, raw);

    byte[] decoded = App.verifyAndDecodeWebhook(API_SECRET, raw, sig, null);
    Assertions.assertArrayEquals(raw, decoded);

    byte[] decodedEmpty = App.verifyAndDecodeWebhook(API_SECRET, raw, sig, "");
    Assertions.assertArrayEquals(raw, decodedEmpty);
  }

  @Test
  @DisplayName("verifyAndDecodeWebhook throws SecurityException on signature mismatch")
  void verifyAndDecodeWebhook_badSignature() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    byte[] compressed = gzip(raw);

    Assertions.assertThrows(
        SecurityException.class,
        () -> App.verifyAndDecodeWebhook(API_SECRET, compressed, "00", "gzip"));
  }

  @Test
  @DisplayName(
      "verifyAndDecodeWebhook rejects gzip body when signature was computed over compressed bytes")
  void verifyAndDecodeWebhook_signatureMustBeOverUncompressed() throws Exception {
    byte[] raw = JSON_BODY.getBytes(StandardCharsets.UTF_8);
    byte[] compressed = gzip(raw);
    String sigOverCompressed = hmacSHA256Hex(API_SECRET, compressed);

    Assertions.assertThrows(
        SecurityException.class,
        () -> App.verifyAndDecodeWebhook(API_SECRET, compressed, sigOverCompressed, "gzip"));
  }
}
