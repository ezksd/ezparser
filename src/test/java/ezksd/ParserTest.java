package ezksd;


import data.Pair;
import org.junit.Test;
import parser.http.HttpMessage;
import parser.http.HttpParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static ezksd.Parsers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @org.junit.Test
    public void aplhabetAndNumber() {
        Parser<Pair<String, Integer>> alphaAndNum =  matchString(Character::isAlphabetic).link(till(Character::isDigit).map(toInt));
        Result<Pair<String, Integer>> r = alphaAndNum.parse("abc123");
        assertTrue(r.isSucess());
        assertEquals(Pair.of("abc", 123), r.get());
    }



}
