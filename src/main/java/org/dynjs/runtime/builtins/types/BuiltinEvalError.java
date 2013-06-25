package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinEvalError extends AbstractBuiltinNativeError {

    public BuiltinEvalError(GlobalObject globalObject) {
        super(globalObject, "EvalError");
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinEvalError.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: EvalError>";
    }

}
