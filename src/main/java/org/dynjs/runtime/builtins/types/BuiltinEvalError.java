package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalContext;

public class BuiltinEvalError extends AbstractBuiltinNativeError {

    public BuiltinEvalError(GlobalContext globalContext) {
        super(globalContext, "EvalError");
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
