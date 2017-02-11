package parser.http;

import ezksd.Parser;
import ezksd.Result;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static ezksd.Parsers.*;

public class HttpParser implements Parser<HttpMessage.Resp> {
    static final byte SP = ' ', LF = '\n', SEMI = ':';



    @Override
    public Result<HttpMessage.Resp> tryParse(ByteBuffer buffer) {
         Parser<HttpMessage.Resp.StatusLine> reqLineParser =
                until(SP)
                        .skip(SP)
                        .link(matchInt())
                        .skip(SP)
                        .link(until(LF))
                        .skip(LF)
                        .map(p -> new HttpMessage.Resp.StatusLine(p.first.first, p.first.second, p.second));
         Parser<Map<String, String>> headerParser =
                until(SEMI)
                        .skip(SEMI).skip(SP)
                        .link(until(LF))
                        .skip(LF)
                        .plus()
                        .map(list -> let(new HashMap<String, String>(), map -> begin(() -> list.forEach(p -> map.put(p.first, p.second)), () -> map)));
         Parser<byte[]> entityparser = matchByteArray();

         Parser<HttpMessage.Resp> respParser = reqLineParser.link(headerParser).link(entityparser).map(p -> new HttpMessage.Resp(p.first.first, p.first.second, p.second));
        return respParser.parse(buffer);
    }
}
