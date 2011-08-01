package org.dynjs.api;


import org.dynjs.runtime.DynAtom;

public interface Function {
    public DynAtom call(DynAtom... arguments);
}
