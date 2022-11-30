package newfeatures;

// final class cannot be subclassed
// (so can't have final abstract, nor final interface)
// Simon says "always declare new classes as final"
// 1) subclassing is terribly 1995
// 2) if you do this and realize you want a subtype, simply remove final
//    if you didn't, and realize that subtype is terribly 1995 and you
//    need to prevent it, it might be too late
final class Parent {}
//class Child extends Parent {} // not permitted
// final method cannot be overridden

// permitted subtypes must either:
// final, sealed type, or non-sealed
sealed interface X permits Car, Truck, RogueVehicle {}

// fields of a record are private & final
// intended for immutable data
// parent is always java.lang.Record, this cannot use extends
// but can implement an interface
// also, they are final classes
record Car(int seats, String color) implements X { }
record Truck(int payload) implements X {}

non-sealed class RogueVehicle implements X {}
class LandSpeeder extends RogueVehicle {}

public class PatternMatching {
  public static void main(String[] args) {
    Object obj = Math.random() > 0.5
      ? new Car(5, "Red")
      : new Truck(10_000);

    int packageWeight = Math.random() > 0.5
        ? 9_000
        : 600;

    // can this vehicle carry this package?
    // if it's a truck, must be less than payload
    // if it's a car, we need one driver, and the remaining seats
    // (by FAA rules) are good for 170 lbs each :)

    boolean canCarry = false;

//    if (obj instanceof Truck) {
//      Truck t = (Truck)obj;
//      if (t.payload() >= packageWeight) canCarry = true;
//    }
//    if (obj instanceof Car) {
//      Car c = (Car)obj;
//      if ((c.seats() - 1) * 170 > packageWeight) canCarry = true;
//    }

//    if (obj instanceof Truck t) {
//      if (t.payload() >= packageWeight) canCarry = true;
//    }
//    if (obj instanceof Car c) {
//      if ((c.seats() - 1) * 170 > packageWeight) canCarry = true;
//    }

    canCarry = switch ((X)obj) {
//    canCarry = switch (obj) {
//      case Car c when (c.seats() - 1) * 170 >= packageWeight -> true;
//      case Truck t when t.payload() >= packageWeight -> true;
//      default -> false;
      case Car c -> (c.seats() - 1) * 170 >= packageWeight;
      case Truck t -> t.payload() >= packageWeight;
      case RogueVehicle r -> false;
//      default -> throw new IllegalArgumentException(obj + " can't carry payloads");
    };

    System.out.printf("Vehicle %s %s carry package weighing %d\n",
        obj, canCarry ? "can" : "cannot", packageWeight);

    int x = 3;
    System.out.println(switch(x) {
      case 1,2,3 -> {
        int y = x + 2;
        yield "it's pretty small, less than " + y;
      }
      default -> "not that little";
    });
  }
}
