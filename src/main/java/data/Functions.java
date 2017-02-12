package data;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Functions {
    @FunctionalInterface
    interface TriFunction<A,B,C,R>{
        R aplly(A a, B b, C c);
    }

    static <T,U,R> Function<Pair<T,U>,R> adpater(BiFunction<T,U,R> f) {
        return pair -> f.apply(pair.first, pair.second);
    }

    static <A,B,C,R> Function<Pair<Pair<A,B>,C>,R> adpater(TriFunction<A,B,C,R> f ){
        return pair -> f.aplly(pair.first.first, pair.first.second, pair.second);
    }
}
