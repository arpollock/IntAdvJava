package superiterable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SuperIterable<E> implements Iterable<E> {
  private Iterable<E> self;

  public SuperIterable(Iterable<E> self) {
    this.self = self;
  }

//  @Override
//  public void forEach(Consumer<? super E> op) {
//    for (E e : self) {
//      op.accept(e);
//    }
//  }

  public SuperIterable<E> filter(Predicate<E> op) {
    List<E> res = new ArrayList<>();
    for (E e : self) {
      if (op.test(e)) {
        res.add(e);
      }
    }
    return new SuperIterable<>(res);
  }

  // "Bucket o'Data" with this kind of "map" operation
  // is called by mathematicians and function programmers
  // a "Functor"
  public <F> SuperIterable<F> map(Function<E, F> op) {
    List<F> res = new ArrayList<>();
    for (E e : self) {
      F f = op.apply(e);
      res.add(f);
    }
    return new SuperIterable<>(res);
  }

  // "Bucket o'Data" with this kind of flatMap operation
  // it's called a "Monad"
  public <F> SuperIterable<F> flatMap(Function<E, SuperIterable<F>> op) {
    List<F> res = new ArrayList<>();
    for (E e : self) {
      SuperIterable<F> manyF = op.apply(e);
      for (F f : manyF) {
        res.add(f);
      }
    }
    return new SuperIterable<>(res);
  }

  @Override
  public Iterator<E> iterator() {
    return self.iterator();
  }
}
