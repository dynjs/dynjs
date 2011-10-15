package org.dynjs.api;

import org.dynjs.runtime.DynAtom;

public interface Scope {

    Scope getEnclosingScope();

    Object resolve(String name);

    void define(String property, Object value);
}
