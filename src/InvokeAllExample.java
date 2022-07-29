import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class InvokeAllExample {
  public static void main(String[] args) {
    ExecutorService service = null;
    List<Future<IntSummaryStatistics>> resultList = null;
    Collection<Callable<IntSummaryStatistics>> taskList =
         List.of(
              () -> InvokeAllExample.doSomething(1, 111),
              () -> InvokeAllExample.doSomething(2, 222),
              () -> InvokeAllExample.doSomething(3, 333));

    try {
      service = Executors.newSingleThreadExecutor();
      resultList = service.invokeAll(taskList); // NO time-limit for complete
//      resultList = service.invokeAll(taskList,// time-limit: 02 Milisec.
//                      2, TimeUnit.MILLISECONDS);
    }
    catch (InterruptedException e) {    e.printStackTrace();    }
    finally {
      if (service != null) {
        service.shutdown();
        try {
          if (resultList != null) {
            for (Future<IntSummaryStatistics> result : resultList) {
              System.out.println("Completed: Task: "+result+"|"+result.get());
            }  }  }
        catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();       }
      }  }
  }

  private static IntSummaryStatistics doSomething(int seed, int limit) {

    return Stream.iterate(seed, (t) -> t + seed)
                 .limit(limit)
                 .mapToInt((s) -> s)
                 .summaryStatistics();
  }
}