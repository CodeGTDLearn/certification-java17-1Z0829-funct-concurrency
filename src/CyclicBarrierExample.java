import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class CyclicBarrierExample {
  public static void main(String[] args) throws Exception {
    CyclicBarrier checkpoint =
         new CyclicBarrier(3,() -> {System.out.println("Checkpoint!!\n");});

    Callable<Boolean> task2Actions = () -> {//Task with 2 Actions(doIt1+doIt2)
      doIt(1);    // All threads execute doIt-1
      try {   checkpoint.await(5, TimeUnit.SECONDS); }
      catch (BrokenBarrierException e) {
        System.out.println("Checkpoint Fail: " + checkpoint.isBroken() +"\n");}

      doIt(2);                    // All threads execute doIt-2
      return true;  };

    ExecutorService service = Executors.newFixedThreadPool(2);
    service.invokeAll(List.of(task2Actions, task2Actions));
    System.out.println("\nShutting service down");
    service.shutdown();
  }
  public static void doIt(int stage) throws Exception {

    int ms = new Random().nextInt(5) * 1000;
    System.out.println(Thread.currentThread()
                             .getName() + " - Start doIt: " + stage);
    Thread.sleep(ms);

    System.out.println(Thread.currentThread()
                             .getName() + " - doIt: " + stage + " - Done");

  }
}