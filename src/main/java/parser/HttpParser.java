package parser;

import data.Pair;
import ezksd.Parser;
import ezksd.Parsers;
import ezksd.Result;
import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import static ezksd.Parsers.*;

public class HttpParser {

    static class HttpRequest {
        public HttpRequest(String version, String statusCode, String reasonPhrase, Map<String, String> headers, byte[] entity) {
            this.version = version;
            this.statusCode = statusCode;
            this.reasonPhrase = reasonPhrase;
            this.headers = headers;
            this.entity = entity;
        }

        String version;
        String statusCode;
        String reasonPhrase;
        Map<String, String> headers = new HashMap<>();
        byte[] entity;
    }

    static final byte SEMI = ':';
    static final byte CR = '\n';
    static final byte SPACE = ' ';

    @Test
    public void parse() {
        Parser<String> notSpace = Parsers.match(not(SPACE));
        Parser<String> notSemi = Parsers.match(not(SEMI,CR));
        Parser<String> notCR = Parsers.match(not(CR));
        Parser<Byte> matchSEMI = Parsers.matchOnce(SEMI);
        Parser<Byte> matchSpace = Parsers.matchOnce(SPACE);
        Parser<Byte> matchCRLF = Parsers.matchOnce(CR);
        Parser<HashMap<String,String>> parseHeader = notSemi.skip(matchSEMI).skip(matchSpace).link(notCR).skip(matchCRLF)
                .kleenPlus().map(list -> {
                    HashMap<String, String> map = new HashMap<>();
                    for (Pair<String, String> pair : list) {
                        map.put(pair.getFirst(), pair.getSecond());
                    }
                    return map;
                });

        Parser<byte[]> parseEntity = empty().kleenStar().map(Parsers::byteListToArray);

        Parser<HttpRequest> htttParser = notSpace
                .skip(matchSpace).link(notSpace).skip(matchSpace).link(notCR).skip(matchCRLF)
                .link(parseHeader)
                .link(parseEntity).map(p -> {
                        return new HttpRequest(p.first.first.first.first, p.first.first.first.second, p.first.first.second, p.first.second, p.second);

                });

        try {

            String httpMsg = "GET http://baidu.com/ HTTP/1.1\r\n" +
                    "Content-Type:application/x-www-form-urlencoded\r\n\r\n";
            //Create connection
            SocketChannel ch = SocketChannel.open();
            ch.connect(new InetSocketAddress("baidu.com", 80));

            ch.write(ByteBuffer.wrap(httpMsg.getBytes()));

            ByteBuffer read = ByteBuffer.allocate(1000);
            int count = ch.read(read);
            if (count > 0) {
                read.flip();
                Result<HttpRequest> parse = htttParser.parse(read);
                HttpRequest request = parse.get();
                System.out.println(request.version);
                System.out.println(request.statusCode);
                System.out.println(request.reasonPhrase);
                request.headers.forEach((a, b) -> System.out.println(a + ":" + b));
                 System.out.println(new String(request.entity));

            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
