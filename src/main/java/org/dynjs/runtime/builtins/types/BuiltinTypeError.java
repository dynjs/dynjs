package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinTypeError extends AbstractBuiltinNativeError {

    public BuiltinTypeError(GlobalObject globalObject) {
        super(globalObject, "TypeError");
    }

}
