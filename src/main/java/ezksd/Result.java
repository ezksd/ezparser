package ezksd;

import data.Pair;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Result<E> {
    E get();

    default E orELse(Supplier<E> s) {
        return isSucess() ? get() : s.get();
    }

    default boolean isSucess() {
        return this instanceof Success;
    }

    default <U> Result<U> map(Function<E, U> f) {
        return isSucess() ? of(f.apply(get())) : fail();
    }

    default <U> Result<U> flatmap(Function<E, Result<U>> f) {
        return isSucess() ? f.apply(get()) : fail();
    }
    default Result<E> match(Predicate<E> pred) {
        return flatmap(r -> pred.test(r) ? of(r) : fail());
    }

    default <U> Result<U> onSucc(Supplier<U> sup) {
        return map(e -> sup.get());
    }

    default Result<E> onFailExec(Supplier<?> stat) {
        if(!isSucess())
            stat.get();
        return this;
    }

    default Result<E> or(Supplier<Result<E>> sup){
        return isSucess()?this:sup.get();
    }

    static <E> Result<E> of(E e) {
        return new Success<>(e);
    }

    static <A, B> Result<Pair<A, B>> of(A a, B b) {
        return of(Pair.of(a, b));
    }

    static <E> Result<E> fail() {
        return new Fail<>();
    }

    class Success<E> implements Result<E> {
        E val;

        Success(E val) {
            this.val = val;
        }

        public E get() {
            return val;
        }
    }

    class Fail<E> implements Result<E> {
        public E get() {
            return null;
        }
    }
}
