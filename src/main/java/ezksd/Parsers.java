package ezksd;

import java.util.List;
import java.util.function.Predicate;

public class Parsers {
    public static Predicate<Character> is(char c) {
        return ch -> ch == c;
    }

    public static Predicate<Byte> is(byte b) {
        return b1 -> b1 == b;
    }

    public static Predicate<Byte> not(byte b) {
        return by -> by != b;
    }
    public static Predicate<Byte> not(byte b1,byte b2) {
        return by -> by != b1 && by != b2;
    }

    public static Parser<Byte> empty() {
        return b -> b.hasRemaining() ? Result.of(b.get()) : Result.fail();
    }

    public static Parser<String> match(Predicate<Byte> pred) {
        return empty().match(pred).kleenPlus().map(Parsers::byteListToString);
    }

    public static Parser<Byte> matchOnce(byte b) {
        return empty().match(is(b));
    }

    public static byte[] byteListToArray(List<Byte> list) {
        byte[] r = new byte[list.size()];
        int i = 0;
        for (Byte b : list) {
            r[i++] = b;
        }
        return r;
    }

    public static String byteListToString(List<Byte> list) {
        return new String(byteListToArray(list));
    }


    static int listToInt(List<Integer> list) {
        int r = 0;
        for (Integer i : list) {
            r = r * 10 + i;
        }
        return r;
    }


}
