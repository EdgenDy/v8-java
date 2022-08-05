package org.jsengine.v8.internal.torque;

public class TopType extends Type {
    private String reason_;
    private Type source_type_;

    public TopType(String reason, Type source_type) {
        super(Kind.kTopType, null);
        reason_ = reason;
        source_type_ = source_type;
    }

    @Override
    public String mangledName() {
        return "top";
    }

    @Override
    public String toExplicitString() {
        StringBuilder s = new StringBuilder();
        s.append("inaccessible " + source_type_.toString());
        return s.toString();
    }

    @Override
    public String getGeneratedTypeNameImpl() {
        throw new RuntimeException("UNREACHABLE");
    }

    @Override
    public String getGeneratedTNodeTypeNameImpl() {
        return source_type_.getGeneratedTNodeTypeName();
    }

    public static TopType cast(TypeBase declarable) {
        return (TopType) declarable;
    }

    public Type source_type() {
        return source_type_;
    }

    public String reason() {
        return reason_;
    }
}
