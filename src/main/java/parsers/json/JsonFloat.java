package parsers.json;

public class JsonFloat implements JsonNumber {
    double val;

    public JsonFloat(double val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
