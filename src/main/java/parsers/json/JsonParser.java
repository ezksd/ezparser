package parsers.json;


import data.IList;
import data.Option;
import data.Pair;
import ezksd.Parser;

import java.util.function.Function;

import static ezksd.Parser.pure;
import static ezksd.Parsers.*;
import static java.lang.Math.negateExact;
import static java.lang.Math.pow;

public class JsonParser implements Parser<JsonValue> {

    private static final String HEX = "0123456789ABCDEF";
    private static final Parser<JsonValue> impl = object().or(JsonParser::array);

    private static int sign(char c) {
        if (c == '-') {
            return -1;
        } else {
            return 1;
        }
    }

    public static void main(String[] args) {
        System.out.println(new JsonParser().parse("{" +
                "\"name\":\"xiaowang\"," +
                "\"score\":123.00e-1," +
                "\"flag\":true," +
                "    \"friend\":[\n" +
                "        {\n" +
                "            \"name\":\"xiaoli\",\n" +
                "            \"age\":21\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\":\"hehe\",\n" +
                "            \"sex\":\"male\"\n" +
                "        }\n" +
                "    ]\n" +
                "}"));

    }

    static private Parser<String> string() {
        Parser<Character> escape = chr('\\').flatMap(h ->
                oneOf("\"\\/bfnrt").map(x -> {
                    switch (x) {
                        case 'b':
                            return '\b';
                        case 'f':
                            return '\f';
                        case 'n':
                            return '\n';
                        case 'r':
                            return '\r';
                        case 't':
                            return '\t';
                        default:
                            return x;
                    }
                })
        );

        Parser<Character> unicode = chr('u').flatMap(j ->
                oneOf(HEX)
                        .map(HEX::indexOf)
                        .num(4)
                        .map(l -> l.foldl((a, b) -> a * 16 + b, 0))
                        .map(i -> (char) i.shortValue()));

        Parser<Character> ch = sat(x -> x != '\\' && x != '"');
        return ch.or(() -> escape).or(() -> unicode)
                .many()
                .map(l -> l.foldl(StringBuffer::append, new StringBuffer()))
                .map(StringBuffer::toString)
                .bracket(chr('"'), chr('"'));
    }

    static private Parser<JsonValue> number() {
        Parser<Character> sign = chr('-').or(() -> pure('+'));
        Function<IList<Integer>, Pair<Double, Integer>> collect = list
                -> list.foldl((p, i)
                -> new Pair<>(p.left() + i * pow(10d, negateExact(p.right())), p.right() + 1), new Pair<>(0d, 1));

        Parser<Double> decimal = chr('.').flatMap(z ->
                sat(Character::isDigit)
                        .map(c -> c - '0')
                        .many1()
                        .map(collect)
                        .map(Pair::left))
                .or(() -> pure(-1d));

        Parser<Integer> exponent = oneOf("eE").flatMap(z
                -> oneOf("+-").or(() -> pure(' ')).flatMap(s
                -> integer().map(num -> sign(s) * num)))
                .or(() -> pure(0));

        return sign.flatMap(s
                -> integer().flatMap(inte
                -> decimal.flatMap(dec
                -> exponent.map(exp
                ->
        {
            if (dec < 0) {
                return new JsonInteger((int) (sign(s) * inte * pow(10, exp)));
            } else {
                return new JsonFloat(sign(s) * (inte + dec) * pow(10, exp));
            }
        }))));
    }

    private static Parser<JsonValue> object() {
        Parser<Pair<String, JsonValue>> entry = string().token().flatMap(key
                -> chr(':').token().flatMap(s
                -> value().map(val
                -> new Pair<>(key, val))));
        return entry.sepby(chr(',').token()).bracket(chr('{').token(), chr('}').token()).map(JsonObject::new);
    }

    private static Parser<JsonValue> array() {
        return value()
                .sepby(chr(',').token())
                .bracket(chr('[').token(), chr(']').token())
                .map(JsonArray::new);
    }

    private static Parser<JsonValue> value() {
        Parser<JsonValue> jsonString = string().token().map(JsonString::new);
        Parser<JsonValue> jsonNull = symbol("null").token().map(x -> JsonNull.Value);
        Parser<JsonValue> jsonBool = symbol("true").token().or(() -> symbol("false").token()).map(x -> {
            if (x.equals("true")) {
                return JsonBool.TRUE;
            } else {
                return JsonBool.FALSE;
            }
        });

        return object()
                .or(JsonParser::array)
                .or(JsonParser::number)
                .or(() -> jsonString)
                .or(() -> jsonBool)
                .or(() -> jsonNull);
    }

    @Override
    public Option<Pair<JsonValue, String>> parse(String input) {
        return impl.parse(input);
    }


}
