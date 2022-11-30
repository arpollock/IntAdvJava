package trywithresources;

import java.io.*;

public class NewExample {
  public static void main(String[] args) throws FileNotFoundException {

    FileReader fr = new FileReader("a.txt");

    try ( // These are "resources", they are:
          // effectively final
          // implements AutoCloseable
//        FileReader fr = new FileReader("a.txt");
          fr; // now will be closed but remains in scope
        BufferedReader input = new BufferedReader(fr);
        FileWriter fw = new FileWriter("b.txt");
        PrintWriter pw = new PrintWriter(fw);
    ) {
      String line;
      while ((line = input.readLine()) != null) {
        pw.println(line);
      }
//      fr = null; // nope, it's effectively final
    } catch (IOException ioe) {
      System.out.println("oops, that broke " + ioe.getMessage());
    } finally { // auto-generated finally before any we add
      // auto generared finally closes in reverse order of resource
      // appearance above
//      fr. // NOPE, out of scope now (unless declared prior to try () block)
    }
//    fr // still out of scope! (unless declared prior to try () block)
  }
}
