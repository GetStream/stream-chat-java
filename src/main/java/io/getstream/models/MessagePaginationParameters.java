package io.getstream.models;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat
  (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  private Date createdAtAfterOrEqual;

  @Nullable
  @JsonProperty("created_at_after")
  @JsonFormat
  (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  private Date createdAtAfter;

  @Nullable
  @JsonProperty("created_at_before_or_equal")
  @JsonFormat
  (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  private Date createdAtBeforeOrEqual;

  @Nullable
  @JsonProperty("created_at_before")
  @JsonFormat
  (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
  private Date createdAtBefore;
}
