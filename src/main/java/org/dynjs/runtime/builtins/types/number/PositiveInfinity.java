package org.dynjs.runtime.builtins.types.number;

import org.dynjs.runtime.GlobalObject;

public class PositiveInfinity extends DynNumber {
    
    public PositiveInfinity(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object getPrimitiveValue() {
        return Double.POSITIVE_INFINITY;
    }

}
