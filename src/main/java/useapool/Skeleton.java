package useapool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class MyTask implements Runnable {
  private String name;

  public MyTask(String name) {
    this.name = name;
  }

  @Override
  public void run() {
    // announce task name is starting
    // somewhat random "delay" (between 2 and 5 seconds)
    // -- simply use Thread.sleep()
    // announce task name is finishing
  }
}

public class Skeleton {
  public static void main(String[] args) {

    // create an Executor:
    Executor ex = Executors.newFixedThreadPool(2);

    // create a number of tasks (> 4), represented as Runnables
    // "execute" (add to the input task queue) each task

    // watch the output, and decide if it makes sense :)
  }
}
