package badqueue;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BadQueue<E> {
  private final Object rendezvous = new Object();
  private static final int CAPACITY = 10;
  private E[] data = (E[]) new Object[CAPACITY];
  private int count = 0;

  public void put(E e) throws InterruptedException {
    // synchronize, almost always, on something that is
    // private to my data structure
    // Also, to have any chance of protecting a data structure
    // in the face of concurrent access, all the elements of
    // that structure MUST be private.
    synchronized (this.rendezvous) {
      while (count >= CAPACITY) { // MUST be a loop, not a single test
        // pause until we have space
        // reduced CPU usage good, but does not release lock
//        Thread.sleep(1);

        // wait, on the other hand
        // deschedules CPU until...
        // releases monitor lock
        // does not continue executing until lock is regained
        // (so be sure to be in a transactionally stable state at this point!)
        this.rendezvous.wait();
      }
      data[count++] = e;
      // notify causes ONE thread that is waiting on this same object (pillow :)
      // to move from "waiting for notification" to "waiting for monitor lock"
      // eventually (we hope) that other thread will gain the lock, and resume
      // execution. Notifications are NOT stored (and they wake at most
      // ONE thread)
//      this.rendezvous.notify();
      // notifyAll cases ALL waiting threads to move to
      // "waiting for monitor lock"
      // this is BADLY UNSCALABLE!!!
      this.rendezvous.notifyAll();
    }
  }

  public E take() throws InterruptedException {
    synchronized (this.rendezvous) {
      while (count <= 0) {
        // pause until we have data
        this.rendezvous.wait();
      }
      E retVal = data[0];
      System.arraycopy(data, 1, data, 0, --count);
//      this.rendezvous.notify();
      this.rendezvous.notifyAll();
      return retVal;
    }
  }
}

/*
What causes concurrency problems (root cause)
- Shared mutating state
- ALL DATA MUTATES... initialization!!

What are the three problems we must solve to have workable concurrent code
- Timing
- Transactions
- Visibility

What is the meaning of a happens-before (aka visibility) relationship?
- if you have a happens-before relationship you are entitled to expect
  to see the "effect" UNLESS some other effect (with or without an hb
  relationship) has changed that effect. I.e. absence of hb does NOT
  guarantee you WON'T see an effect! (Watch out for "race" conditions!)

What happens-before relationships exist?
- "sequence"-- if a and b are sequential in "normal program order" IN
  A SINGLE THREAD then hb(a, b)
- if hb(a, b) and hb(b, c) then hb(a, c) :)

- a bunch more, but the important ones are really those in the
  java.util.concurrent package :)

- if a is the last action in a chain of constructors and b is the first
  action of a finalizer then hb(a, b) (don't use finalizers!!!)
 */

/*
Test harness for BadQueue
- one producer thread, one consumer thread.
Producer:
- in a loop, counting to ~1000???
- allocate an array of two int values initialized to { counter, 0 }
- update the array to { counter, counter }
  idea is that { counter, counter } is "transactionally valid"
- put the array object into the queue

Consumer:
- in a loop with the same count limit
- take an item from the queue,
- validate that the item contains two matching values which are equal to
  the current loop counter--print a message if not!

Ensure:
- for the first 100 puts, have a delay between creating the array
  and setting the second element to count.
  - two reasons: 1) exaccerbate the potential for transactional problems
    2) it ensures that the queue is empty for a significant part of the test
- for the last 100 takes, have a delay...
  this ensures we test the "full-queue" behavior
- one of the put operations SHOULD put INVALID data in the queue to prove
  the validation of the consumer is working
 */

class TestBadQueue {
  public static void main(String[] args) throws InterruptedException {
    final int COUNT = 1000;
//    final BadQueue<int[]> queue = new BadQueue<>();
    final BlockingQueue<int[]> queue = new ArrayBlockingQueue<>(10);
    Thread producer = new Thread(() -> {
      System.out.println("Producer starting.");
      try {
        for (int i = 0; i < COUNT; i++) {
          int[] data = {i, 0}; // allocates a NEW array every time
          if (i < 100) {
            // test empty queue and transactional validity
            Thread.sleep(1);
          }
          data[1] = i;
          if (i == COUNT / 2) {
            data[0] = -1; // test the test
          }
          queue.put(data);
          data = null; // prevents reuse
//          data[0]... NONONONONO! Don't use it
        }
      } catch (InterruptedException ie) {
        System.out.println("Producer shutdown requested!");
      }
      System.out.println("Producer ending.");
    });
    Thread consumer = new Thread(() -> {
      System.out.println("Consumer starting.");
      try {
        for (int i = 0; i < COUNT; i++) {
          int[] data = queue.take();
          if (data[0] != data[1] || data[0] != i) {
            System.out.printf("Error at count %d, %s\n",
                i, Arrays.toString(data));
          }
          if (i > COUNT - 100) {
            Thread.sleep(1); // test queue full behavior
          }
        }
      } catch (InterruptedException ie) {
        System.out.println("Consumer shutdown requested!");
      }
      System.out.println("Consumer ending.");
    });
    // grab time...
    producer.start();
    consumer.start();
    System.out.println("Producer and consumer started by main");
    producer.join();
    consumer.join();
    // grab time
    System.out.println("All done, exiting.");
  }
}

/* Day 2 Lab 2
Add timing to the BadQueue transfer:
- remove delays in the test code
- add timing code and output
- increase the volume of data copied to get a total time of 10~30 seconds
Then:
- change notifyAll to notify
- redo timing any note any change
Then:
- change use of BadQueue to java.util.concurrent.ArrayBlockingQueue
- redo timing any note any change
 */

class TimeBadQueue {
  public static void main(String[] args) throws InterruptedException {
    final int COUNT = 50_000_000;
    final BadQueue<int[]> queue = new BadQueue<>();
    Thread producer = new Thread(() -> {
      System.out.println("Producer starting.");
      try {
        for (int i = 0; i < COUNT; i++) {
          int[] data = {i, 0}; // allocates a NEW array every time
//          if (i < 100) {
//            Thread.sleep(1);
//          }
          data[1] = i;
          if (i == COUNT / 2) {
            data[0] = -1; // test the test
          }
          queue.put(data);
          data = null; // prevents reuse
//          data[0]... NONONONONO! Don't use it
        }
      } catch (InterruptedException ie) {
        System.out.println("Producer shutdown requested!");
      }
      System.out.println("Producer ending.");
    });
    Thread consumer = new Thread(() -> {
      System.out.println("Consumer starting.");
      try {
        for (int i = 0; i < COUNT; i++) {
          int[] data = queue.take();
//          if (data[0] != data[1] || data[0] != i) {
//            System.out.printf("Error at count %d, %s\n",
//                i, Arrays.toString(data));
//          }
//          if (i > COUNT - 100) {
//            Thread.sleep(1); // test queue full behavior
//          }
        }
      } catch (InterruptedException ie) {
        System.out.println("Consumer shutdown requested!");
      }
      System.out.println("Consumer ending.");
    });
    // grab time...
    long start = System.nanoTime();
    producer.start();
    consumer.start();
    System.out.println("Producer and consumer started by main");
    producer.join();
    consumer.join();
    // grab time
    long time = System.nanoTime() - start;
    System.out.println("All done, exiting.");
    double ftime = time / 1_000_000_000.0;
    System.out.printf("Time %7.3f, throughput rate is %7.3f\n",
        ftime, COUNT / ftime );
  }
}
