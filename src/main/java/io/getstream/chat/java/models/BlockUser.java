package io.getstream.chat.java.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.getstream.chat.java.models.framework.StreamRequest;
import io.getstream.chat.java.models.framework.StreamResponseObject;
import io.getstream.chat.java.services.BlockUserService;
import io.getstream.chat.java.services.UserService;
import io.getstream.chat.java.services.framework.Client;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BlockUser {

    @Builder(
            builderClassName = "BlockUserRequest",
            builderMethodName = "",
            buildMethodName = "internalBuild"
    )
    public static class BlockUserRequestData {
        @NotNull
        @JsonProperty("blocked_user_id")
        private String blockedUserID;

        @NotNull
        @JsonProperty("user_id")
        private String userID;

        public static class BlockUserRequest extends StreamRequest<BlockUserResponse> {
            @Override
            protected Call<BlockUserResponse> generateCall(Client client) {
                var data = this.internalBuild();
                return client.create(BlockUserService.class).blockUser(data);
            }
        }
    }

    @Builder(
            builderClassName = "UnblockUserRequest",
            builderMethodName = "",
            buildMethodName = "internalBuild"
    )
    public static class UnblockUserRequestData {
        @NotNull
        @JsonProperty("blocked_user_id")
        private String blockedUserID;

        @NotNull
        @JsonProperty("user_id")
        private String userID;

        public static class UnblockUserRequest extends StreamRequest<UnblockUserResponse> {
            @Override
            protected Call<UnblockUserResponse> generateCall(Client client) {
                var data = this.internalBuild();
                return client.create(BlockUserService.class).unblockUser(data);
            }
        }
    }

    @NotNull
    public static BlockUserRequestData.BlockUserRequest blockUser() {
        return new BlockUserRequestData.BlockUserRequest();
    }

    @NotNull
    public static UnblockUserRequestData.UnblockUserRequest unblockUser() {
        return new UnblockUserRequestData.UnblockUserRequest();
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class BlockUserResponse extends StreamResponseObject {
        @JsonProperty("blocked_by_user_id")
        private String blockedByUserID;

        @JsonProperty("blocked_user_id")
        private String blockedUserID;

        @JsonProperty("created_at")
        private Date createdAt;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class UnblockUserResponse extends StreamResponseObject {
        // Add relevant fields if needed
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class GetBlockedUsersResponse extends StreamResponseObject {
        @JsonProperty("blocks")
        private List<BlockedUserResponse> blockedUsers;
    }

    @Data
    @NoArgsConstructor
    public static class BlockedUserResponse {
        @JsonProperty("user")
        private User blockedByUser;

        @JsonProperty("user_id")
        private String blockedByUserID;

        @JsonProperty("blocked_user")
        private User blockedUser;

        @JsonProperty("blocked_user_id")
        private String blockedUserID;

        @JsonProperty("created_at")
        private Date createdAt;
    }

    @Builder(
            builderClassName = "GetBlockedUsersRequest",
            builderMethodName = "",
            buildMethodName = "internalBuild"
    )
    public static class GetBlockedUsersRequestData {
        @NotNull
        @JsonProperty("user_id")
        private String blockedByUserID;

        public static class GetBlockedUsersRequest extends StreamRequest<GetBlockedUsersResponse> {
            private String blockedByUserID;

            public GetBlockedUsersRequest(String blockedByUserID) {
                this.blockedByUserID = blockedByUserID;
            }

            @Override
            protected Call<GetBlockedUsersResponse> generateCall(Client client) {
                return client.create(BlockUserService.class).getBlockedUsers(blockedByUserID);
            }
        }
    }

    @NotNull
    public static GetBlockedUsersRequestData.GetBlockedUsersRequest getBlockedUsers(String blockedByUserID) {
        return new GetBlockedUsersRequestData.GetBlockedUsersRequest(blockedByUserID);
    }
}
