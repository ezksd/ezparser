package ezksd;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ezksd.Result.fail;
import static ezksd.Result.of;

public class Parsers {
    public static Predicate<Byte> is(byte b) {
        return b1 -> b1 == b;
    }

    public static Predicate<Byte> not(byte b) {
        return by -> by != b;
    }

    public static <T> Predicate<T> and(Predicate<T>... preds) {
        return t -> {
            for (Predicate<T> pred : preds) {
                if (!pred.test(t))
                    return false;
            }
            return true;
        };
    }

    public static <T> Predicate<T> or(Predicate<T>... preds) {
        return t -> {
            for (Predicate<T> pred : preds) {
                if (pred.test(t))
                    return true;
            }
            return false;
        };
    }

    static public final Function<List<Byte>, byte[]> toArray = list -> {
        byte[] r = new byte[list.size()];
        int i = 0;
        for (Byte b : list) {
            r[i++] = b;
        }
        return r;
    };

    @FunctionalInterface
    public interface Statement {
        void exec();
    }

    public static <E,R> R let(E val,Function<E,R> f){
        return f.apply(val);
    }

    public static <E> E begin(Statement stat, Supplier<E> s) {
        stat.exec();
        return s.get();
    }

    static public final Function<List<Byte>, String> toString = toArray.andThen(String::new);

    static public final Function<List<Byte>, Integer> toInt = toString.andThen(Integer::valueOf);

    public static Parser<Byte> start() {
        return b -> b.hasRemaining() ? of(b.get()) : fail();
    }

    public static Parser<Byte> matchOnce(Predicate<Byte> p) {
        return start().test(p);
    }

    public static Parser<List<Byte>> match(Predicate<Byte> p) {
        return matchOnce(p).star();
    }


    public static Parser<String> matchString(Predicate<Byte> p) {
        return match(p).map(toString);
    }

    public static Parser<String> until(byte b) {
        return matchString(not(b));
    }

    public static Parser<byte[]> matchByteArray() {
        return start().star().map(toArray);
    }

    public static Parser<Integer> matchInt() {
        return match(Character::isDigit).map(toInt);
    }



}
