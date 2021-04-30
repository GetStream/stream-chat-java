package io.stream;

import io.stream.exceptions.StreamException;
import io.stream.models.Channel;
import io.stream.models.Channel.ChannelGetResponse;
import io.stream.models.Channel.ChannelMemberRequestObject;
import io.stream.models.Channel.ChannelRequestObject;
import io.stream.models.User;
import io.stream.models.User.UserRequestObject;
import io.stream.models.User.UserUpsertRequestData.UserUpsertRequest;
import io.stream.services.framework.StreamServiceGenerator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BasicTest {
  protected static UserRequestObject serverUser;
  protected static List<UserRequestObject> testUsers = new ArrayList<>();

  static void enableLoging() {
    StreamServiceGenerator.logEnabled = true;
  }

  @BeforeEach
  void resetProperties() {
    setProperties();
  }

  @BeforeAll
  static void setup() throws Exception {
    // failOnUnknownProperties();
    StreamServiceGenerator.logEnabled = true;
    setProperties();
    upsertUsers();
  }

  static void failOnUnknownProperties() throws Exception {
    Field failOnUnknownProperties =
        StreamServiceGenerator.class.getDeclaredField("failOnUnknownProperties");
    failOnUnknownProperties.setAccessible(true);
    failOnUnknownProperties.set(StreamServiceGenerator.class, true);
  }

  static void upsertUsers() throws StreamException {
    serverUser =
        UserRequestObject.builder()
            .withId(RandomStringUtils.randomAlphabetic(10))
            .withName("Gandalf the Grey")
            .build();
    testUsers.add(serverUser);
    testUsers.add(
        UserRequestObject.builder()
            .withId(RandomStringUtils.randomAlphabetic(10))
            .withName("Frodo Baggins")
            .build());
    testUsers.add(
        UserRequestObject.builder()
            .withId(RandomStringUtils.randomAlphabetic(10))
            .withName("Frodo Baggins")
            .build());
    testUsers.add(
        UserRequestObject.builder()
            .withId(RandomStringUtils.randomAlphabetic(10))
            .withName("Samwise Gamgee")
            .build());
    UserUpsertRequest usersUpsertRequest = User.upsert();
    testUsers.forEach(user -> usersUpsertRequest.addUser(user));
    usersUpsertRequest.request();
  }

  static void setProperties() {
    System.setProperty("STREAM_KEY", "vk73cqmmjxe6");
    System.setProperty(
        "STREAM_SECRET", "mxxtzdxc932n8k9dg47p49kkz6pncxkqu3z6g6s57rh9nca363kdqaxd6jbw5mtq");
    System.setProperty(
        "java.util.logging.SimpleFormatter.format",
        "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
  }

  protected List<ChannelMemberRequestObject> buildChannelMembersList() {
    return testUsers.stream()
        .map(user -> ChannelMemberRequestObject.builder().withUser(user).build())
        .collect(Collectors.toList());
  }

  protected ChannelGetResponse createRandomChannel() throws StreamException {
    return Channel.getOrCreate("team", RandomStringUtils.randomAlphabetic(12))
        .withData(
            ChannelRequestObject.builder()
                .withCreatedBy(serverUser)
                .withMembers(buildChannelMembersList())
                .build())
        .request();
  }

  protected ChannelGetResponse getRandomChannel() throws StreamException {
    return Channel.getOrCreate("team", null)
        .withData(
            ChannelRequestObject.builder()
                .withCreatedBy(serverUser)
                .withMembers(buildChannelMembersList())
                .build())
        .request();
  }
}
