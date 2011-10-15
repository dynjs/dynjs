package org.dynjs.api;

import org.dynjs.runtime.DynAtom;
import org.dynjs.runtime.DynThreadContext;

public interface Function extends DynAtom, Scope {

    public Object call(DynThreadContext context, Scope scope, Object[] arguments);

}
