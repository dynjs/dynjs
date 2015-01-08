package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalContext;

public class BuiltinReferenceError extends AbstractBuiltinNativeError {

    public BuiltinReferenceError(GlobalContext globalContext) {
        super(globalContext, "ReferenceError");
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinReferenceError.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: ReferenceError>";
    }

}
