package useapool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

class MyTaskWithResult implements Callable<String> {
  private static int nextId = 0;
  private String name = "Task " + nextId++;

  private void delay() {
    try {
      Thread.sleep((int)(Math.random() * 3000 + 2000));
    } catch (InterruptedException ie) {
      System.out.println("interrupt received!!!");
    }
  }

  @Override
  public String call() throws Exception {
    System.out.println(name + " starting");
    delay();
    if (Math.random() > 0.75) {
      System.out.println(name + "throwing exception");
      throw new SQLException("DB busted!!");
    }
    System.out.println(name + " ending");
    return "Completed " + name;
  }
}

public class TasksWithResults {
  public static void main(String[] args) throws InterruptedException {
    ExecutorService es = Executors.newFixedThreadPool(2);

    List<Future<String>> lfs = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      Future<String> fs = es.submit(new MyTaskWithResult());
      lfs.add(fs);
    }
    System.out.println("All tasks submitted...");
    Thread.sleep(500);
    // true sends interrupt IF the task is already running
    // task should respond to interrupt by either:
    // - shutting down cleanly "now"
    // - ignoring and completing normally BUT in the case of
    //   a task in the executor service, normal completion still
    //   cannot return a value through the Future
//    lfs.get(1).cancel(true);
//    System.out.println("Task 1 canceled...");

    // blocks input queue
    // removes not-started tasks
    // interrupts started tasks
    // DOES wait for tasks to complete
//    System.out.println("Doing shutdownNow");
//    es.shutdownNow();

    es.shutdown();
//    es.submit(new MyTaskWithResult());

    while (lfs.size() > 0) {
      Iterator<Future<String>> ifs = lfs.iterator();
      while (ifs.hasNext()) {
        Future<String> fs = ifs.next();
        if (fs.isDone()) {
          if (fs.isCancelled()) {
            System.out.println("That one was canceled");
          } else {
            try {
              String result = fs.get();
              System.out.println("Result is: " + result);
            } catch (InterruptedException ie) {
              System.out.println("main method got interrupt");
            } catch (ExecutionException ee) {
              System.out.println("Task threw an exception " + ee.getCause());
              // don't expect cancellation exception because we test
              // for canceled above
//          } catch (CancellationException ce) {
//            System.out.println("oops, that task was already canceled");
            }
          }
          ifs.remove();
        }
      }
    }
    System.out.println("All done");
  }
}
