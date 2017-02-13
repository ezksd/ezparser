package ezksd;

import data.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static ezksd.Result.fail;
import static ezksd.Result.of;

public class Parsers {
    public static final Function<List<Byte>, byte[]> toArray = list -> {
        byte[] r = new byte[list.size()];
        int i = 0;
        for (Byte b : list) {
            r[i++] = b;
        }
        return r;
    };

    public static final Function<List<Byte>, String> toString = toArray.andThen(String::new);
    public static final Function<List<Byte>, Integer> toInt = toString.andThen(Integer::valueOf);

    public static Predicate<Byte> is(byte b) {
        return b1 -> b1 == b;
    }

    public static Predicate<Byte> not(byte b) {
        return by -> by != b;
    }

    public static Predicate<Byte> in(byte... bytes) {
        return by -> {
            boolean flag = false;
            for (byte b : bytes) {
                if (by == b) {
                    return true;
                }
            }
            return false;
        };
    }

    public static <E, R> R let(E val, Function<E, R> f) {
        return f.apply(val);
    }

    static public <K, V> HashMap<K, V> toMap(List<Pair<K, V>> list) {
        return list.stream().collect(HashMap::new, (map, pair) -> map.put(pair.fisrt(), pair.second()), HashMap::putAll);
    }

    public static Parser<Byte> start() {
        return b -> b.hasRemaining() ? of(b.get()) : fail();
    }

    public static Parser<Byte> match(Predicate<Byte> p) {
        return start().test(p);
    }

    public static Parser<List<Byte>> till(Predicate<Byte> p) {
        return match(p).plus();
    }

    public static Parser<String> matchString(Predicate<Byte> p) {
        return till(p).map(toString);
    }

    public static Parser<String> until(byte b) {
        return matchString(not(b));
    }

    public static Parser<byte[]> matchByteArray() {
        return start().star().map(toArray);
    }

    public static Parser<Integer> matchInt() {
        return till(Character::isDigit).map(toInt);
    }

}
