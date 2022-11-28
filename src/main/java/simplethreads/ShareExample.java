package simplethreads;

import java.lang.reflect.Method;

class TaskOne implements Runnable {
  @Override
  public void run() {
    for (int i = 0; i < 10; i++) {
      ShareExample.message = "Hello, counter is " + i;
      ShareExample.delay(1000);
    }
  }
//
//  @Override
//  public String toString() {
//      return "I'm TaskOne";
//  }
}

public class ShareExample {

  static String message;

  static void delay(int delay) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {

    }
  }

  public static void main(String[] args) {
    Runnable myTaskOne = new TaskOne();
    myTaskOne.run();
//    new Thread(myTaskOne).start();
    // ONLY acceptable thing in place of ??? is an object implementing Runnable
    // AND Runnable declares EXACTLY ONE abstract method
    // AND we are ONLY INTERESTED in implementing THAT ONE METHOD
    // then we might reasonably provide the body of that method,
    // and ask the compiler to fill in the rest of the class declaration
    // and call new to instantiate it.
    new Thread(() -> {
      for (int i = 0; i < 10; i++) {
        ShareExample.message = "Hello, counter is " + i;
        ShareExample.delay(1000);
      }
    }).start();

    Runnable obj = () -> {
      for (int i = 0; i < 10; i++) {
        ShareExample.message = "Hello, counter is " + i;
        ShareExample.delay(1000);
      }
    };
    System.out.println("Examining obj");
    System.out.println("obj class is " + obj.getClass().getName());
    Class<?> cl = obj.getClass();
    Method [] methods = cl.getMethods();
    for (Method m : methods) {
      System.out.println("> " + m);
    }
////    obj(); // NO
//    obj.run(); // YES, it's an OBJECT literal...

    new Thread(() -> {
      for (int i = 0; i < 10; i++) {
        System.out.println(message);
        delay(2000);
      }
    }).start();
    System.out.println("Tasks started...");
  }
}



/*
Create a shared variable "count"
create two threads. Each thread increments count 1_000_000_000 times.
pause the main thread "long enough" that both of the counting threads
have reliably completed
print out the total count value.
 */


class LabSolution {
  static {
    System.out.println("Static init in thread " +
        Thread.currentThread().getName());
  }
  static /*volatile*/ long count = 0;
  private static Object rendezvous = new Object();

  static Runnable incrementer = () -> {
    System.out.println(Thread.currentThread().getName() + " incrementer starting");
    for (int i = 0; i < 100_000_000; i++) {
      synchronized (rendezvous) {
        count++;
      }
    }
    System.out.println(Thread.currentThread().getName() + " incrementer completed");
  };
  public static void main(String[] args) throws Throwable {
    System.out.println("main() method in thread " +
        Thread.currentThread().getName());
    Thread t1 = new Thread(incrementer);
    Thread t2 = new Thread(incrementer);

    long start = System.nanoTime();
    t1.start();
//    Thread.sleep(1000);
    t2.start();

//    Thread.sleep(1000);
    t1.join();
    t2.join();
    long time = System.nanoTime() - start;

//    while (t1.isAlive() || t2.isAlive())
//      ;

    System.out.println("count is " + count);
    System.out.printf("Time taken: %7.3f\n", (time / 1_000_000_000.0));
  }
}







