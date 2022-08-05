package org.jsengine.v8.internal.torque;

import org.jsengine.v8.internal.Torque;
import java.util.Vector;

public class ParameterDifference {
    private Vector<Type> difference_ = new Vector<Type>();
    public ParameterDifference(TypeVector to, TypeVector from) {
        for (int i = 0; i < to.size(); ++i) {
            addParameter(to.get(i), from.get(i));
        }
    }

    public boolean strictlyBetterThan(ParameterDifference other) {
        boolean better_parameter_found = false;
        for (int i = 0; i < difference_.size(); ++i) {
            Type a = difference_.get(i);
            Type b = other.difference_.get(i);
            if (a == b) {
                continue;
            } else if (a != null && b != null && a != b && a.isSubtypeOf(b)) {
                better_parameter_found = true;
            } else if (a != null && b == null) {
                better_parameter_found = true;
            } else {
                return false;
            }
        }
        return better_parameter_found;
    }

    private void addParameter(Type to, Type from) {
        if (from.isSubtypeOf(to)) {
            difference_.add(to);
        } else if (Torque.isAssignableFrom(to, from)) {
            difference_.add(null);
        } else {
            throw new RuntimeException("UNREACHABLE");
        }
    }
}
