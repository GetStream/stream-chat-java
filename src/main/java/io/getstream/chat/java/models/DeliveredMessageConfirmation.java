package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@NoArgsConstructor
public class DeliveredMessageConfirmation {
  @NotNull
  @JsonProperty("cid")
  private String cid;

  @NotNull
  @JsonProperty("id")
  private String id;

  @Nullable
  @JsonProperty("parent_id")
  private String parentId;
}
