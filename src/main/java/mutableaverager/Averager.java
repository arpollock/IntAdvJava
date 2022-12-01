package mutableaverager;

import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

class Average {
  private double sum;
  private long count;

  public Average(double sum, long count) {
    this.sum = sum;
    this.count = count;
  }

  public void include(double d) {
    this.sum += d;
    this.count++;
  }

  public void merge(Average other) {
    this.sum += other.sum;
    this.count += other.count;
  }

  public OptionalDouble get() {
    if (count > 0) {
      return OptionalDouble.of(sum / count);
    } else {
      return OptionalDouble.empty();
    }
  }
}

public class Averager {
  public static void main(String[] args) {
    long start = System.nanoTime();
    DoubleStream.generate(() -> ThreadLocalRandom.current().nextDouble(-1, +1))
        .limit(9_000_000_000L)

    // generates an ordered stream, likely to cause out of memory!!!
//    DoubleStream.iterate(0, x -> ThreadLocalRandom.current().nextDouble(-1, +1))
//        .limit(1_000_000_000L)

    // seems to have a regression bug!!! Works well in Java 8, but
    // far worse in parallel than sequential ever since.
//    ThreadLocalRandom.current().doubles(1_000_000_000L, -1, +1)
        .parallel()
//        .unordered() // doesn't seem to be effective :(
        .collect(() -> new Average(0, 0),
            (a, d) -> a.include(d),
            (aFinal, aOther) -> aFinal.merge(aOther)
            )
        .get()
        .ifPresent(a -> System.out.println("Average is " + a));
    long time = System.nanoTime() - start;
    System.out.printf("Time was %7.3f\n", (time / 1_000_000_000.0));
  }
}
