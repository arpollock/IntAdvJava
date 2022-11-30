package trycompfut;

import java.util.concurrent.CompletableFuture;

public class TryCompletableFuture {

  public static CompletableFuture<String> readFilePretend(String fn) {
    CompletableFuture<String> rv = new CompletableFuture<>();
    new Thread(() -> {
      System.out.println("'Kernel thread' started :)");
      delay();
      String result = "This is the contents of a file called " + fn
          + " isn't this a fascinating read!";
//      if (Math.random())
      rv.complete(result);
    }).start();
    return rv;
  }

  public static void delay() {
    try {
      Thread.sleep((int)(Math.random() * 1000 + 1000));
    } catch (InterruptedException ie) {}
  }

  public static void main(String[] args) throws Throwable {
    CompletableFuture<Void> cfv = CompletableFuture.supplyAsync(() -> {
      System.out.println("supplying from thread "
          + Thread.currentThread().getName());
      delay();
      System.out.println("Supplier calculation complete!");
      return "MyData.txt";
    })
        .thenApplyAsync(x -> {
          System.out.println("applying transformation to " + x);
          System.out.println("applying with thread "
              + Thread.currentThread().getName());
          delay();
          return x.toUpperCase();
        })
        .thenCompose(x -> readFilePretend(x))
        .thenAcceptAsync(x -> {
          System.out.println("accepting " + x);
          System.out.println("accepting with thread "
              + Thread.currentThread().getName());
          delay();
          System.out.println("The final answer is " + x);
        });
    System.out.println("Pipeline started");
    cfv.join();
    System.out.println("Delay finished");
    cfv.thenAccept(v -> {
      System.out.println("v is " + v);
    });
  }
}
