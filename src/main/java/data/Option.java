package data;

import java.util.Objects;
import java.util.function.Function;

public class Option<T> {
    T val;
    private static final Option<?> EMPTY = new Option<>(null);
    private static final Option<Void> VOID = new Option<>(null);
    private Option(T val) {
        this.val = val;
    }

    public T get() {
        if (this == EMPTY) {
            throw new IllegalStateException();
        } else {
            return val;
        }
    }

    public Option<T> or(Option<T> other) {
        if (isEmpty()) {
            return other;
        } else {
            return this;
        }
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
    public <R> Option<R> map(Function<T, R> f) {
        return flatMap(t -> of(f.apply(t)));
    }

    public <R> Option<R> flatMap(Function<T, Option<R>> f) {
        if (isEmpty()) {
            return empty();
        } else {
            return f.apply(val);
        }
    }

    public static <T> Option<T> of(T t) {
        Objects.requireNonNull(t);
        return new Option<>(t);
    }


    @SuppressWarnings("unchecked")
    public static <T> Option<T> empty() {
        return (Option<T>) EMPTY;
    }

    public static Option<Void> guard(boolean b) {
        if (b) {
            return VOID;
        } else {
            return empty();
        }
    }


    @Override
    public String toString() {
        if (isEmpty()) {
            return "Option.empty";
        } else {
            return "Option : " + val.toString();
        }
    }
}
