import java.util.Random;
import java.util.concurrent.*;

public class SubmitExamples {
  public static void main(String[] args) {

    ExecutorService service = null;

    try {
      service = Executors.newSingleThreadExecutor();

      Future<?> firstResult =
           service.submit(
                () -> new Random()
                     .ints(1, 1000)
                     .limit(5)
                     .forEach(System.out::println));

      while (! firstResult.isDone()) {
        Thread.sleep(250);
      }

      System.out.println("firstResult = " + firstResult);
      System.out.println("firstResult.get() = " + firstResult.get());
    }
    catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    finally {
      if (service != null) service.shutdown();
    }
    //-------------------------------------------------------------------

    Future<String> futureResult =
         service.submit(
              () -> new Random()
                   .ints(1, 1000)
                   .limit(5)
                   .forEach(System.out::println)
              , "Thread is finished");

    while (! futureResult.isDone()) {
      try {
        Thread.sleep(250);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.println("futureResult = " + futureResult);
    try {
      System.out.println("futureResult = " + futureResult.get());
    }
    catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    Future<?> thirdResult =
         service.submit(
              () -> new Random()
                   .ints(1, 100_000)
                   .limit(1000)
                   .summaryStatistics()
         );

    try {
      System.out.println("thirdResult = " + thirdResult.get(5, TimeUnit.SECONDS));
    }
    catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }
    System.out.println("thirdResult = " + thirdResult);
  }
}