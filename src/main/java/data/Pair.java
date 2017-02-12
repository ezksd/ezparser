package data;

import java.util.Objects;

public interface Pair<T, S> {
    static <T, S> Pair<T, S> of(T t, S s) {
        return new PairImpl<T, S>(t, s);
    }

    T fisrt();

    S second();

    class PairImpl<T, S> implements Pair<T, S> {
        final T first;
        final S second;

        public PairImpl(T first, S second) {
            Objects.requireNonNull(first);
            Objects.requireNonNull(second);
            this.first = first;
            this.second = second;
        }

        @Override
        public T fisrt() {
            return first;
        }

        @Override
        public S second() {
            return second;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof PairImpl && (fisrt().equals(((PairImpl) obj).fisrt()) && second().equals(((PairImpl) obj).second()));
        }
    }

}
