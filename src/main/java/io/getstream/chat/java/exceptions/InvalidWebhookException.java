package io.getstream.chat.java.exceptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Raised by every webhook ingestion primitive when the request cannot be safely turned into a typed
 * event. A single exception type lets handler code use one catch arm and, when needed, branch on
 * the failure-mode message constants exposed here.
 */
public class InvalidWebhookException extends StreamException {
  private static final long serialVersionUID = 1L;

  public static final String SIGNATURE_MISMATCH = "signature mismatch";
  public static final String INVALID_BASE64 = "invalid base64 encoding";
  public static final String GZIP_FAILED = "gzip decompression failed";
  public static final String INVALID_JSON = "invalid JSON payload";

  public InvalidWebhookException(@NotNull String message) {
    super(message, (Throwable) null);
  }

  public InvalidWebhookException(@NotNull String message, @Nullable Throwable cause) {
    super(message, cause);
  }
}
