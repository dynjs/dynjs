package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalContext;

public class BuiltinSyntaxError extends AbstractBuiltinNativeError {

    public BuiltinSyntaxError(GlobalContext globalContext) {
        super(globalContext, "SyntaxError");
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
