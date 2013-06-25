package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.GlobalObject;

public class BuiltinURIError extends AbstractBuiltinNativeError {

    public BuiltinURIError(GlobalObject globalObject) {
        super(globalObject, "URIError");
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
