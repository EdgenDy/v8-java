package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class VisitResultVector extends Vector<VisitResult> {
    public VisitResultVector() {
        super();
    }

    public VisitResultVector(VisitResult... args) {
        this();
        for(VisitResult result : args) {
            add(result);
        }
    }

    public TypeVector computeTypeVector() {
        TypeVector result = new TypeVector();
        for (VisitResult visit_result : this) {
            result.add(visit_result.type());
        }
        return result;
    }
}
