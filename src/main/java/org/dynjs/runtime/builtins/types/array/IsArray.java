package org.dynjs.runtime.builtins.types.array;

import org.dynjs.runtime.*;

public class IsArray extends AbstractNativeFunction {

    public IsArray(GlobalContext globalContext) {
        super(globalContext, true, "arg");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.3.2
        if (args[0] instanceof DynArray) {
            return true;
        }

        // TODO: Nodyn buffers should not be considered an array according to Array.isArray
//        if ( args[0] instanceof JSObject && ((JSObject)args[0]).hasExternalIndexedData() ) {
//            return true;
//        }

        return false;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/IsArray.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: isArray>";
    }

}
