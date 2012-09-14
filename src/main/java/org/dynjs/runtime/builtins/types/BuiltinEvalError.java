package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinEvalError extends AbstractBuiltinNativeError {

    public BuiltinEvalError(GlobalObject globalObject) {
        super(globalObject, "EvalError");
    }

}
