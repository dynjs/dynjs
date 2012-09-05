package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinReferenceError extends AbstractBuiltinNativeError {

    public BuiltinReferenceError(GlobalObject globalObject) {
        super(globalObject, "ReferenceError");
    }

}
