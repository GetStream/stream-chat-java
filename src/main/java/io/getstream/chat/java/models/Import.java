package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.ImportService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Import {
  @Nullable
  @JsonProperty("id")
  private String id;

  @Nullable
  @JsonProperty("path")
  private String path;

  @Nullable
  @JsonProperty("mode")
  private ImportMode mode;

  @Nullable
  @JsonProperty("state")
  private ImportState state;

  @Nullable
  @JsonProperty("history")
  private List<ImportHistoryItem> history;

  @Nullable
  @JsonProperty("created_at")
  private Date createdAt;

  @Nullable
  @JsonProperty("updated_at")
  private Date updatedAt;

  public enum ImportMode {
    @JsonProperty("upsert")
    Upsert,
    @JsonProperty("insert")
    Insert,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  public enum ImportState {
    @JsonProperty("uploaded")
    Uploaded,
    @JsonProperty("analyzing")
    Analyzing,
    @JsonProperty("analyzing_failed")
    AnalyzingFailed,
    @JsonProperty("waiting_for_confirmation")
    WaitingForConfirmation,
    @JsonProperty("importing")
    Importing,
    @JsonProperty("importing_failed")
    ImportingFailed,
    @JsonProperty("completed")
    Completed,
    @JsonProperty("failed")
    Failed,
    @JsonEnumDefaultValue
    UNKNOWN
  }

  @Builder(
      builderClassName = "CreateImportUrlRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class CreateImportUrlRequestData {
    @Nullable
    @JsonProperty("filename")
    private String fileName;

    public static class CreateImportUrlRequest
        extends StreamRequest<Import.CreateImportUrlResponse> {
      @Override
      protected Call<Import.CreateImportUrlResponse> generateCall(Client client) {
        return client.create(ImportService.class).createImportUrl(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CreateImportUrlResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("upload_url")
    private String uploadUrl;

    @NotNull
    @JsonProperty("path")
    private String path;
  }

  @Builder(
      builderClassName = "CreateImportRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class CreateImportRequestData {
    @NotNull
    @JsonProperty("path")
    private String path;

    @NotNull
    @JsonProperty("mode")
    private ImportMode mode;

    public static class CreateImportRequest extends StreamRequest<Import.CreateImportResponse> {
      @Override
      protected Call<Import.CreateImportResponse> generateCall(Client client) {
        return client.create(ImportService.class).createImport(this.internalBuild());
      }
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class CreateImportResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("import_task")
    private Import importTask;
  }

  public static class GetImportRequest extends StreamRequest<Import.GetImportResponse> {
    private final String id;

    public GetImportRequest(String id) {
      this.id = id;
    }

    @Override
    protected Call<Import.GetImportResponse> generateCall(Client client) {
      return client.create(ImportService.class).getImport(this.id);
    }
  }

  @Getter
  @EqualsAndHashCode
  public static class ListImportsRequest extends StreamRequest<Import.ListImportsResponse> {
    private final Integer limit;
    private final Integer offset;

    public ListImportsRequest(Integer limit, Integer offset) {
      this.limit = limit;
      this.offset = offset;
    }

    @Override
    protected Call<Import.ListImportsResponse> generateCall(Client client) {
      return client.create(ImportService.class).listImports(this.limit, this.offset);
    }
  }

  public static class GetImportResponse extends CreateImportResponse {}

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ListImportsResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("import_tasks")
    private List<Import> importTasks;
  }

  @Data
  @NoArgsConstructor
  public static class ImportHistoryItem {
    @NotNull
    @JsonProperty("created_at")
    private Date createdAt;

    @NotNull
    @JsonProperty("prev_state")
    private String prevState;

    @NotNull
    @JsonProperty("next_state")
    private String nextState;
  }

  /**
   * Creates a create import url request
   *
   * @param fileName the name of the file to be imported
   * @return the created request
   */
  @NotNull
  public static CreateImportUrlRequestData.CreateImportUrlRequest createImportUrl(
      @NotNull String fileName) {
    return new CreateImportUrlRequestData.CreateImportUrlRequest().fileName(fileName);
  }

  /**
   * Creates a create import request
   *
   * @param path the path returned by createImportUrl endpoint
   * @param mode the import mode
   * @return the created request
   */
  @NotNull
  public static CreateImportRequestData.CreateImportRequest createImport(
      @NotNull String path, @Nullable ImportMode mode) {
    return new CreateImportRequestData.CreateImportRequest().path(path).mode(mode);
  }

  /**
   * Creates a get import request
   *
   * @param id the id of the import
   * @return the created request
   */
  @NotNull
  public static GetImportRequest getImport(@NotNull String id) {
    return new GetImportRequest(id);
  }

  /**
   * Creates a list import request
   *
   * @param limit how many records to return
   * @param offset how many records to skip during pagination
   * @return the created request
   */
  @NotNull
  public static ListImportsRequest listImports(@Nullable Integer limit, @Nullable Integer offset) {
    return new ListImportsRequest(limit, offset);
  }
}
