import java.util.IntSummaryStatistics;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ScheduleMultiples {
  private static long talley;

  private static void addToTalley(long count) {

    talley += count;

  }

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    Runnable r1 = () -> {
      IntSummaryStatistics sums = ScheduleMultiples.doSomething(3, 15);
      System.out.println(sums);
      // Each task increments talley by 5
      ScheduleMultiples.addToTalley(sums.getCount());      };

    ScheduledFuture<?> result1 = null;
    ScheduledExecutorService service = null;
    try {
      service = Executors.newSingleThreadScheduledExecutor();
      service.scheduleAtFixedRate(r1, 2, 2, TimeUnit.SECONDS);

      while (ScheduleMultiples.talley < 25) {
        Thread.sleep(3000);         }        }
    finally {
      if (service != null) {
        service.shutdown();
        service.awaitTermination(4, TimeUnit.SECONDS);
        System.out.println("Final talley = " + talley);
      }     }
  }

  // Method returns a Stream pipeline that counts by the seed number
  // up until maxNumber is reached.
  private static IntSummaryStatistics doSomething(int seed, int maxNumber) {

    return Stream.iterate(seed, (s) -> s <= maxNumber, (t) -> t + seed)
                 .mapToInt((s) -> s)
                 .peek((s) -> {
                   System.out.print("[" + seed + "'s] " + s + ", ");
                   if (s == maxNumber) System.out.println("");
                 })
                 .summaryStatistics();

  }
}