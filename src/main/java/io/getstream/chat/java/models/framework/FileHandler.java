package io.getstream.chat.java.models.framework;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Message.ImageSizeRequestObject;
import io.getstream.chat.java.models.Message.MessageUploadFileResponse;
import io.getstream.chat.java.models.Message.MessageUploadImageResponse;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileHandler {

  MessageUploadFileResponse uploadFile(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable File file,
      @Nullable String contentType)
      throws StreamException;

  MessageUploadImageResponse uploadImage(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable File file,
      @Nullable String contentType,
      @Nullable List<ImageSizeRequestObject> uploadSizes)
      throws StreamException;

  StreamResponseObject deleteFile(
      @NotNull String channelType, @NotNull String channelId, @NotNull String url)
      throws StreamException;

  StreamResponseObject deleteImage(
      @NotNull String channelType, @NotNull String channelId, @NotNull String url)
      throws StreamException;

  void uploadFileAsync(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable File file,
      @Nullable String contentType,
      @Nullable Consumer<MessageUploadFileResponse> onSuccess,
      @Nullable Consumer<StreamException> onError);

  void uploadImageAsync(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String userId,
      @Nullable File file,
      @Nullable String contentType,
      @Nullable List<ImageSizeRequestObject> uploadSizes,
      @Nullable Consumer<MessageUploadImageResponse> onSuccess,
      @Nullable Consumer<StreamException> onError);

  void deleteFileAsync(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String url,
      @Nullable Consumer<StreamResponseObject> onSuccess,
      @Nullable Consumer<StreamException> onError);

  void deleteImageAsync(
      @NotNull String channelType,
      @NotNull String channelId,
      @NotNull String url,
      @Nullable Consumer<StreamResponseObject> onSuccess,
      @Nullable Consumer<StreamException> onError);
}
