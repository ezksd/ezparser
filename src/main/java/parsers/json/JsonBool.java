package parsers.json;

public class JsonBool implements JsonValue{

    private JsonBool() {}

    public static final JsonBool TRUE = new JsonBool();
    public static final JsonBool FALSE = new JsonBool();

    @Override
    public String toString() {
        if (this == TRUE) {
            return "TRUE";
        } else if (this == FALSE) {
            return "FALSE";
        } else {
            throw new IllegalStateException();
        }
    }
}
