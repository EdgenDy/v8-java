package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class NameVector extends Vector<Identifier> {
    public NameVector() {
        super();
    }

    public NameVector(Vector<Identifier> vector_identifier) {
        this();
        for(Identifier identifier : vector_identifier)
            this.add(identifier);
    }
}
