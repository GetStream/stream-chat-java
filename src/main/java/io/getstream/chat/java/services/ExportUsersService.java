package io.getstream.chat.java.services;

import io.getstream.chat.java.models.ExportUsers;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.*;

public interface ExportUsersService {
  @POST("export/users")
  Call<ExportUsers.ExportUsersResponse> exportUsers(
      @NotNull @Body ExportUsers.ExportUsersRequestData exportUsersRequest);
}
