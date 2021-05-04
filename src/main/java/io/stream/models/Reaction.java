package io.stream.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.stream.models.User.UserRequestObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class Reaction {
  @NotNull
  @JsonProperty("message_id")
  private String messageId;

  @NotNull
  @JsonProperty("user_id")
  private String userId;

  @NotNull
  @JsonProperty("type")
  private String type;

  @Nullable
  @JsonProperty("score")
  private Integer score;

  @NotNull
  @JsonProperty("created_at")
  private Date createdAt;

  @NotNull
  @JsonProperty("updated_at")
  private Date updatedAt;

  @NotNull @JsonIgnore private Map<String, Object> additionalFields;

  public Reaction() {
    additionalFields = new HashMap<>();
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return this.additionalFields;
  }

  @JsonAnySetter
  public void setAdditionalField(String name, Object value) {
    this.additionalFields.put(name, value);
  }

  @Builder
  public static class ReactionRequestObject {
    @NotNull
    @JsonProperty("message_id")
    private String messageId;

    @Nullable
    @JsonProperty("user_id")
    private String userId;

    @Nullable
    @JsonProperty("user")
    private UserRequestObject user;

    @NotNull
    @JsonProperty("type")
    private String type;

    @Nullable
    @JsonProperty("score")
    private Integer score;

    @Singular @Nullable @JsonIgnore private Map<String, Object> additionalFields;

    @JsonAnyGetter
    public Map<String, Object> getAdditionalFields() {
      return this.additionalFields;
    }

    @JsonAnySetter
    public void setAdditionalField(String name, Object value) {
      this.additionalFields.put(name, value);
    }
  }
}
