package superiterable;

import java.util.List;
import java.util.function.Function;

public class TrySuperIterable {
  public static void main(String[] args) {
    SuperIterable<String> names = new SuperIterable<>(
        List.of("Fred", "Jim", "Sheila")
    );

    names.forEach(System.out::println);
/*
    for (String s : names) {
      System.out.println("> " + s);
    }
*/

    Function<String, Integer> fsi = s -> s.length();

    names
        .filter(s -> s.charAt(0) != 'S')
        .map(fsi)
        .forEach(i -> System.out.println("length is " + i));

  }
}
