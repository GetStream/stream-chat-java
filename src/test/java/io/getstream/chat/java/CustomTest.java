package io.getstream.chat.java;

import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.Thread;
import io.getstream.chat.java.models.User;
import io.getstream.chat.java.services.UserService;
import io.getstream.chat.java.services.framework.DefaultClient;
import org.junit.jupiter.api.Test;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CustomTest {

  @Test
  void customTest() throws Exception {
    var userId = "admin";
    var userToken = User.createToken(userId, null, null);
    var response = User.list().userId(userId).filterCondition("id", userId).request();
    System.out.println(response);
  }


  @Test
  void userReqTest() throws Exception {
    var userIds = List.of( "admin", "MWRYXIHURH", "SRLOTCPYQS", "IOEXOFYTRH", "RIOPOIGMDQ", "XUXJMHTNOI", "QQASWMJEQI");

    for (var userId : userIds) {
      var userToken = User.createToken(userId, null, null);
      User.list().filterCondition("id", userId).withUserToken(userToken).requestAsync(
          userListResponse -> System.out.println("\n!.!.! " + userListResponse + "\n"),
          e -> {}
      );
    }

    java.lang.Thread.sleep(10000);
  }

  @Test
  void measureClientCreate() throws Exception {
    var userId = "admin";
    var userToken = User.createToken(userId, null, null);

    // Test creating a UserClient directly - should use Client-Side auth
    var defaultClient = new DefaultClient();

    var iterations = 30_000_000;

    // Warm up JVM to avoid cold start effects
    for (int i = 0; i < 10_000; i++) {
      defaultClient.create(UserService.class, userToken);
      defaultClient.create2(UserService.class, userToken);
    }

    // Get ThreadMXBean for accurate memory allocation tracking
    com.sun.management.ThreadMXBean threadBean = 
        (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean();

    // Measure first test
    long allocatedBefore1 = threadBean.getCurrentThreadAllocatedBytes();
    long startTime = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      defaultClient.create(UserService.class, userToken);
    }
    long endTime = System.nanoTime();
    long allocatedAfter1 = threadBean.getCurrentThreadAllocatedBytes();
    long elapsedTimeInNs1 = endTime - startTime;
    long allocated1 = allocatedAfter1 - allocatedBefore1;

    System.out.println("=========================================================");

    System.out.println("> First loop elapsed time: " + TimeUnit.NANOSECONDS.toMillis(elapsedTimeInNs1) + " ms");
    System.out.println("> First loop memory allocated: " + (allocated1 / 1024 / 1024) + " MB");
    System.out.println("> First loop avg time per call: " + (elapsedTimeInNs1 / (double) iterations) + " ns");
    System.out.println("> First loop avg memory per call: " + (allocated1 / (double) iterations) + " bytes");

    // Measure second test
    long allocatedBefore2 = threadBean.getCurrentThreadAllocatedBytes();
    startTime = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      defaultClient.create2(UserService.class, userToken);
    }
    endTime = System.nanoTime();
    long allocatedAfter2 = threadBean.getCurrentThreadAllocatedBytes();
    long elapsedTimeInNs2 = endTime - startTime;
    long allocated2 = allocatedAfter2 - allocatedBefore2;

    System.out.println("> Second loop elapsed time: " + TimeUnit.NANOSECONDS.toMillis(elapsedTimeInNs2) + " ms");
    System.out.println("> Second loop memory allocated: " + (allocated2 / 1024 / 1024) + " MB");
    System.out.println("> Second loop avg time per call: " + (elapsedTimeInNs2 / (double) iterations) + " ns");
    System.out.println("> Second loop avg memory per call: " + (allocated2 / (double) iterations) + " bytes");

    // Performance comparison
    if (elapsedTimeInNs1 < elapsedTimeInNs2) {
      double timesFaster = (double) elapsedTimeInNs2 / elapsedTimeInNs1;
      System.out.println("> create is " + String.format("%.2fx", timesFaster) + " faster than create2");
    } else {
      double timesFaster = (double) elapsedTimeInNs1 / elapsedTimeInNs2;
      System.out.println("> create2 is " + String.format("%.2fx", timesFaster) + " faster than create");
    }
    
    if (allocated1 < allocated2) {
      double timesLess = (double) allocated2 / allocated1;
      System.out.println("> create allocates " + String.format("%.2fx", timesLess) + " less memory than create2");
    } else {
      double timesLess = (double) allocated1 / allocated2;
      System.out.println("> create2 allocates " + String.format("%.2fx", timesLess) + " less memory than create");
    }

    System.out.println("=========================================================");
  }
}
