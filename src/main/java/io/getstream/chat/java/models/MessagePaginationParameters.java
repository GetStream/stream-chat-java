package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Builder;
import org.jetbrains.annotations.Nullable;

@Builder
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
}
