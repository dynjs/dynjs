package org.dynjs.runtime.builtins.types.bool;

import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.PrimitiveDynObject;

public class DynBoolean extends PrimitiveDynObject {

    public DynBoolean(GlobalContext globalContext) {
        this(globalContext, null);
    }

    public DynBoolean(GlobalContext globalContext, Boolean value) {
        super(globalContext, value);
        setClassName("Boolean");
        setPrototype(globalContext.getPrototypeFor("Boolean"));
    }

}
