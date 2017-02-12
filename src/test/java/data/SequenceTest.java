package data;

import org.junit.Test;

import java.util.function.BiFunction;

import static org.junit.Assert.assertEquals;

public class SequenceTest {
    @Test
    public void test1() {
        Sequence<Integer> seq0 = Sequence.of(1);
        assertEquals(Integer.valueOf(1), seq0.head());
        Sequence<Integer> seq1 = Sequence.of(1, 2, 3, 4, 5);
        Sequence<Integer> seq2 = seq1.filter(i -> i % 2 == 0);
        Sequence<Integer> seq3 = seq1.map(i -> i * i);
        Sequence<Integer> seq4 = seq1.append(seq2);
        Sequence<Integer> seq5 = seq1.add(6);
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        assertEquals(Integer.valueOf(15), seq1.accumulate(0, add));
        assertEquals(Integer.valueOf(6), seq2.accumulate(0, add));
        assertEquals(Integer.valueOf(55), seq3.accumulate(0, add));
        assertEquals(Integer.valueOf(21), seq4.accumulate(0, add));
        assertEquals(Integer.valueOf(21), seq5.accumulate(0, add));
    }

}