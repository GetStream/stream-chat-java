package io.stream.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import okhttp3.ResponseBody;

public class StreamException extends Exception {

  private static final long serialVersionUID = 1L;

  @Getter private ResponseData responseData;

  private StreamException(String message, ResponseData responseData) {
    super(message);
    this.responseData = responseData;
  }

  private StreamException(String message, Throwable t) {
    super(message, t);
  }

  private StreamException(Throwable t) {
    super(t);
  }

  /**
   * Builds a StreamException to signal an issue
   *
   * @param issue the issue
   * @return the StreamException
   */
  public static StreamException build(String issue) {
    return new StreamException(issue, (Throwable) null);
  }

  /**
   * Builds a StreamException using the response body when Stream API request fails
   *
   * @param responseBody Stream API response body
   * @return the StreamException
   */
  public static StreamException build(ResponseBody responseBody) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      String responseBodyString = responseBody.string();
      try {
        ResponseData responseData = objectMapper.readValue(responseBodyString, ResponseData.class);
        return new StreamException(responseData.getMessage(), responseData);
      } catch (JsonProcessingException e) {
        return new StreamException(responseBodyString, e);
      }
    } catch (IOException e) {
      return new StreamException(e);
    }
  }

  /**
   * Builds a StreamException when an exception occurs calling the API
   *
   * @param t the underlying exception
   * @return the StreamException
   */
  public static StreamException build(Throwable t) {
    return new StreamException(t);
  }

  @Data
  public static class ResponseData {
    @JsonProperty("code")
    private Integer code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("exception_fields")
    private Map<String, String> exceptionFields;

    @JsonProperty("StatusCode")
    private Integer statusCode;

    @JsonProperty("duration")
    private String duration;

    @JsonProperty("more_info")
    private String moreInfo;
  }
}
