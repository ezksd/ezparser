package ezksd;

import java.util.List;
import java.util.function.Predicate;

public class Parsers {
    static Predicate<Character> is(char c) {
        return ch -> ch == c;
    }
    static Parser<Byte> empty() {
        return b -> b.hasRemaining() ? Result.of(b.get()) : Result.fail();
    }

    static Parser<Character> ofChar(char c) {
        return empty().map(b -> (char) b.byteValue()).match(is(c));
    }


    static String listToString(List<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (Character c : list) {
            builder.append(c);
        }
        return builder.toString();
    }

    static int listToInt(List<Integer> list){
        int r = 0;
        for (Integer i : list) {
            System.out.println(i);
            r = r * 10 + i;
        }
        return r;
    }
    static Parser<String> alphaPaser = empty().match(Character::isAlphabetic).map(b -> (char) b.byteValue()).kleenPlus().map(Parsers::listToString);

    static Parser<Integer> numberPaser = empty().match(Character::isDigit).map(b -> b.intValue() - '0').kleenPlus().map(Parsers::listToInt);
}
