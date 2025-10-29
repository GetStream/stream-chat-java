package io.getstream.chat.java;

import io.getstream.chat.java.models.User;
import org.junit.jupiter.api.Test;

public class CustomTest {

  @Test
  void customTest() throws Exception {
    var userId = "han_solo";
    var userToken = User.createToken("han_solo", null, null);
    var response = User.list().userId(userId).filterCondition("id", userId).request();
    System.out.println(response);
  }


    @Test
    void userReqTest() throws Exception {
        var userId = "han_solo";
        var userToken = User.createToken("han_solo", null, null);
        var response = User.list().filterCondition("id", userId).withUserToken(userToken).request();
        System.out.println("\n> " + response + "\n");
    }

    @Test
    void directClientTest() throws Exception {
        var userId = "han_solo";
        var userToken = User.createToken("han_solo", null, null);
        
        // Test creating a UserClient directly - should use Client-Side auth
        var defaultClient = io.getstream.chat.java.services.framework.Client.getInstance();
        var userClient = new io.getstream.chat.java.services.framework.UserClient(defaultClient, userToken);
        
        var response = User.list()
            .filterCondition("id", userId)
            .withClient(userClient)
            .request();
        System.out.println("\n> Direct UserClient: " + response + "\n");
    }
}
