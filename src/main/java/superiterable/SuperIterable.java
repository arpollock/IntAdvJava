package superiterable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class SuperIterable<E> implements Iterable<E> {
  private Iterable<E> self;

  public SuperIterable(Iterable<E> self) {
    this.self = self;
  }

  public <F> SuperIterable<F> transform(Function<E, F> op) {
    List<F> res = new ArrayList<>();
    for (E e : self) {
      F f = op.apply(e);
      res.add(f);
    }
    return new SuperIterable<>(res);
  }

  @Override
  public Iterator<E> iterator() {
    return self.iterator();
  }
}
