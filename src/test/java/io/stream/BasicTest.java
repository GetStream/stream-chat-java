package io.stream;

import io.stream.exceptions.StreamException;
import io.stream.models.Channel;
import io.stream.models.ChannelType;
import io.stream.models.Channel.ChannelGetResponse;
import io.stream.models.Channel.ChannelMemberRequestObject;
import io.stream.models.Channel.ChannelRequestObject;
import io.stream.models.Message;
import io.stream.models.Message.MessageRequestObject;
import io.stream.models.User;
import io.stream.models.User.UserRequestObject;
import io.stream.models.User.UserUpsertRequestData.UserUpsertRequest;
import io.stream.services.framework.HttpLoggingInterceptor;
import io.stream.services.framework.StreamServiceGenerator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BasicTest {
  protected static UserRequestObject testUserRequestObject;
  protected static List<UserRequestObject> testUsersRequestObjects = new ArrayList<>();
  protected static ChannelGetResponse testChannelGetResponse;
  protected static Channel testChannel;
  protected static Message testMessage;

  static void enableLogging() {
    StreamServiceGenerator.logLevel = HttpLoggingInterceptor.Level.BODY;
  }

  @BeforeEach
  void resetAuth()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
          IllegalAccessException {
    setAuth();
  }

  @BeforeAll
  static void setup() throws StreamException {
    // failOnUnknownProperties();
    enableLogging();
    setProperties();
    cleanChannelTypes();
    upsertUsers();
    createTestChannel();
    createTestMessage();
  }

  private static void cleanChannelTypes() throws StreamException {
    ChannelType.list()
        .request()
        .getChannelTypes()
        .values()
        .forEach(
            channelType -> {
              try {
                ChannelType.delete(channelType.getName()).request();
              } catch (StreamException e) {
                // Do nothing. Happens when there are channels of that type
              }
            });
  }

  private static void createTestMessage() throws StreamException {
    testMessage = sendTestMessage();
  }

  private static void createTestChannel() throws StreamException {
    testChannelGetResponse = createRandomChannel();
    testChannel = testChannelGetResponse.getChannel();
  }

  static void failOnUnknownProperties() throws Exception {
    Field failOnUnknownProperties =
        StreamServiceGenerator.class.getDeclaredField("failOnUnknownProperties");
    failOnUnknownProperties.setAccessible(true);
    failOnUnknownProperties.set(StreamServiceGenerator.class, true);
  }

  static void upsertUsers() throws StreamException {
    testUserRequestObject =
        UserRequestObject.builder()
            .id(RandomStringUtils.randomAlphabetic(10))
            .name("Gandalf the Grey")
            .build();
    testUsersRequestObjects.add(testUserRequestObject);
    testUsersRequestObjects.add(
        UserRequestObject.builder()
            .id(RandomStringUtils.randomAlphabetic(10))
            .name("Frodo Baggins")
            .build());
    testUsersRequestObjects.add(
        UserRequestObject.builder()
            .id(RandomStringUtils.randomAlphabetic(10))
            .name("Frodo Baggins")
            .build());
    testUsersRequestObjects.add(
        UserRequestObject.builder()
            .id(RandomStringUtils.randomAlphabetic(10))
            .name("Samwise Gamgee")
            .build());
    UserUpsertRequest usersUpsertRequest = User.upsert();
    testUsersRequestObjects.forEach(user -> usersUpsertRequest.user(user));
    usersUpsertRequest.request();
  }

  static void setProperties() {
    System.setProperty(
        "java.util.logging.SimpleFormatter.format",
        "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
    System.setProperty("STREAM_KEY", "vk73cqmmjxe6");
    System.setProperty(
        "STREAM_SECRET", "mxxtzdxc932n8k9dg47p49kkz6pncxkqu3z6g6s57rh9nca363kdqaxd6jbw5mtq");
  }

  private void setAuth()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
          IllegalAccessException {
    Field apiKeyField = StreamServiceGenerator.class.getDeclaredField("apiKey");
    apiKeyField.setAccessible(true);
    apiKeyField.set(StreamServiceGenerator.class, System.getProperty("STREAM_KEY"));
    Field apiSecretField = StreamServiceGenerator.class.getDeclaredField("apiSecret");
    apiSecretField.setAccessible(true);
    apiSecretField.set(StreamServiceGenerator.class, System.getProperty("STREAM_SECRET"));
  }

  protected static List<ChannelMemberRequestObject> buildChannelMembersList() {
    return testUsersRequestObjects.stream()
        .map(user -> ChannelMemberRequestObject.builder().user(user).build())
        .collect(Collectors.toList());
  }

  protected static ChannelGetResponse createRandomChannel() throws StreamException {
    return Channel.getOrCreate("team", RandomStringUtils.randomAlphabetic(12))
        .data(
            ChannelRequestObject.builder()
                .createdBy(testUserRequestObject)
                .members(buildChannelMembersList())
                .build())
        .request();
  }

  protected static Message sendTestMessage() throws StreamException {
    String text = "This is a message";
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();
    return Assertions.assertDoesNotThrow(
            () ->
                Message.send(testChannel.getType(), testChannel.getId())
                    .message(messageRequest)
                    .request())
        .getMessage();
  }
}
