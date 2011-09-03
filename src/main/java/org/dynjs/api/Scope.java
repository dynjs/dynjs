package org.dynjs.api;

import org.dynjs.runtime.DynAtom;

public interface Scope {

    Scope getEnclosingScope();

    DynAtom resolve(String name);

    void define(String property, DynAtom value);

}
