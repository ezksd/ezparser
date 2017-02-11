package ezksd;

public class Pair<E, S> {
    public E first;
    public S second;

    public Pair(E first, S second) {
        this.first = first;
        this.second = second;
    }

    public E getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return first.hashCode() + second.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Pair && (this.first.equals(((Pair) that).first) && this.second.equals(((Pair) that).second));
    }
}
