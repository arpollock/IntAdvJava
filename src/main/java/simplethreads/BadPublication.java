package simplethreads;

public class BadPublication {
  static volatile boolean stop = false;

  public static void main(String[] args) throws Throwable {
    new Thread(() ->{
      System.out.println("worker thread starts...");
      while (! stop)
        /*System.out.print(".")*/;
      System.out.println("worker thread stops...");
    }).start();
    System.out.println("main thread started worker");
    Thread.sleep(1000);
    System.out.println("Setting stop flag");
    stop = true;
    System.out.println("Main exiting, stop = " + stop);
  }
}
