package superiterable;

import java.util.List;
import java.util.stream.Stream;

class Student {
  String name;
  double gpa;
  List<String> courses;

  public Student(String name, double gpa, String... courses) {
    this.name = name;
    this.gpa = gpa;
    this.courses = List.of(courses);
  }

  public String getName() {
    return name;
  }

  public double getGpa() {
    return gpa;
  }

  public List<String> getCourses() {
    return courses;
  }

  @Override
  public String toString() {
    return "Student{" +
        "name='" + name + '\'' +
        ", gpa=" + gpa +
        ", courses=" + courses +
        '}';
  }
}

public class School {
  public static void main(String[] args) {
    List<Student> students = List.of(
        new Student("Fred", 3.2, "Math", "Physics"),
        new Student("Jim", 2.2, "Journalism"),
        new Student("Shiela", 3.7,
            "Math", "Physics", "Quantum Mechanics", "Astro Physics")
    );
    SuperIterable<Student> school = new SuperIterable<>(students);

    school
        .filter(s -> s.getGpa() > 3)
        .map(s -> "Student " + s.getName() + " takes "
            + s.getCourses().size() + " courses "
            + " and has a gpa of " + s.getGpa()
        )
        .forEach(System.out::println);

    System.out.println("----------");
    school
        .flatMap(s -> new SuperIterable<>(s.getCourses()))
        .forEach(System.out::println);

    System.out.println("----------");

    students.stream()
        .filter(s -> s.getGpa() > 3)
        .map(s -> s.getName() + " takes " + s.getCourses())
        .forEach(System.out::println);

    System.out.println("----------");

    students.stream()
        .flatMap(s -> s.getCourses().stream()
            .map(c -> "Student " + s.getName() + " takes " + c))
        .forEach(System.out::println);

    System.out.println("----------");

    Stream<Student> ss = students.stream();
    // these two reuse the stream, that's NOT ALLOWED
//    ss.forEach(System.out::println);
//    ss.filter(s -> s.getGpa() < 3.5);

    ss
        .peek(s -> System.out.println("peek1: " + s))
        .filter(s -> s.getGpa() > 3)
        .peek(s -> System.out.println("peek2: " + s))
        .forEach(System.out::println);

    System.out.println("----------");

    Stream.iterate(0, x -> x + 1)
        .forEach(System.out::println);

  }
}
