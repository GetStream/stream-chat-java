package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.Blocklist.BlocklistCreateRequestData.BlocklistCreateRequest;
import io.getstream.chat.java.models.Blocklist.BlocklistUpdateRequestData.BlocklistUpdateRequest;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.BlocklistService;
import io.getstream.chat.java.services.framework.Client;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
  @Getter
  @EqualsAndHashCode
  public static class BlocklistCreateRequestData {
    @Nullable
    @JsonProperty("name")
    private String name;

    @Nullable
    @JsonProperty("words")
    private List<String> words;

    public static class BlocklistCreateRequest extends StreamRequest<StreamResponseObject> {
      @Override
      protected Call<StreamResponseObject> generateCall(Client client) {
        return client.create(BlocklistService.class).create(this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  @Getter
  @EqualsAndHashCode
  public static class BlocklistGetRequest extends StreamRequest<BlocklistGetResponse> {
    @NotNull private String name;

    @Override
    protected Call<BlocklistGetResponse> generateCall(Client client) {
      return client.create(BlocklistService.class).get(name);
    }
  }

  @Builder(
      builderClassName = "BlocklistUpdateRequest",
      builderMethodName = "",
      buildMethodName = "internalBuild")
  @Getter
  @EqualsAndHashCode
  public static class BlocklistUpdateRequestData {
    @Nullable
    @JsonProperty("words")
    private List<String> words;

    public static class BlocklistUpdateRequest extends StreamRequest<StreamResponseObject> {
      @NotNull private String name;

      private BlocklistUpdateRequest(@NotNull String name) {
        this.name = name;
      }

      @Override
      protected Call<StreamResponseObject> generateCall(Client client) {
        return client.create(BlocklistService.class).update(name, this.internalBuild());
      }
    }
  }

  @RequiredArgsConstructor
  @Getter
  @EqualsAndHashCode
  public static class BlocklistDeleteRequest extends StreamRequest<StreamResponseObject> {
    @NotNull private String name;

    @Override
    protected Call<StreamResponseObject> generateCall(Client client) {
      return client.create(BlocklistService.class).delete(name);
    }
  }

  public static class BlocklistListRequest extends StreamRequest<BlocklistListResponse> {
    @Override
    protected Call<BlocklistListResponse> generateCall(Client client) {
      return client.create(BlocklistService.class).list();
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
