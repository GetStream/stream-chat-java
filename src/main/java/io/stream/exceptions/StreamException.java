package io.stream.exceptions;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import okhttp3.ResponseBody;

public class StreamException extends Exception {

  private static final long serialVersionUID = 1L;

  @Getter
  private ResponseData responseData;

  private StreamException(String message, ResponseData responseData) {
    super(message);
    this.responseData = responseData;
  }

  private StreamException(IOException e) {
    super(e);
  }

  /**
   * Builds a StreamException to signal a configuration problem
   * 
   * @param configurationErrorMessage the configuration problem description
   * @return the StreamException
   */
  public static StreamException build(String configurationErrorMessage) {
    return new StreamException(configurationErrorMessage, null);
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
      ResponseData responseData = objectMapper.readValue(responseBody.string(), ResponseData.class);
      return new StreamException(responseData.getMessage(), responseData);
    } catch (IOException e) {
      return new StreamException(e);
    }
  }

  /**
   * Builds a StreamException when an IOException occurs calling the API
   * 
   * @param e the underlying IOException
   * @return the StreamException
   */
  public static StreamException build(IOException e) {
    return new StreamException(e);
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
