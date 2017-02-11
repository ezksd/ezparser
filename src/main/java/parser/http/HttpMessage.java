package parser.http;

import java.net.URI;
import java.util.Map;

public abstract class HttpMessage {
    Map<String, String> headers;
    byte[] entity;

    public HttpMessage(Map<String, String> headers, byte[] entity) {
        this.headers = headers;
        this.entity = entity;
    }

    public static class Req extends HttpMessage {
        static class RequestLine {
            Method method;
            URI path;
            String version;

            public RequestLine(Method method, URI path, String version) {
                this.method = method;
                this.path = path;
                this.version = version;
            }

            public Method getMethod() {
                return method;
            }

            public URI getPath() {
                return path;
            }

            public String getVersion() {
                return version;
            }
        }

        RequestLine requestLine;

        public Req(RequestLine requestLine, Map<String, String> headers, byte[] entity) {
            super(headers, entity);
            this.requestLine = requestLine;
        }

        public RequestLine getRequestLine() {
            return requestLine;
        }
    }

    public static class Resp extends HttpMessage {
        static class StatusLine{
            String version;
            int status;
            String reason;

            public StatusLine(String version, int status, String reason) {
                this.version = version;
                this.status = status;
                this.reason = reason;
            }

            public String getVersion() {
                return version;
            }

            public int getStatus() {
                return status;
            }

            public String getReason() {
                return reason;
            }
        }

        StatusLine statusLine;

        public Resp(StatusLine statusLine,Map<String, String> headers, byte[] entity) {
            super(headers, entity);
            this.statusLine = statusLine;
        }

        public StatusLine getStatusLine() {
            return statusLine;
        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getEntity() {
        return entity;
    }
}
