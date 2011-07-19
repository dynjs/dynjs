package org.dynjs.runtime;

public class AttributeBuilder<T extends DynAtom> {
    private final DynObject dynObject;
    private final String attributeName;

    public AttributeBuilder(DynObject dynObject, String attributeName) {
        this.dynObject = dynObject;
        this.attributeName = attributeName;
    }

    public DynObject to(T object) {
        final Attribute attribute = new Attribute<T>(object);
        dynObject.setAttribute(attributeName, attribute);
        return dynObject;
    }
}
