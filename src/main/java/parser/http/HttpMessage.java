package parser;

import java.util.Map;

public abstract class HttpMessage {
    Map<String, String> headers;
    byte[] entity;

    static class HttpRequest extends HttpMessage {
        static class RequestLine{
            
        }
    }
}
