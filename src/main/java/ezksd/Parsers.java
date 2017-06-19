package ezksd;

import data.Option;
import data.Pair;
import java.util.function.Predicate;

public class Parsers {
    static boolean isNull(String s) {
        return s == null || s.length() == 0;
    }

    public static Parser<Character> item() {
        return s
                -> Option.guard(!isNull(s)).map(x
                -> new Pair<>(s.charAt(0), s.substring(1)));
    }

    public static Parser<Character> sat(Predicate<Character> pred) {
        return item().test(pred);
    }

    public static Parser<Character> oneOf(String s) {
        return sat(c -> s.indexOf(c) != -1);
    }

    public static Parser<Character> chr(char c) {
        return sat(x -> x == c);
    }

    public static Parser<Integer> integer() {
        Parser<Integer> num = sat(Character::isDigit)
                .map(c -> c - '0')
                .many1()
                .map(list -> list.foldl((a, b) -> a * 10 + b,0));
        return chr('-').skip(num).map(Math::negateExact)
                .or(() -> num);
    }

    public static Parser<String> alpha() {
        return sat(Character::isAlphabetic)
                .many1()
                .map(l -> l.foldl(StringBuffer::append,new StringBuffer()).toString());
    }

    public static Parser<String> symbol(String s) {
        if (isNull(s)) {
            return Parser.pure("");
        } else {
            return chr(s.charAt(0)).flatMap(h
                    -> symbol(s.substring(1)).map(rest
                    -> h + rest));
        }
    }

    public static Parser<Void> spaces() {
        return sat(c -> Character.isISOControl(c)||Character.isWhitespace(c))
                .many()
                .flatMap(x -> Parser.VOID_PARSER);
    }

    public static void main(String[] args){
        System.out.println(symbol("abc").parse("abcd"));
    }


}
