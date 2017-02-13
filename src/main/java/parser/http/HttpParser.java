package parser.http;

import ezksd.Parser;
import ezksd.Parsers;
import ezksd.Result;

import java.nio.ByteBuffer;
import java.util.Map;

import static data.Funcs.adpater;
import static ezksd.Parsers.*;

public class HttpParser implements Parser<HttpMessage.Resp> {
    static final byte SP = ' ', LF = '\n', SEMI = ':';


    @Override
    public Result<HttpMessage.Resp> tryParse(ByteBuffer buffer) {
        Parser<HttpMessage.Resp.StatusLine> status =
                until(SP)
                        .skip(SP)
                        .link(matchInt())
                        .skip(SP)
                        .link(until(LF))
                        .skip(LF)
                        .map(adpater(HttpMessage.Resp.StatusLine::new));

        Parser<Map<String, String>> head =
                until(SEMI)
                        .skip(SEMI).skip(SP)
                        .link(until(LF))
                        .skip(LF)
                        .plus()
                        .map(Parsers::toMap);

        Parser<byte[]> entity = matchByteArray();

        Parser<HttpMessage.Resp> respParser =
                status
                        .link(head)
                        .link(entity)
                        .map(adpater(HttpMessage.Resp::new));

        return respParser.parse(buffer);
    }
}
