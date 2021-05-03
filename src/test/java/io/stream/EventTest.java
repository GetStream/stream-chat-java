package io.stream;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.stream.models.Event;
import io.stream.models.Event.EventRequestObject;
import io.stream.models.Event.EventUserCustomRequestObject;

public class EventTest extends BasicTest {

  @DisplayName("Can send event")
  @Test
  void whenSendingEvent_thenEventIsCreated() {
    String eventType = RandomStringUtils.randomAlphabetic(10);
    Event event =
        Assertions.assertDoesNotThrow(
                () ->
                    Event.send(testChannel.getType(), testChannel.getId())
                        .event(
                            EventRequestObject.builder()
                                .type(eventType)
                                .user(testUserRequestObject)
                                .build())
                        .request())
            .getEvent();
    Assertions.assertEquals(eventType, event.getType());
  }

  @DisplayName("Can send user custom event")
  @Test
  void whenSendingCustomEvent_thenNoException() {
    Map<String, Object> eventData = new HashMap<>();
    eventData.put("customField", "customValue");
    Assertions.assertDoesNotThrow(
        () ->
            Event.sendUserCustom(testUserRequestObject.getId())
                .event(
                    EventUserCustomRequestObject.builder()
                        .type(RandomStringUtils.randomAlphabetic(10))
                        .additionalFields(eventData)
                        .build())
                .request());
  }
}
