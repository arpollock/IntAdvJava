package immutableaverager;

import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

record Average(double sum, long count) {
  public Average merge(Average other) {
    return new Average(this.sum + other.sum, this.count + other.count);
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
    DoubleStream.generate(
        () -> ThreadLocalRandom.current().nextDouble(-1, +1)
    )
        .limit(3_000_000_000L)
        .parallel()
        .mapToObj(v -> new Average(v, 1))
        .reduce(new Average(0, 0), Average::merge)
        .get()
        .ifPresent(a -> System.out.println("The average is " + a));
    long time = System.nanoTime() - start;
    System.out.printf("Time was %7.3f\n", (time / 1_000_000_000.0));

  }
}
