package data;

@SuppressWarnings("unchecked")
public class AorB<A, B> {
    Object val;

    public AorB(Object val) {
        this.val = val;
    }

    public boolean instanceOf(Class<?> c) {
        return c.isInstance(val);
    }

    public A getA() {
        return (A) val;
    }

    public B getB() {
        return (B) val;
    }

}
