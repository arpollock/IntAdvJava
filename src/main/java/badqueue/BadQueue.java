package badqueue;

public class BadQueue<E> {
  private static final int CAPACITY = 10;
  private E[] data = (E[])new Object[CAPACITY];
  private int count = 0;

  public void put(E e) {
    if (count < CAPACITY) {
      data[count++] = e;
    } // else ???
  }

  public E take() {
    if (count > 0) {
      E retVal = data[0];
      System.arraycopy(data, 1, data, 0, --count);
      return retVal;
    } // else ???
  }
}
