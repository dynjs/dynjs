package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinSyntaxError extends AbstractBuiltinNativeError {

    public BuiltinSyntaxError(GlobalObject globalObject) {
        super(globalObject, "SyntaxError");
    }

}
