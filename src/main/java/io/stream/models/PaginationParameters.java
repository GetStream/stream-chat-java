package io.stream.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaginationParameters {
  @Nullable
  @JsonProperty("limit")
  private Integer limit;

  @Nullable
  @JsonProperty("offset")
  private Integer offset;

  @Nullable
  @JsonProperty("id_gte")
  private String idGte;

  @Nullable
  @JsonProperty("id_gt")
  private String idGt;

  @Nullable
  @JsonProperty("id_lte")
  private String idLte;

  @Nullable
  @JsonProperty("id_lt")
  private String idLt;

  private PaginationParameters(Builder builder) {
    this.limit = builder.limit;
    this.offset = builder.offset;
    this.idGte = builder.idGte;
    this.idGt = builder.idGt;
    this.idLte = builder.idLte;
    this.idLt = builder.idLt;
  }

  /**
   * Creates builder to build {@link PaginationParameters}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder to build {@link PaginationParameters}. */
  public static final class Builder {
    private Integer limit;
    private Integer offset;
    private String idGte;
    private String idGt;
    private String idLte;
    private String idLt;

    private Builder() {}

    @NotNull
    public Builder withLimit(@NotNull Integer limit) {
      this.limit = limit;
      return this;
    }

    @NotNull
    public Builder withOffset(@NotNull Integer offset) {
      this.offset = offset;
      return this;
    }

    @NotNull
    public Builder withIdGte(@NotNull String idGte) {
      this.idGte = idGte;
      return this;
    }

    @NotNull
    public Builder withIdGt(@NotNull String idGt) {
      this.idGt = idGt;
      return this;
    }

    @NotNull
    public Builder withIdLte(@NotNull String idLte) {
      this.idLte = idLte;
      return this;
    }

    @NotNull
    public Builder withIdLt(@NotNull String idLt) {
      this.idLt = idLt;
      return this;
    }

    @NotNull
    public PaginationParameters build() {
      return new PaginationParameters(this);
    }
  }
}
