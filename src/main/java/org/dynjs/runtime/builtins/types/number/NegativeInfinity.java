package org.dynjs.runtime.builtins.types.number;

import org.dynjs.runtime.GlobalObject;

public class NegativeInfinity extends DynNumber {
    
    public NegativeInfinity(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object getPrimitiveValue() {
        return Double.NEGATIVE_INFINITY;
    }

}
