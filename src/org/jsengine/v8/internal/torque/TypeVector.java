package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class TypeVector extends Vector<Type> {
    public TypeVector() {
        super();
    }

    public TypeVector(TypeVector type_vector, int implicit_count) {
        for(int index = implicit_count, end = type_vector.size(); index < end; index++)
            this.add(type_vector.get(index));
    }

    public TypeVector(Type... types) {
        for(Type type : types) {
            this.add(type);
        }
    }
}
