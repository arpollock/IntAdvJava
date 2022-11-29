package useapool;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyTask implements Runnable {
  private String name;

  public MyTask(String name) {
    this.name = name;
  }

  private static void delay() {
    try {
      Thread.sleep((int)(Math.random() * 3000 + 2000));
    } catch (InterruptedException ie) {
      System.out.println("Something tried to stop me!");
    }
  }

  @Override
  public void run() {
    System.out.println("Task " + name + " starting");
    // announce task name is starting
    // somewhat random "delay" (between 2 and 5 seconds)
    // -- simply use Thread.sleep()
    delay();
    // announce task name is finishing
    System.out.println("Task " + name + " finishing");
  }
}

public class Skeleton {
  public static void main(String[] args) throws Throwable {

    // create an Executor:
    ExecutorService ex = Executors.newFixedThreadPool(2);

    // create a number of tasks (> 4), represented as Runnables
    // "execute" (add to the input task queue) each task
    for (int i = 0; i < 6; i++) {
      ex.execute(new MyTask("Task number: " + i));
    }
    System.out.println("All tasks submitted");
    ex.shutdown();
    System.out.println("is shutdown? " + ex.isShutdown());
    System.out.println("is terminated? " + ex.isTerminated());
    // watch the output, and decide if it makes sense :)

    System.out.println("press enter");
    System.in.read();
    System.out.println("is terminated? " + ex.isTerminated());
    System.out.println("Exiting");
  }
}
