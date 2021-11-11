package io.getstream.chat.java.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class StreamException extends Exception {
  private static final long serialVersionUID = 1L;

  @Getter private ResponseData responseData;

  public StreamException(String message, ResponseData responseData) {
    super(message);
    this.responseData = responseData;
  }

  public StreamException(String message, Throwable t) {
    super(message, t);
  }

  public StreamException(Throwable t) {
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
  @Deprecated
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
   * Builds a StreamException based on response from the server and http code
   *
   * @param httpResponse Stream API response
   * @return the StreamException
   */
  public static StreamException build(Response<?> httpResponse) {
    StreamException exception;

    ResponseBody errorBody = httpResponse.errorBody();
    if (errorBody != null) {
      exception = StreamException.build(errorBody);
    } else {
      exception =
          StreamException.build(
              String.format("Unexpected server response code %d", httpResponse.code()));
    }

    if (exception.responseData == null) {
      ResponseData responseData = new ResponseData();
      responseData.statusCode = httpResponse.code();
      exception.responseData = responseData;
    }

    return exception;
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
