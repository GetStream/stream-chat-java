package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Blocklist;
import io.getstream.chat.java.models.Channel;
import io.getstream.chat.java.models.Channel.ChannelGetResponse;
import io.getstream.chat.java.models.Channel.ChannelMemberRequestObject;
import io.getstream.chat.java.models.Channel.ChannelRequestObject;
import io.getstream.chat.java.models.ChannelType;
import io.getstream.chat.java.models.Command;
import io.getstream.chat.java.models.Message;
import io.getstream.chat.java.models.Message.MessageRequestObject;
import io.getstream.chat.java.models.User;
import io.getstream.chat.java.models.User.UserRequestObject;
import io.getstream.chat.java.models.User.UserUpsertRequestData.UserUpsertRequest;
import io.getstream.chat.java.services.framework.HttpLoggingInterceptor;
import io.getstream.chat.java.services.framework.StreamServiceGenerator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BasicTest {
  protected static UserRequestObject testUserRequestObject;
  protected static List<UserRequestObject> testUsersRequestObjects = new ArrayList<>();
  protected static ChannelGetResponse testChannelGetResponse;
  protected static Channel testChannel;
  protected static Message testMessage;

  static void enableLogging() {
    String enable =
        System.getenv("ENABLE_LOGGING") != null
            ? System.getenv("ENABLE_LOGGING")
            : System.getProperty("ENABLE_LOGGING");
    if (enable == null || enable.equals("false")) {
      return;
    }
    StreamServiceGenerator.logLevel = HttpLoggingInterceptor.Level.BODY;
    Logger root = Logger.getLogger("");
    root.setLevel(Level.FINE);
    for (Handler handler : root.getHandlers()) {
      handler.setLevel(Level.FINE);
    }
  }

  @BeforeEach
  void resetAuth()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
          IllegalAccessException {
    setAuth();
  }

  @BeforeAll
  static void setup()
      throws StreamException, NoSuchFieldException, SecurityException, IllegalArgumentException,
          IllegalAccessException {
    // failOnUnknownProperties();
    setAuth();
    enableLogging();
    setProperties();
    cleanChannelTypes();
    cleanBlocklists();
    cleanCommands();
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

  private static void cleanBlocklists() throws StreamException {
    Blocklist.list()
        .request()
        .getBlocklists()
        .forEach(
            blocklist -> {
              try {
                Blocklist.delete(blocklist.getName()).request();
              } catch (StreamException e) {
                // Do nothing this happens for built in
              }
            });
  }

  private static void cleanCommands() throws StreamException {
    Command.list()
        .request()
        .getCommands()
        .forEach(
            command -> {
              try {
                Command.delete(command.getName()).request();
              } catch (StreamException e) {
                // Do nothing
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
  }

  private static void setAuth()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
          IllegalAccessException {
    String apiKey =
        System.getenv("STREAM_KEY") != null
            ? System.getenv("STREAM_KEY")
            : System.getProperty("STREAM_KEY");
    String apiSecret =
        System.getenv("STREAM_SECRET") != null
            ? System.getenv("STREAM_SECRET")
            : System.getProperty("STREAM_SECRET");
    Field apiKeyField = StreamServiceGenerator.class.getDeclaredField("apiKey");
    apiKeyField.setAccessible(true);
    apiKeyField.set(StreamServiceGenerator.class, apiKey);
    Field apiSecretField = StreamServiceGenerator.class.getDeclaredField("apiSecret");
    apiSecretField.setAccessible(true);
    apiSecretField.set(StreamServiceGenerator.class, apiSecret);
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
    String text = UUID.randomUUID().toString();
    MessageRequestObject messageRequest =
        MessageRequestObject.builder().text(text).userId(testUserRequestObject.getId()).build();
    return Message.send(testChannel.getType(), testChannel.getId())
        .message(messageRequest)
        .request()
        .getMessage();
  }

  /**
   * This is used to pause after creation, as there can be a small delay before we can act upon the
   * resource
   *
   * @param milliseconds
   */
  protected void pause() {
    try {
      Thread.sleep(6000);
    } catch (InterruptedException e) {
      // Do nothing
    }
  }
}
