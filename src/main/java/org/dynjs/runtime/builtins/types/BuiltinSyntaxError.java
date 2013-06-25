package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinSyntaxError extends AbstractBuiltinNativeError {

    public BuiltinSyntaxError(GlobalObject globalObject) {
        super(globalObject, "SyntaxError");
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinSyntaxError.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: SyntaxError>";
    }

}
