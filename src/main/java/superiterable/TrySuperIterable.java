package superiterable;

import java.util.List;
import java.util.function.Function;

public class TrySuperIterable {
  public static void main(String[] args) {
    SuperIterable<String> names = new SuperIterable<>(
        List.of("Fred", "Jim", "Sheila")
    );

    for (String s : names) {
      System.out.println("> " + s);
    }

    Function<String, Integer> fsi = s -> s.length();
  }
}
