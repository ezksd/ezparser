package parsers.json;


import data.IList;
import data.Pair;

public class JsonObject implements JsonValue {
    IList<Pair<String, JsonValue>> map;

    public JsonObject(IList<Pair<String, JsonValue>> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        StringBuffer buf = map.foldl((sb, p) ->
                sb.append('"').append(p.left()).append("\" : ").append(p.right().toString()).append(','),new StringBuffer().append('{'));
        if (!map.isEmpty()) {
            buf.deleteCharAt(buf.length() - 1);
        }
        buf.append('}');
        return buf.toString();
    }
}
