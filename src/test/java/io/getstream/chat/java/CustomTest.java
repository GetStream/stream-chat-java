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
    var response = User.list().filterCondition("id", userId).withUserToken(userToken).request();
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
      defaultClient.create3(UserService.class, userToken);
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

    // Measure third test
    long allocatedBefore3 = threadBean.getCurrentThreadAllocatedBytes();
    startTime = System.nanoTime();
    for (int i = 0; i < iterations; i++) {
      defaultClient.create3(UserService.class, userToken);
    }
    endTime = System.nanoTime();
    long allocatedAfter3 = threadBean.getCurrentThreadAllocatedBytes();
    long elapsedTimeInNs3 = endTime - startTime;
    long allocated3 = allocatedAfter3 - allocatedBefore3;

    System.out.println("> Third loop elapsed time: " + TimeUnit.NANOSECONDS.toMillis(elapsedTimeInNs3) + " ms");
    System.out.println("> Third loop memory allocated: " + (allocated3 / 1024 / 1024) + " MB");
    System.out.println("> Third loop avg time per call: " + (elapsedTimeInNs3 / (double) iterations) + " ns");
    System.out.println("> Third loop avg memory per call: " + (allocated3 / (double) iterations) + " bytes");

    // Performance comparison - Time
    long fastestTime = Math.min(elapsedTimeInNs1, Math.min(elapsedTimeInNs2, elapsedTimeInNs3));
    String fastestMethod = "";
    if (fastestTime == elapsedTimeInNs1) fastestMethod = "create";
    else if (fastestTime == elapsedTimeInNs2) fastestMethod = "create2";
    else fastestMethod = "create3";
    
    System.out.println("> Time comparison (fastest: " + fastestMethod + "):");
    System.out.println("  - create:  " + String.format("%.2fx", (double) elapsedTimeInNs1 / fastestTime));
    System.out.println("  - create2: " + String.format("%.2fx", (double) elapsedTimeInNs2 / fastestTime));
    System.out.println("  - create3: " + String.format("%.2fx", (double) elapsedTimeInNs3 / fastestTime));
    
    // Performance comparison - Memory
    long leastMemory = Math.min(allocated1, Math.min(allocated2, allocated3));
    String mostEfficientMethod = "";
    if (leastMemory == allocated1) mostEfficientMethod = "create";
    else if (leastMemory == allocated2) mostEfficientMethod = "create2";
    else mostEfficientMethod = "create3";
    
    System.out.println("> Memory comparison (least: " + mostEfficientMethod + "):");
    System.out.println("  - create:  " + String.format("%.2fx", (double) allocated1 / leastMemory));
    System.out.println("  - create2: " + String.format("%.2fx", (double) allocated2 / leastMemory));
    System.out.println("  - create3: " + String.format("%.2fx", (double) allocated3 / leastMemory));

    System.out.println("=========================================================");
  }
}
