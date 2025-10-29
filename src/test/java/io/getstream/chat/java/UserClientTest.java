package io.getstream.chat.java;

import io.getstream.chat.java.models.User;
import io.getstream.chat.java.services.framework.DefaultClient;
import io.getstream.chat.java.services.framework.UserClient;
import org.junit.jupiter.api.Test;

/**
 * Demonstrates how to use UserClient for client-side requests with user tokens.
 *
 * <p>Instead of server-side auth (API key + secret), UserClient allows you to make requests
 * authenticated with a user token (JWT).
 */
public class UserClientTest {

  @Test
  public void demonstrateUserClientUsage() throws Exception {
    // Server-side setup - generate a user token for a specific user
    String userToken =
        User.createToken(
            System.getProperty("io.getstream.chat.apiSecret"),
            "test-user-id",
            null, // no expiration
            null // default issued at
            );

    // Create a client-side client for this user
    DefaultClient serverClient = DefaultClient.getInstance();
    UserClient userClient = new UserClient(serverClient, userToken);

    // Now you can make requests on behalf of this user
    // The token will be automatically included in the Authorization header

    // Example: Query channels visible to this user
    // Channel.list().withClient(userClient).request();

    // Example: Send a message as this user
    // Message.send("messaging", "general")
    //     .withClient(userClient)
    //     .message(MessageRequestObject.builder()
    //         .text("Hello from client-side!")
    //         .userId("test-user-id")
    //         .build())
    //     .request();

    System.out.println(
        "UserClient created successfully with token: " + userToken.substring(0, 20) + "...");
  }

  @Test
  public void demonstrateMultipleUserClients() throws Exception {
    // You can create multiple UserClients for different users
    // Each shares the same underlying connection pool and thread pool

    String apiSecret = System.getProperty("io.getstream.chat.apiSecret");
    DefaultClient serverClient = DefaultClient.getInstance();

    UserClient user1Client =
        new UserClient(serverClient, User.createToken(apiSecret, "user-1", null, null));

    UserClient user2Client =
        new UserClient(serverClient, User.createToken(apiSecret, "user-2", null, null));

    // Both clients share the same connection pool (efficient!)
    // But each request is authenticated with its respective user token

    // You can make parallel requests with different user contexts:
    // CompletableFuture<MessageSendResponse> future1 = CompletableFuture.supplyAsync(() ->
    //     Message.send("messaging", "channel1").withClient(user1Client).request()
    // );
    //
    // CompletableFuture<MessageSendResponse> future2 = CompletableFuture.supplyAsync(() ->
    //     Message.send("messaging", "channel2").withClient(user2Client).request()
    // );

    System.out.println("Multiple UserClients can coexist safely");
  }
}
