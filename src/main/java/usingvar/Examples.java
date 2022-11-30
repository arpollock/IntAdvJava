package usingvar;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Examples {
//  var name = "Albert";

//  public static void doBadStuff(var x) {}

  public static Map<List<String>, Map.Entry<LocalDateTime, List<StringBuilder>>> get() {return null;}

  public static void doStuff(int[] ia) {}
  public static void main(String[] args) {
    var silly =
        get();
    var x = 2;
//    x = "hello";
    System.out.println(x);

    var ai = new int[2];
    var ai2 = new int[]{1,2,3};

    doStuff(ai2);
    doStuff(new int[]{1,2,3});
//    doStuff({1,2,3});

    int[] ai3 = {1,2,3};
//    var ai3 = {1,2,3};

    BiPredicate<String, Integer> ps =
        (@NotNull var s, var i) -> true;

    for (var x2 = 0; x2 < 10; x2++) {}

    try (var in = new FileInputStream("")) {

    } catch (IOException fnfe) {
//    } catch (var fnfe) { // NOPE
    }
    boolean b = Math.random() > 0.5;
    var obj =
        b ? "Hello" : 99;

    obj.compareTo(null);

//    "".compareTo("")
//    Integer.valueOf(99).compareTo(100)
//    99.compareTo(100)

//    int var = 99;
//    int goto = 100;
    var var = "var";
  }
}
