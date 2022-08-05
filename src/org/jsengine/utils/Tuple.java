package org.jsengine.utils;

public class Tuple<F, S> {
    public F first = null;
    public S second = null;

    public Tuple(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F,S> Tuple<F, S> makeTuple(F first, S second) {
        return new Tuple(first, second);
    }
}
