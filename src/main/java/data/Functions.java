package data;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Functions {
    @FunctionalInterface
    interface TriFunction<T, S, U,R>{
        R aplly(T a, S b, U c);
    }

    static <T,U,R> Function<Pair<T,U>,R> adpater(BiFunction<T,U,R> f) {
        return pair -> f.apply(pair.fisrt(), pair.second());
    }

    static <A,B,C,R> Function<Pair<Pair<A,B>,C>,R> adpater(TriFunction<A,B,C,R> f ){
        return pair -> f.aplly(pair.fisrt().fisrt(), pair.fisrt().second(), pair.second());
    }

    static <T, U, R> Function<T, R> partial(BiFunction<T, U, R> f, U u) {
        return a -> f.apply(a, u);
    }
}
