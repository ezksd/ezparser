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
        Parser<Pair<String, Integer>> alphaAndNum =  matchString(Character::isAlphabetic).link(match(Character::isDigit).map(toInt));
        Result<Pair<String, Integer>> r = alphaAndNum.parse("abc123");
        assertTrue(r.isSucess());
        assertEquals(Pair.of("abc", 123), r.get());
    }


    @Test
    public void httpParserTest() {

        try (SocketChannel ch = SocketChannel.open();){
            ch.connect(new InetSocketAddress("baidu.com", 80));
            String msg = "GET / HTTP/1.1\r\n" +
                    "Host: baidu.com\r\n" +
                    "Connection: keep-alive\r\n" +
                    "Upgrade-Insecure-Requests: 1\r\n" +
                    "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36\r\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" +
                    "Accept-Encoding: gzip, deflate, sdch, br\r\n" +
                    "Accept-Language: en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4\r\n\r\n";
            ch.write(ByteBuffer.wrap(msg.getBytes()));
            ByteBuffer buff = ByteBuffer.allocate(1000);
            buff.flip();
            Result<HttpMessage.Resp> r = new HttpParser().parse(buff);
            assertTrue(r.isSucess());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
