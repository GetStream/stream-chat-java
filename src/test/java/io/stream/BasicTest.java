package io.stream;

import io.stream.services.framework.StreamServiceGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BasicTest {
  @BeforeAll
  static void enableLoging() {
    StreamServiceGenerator.logEnabled = true;
  }

  @BeforeEach
  void setup() {
    System.setProperty("STREAM_KEY", "vk73cqmmjxe6");
    System.setProperty(
        "STREAM_SECRET", "mxxtzdxc932n8k9dg47p49kkz6pncxkqu3z6g6s57rh9nca363kdqaxd6jbw5mtq");
    System.setProperty(
        "java.util.logging.SimpleFormatter.format",
        "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
  }
}
