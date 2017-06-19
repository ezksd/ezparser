package parsers.json;

public class JsonString implements JsonValue{
    String s;

    public JsonString(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return '"' + s + '"';
    }
}
