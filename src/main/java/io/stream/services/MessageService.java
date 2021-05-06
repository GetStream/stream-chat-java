package io.stream.services;

import io.stream.models.Message.MessageDeleteResponse;
import io.stream.models.Message.MessageGetManyResponse;
import io.stream.models.Message.MessageGetRepliesResponse;
import io.stream.models.Message.MessageGetResponse;
import io.stream.models.Message.MessageRunCommandActionRequestData;
import io.stream.models.Message.MessageRunCommandActionResponse;
import io.stream.models.Message.MessageSearchRequestData;
import io.stream.models.Message.MessageSearchResponse;
import io.stream.models.Message.MessageSendRequestData;
import io.stream.models.Message.MessageSendResponse;
import io.stream.models.Message.MessageTranslateRequestData;
import io.stream.models.Message.MessageTranslateResponse;
import io.stream.models.Message.MessageUpdateRequestData;
import io.stream.models.Message.MessageUpdateResponse;
import io.stream.models.Message.MessageUploadFileResponse;
import io.stream.models.Message.MessageUploadImageResponse;
import io.stream.models.framework.StreamResponseObject;
import io.stream.services.framework.ToJson;
import java.util.Date;
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

  @GET("messages/{id}")
  Call<MessageGetResponse> get(@NotNull @Path("id") String id);

  @GET("channels/{type}/{id}/messages")
  Call<MessageGetManyResponse> getMany(
      @NotNull @Path("type") String channelType,
      @NotNull @Path("id") String channelId,
      @NotNull @Query("ids") String messageIds);

  @GET("messages/{parent_id}/replies")
  Call<MessageGetRepliesResponse> getReplies(
      @NotNull @Path("parent_id") String parentId,
      @Nullable @Query("id_gte") String idGte,
      @Nullable @Query("id_gt") String idGt,
      @Nullable @Query("id_lte") String idLte,
      @Nullable @Query("id_lt") String idLt,
      @Nullable @Query("created_at_after_or_equal") Date createdAtAfterOrEqual,
      @Nullable @Query("created_at_after") Date createdAtAfter,
      @Nullable @Query("created_at_before_or_equal") Date createdAtBeforeOrEqual,
      @Nullable @Query("created_at_before") Date createdAtBefore);

  @POST("messages/{id}/action")
  Call<MessageRunCommandActionResponse> runCommandAction(
      @NotNull @Path("id") String messageId,
      @NotNull @Body MessageRunCommandActionRequestData internalBuild);

  @POST("messages/{id}/translate")
  Call<MessageTranslateResponse> translate(
      @NotNull @Path("id") String messageId,
      @NotNull @Body MessageTranslateRequestData internalBuild);
}
