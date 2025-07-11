package io.getstream.chat.java;

import io.getstream.chat.java.models.App;
import io.getstream.chat.java.models.ChannelType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BackwardsCompatibilityTest extends BasicTest {

  @Nested
  @DisplayName("backwards compatibility")
  class BackwardsCompatibility {

    @Test
    @DisplayName("message_re_engagement_hooks_interval is included in the app settings")
    void messageReEngagementHooksIntervalIsIncludedInAppSettings() {
      App.AppConfig appConfig = Assertions.assertDoesNotThrow(() -> App.get().request()).getApp();
      
      Assertions.assertNotNull(appConfig.getMessageReEngagementHooksInterval());
      Assertions.assertNotNull(appConfig.getRemindersInterval());
    }

    @Test
    @DisplayName("message_re_engagement_hooks_interval can be changed server-side for the app")
    void messageReEngagementHooksIntervalCanBeChangedServerSide() {
      Assertions.assertDoesNotThrow(() -> App.update()
          .messageReEngagementHooksInterval(68)
          .request());
      
      App.AppConfig appConfig = Assertions.assertDoesNotThrow(() -> App.get().request()).getApp();
      
      Assertions.assertEquals(68, appConfig.getMessageReEngagementHooksInterval());
      Assertions.assertEquals(68, appConfig.getRemindersInterval());
    }

    @Test
    @DisplayName("can be enabled for channel type")
    void canBeEnabledForChannelType() {
      ChannelType.ChannelTypeUpdateResponse response = Assertions.assertDoesNotThrow(() -> 
          ChannelType.update("messaging")
              .messageReEngagementHooks(true)
              .request());
      
      Assertions.assertEquals(true, response.getMessageReEngagementHooks());
      Assertions.assertEquals(true, response.getReminders());
    }

    @Test
    @DisplayName("can be disabled for a channel type")
    void canBeDisabledForChannelType() {
      ChannelType.ChannelTypeUpdateResponse response = Assertions.assertDoesNotThrow(() -> 
          ChannelType.update("messaging")
              .messageReEngagementHooks(false)
              .request());
      
      Assertions.assertEquals(false, response.getMessageReEngagementHooks());
      Assertions.assertEquals(false, response.getReminders());
    }
  }
} 