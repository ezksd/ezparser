package ezksd;

import data.AorB;
import data.Pair;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Parser<E> {
    Result<E> parse(ByteBuffer buffer);

    default <U> Parser<U> map(Function<E, U> f) {
        return b -> {
            Result<E> r = parse(b);
            return r.isSucess() ? r.map(f) : Result.fail();
        };
    }


    default Parser<E> match(Predicate<E> pred) {
        return b -> {
            b.mark();
            Result<E> r = parse(b);
            if (r.test(pred)) {
                return r;
            } else {
                b.reset();
                return Result.fail();
            }
        };
    }

    default <U> Parser<Pair<E, U>> link(Parser<U> that) {
        return (ByteBuffer b) -> {
            b.mark();
            Result<E> r1;
            Result<U> r2;
            if ((r1 = this.parse(b)).isSucess() && (r2 = that.parse(b)).isSucess()) {
                return r1.combine(r2);
            } else {
                b.reset();
                return Result.fail();
            }
        };
    }

    default Parser<Optional<E>> option(Parser<E> parser) {
        return b -> Result.of(parser.parse(b).toOptional());
    }

    default <U> Parser<E> skip(Parser<U> that) {
        return link(that).map(Pair::getFirst);
    }

    default <U> Parser<AorB<E, U>> or(Parser<U> that) {
        return b -> {
            Result<E> r1;
            Result<U> r2;
            if ((r1 = this.parse(b)).isSucess()) {
                return Result.of(new AorB<E, U>(r1.get()));
            } else if ((r2 = that.parse(b)).isSucess()) {
                return Result.of(new AorB<E, U>(r2.get()));
            } else {
                return Result.fail();
            }
        };
    }


    default Parser<List<E>> kleenPlus() {
        return b -> {
            ArrayList<E> list = new ArrayList<>();
            Result<E> r;
            while ((r = parse(b)).isSucess()) {
                list.add(r.get());
            }
            return list.isEmpty() ? Result.fail() : Result.of(list);
        };
    }

    default Parser<List<E>> kleenStar() {
        return b -> {
            ArrayList<E> list = new ArrayList<>();
            Result<E> r;
            while ((r = parse(b)).isSucess()) {
                list.add(r.get());
            }
            return Result.of(list);
        };
    }


}
