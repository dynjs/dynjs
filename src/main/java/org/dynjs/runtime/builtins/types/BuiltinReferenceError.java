package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinReferenceError extends AbstractBuiltinNativeError {

    public BuiltinReferenceError(GlobalObject globalObject) {
        super(globalObject, "ReferenceError");
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
