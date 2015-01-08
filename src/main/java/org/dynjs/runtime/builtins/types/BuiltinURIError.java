package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalContext;

public class BuiltinURIError extends AbstractBuiltinNativeError {

    public BuiltinURIError(GlobalContext globalContext) {
        super(globalContext, "URIError");
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinURIError.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: URIError>";
    }

}
