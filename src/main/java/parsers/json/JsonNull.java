package parsers.json;

public class JsonNull implements JsonValue{
    private JsonNull() {

    }

    public static final JsonNull Value = new JsonNull();

    @Override
    public String toString() {
        return "null";
    }
}
