package parsers.json;


import data.IList;

public class JsonArray implements JsonValue {
    IList<JsonValue> array;

    public JsonArray(IList<JsonValue> array) {
        this.array = array;
    }

    @Override
    public String toString() {
        return array.toString();
    }
}
