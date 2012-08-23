package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinRangeError extends AbstractBuiltinNativeError {

    public BuiltinRangeError(GlobalObject globalObject) {
        super(globalObject, "RangeError" );
    }
    

}
