package parser.evaluator;

import data.Pair;
import ezksd.Parser;
import ezksd.Result;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.BinaryOperator;

import static data.Functions.adpater;
import static ezksd.Parsers.*;

public class Evaluator {
    static final byte PLUS = '+', MINUS = '-', MULTI = '*', DIVID = '/', LEFT = '(', RIGHT = ')';

    static BinaryOperator<Integer> toOperator(byte by) {
        switch (by) {
            case PLUS:
                return (a, b) -> a + b;
            case MINUS:
                return (a, b) -> a - b;
            case MULTI:
                return (a, b) -> a * b;
            default:
                return (a, b) -> a / b;
        }
    }

    static int accumulate(int i, List<Pair<BinaryOperator<Integer>, Integer>> list) {
        return list.stream().reduce(i, (r, p) -> p.fisrt().apply(r, p.second()), (a, b) -> a + b);
    }

    /*
        expr ::= term ('+' term | '-' term) *
        term :: = number ('*' number| '/' number) *
        number ::=

     */

    static Result<Integer> expr(ByteBuffer buffer) {
        Parser<Integer> term = Evaluator::term;
        return term.link(match(in(PLUS, MINUS)).map(Evaluator::toOperator).link(term).star()).map(adpater(Evaluator::accumulate)).parse(buffer);
    }

    static Result<Integer> term(ByteBuffer buffer) {
        Parser<Integer> number = Evaluator::number;
        return number.link(match(in(MULTI, DIVID)).map(Evaluator::toOperator).link(number).star()).map(adpater(Evaluator::accumulate)).parse(buffer);
    }

    static Result<Integer> number(ByteBuffer buffer) {
        return matchInt().or(match(is(LEFT)).link(Evaluator::expr).skip(RIGHT).map(Pair::second)).parse(buffer);
    }

    public static void main(String[] args) {
        /*

         */
        Result<Integer> parser = expr(ByteBuffer.wrap("1+2*3-4/(2+2)".getBytes()));
        System.out.println(parser.get());
    }

}
