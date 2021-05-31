package io.getstream.chat.java.models.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Message.ImageSizeRequestObject;
import io.getstream.chat.java.models.Message.MessageUploadFileResponse;
import io.getstream.chat.java.models.Message.MessageUploadImageResponse;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.services.MessageService;
import io.getstream.chat.java.services.framework.StreamServiceGenerator;
import io.getstream.chat.java.services.framework.StreamServiceHandler;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import lombok.extern.java.Log;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

@Log
public class DefaultFileHandler implements FileHandler {

  @Override
  public MessageUploadFileResponse uploadFile(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable File file,
      @Nullable String contentType)
      throws StreamException {
    return new StreamServiceHandler()
        .handle(generateUploadFileCall(channelType, channelId, userId, file, contentType));
  }

  @Override
  public MessageUploadImageResponse uploadImage(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable File file,
      @Nullable String contentType,
      @Nullable List<ImageSizeRequestObject> uploadSizes)
      throws StreamException {
    return new StreamServiceHandler()
        .handle(
            generateUploadImageCall(
                channelType, channelId, userId, file, contentType, uploadSizes));
  }

  @Override
  public StreamResponseObject deleteFile(
      @NotNull String channelType, @NotNull String channelId, @NotNull String url)
      throws StreamException {
    return new StreamServiceHandler().handle(generateDeleteFileCall(channelType, channelId, url));
  }

  @Override
  public StreamResponseObject deleteImage(
      @NotNull String channelType, @NotNull String channelId, @NotNull String url)
      throws StreamException {
    return new StreamServiceHandler().handle(generateDeleteImageCall(channelType, channelId, url));
  }

  @Override
  public void uploadFileAsync(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable File file,
      @Nullable String contentType,
      @Nullable Consumer<MessageUploadFileResponse> onSuccess,
      @Nullable Consumer<StreamException> onError) {
    try {
      new StreamServiceHandler()
          .handleAsync(
              generateUploadFileCall(channelType, channelId, userId, file, contentType),
              onSuccess,
              onError);
    } catch (StreamException e) {
      if (onError != null) {
        onError.accept(e);
      }
    }
  }

  @Override
  public void uploadImageAsync(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable File file,
      @Nullable String contentType,
      @Nullable List<ImageSizeRequestObject> uploadSizes,
      @Nullable Consumer<MessageUploadImageResponse> onSuccess,
      @Nullable Consumer<StreamException> onError) {
    try {
      new StreamServiceHandler()
          .handleAsync(
              generateUploadImageCall(
                  channelType, channelId, userId, file, contentType, uploadSizes),
              onSuccess,
              onError);
    } catch (StreamException e) {
      if (onError != null) {
        onError.accept(e);
      }
    }
  }

  @Override
  public void deleteFileAsync(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String url,
      @Nullable Consumer<StreamResponseObject> onSuccess,
      @Nullable Consumer<StreamException> onError) {
    new StreamServiceHandler()
        .handleAsync(generateDeleteFileCall(channelType, channelId, url), onSuccess, onError);
  }

  @Override
  public void deleteImageAsync(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String url,
      @Nullable Consumer<StreamResponseObject> onSuccess,
      @Nullable Consumer<StreamException> onError) {
    new StreamServiceHandler()
        .handleAsync(generateDeleteImageCall(channelType, channelId, url), onSuccess, onError);
  }

  private Call<MessageUploadFileResponse> generateUploadFileCall(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable File file,
      @Nullable String contentType)
      throws StreamException {
    try {
      if (file == null) {
        throw StreamException.build("You should specify the file");
      }
      String resolvedContentType = contentType != null ? contentType : "application/octet-stream";
      RequestBody fileRequestBody = RequestBody.create(MediaType.parse(resolvedContentType), file);
      MultipartBody.Part multipartFile =
          MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
      UserRequestObject user = UserRequestObject.builder().id(userId).build();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      new ObjectMapper().writeValue(baos, user);
      RequestBody userRequestBody = RequestBody.create(MultipartBody.FORM, baos.toString("UTF-8"));
      return StreamServiceGenerator.createService(MessageService.class)
          .uploadFile(channelType, channelId, userRequestBody, multipartFile);
    } catch (IOException e) {
      // This should not happen, can only be a development error
      log.log(
          Level.SEVERE,
          "Seems there is a problem with the conversion of user request object to json",
          e);
      return null;
    }
  }

  private Call<MessageUploadImageResponse> generateUploadImageCall(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable File file,
      @Nullable String contentType,
      @Nullable List<ImageSizeRequestObject> uploadSizes)
      throws StreamException {
    try {
      if (file == null) {
        throw StreamException.build("You should specify the file");
      }
      RequestBody fileRequestBody = RequestBody.create(MediaType.parse(contentType), file);
      MultipartBody.Part multipartFile =
          MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);
      UserRequestObject user = UserRequestObject.builder().id(userId).build();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      new ObjectMapper().writeValue(baos, user);
      RequestBody userRequestBody = RequestBody.create(MultipartBody.FORM, baos.toString("UTF-8"));
      baos = new ByteArrayOutputStream();
      new ObjectMapper().writeValue(baos, uploadSizes);
      RequestBody uploadSizesRequestBody =
          RequestBody.create(MultipartBody.FORM, baos.toString("UTF-8"));
      return StreamServiceGenerator.createService(MessageService.class)
          .uploadImage(
              channelType, channelId, userRequestBody, multipartFile, uploadSizesRequestBody);
    } catch (IOException e) {
      // This should not happen, can only be a development error
      log.log(
          Level.SEVERE,
          "Seems there is a problem with the conversion of user request object or image size"
              + " request object to json",
          e);
      return null;
    }
  }

  private Call<StreamResponseObject> generateDeleteFileCall(
      @NotNull String channelType, @NotNull String channelId, @NotNull String url) {
    return StreamServiceGenerator.createService(MessageService.class)
        .deleteFile(channelType, channelId, url);
  }

  private Call<StreamResponseObject> generateDeleteImageCall(
      @NotNull String channelType, @NotNull String channelId, @NotNull String url) {
    return StreamServiceGenerator.createService(MessageService.class)
        .deleteImage(channelType, channelId, url);
  }
}
