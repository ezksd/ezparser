package data;

public class Pair<A,B> {
    A a;
    B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A left() {
        return a;
    }

    public B right() {
        return b;
    }

    @Override
    public String toString() {
        return "(" + a + "," + b + ")";
    }
}
