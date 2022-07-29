import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class InvokeAnyExample {
  public static void main(String[] args) {

    Collection<Callable<IntSummaryStatistics>> taskList =
         List.of(
              () -> InvokeAnyExample.doSomething(3, 7),
              () -> InvokeAnyExample.doSomething(100, 5)
         );

    ExecutorService poolService = null;
    try {
      poolService = Executors.newFixedThreadPool(2);

      IntSummaryStatistics result =
           poolService.invokeAny(taskList, 1, TimeUnit.SECONDS);

      if (result != null) System.out.println("Result: " + result);
    }
    catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }
    finally {
      if (poolService != null) poolService.shutdown();
    }
  }


  private static IntSummaryStatistics doSomething(int seed, int limit) {

    return Stream.iterate(seed, (t) -> t + seed)
                 .limit(limit)
                 .mapToInt((s) -> s)
                 .peek((s) -> {
                   System.out.print("[" + seed + "'s] " + s + ", ");
                   if (s == limit * seed) System.out.println("");
                 })
                 .summaryStatistics();

  }
}