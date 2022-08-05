package org.jsengine.v8.internal.torque;

import java.util.Vector;

public class LocalLabel {
    public Block block;
    public Vector<Type> parameter_types;

    public LocalLabel(Block block, Vector<Type> parameter_types) {
        this.block = block;
        this.parameter_types = parameter_types;
    }

    public LocalLabel(Block block) {
        this(block, new Vector<Type>());
    }
}
