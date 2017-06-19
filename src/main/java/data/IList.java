package data;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IList<T> extends Pair<T, IList<T>> {

    private static final IList<Void> EMPTY = new IList<>(null, null);

    public IList(T t, IList<T> tiList) {
        super(t, tiList);
    }

    @SuppressWarnings("unchecked")
    public static <T> IList<T> empty() {
        return (IList<T>) EMPTY;
    }

    public static <T> IList<T> of(T... xs) {
        Objects.requireNonNull(xs);
        IList<T> t = empty();
        int i = xs.length - 1;
        while (i >= 0) {
            t = new IList<>(xs[i--], t);
        }
        return t;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public IList<T> append(IList<T> list) {
        if (isEmpty()) {
            return list;
        } else {
            return new IList<>(a, b.append(list));
        }
    }

    public <R> IList<R> map(Function<? super T, ? extends R> f) {
        if (isEmpty()) {
            return empty();
        } else {
            return new IList<>(f.apply(a), b.map(f));
        }
    }

    public <R> IList<R> flatMap(Function<? super T, ? extends IList<R>> f) {
        if (isEmpty()) {
            return empty();
        } else {
            return f.apply(a).append(b.flatMap(f));
        }
    }

    public <U> U foldl(BiFunction<U, T, U> comb,U init) {
        IList<T> t = this;
        U r = init;
        while (!t.isEmpty()) {
            r = comb.apply(r, t.a);
            t = t.b;
        }
        return r;
    }

    public <U> U foldr(BiFunction<T, U, U> comb,U init) {
        return foldl( (u, t) -> comb.apply(t, u),init);
    }


    @Override
    public String toString() {
        StringBuffer buf = foldl((sb, t) -> sb.append(t).append(','),new StringBuffer().append('['));
        if (!isEmpty()) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.append(']').toString();
    }


}
