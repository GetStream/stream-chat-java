package io.stream.services;

import io.stream.models.Message.MessageDeleteResponse;
import io.stream.models.Message.MessageSearchRequestData;
import io.stream.models.Message.MessageSearchResponse;
import io.stream.models.Message.MessageSendRequestData;
import io.stream.models.Message.MessageSendResponse;
import io.stream.models.Message.MessageUpdateRequestData;
import io.stream.models.Message.MessageUpdateResponse;
import io.stream.models.Message.MessageUploadFileResponse;
import io.stream.models.Message.MessageUploadImageResponse;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.framework.ToJson;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MessageService {
  @POST("channels/{type}/{id}/message")
  Call<MessageSendResponse> send(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Body MessageSendRequestData messageSendRequestData);

  @POST("messages/{id}")
  Call<MessageUpdateResponse> update(
      @NotNull @Path("id") String id,
      @NotNull @Body MessageUpdateRequestData messageUpdateRequestData);

  @GET("search")
  Call<MessageSearchResponse> search(
      @NotNull @ToJson @Query("payload") MessageSearchRequestData messageSearchRequestData);

  @Multipart
  @Headers("X-Stream-LogRequestBody: false")
  @POST("channels/{type}/{id}/file")
  Call<MessageUploadFileResponse> uploadFile(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Part("user") RequestBody userRequestBody,
      @NotNull @Part MultipartBody.Part multipartFile);

  @Multipart
  @Headers("X-Stream-LogRequestBody: false")
  @POST("channels/{type}/{id}/image")
  Call<MessageUploadImageResponse> uploadImage(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Part("user") RequestBody userRequestBody,
      @NotNull @Part MultipartBody.Part multipartFile,
      @NotNull @Part("upload_sizes") RequestBody uploadSizesRequestBody);

  @DELETE("channels/{type}/{id}/file")
  Call<StreamResponseObject> deleteFile(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Query("url") String url);

  @DELETE("channels/{type}/{id}/image")
  Call<StreamResponseObject> deleteImage(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Query("url") String url);

  @DELETE("messages/{id}")
  Call<MessageDeleteResponse> delete(
      @NotNull @Path("id") String id, @Nullable @Query("hard") Boolean hard);
}
