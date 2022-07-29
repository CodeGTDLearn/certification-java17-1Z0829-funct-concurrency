import java.util.IntSummaryStatistics;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ScheduledExamples {
  public static void main(String[] args) throws InterruptedException, ExecutionException {

    // Create some callable lambda expressions
    Callable r1 = () -> ScheduledExamples
         .doSomething(3, 15);

    Callable r2 = () -> ScheduledExamples
         .doSomething(5, 15);

    // Set up variables to house result of scheduling task
    ScheduledFuture<?> result1 = null;
    ScheduledFuture<?> result2 = null;

    // Create a new service using Executors class
    ScheduledExecutorService scheduledService = null;
    try {
      // Factory method to get single threaded Scheduled executor
      scheduledService = Executors.newSingleThreadScheduledExecutor();

      // Schedule task
      result1 = scheduledService.schedule(r1, 3, TimeUnit.SECONDS);

      // getDelay() returns time remaining before execution starts
      System.out.println("Task should start in " +
                              result1.getDelay(TimeUnit.SECONDS) + " seconds");

      // Schedule task
      result2 = scheduledService.schedule(r2, 4, TimeUnit.SECONDS);

      // etDelay() returns time remaining before execution starts
      System.out.println("Task should start in " +
                              result2.getDelay(TimeUnit.SECONDS) + " seconds");

    }
    finally {
      if (scheduledService != null) {
        scheduledService.shutdown();

        // Wait no longer than 4 seconds for completion confirmation
        scheduledService.awaitTermination(4, TimeUnit.SECONDS);

        // Print Results
        if (result1.isDone()) {
          System.out.println(result1.get());
        }
        if (result2.isDone()) {
          System.out.println(result2.get());
        }
      }
    }
  }

  private static IntSummaryStatistics doSomething(int seed, int maxNumber) {

    return Stream
         .iterate(seed, (s) -> s <= maxNumber, (t) -> t + seed)
         .mapToInt((s) -> s)
         .peek((s) -> {
           System.out.print("[" + seed + "'s] " + s + ", ");
           if (s == maxNumber) System.out.println("");
         })
         .summaryStatistics();

  }
}