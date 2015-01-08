package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalContext;

public class BuiltinTypeError extends AbstractBuiltinNativeError {

    public BuiltinTypeError(GlobalContext globalContext) {
        super(globalContext, "TypeError");
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinTypeError.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: TypeError>";
    }

}
