package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinRangeError extends AbstractBuiltinNativeError {

    public BuiltinRangeError(GlobalObject globalObject) {
        super(globalObject, "RangeError");
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
