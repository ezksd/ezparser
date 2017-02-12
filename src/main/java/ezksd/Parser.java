package ezksd;

import data.Pair;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static ezksd.Parsers.*;

@FunctionalInterface
public interface Parser<E> {
    Result<E> tryParse(ByteBuffer buffer);
    default Result<E> parse(ByteBuffer buffer){
        return let(buffer.position(), p -> tryParse(buffer).onFailExec(()->buffer.position(p)));
    }

    default <R> Parser<R> then(Function<Result<E>, Result<R>> f) {
        return b -> f.apply(parse(b));
    }

    default <U> Parser<U> map(Function<E, U> f) {
        return b -> parse(b).map(f);
    }

    default Result<E> parse(String s) {
        return parse(ByteBuffer.wrap(s.getBytes()));
    }


    default Parser<E> test(Predicate<E> p) {
        return (b -> parse(b).match(p));
    }

    default <U> Parser<Pair<E, U>> link(Parser<U> p) {
        return b -> parse(b).flatmap(r1 -> p.parse(b).flatmap(r2 -> Result.of(r1, r2)));
    }

    default <U> Parser<E> or(Parser<E> p) {
        return b -> parse(b).or(() -> p.parse(b));
    }

    default <U> Parser<E> skip(Parser<U> p) {
        return link(p).map(Pair::getFirst);
    }

    default Parser<E> skip(Predicate<Byte> p) {
        return skip(match(p));
    }

    default Parser<E> skipAll(Predicate<Byte> p) {
        return skip(match(p).star());
    }

    default Parser<E> skip(byte c) {
        return skip(matchOnce(is(c)));
    }

    default Parser<List<E>> plus() {
        return b -> star().parse(b).match(l -> !l.isEmpty());
    }

    default Parser<List<E>> star() {
        return b -> let(new ArrayList<E>(), list -> {
            Result<E> r;
            while ((r = parse(b)).isSucess()) {
                list.add(r.get());
            }
            return Result.of(list);
        });
    }


}
