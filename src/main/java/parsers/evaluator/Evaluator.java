package parsers.evaluator;


import data.IList;
import data.Option;
import data.Pair;
import ezksd.Parser;

import java.util.function.BiFunction;
import java.util.function.Function;

import static ezksd.Parser.pure;
import static ezksd.Parsers.*;
import static java.lang.Math.negateExact;
import static java.lang.Math.pow;

public class Evaluator implements Parser<Double> {
    private static final Parser<Double> impl = expr();

    @Override
    public Option<Pair<Double, String>> parse(String input) {
        return impl.parse(input);
    }

    private static Parser<Double> expr() {
        return factor().chainl1(oneOf("+-").token().map(Evaluator::trans));
    }

    private static Parser<Double> factor(){
        return term().chainl1(oneOf("*/").token().map(Evaluator::trans));
    }

    private static Parser<Double> term(){
        return num().token().or(() -> expr().bracket(chr('(').token(),chr(')').token()));
    }

    private static Parser<Double> num() {
        Function<IList<Integer>, Pair<Double, Integer>> collect = list
                -> list.foldl((p, i)
                -> new Pair<>(p.left() + i * pow(10d, negateExact(p.right())), p.right() + 1),
                new Pair<>(0d, 1));

        Parser<Double> decimal = chr('.').flatMap(z ->
                sat(Character::isDigit)
                        .map(c -> c - '0')
                        .many1()
                        .map(collect)
                        .map(Pair::left))
                .or(() -> pure(0d));

        return integer().flatMap(integer
                -> decimal.map(dec
                -> integer + dec));
    }

    private static BiFunction<Double,Double,Double> trans(char c) {
        switch (c) {
            case '+':
                return (a,b) -> a + b;
            case '-':
                return (a,b) -> a - b;
            case'*':
                return (a,b) -> a * b;
            case '/':
                return (a,b) -> a / b;
            default:
                throw new IllegalStateException();
        }
    }


    public static void main(String[] args){
        System.out.println(new Evaluator().parse("1 + 2 * 3 / (2 + 2)"));
    }
}
