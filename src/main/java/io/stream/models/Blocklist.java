package io.stream.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.Blocklist.BlocklistCreateRequestData.BlocklistCreateRequest;
import io.stream.models.Blocklist.BlocklistUpdateRequestData.BlocklistUpdateRequest;
import io.stream.models.framework.StreamRequest;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.BlocklistService;
import io.stream.services.framework.StreamServiceGenerator;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;

@Data
@NoArgsConstructor
public class Blocklist {
  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

  @NotNull
  @JsonProperty("name")
  private String name;

  @NotNull
  @JsonProperty("words")
  private List<String> words;

  @Builder(
      builderClassName = "BlocklistCreateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class BlocklistCreateRequestData {
    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("words")
    private List<String> words;

    public static class BlocklistCreateRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(BlocklistService.class)
            .create(this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class BlocklistGetRequest extends StreamRequest<BlocklistGetResponse> {
    @NotNull private String name;

    @Override
    protected Call<BlocklistGetResponse> generateCall() {
      return StreamServiceGenerator.createService(BlocklistService.class).get(name);
    }
  }

  @Builder(
      builderClassName = "BlocklistUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  public static class BlocklistUpdateRequestData {
    @NotNull
    @JsonProperty("words")
    private List<String> words;

    public static class BlocklistUpdateRequest extends StreamRequest<StreamResponseObject> {
      @NotNull private String name;

      private BlocklistUpdateRequest(@NotNull String name) {
        this.name = name;
      }

      @Override
      protected Call<StreamResponseObject> generateCall() {
        return StreamServiceGenerator.createService(BlocklistService.class)
            .update(name, this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  public static class BlocklistDeleteRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String name;

    @Override
    protected Call<StreamResponseObject> generateCall() {
      return StreamServiceGenerator.createService(BlocklistService.class).delete(name);
    }
  }

  public static class BlocklistListRequest extends StreamRequest<BlocklistListResponse> {
    @Override
    protected Call<BlocklistListResponse> generateCall() {
      return StreamServiceGenerator.createService(BlocklistService.class).list();
    }
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class BlocklistGetResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("blocklist")
    private Blocklist blocklist;
  }

  @Data
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class BlocklistListResponse extends StreamResponseObject {
    @NotNull
    @JsonProperty("blocklists")
    private List<Blocklist> blocklists;
  }

  /**
   * Creates a create request
   *
   * @return the created request
   */
  @NotNull
  public static BlocklistCreateRequest create() {
    return new BlocklistCreateRequest();
  }

  /**
   * Creates a get request
   *
   * @param name the blocklist name
   * @return the created request
   */
  @NotNull
  public static BlocklistGetRequest get(@NotNull String name) {
    return new BlocklistGetRequest(name);
  }

  /**
   * Creates an update request
   *
   * @param name the blocklist name
   * @return the created request
   */
  @NotNull
  public static BlocklistUpdateRequest update(@NotNull String name) {
    return new BlocklistUpdateRequest(name);
  }

  /**
   * Creates a delete request
   *
   * @param name the blocklist name
   * @return the created request
   */
  @NotNull
  public static BlocklistDeleteRequest delete(@NotNull String name) {
    return new BlocklistDeleteRequest(name);
  }

  /**
   * Creates a list request
   *
   * @return the created request
   */
  @NotNull
  public static BlocklistListRequest list() {
    return new BlocklistListRequest();
  }
}
