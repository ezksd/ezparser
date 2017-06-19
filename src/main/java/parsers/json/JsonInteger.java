package parsers.json;

public class JsonInteger implements JsonNumber {
    int val;

    public JsonInteger(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
