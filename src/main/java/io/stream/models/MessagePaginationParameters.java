package io.stream.models;

import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessagePaginationParameters {
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

  @Nullable
  @JsonProperty("created_at_after_or_equal")
  private Date createdAtAfterOrEqual;

  @Nullable
  @JsonProperty("created_at_after")
  private Date createdAtAfter;

  @Nullable
  @JsonProperty("created_at_before_or_equal")
  private Date createdAtBeforeOrEqual;

  @Nullable
  @JsonProperty("created_at_before")
  private Date createdAtBefore;

  private MessagePaginationParameters(Builder builder) {
    this.limit = builder.limit;
    this.offset = builder.offset;
    this.idGte = builder.idGte;
    this.idGt = builder.idGt;
    this.idLte = builder.idLte;
    this.idLt = builder.idLt;
    this.createdAtAfterOrEqual = builder.createdAtAfterOrEqual;
    this.createdAtAfter = builder.createdAtAfter;
    this.createdAtBeforeOrEqual = builder.createdAtBeforeOrEqual;
    this.createdAtBefore = builder.createdAtBefore;
  }

  /**
   * Creates builder to build {@link MessagePaginationParameters}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder to build {@link MessagePaginationParameters}. */
  public static final class Builder {
    private Integer limit;
    private Integer offset;
    private String idGte;
    private String idGt;
    private String idLte;
    private String idLt;
    private Date createdAtAfterOrEqual;
    private Date createdAtAfter;
    private Date createdAtBeforeOrEqual;
    private Date createdAtBefore;

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
    public Builder withCreatedAtAfterOrEqual(@NotNull Date createdAtAfterOrEqual) {
      this.createdAtAfterOrEqual = createdAtAfterOrEqual;
      return this;
    }

    @NotNull
    public Builder withCreatedAtAfter(@NotNull Date createdAtAfter) {
      this.createdAtAfter = createdAtAfter;
      return this;
    }

    @NotNull
    public Builder withCreatedAtBeforeOrEqual(@NotNull Date createdAtBeforeOrEqual) {
      this.createdAtBeforeOrEqual = createdAtBeforeOrEqual;
      return this;
    }

    @NotNull
    public Builder withCreatedAtBefore(@NotNull Date createdAtBefore) {
      this.createdAtBefore = createdAtBefore;
      return this;
    }

    @NotNull
    public MessagePaginationParameters build() {
      return new MessagePaginationParameters(this);
    }
  }
}
