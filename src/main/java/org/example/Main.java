package org.example;

record X(int x){}

public class Main {
  public static void main(String[] args) {
    System.out.println(switch(new X(99)){
      case X(int x) when x > 99 -> "Odd";
      default -> "Hello Java 19 preview 3 world!";
    });
  }
}