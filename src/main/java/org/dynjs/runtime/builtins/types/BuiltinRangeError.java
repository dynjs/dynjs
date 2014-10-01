package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalContext;

public class BuiltinRangeError extends AbstractBuiltinNativeError {

    public BuiltinRangeError(GlobalContext globalContext) {
        super(globalContext, "RangeError");
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinRangeError.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: RangeError>";
    }

}
