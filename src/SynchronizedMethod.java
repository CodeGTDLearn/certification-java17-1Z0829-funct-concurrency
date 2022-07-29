import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TheBallInTheAir {

  //  private String ballType = "Volleyball";
  //  private int hit;
  //  private Map<String, Integer> players = new TreeMap<>();

  //  public  int addHit(String player) {
  //    this.hit++;
  //    if (players.containsKey(player)) {
  //      players.computeIfPresent(player, (key, val) -> ++ val); }
  //    else {   players.put(player, 1);   }
  //    return this.hit;
  //  }

  //    public synchronized int addHit(String player) {
  //      this.hit++;
  //      if (players.containsKey(player)) {
  //        players.computeIfPresent(player, (key, val) -> ++ val); }
  //      else {   players.put(player, 1);   }
  //      return this.hit;
  //    }

  private String ballType = "Volleyball";
  private Map<String, Integer> players = new TreeMap<>();

  private AtomicInteger hit = new AtomicInteger(0);

  public int addHit(String player) {
    if (players.containsKey(player)) {
      players.computeIfPresent(player, (key, val) -> ++ val); }
    else {     players.put(player, 1);    }
    return this.hit.getAndIncrement();
  }

  public String toString() {
    return "Total " + ballType + " | Hits: " + this.hit.get() +
              "\nPlayer hits: " + players.values()
                                         .stream()
                                         .mapToInt(s -> s)
                                         .sum() +
              "\nPlayers List: " + players +
              " - Total They Hitted: " + players.values()
                                                .stream()
                                                .mapToInt(s -> s)
                                                .sum();
  }
}

public class SynchronizedMethod {

  public static TheBallInTheAir sharedBall = new TheBallInTheAir();

  public static void main(String[] args) {

    String[] players = {"Jane", "Mary", "Ralph", "Joe"};
    Random r = new Random();
    List<String> playerTurns =
         Stream.generate(() -> players[r.nextInt(4)])
               .limit(100)
               .collect(Collectors.toList());

    ExecutorService executorService = null;

    try {
      executorService = Executors.newFixedThreadPool(5);

      for (String player : playerTurns) {
        executorService.submit(() -> sharedBall.addHit(player));
      }
    }
    finally {
      if (executorService != null) {
        executorService.shutdown();
        try {
          executorService.awaitTermination(1, TimeUnit.SECONDS);
          System.out.println(sharedBall);

        }
        catch (InterruptedException ie) {
          ie.printStackTrace();
        }
      }
    }
  }
}