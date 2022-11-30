package client;

// Compilation:
// javac -d modules --module-source-path "/home/simon/IdeaProjects/IntAdvJava/*/src/main/java" --module-path "/home/simon/IdeaProjects/IntAdvJava/modules" --module yourmods.client,mymods.service
// Execution:
// java --module-path ./modules --module yourmods.client/client.ClientMain

import java.lang.reflect.Field;

public class ClientMain {
  public static void main(String[] args) throws Throwable {
    System.out.println(myservice.TheService.message);
//    System.out.println(myservice.TheService.localMessage);
//    System.out.println(myservice.Sneaky.getTheMessage());

    Class<?> cl = Class.forName("myservice.ReflectMe");
    Field field = cl.getDeclaredField("message");
    field.setAccessible(true);
    Object msg = field.get(null);
    System.out.println("The secret message is " + msg);
  }
}
