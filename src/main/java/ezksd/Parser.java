package ezksd;

import data.IList;
import data.Option;
import data.Pair;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface Parser<T> {
    Parser<Void> VOID_PARSER = pure(null);
    Parser<?> EMPTY_PARSER = s -> Option.empty();

    static <T> Parser<T> pure(T t) {
        return s -> Option.of(new Pair<>(t, s));
    }

    @SuppressWarnings("unchecked")
    static <T> Parser<T> fail() {
        return (Parser<T>) EMPTY_PARSER;
    }

    static Parser<Void> guard(boolean b) {
        if (b) {
            return VOID_PARSER;
        } else {
            return fail();
        }
    }

    Option<Pair<T, String>> parse(String input);

    default <R> Parser<R> ap(Parser<Function<? super T, ? extends R>> parser) {
        return flatMap(x
                -> parser.flatMap(op
                -> pure(op.apply(x))));
    }

    default <R> Parser<R> map(Function<? super T, ? extends R> f) {
        return s
                -> parse(s).map(pair
                -> new Pair<>(f.apply(pair.left()), pair.right()));
    }

    default <R> Parser<R> flatMap(Function<? super T, Parser<R>> f) {
        return s
                -> parse(s).flatMap(pair
                -> f.apply(pair.left()).parse(pair.right()));
    }

    default <R> Parser<R> skip(Parser<R> r) {
        return flatMap(x -> r);
    }

    default Parser<T> test(Predicate<? super T> pred) {
        return flatMap(x
                -> guard(pred.test(x)).map(y
                -> x));
    }

    default Parser<T> or(Supplier<Parser<T>> that) {
        return s -> {
            Option<Pair<T, String>> o = this.parse(s);
            if (o.isEmpty()) {
                return that.get().parse(s);
            } else {
                return o;
            }
        };
    }

    default Parser<IList<T>> many() {
        return many1().or(()
                -> pure(IList.empty()));
    }

    default Parser<IList<T>> many1() {
        return flatMap(x
                -> many().flatMap(xs
                -> pure(new IList<>(x, xs))));
    }

    default Parser<IList<T>> num(int n) {
        if (n == 0) {
            return pure(IList.empty());
        } else {
            return flatMap(x ->
                    num(n - 1).map(xs ->
                            new IList<>(x, xs)));
        }
    }

    default Parser<T> token() {
        return Parsers.spaces().flatMap(x -> this);
    }

    default <A> Parser<IList<T>> sepby1(Parser<A> separatoer) {
        Parser<IList<T>> rest = separatoer.flatMap(x -> this).many();
        return flatMap(x
                -> rest.map(xs
                -> new IList<>(x, xs)));
    }

    default <A> Parser<IList<T>> sepby(Parser<A> parser) {
        return sepby1(parser)
                .or(() -> pure(IList.empty()));
    }

    default Parser<T> chainl1(Parser<BiFunction<T, T, T>> comb) {
        Parser<Function<T, T>> unaryOp = comb.flatMap(op
                -> map(x
                -> t
                -> op.apply(t, x)));
        return flatMap(x ->
                unaryOp.many().map(
                        op -> op.foldr(Function::apply, x)));
    }

    default <A, B> Parser<T> bracket(Parser<A> left, Parser<B> right) {
        return left.flatMap(l
                -> flatMap(val
                -> right.map(r
                -> val)));
    }


}
