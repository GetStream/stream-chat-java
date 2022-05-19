package io.getstream.chat.java.services;

import io.getstream.chat.java.models.Import;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.*;

public interface ImportService {
  @POST("import_urls")
  Call<Import.CreateImportUrlResponse> createImportUrl(
      @NotNull @Body Import.CreateImportUrlRequestData createImportUrlRequestData);

  @POST("imports")
  Call<Import.CreateImportResponse> createImport(
      @NotNull @Body Import.CreateImportRequestData createImportRequestData);

  @GET("imports/{id}")
  Call<Import.GetImportResponse> getImport(@NotNull @Path("id") String id);

  @GET("imports")
  Call<Import.ListImportsResponse> listImports(
      @Nullable @Query("limit") Integer limit, @Nullable @Query("offset") Integer offset);
}
