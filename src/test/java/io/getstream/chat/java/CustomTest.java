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
}
