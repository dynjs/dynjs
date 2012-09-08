package org.dynjs.runtime.builtins.types.date;

import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;

public class DynDate extends PrimitiveDynObject {

    public DynDate(GlobalObject globalObject) {
        super(globalObject);
        setClassName("Date");
        setExtensible(true);
    }
}
