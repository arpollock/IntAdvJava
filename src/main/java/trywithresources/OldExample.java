package trywithresources;

import java.io.*;

public class OldExample {
  public static void main(String[] args) {
    FileReader fr = null;
    BufferedReader input = null;
    FileWriter fw = null;
    PrintWriter pw = null;

    try {
      fr = new FileReader("a.txt");
      input = new BufferedReader(fr);
      fw = new FileWriter("b.txt");
      pw = new PrintWriter(fw);

      String line;
      while ((line = input.readLine()) != null) {
        pw.println(line);
      }
//      pw.close();
//      input.close();
    } catch (IOException ioe) {
      System.out.println("oops, that failed: " + ioe.getMessage());
    } finally {
      if (pw != null) {
        pw.close();
      }
      if (input != null) {
        try {
          input.close();
        } catch (IOException ioe) {
          System.out.println("oops, didn't close");
        }
      }
    }
  }
}
