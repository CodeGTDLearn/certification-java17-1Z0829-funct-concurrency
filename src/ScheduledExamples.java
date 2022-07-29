import java.util.IntSummaryStatistics;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ScheduledExamples {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    Callable r1 = () -> ScheduledExamples.doSomething(3, 15);
    Callable r2 = () -> ScheduledExamples.doSomething(5, 15);

    ScheduledFuture<?> result1 = null;
    ScheduledFuture<?> result2 = null;

    ScheduledExecutorService service = null;
    try {
      service = Executors.newSingleThreadScheduledExecutor();

      result1 = service.schedule(r1, 3, TimeUnit.SECONDS);
      System.out.println("Start in " + result1.getDelay(TimeUnit.SECONDS) + " sec's");

      result2 = service.schedule(r2, 4, TimeUnit.SECONDS);
      System.out.println("Start in " + result2.getDelay(TimeUnit.SECONDS) + " sec's");
    }
    finally {
      if (service != null) {
        service.shutdown();
        // Wait no longer than 4 seconds for completion confirmation
        service.awaitTermination(10, TimeUnit.SECONDS);

        if (result1.isDone()) System.out.println("Task1: " + result1.get());
        if (result2.isDone()) System.out.println("Task2: " + result2.get());
      }   }   }

  private static IntSummaryStatistics doSomething(int seed, int maxNumber) {

    return Stream
         .iterate(
              seed,
              (s) -> s <= maxNumber,
              (t) -> t + seed
         )
         .mapToInt((s) -> s)
         .peek((s) -> {
           System.out.print("[" + seed + "'s] " + s + ", ");
           if (s == maxNumber) System.out.println("");
         })
         .summaryStatistics();
  }
}