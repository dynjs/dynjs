package org.dynjs.runtime;

public class AttributeBuilder {
    private final DynObject dynObject;
    private final String attributeName;

    public AttributeBuilder(DynObject dynObject, String attributeName) {
        this.dynObject = dynObject;
        this.attributeName = attributeName;
    }

    public DynObject to(DynObject object) {
        final Attribute attribute = new Attribute(object);
        dynObject.setAttribute(attributeName, attribute);
        return dynObject;
    }
}
