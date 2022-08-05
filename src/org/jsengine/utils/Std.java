package org.jsengine.utils;

import java.util.Vector;
import java.util.function.Function;

public class Std {
    public static class Arguments<T> extends Vector<T> {
        public Arguments(T... t) {
            for(T type: t) {
                add(type);
            }
        }
    }

    public static <E> E min_element(Vector<E> vector, Function<Arguments<E>, Boolean> comp) {
        if(vector.size() == 1) return vector.get(0);

        E return_value = vector.get(0);
        for(int index = 1, end = vector.size(); index < end; index++) {
            E current = vector.get(index);
            if(comp.apply(new Arguments(current, return_value))) {
                return_value = current;
            }
        }

        return return_value;
    }

    public static <T> Vector<T> reverse(Vector<T> vector) {
        Vector<T> new_vector = new Vector<T>();

        for (int limit = vector.size(); limit > 0; limit--)
            new_vector.add(vector.get(limit));
        return new_vector;
    }
}
