package data;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Sequence<T> extends Pair<T, Sequence<T>> {

    static <T> Sequence<T> of(T t) {
        return of(t, empty());
    }

    static <T> Sequence<T> of(T... elements) {
        Sequence<T> head = empty();
        for (T element : elements) {
            head = of(element, head);
        }
        return head.reverse();
    }

    static <T> Sequence<T> of(T t, Sequence<T> seq) {
        return new NonEmpty<>(t, seq);
    }

    static <T> Sequence<T> empty() {
        return new Empty<>();
    }

    default Sequence<T> add(T t) {
        return of(t, this);
    }


    boolean isEmpty();

    default T head() {
        return fisrt();
    }

    default Sequence<T> tail(){
        return second();
    }

    default Sequence<T> reverse() {
        Sequence<T> temp = this;
        Sequence<T> result = empty();
        while (!temp.isEmpty()) {
            result = of(temp.head(), result);
            temp = temp.tail();
        }
        return result;
    }

    default void foreach(Consumer<T> k) {
        Sequence<T> temp = this;
        while (!temp.isEmpty()) {
            k.accept(temp.head());
            temp = temp.tail();
        }
    }

    default <R> Sequence<R> map(Function<T, R> f) {
        if (isEmpty()) {
            return empty();
        } else {
            return of(f.apply(head()), tail().map(f));
        }
    }

    default Sequence<T> append(Sequence<T> seq) {
        return isEmpty() ? seq : of(head(), tail().append(seq));
    }

    default <R> R accumulate(R init, BiFunction<R, T, R> f) {
        return isEmpty() ? init : tail().accumulate(f.apply(init, head()), f);
    }

    default Sequence<T> filter(Predicate<T> p) {
        return isEmpty() ? empty() : p.test(head()) ? of(head(), tail().filter(p)) : tail().filter(p);
    }

    class NonEmpty<T> implements Sequence<T> {
        final T head;
        final Sequence<T> tail;

        NonEmpty(T head, Sequence<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public T fisrt() {
            return head;
        }

        @Override
        public Sequence<T> second() {
            return tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

    }

    class Empty<T> implements Sequence<T> {
        @Override
        public T fisrt() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Sequence<T> second() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }

}
