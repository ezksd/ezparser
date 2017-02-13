package parser.evaluator;

import data.Funcs;
import data.Pair;
import ezksd.Parser;
import ezksd.Result;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static data.Funcs.adpater;
import static ezksd.Parsers.*;

public class Evaluator implements Parser<Integer> {
    static final byte PLUS = '+', MINUS = '-', MULTI = '*', DIVID = '/', LEFT = '(', RIGHT = ')';
    static Function<Byte, BinaryOperator<Integer>> toOp = by -> {
        switch (by) {
            case PLUS:
                return (a, b) -> a + b;
            case MINUS:
                return (a, b) -> a - b;
            case MULTI:
                return (a, b) -> a * b;
            case DIVID:
                return (a, b) -> a / b;
            default:
                return null;
        }
    };
    static BiFunction<Integer, List<Function<Integer, Integer>>, Integer>
            collect = (i, list) -> list.stream().reduce(i, Funcs.reverse(Function::apply), (a, b) -> a + b);

//
    static Result<Integer> expr(ByteBuffer buffer) {
        return ((Parser<Integer>) Evaluator::term)
                .link(
                        match(in(PLUS, MINUS))
                                .map(toOp)
                                .link(Evaluator::term)
                                .map(adpater(Funcs::partial))
                                .star()
                ).map(adpater(collect)).parse(buffer);
    }

    static Result<Integer> term(ByteBuffer buffer) {
        return ((Parser<Integer>) Evaluator::number)
                .link(
                        match(in(MULTI, DIVID))
                                .map(toOp)
                                .link(Evaluator::number)
                                .map(adpater(Funcs::partial))
                                .star()
                ).map(adpater(collect)).parse(buffer);

    }

    static Result<Integer> number(ByteBuffer buffer) {
        return matchInt().or(match(is(LEFT)).link(Evaluator::expr).skip(RIGHT).map(Pair::second)).parse(buffer);
    }


    @Override
    public Result<Integer> tryParse(ByteBuffer buffer) {
        return expr(buffer);
    }
}
