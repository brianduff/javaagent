package org.dubh.examples.agent.target;

public class Greeter {
  private static String getName() {
    return "World";
  }

  static void sayHello() {
    System.out.printf("Hello %s\n", getName());
  }
}
