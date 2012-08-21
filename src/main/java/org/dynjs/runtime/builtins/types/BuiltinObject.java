package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class BuiltinObject extends AbstractNativeFunction {

    public BuiltinObject(GlobalObject globalObject) {
        super(globalObject, "value");
        DynObject proto = new DynObject();
        setPrototype(proto);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        if ( self != null ) {
            if ( args[0] instanceof JSObject ) {
                return args[0];
            }
            
            if ( args[0] instanceof String || args[0] instanceof Boolean || args[0] instanceof Number ) {
                return Types.toObject(args[0]);
            }
        }
        
        return null;
    }

}
