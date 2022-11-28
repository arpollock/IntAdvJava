package simplethreads;

import java.time.LocalDateTime;

class MyTask implements Runnable {
  @Override
  public void run() {
    String message = "Hello, threading world";
    System.out.println(message);
    LocalDateTime now = LocalDateTime.now();
    System.out.println("Time is " + now);
  }
}

public class Example {
  public static void main(String[] args) {
    MyTask mt = new MyTask();
    Thread t = new Thread(mt);
    t.start(); // NOT t.run()
    System.out.println("Finishing");
  }
}
