package ezksd;


import data.AorB;
import data.Pair;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    static final Parser<String> alphaPaser = Parsers.match(Character::isAlphabetic);
    static final Parser<Integer> numberPaser = Parsers.match(Character::isDigit).map(Integer::valueOf);

    @org.junit.Test
    public void testAlphatbet() {
        ByteBuffer buffer = ByteBuffer.wrap("hehe132".getBytes());
        assertEquals("hehe", alphaPaser.parse(buffer).get());
    }

    @org.junit.Test
    public void testNumber() {
        ByteBuffer buffer = ByteBuffer.wrap("1234567shdfj".getBytes());
        assertEquals(1234567, numberPaser.parse(buffer).orELse(0).intValue());
    }

    @org.junit.Test
    public void aplhabetAndNumber() {
        ByteBuffer buffer1 = ByteBuffer.wrap("hehe132".getBytes());
        ByteBuffer buffer2 = ByteBuffer.wrap("123hehe".getBytes());
        Parser<AorB<String, Integer>> or = alphaPaser.or(numberPaser);
        Parser<Pair<String, Integer>> link = alphaPaser.link(numberPaser);
        Result<AorB<String, Integer>> r1 = or.parse(buffer1);
        Result<AorB<String, Integer>> r2 = or.parse(buffer2);
        assertTrue(r1.isSucess() && r1.get().instanceOf(String.class));
        assertEquals("hehe", r1.get().getA());
        System.out.println(r2.isSucess());
        assertTrue(r2.isSucess() && r2.get().instanceOf(Integer.class));
        assertEquals(123, r2.get().getB().intValue());

        buffer1.position(0);
        assertEquals(new Pair<>("hehe", 132), link.parse(buffer1).get());
    }



}
