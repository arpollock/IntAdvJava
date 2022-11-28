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

/*
 Lab 1 (hopefully very easy :)

 thread 1 - print "Hello <count>" at 2 second intervals
 thread 2 - print "Goodbye <count>" at 1 second intervals
 optionally, shut down after count reaches ten.

 This code will pause execution for 2000 milliseconds (i.e. 2 seconds)
 Thread.sleep(2000);
 However, it throws a checked exception... Ignore that exception :)
*/
