package ezksd;

import data.Pair;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Result<E> {
    E get();

    default E orELse(E e) {
        return isSucess() ? get() : e;
    }

    default boolean isSucess() {
        return this instanceof Success;
    }

    default Optional<E> toOptional() {
        return isSucess() ? Optional.of(get()) : Optional.empty();
    }

    default boolean test(Predicate<E> pred) {
        return isSucess() && pred.test(get());
    }

    default <U> Result<U> map(Function<E, U> f) {
        return isSucess() ? of(f.apply(get())) : fail();
    }

    default <S> Result<Pair<E, S>> combine(Result<S> that) {
        return this.isSucess() && that.isSucess() ? of(new Pair<>(this.get(), that.get())) : fail();
    }

    static <E> Result<E> of(E e) {
        return new Success<>(e);
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
