package io.getstream.chat.java;

import io.getstream.chat.java.models.Import;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ImportTests extends BasicTest {
  @DisplayName("Can run imports")
  @Test
  void importTestEnd2End() {
    var createUrlResponse =
        Assertions.assertDoesNotThrow(
            () -> Import.createImportUrl("streamchatjava.json").request());

    uploadJsonToS3(createUrlResponse);

    var createImportResponse =
        Assertions.assertDoesNotThrow(
            () ->
                Import.createImport(createUrlResponse.getPath(), Import.ImportMode.Upsert)
                    .request());

    var getImportResponse =
        Assertions.assertDoesNotThrow(
            () -> Import.getImport(createImportResponse.getImportTask().getId()).request());
    Assertions.assertEquals(
        createImportResponse.getImportTask().getId(), getImportResponse.getImportTask().getId());

    var listImportResponse =
        Assertions.assertDoesNotThrow(() -> Import.listImports(1, 0).request());
    Assertions.assertEquals(1, listImportResponse.getImportTasks().size());
  }

  private void uploadJsonToS3(Import.CreateImportUrlResponse createUrlResponse) {
    var client =
        new OkHttpClient.Builder()
            .addNetworkInterceptor(
                chain -> {
                  var request =
                      chain
                          .request()
                          .newBuilder()
                          .removeHeader("Accept-Encoding")
                          .removeHeader("User-Agent")
                          .removeHeader("Connection")
                          .removeHeader("Content-Type")
                          .addHeader("Content-Type", "application/json")
                          .build();

                  return chain.proceed(request);
                })
            .build();

    var request =
        new Request.Builder()
            .url(createUrlResponse.getUploadUrl())
            .put(RequestBody.create(MediaType.parse("application/json"), "{}"))
            .build();

    var response = Assertions.assertDoesNotThrow(() -> client.newCall(request).execute());
    Assertions.assertEquals(200, response.code());
  }
}
