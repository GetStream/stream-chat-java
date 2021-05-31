package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.jetbrains.annotations.Nullable;

@Builder
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
}
