package textblock;

public class Example {
  public static void main(String[] args) {
    String text = String.format("""
        hello, this is number %d
        several lines of what I call ""Text""\"""\"""
           There are three in total
        """, 99)/*.indent(8)*/;
    System.out.println(text);
  }
}
