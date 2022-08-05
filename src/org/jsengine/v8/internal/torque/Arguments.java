package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class Arguments {
    public VisitResultVector parameters;
    public Vector<Binding<LocalLabel>> labels;

    public Arguments(VisitResultVector parameters, Vector<Binding<LocalLabel>> labels) {
        this.parameters = parameters;
        this.labels = labels;
    }
}
