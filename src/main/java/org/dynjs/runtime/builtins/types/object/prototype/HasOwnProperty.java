package org.dynjs.runtime.builtins.types.object.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class HasOwnProperty extends AbstractNativeFunction {

    public HasOwnProperty(GlobalObject globalObject) {
        super(globalObject, "v");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.2.4.5
        JSObject o = Types.toObject(context, self);
        String p = Types.toString( args[0] );
        
        Object desc = o.getOwnProperty(context, p);
        if ( desc == Types.UNDEFINED ) {
            return false;
        }
        
        return true;
    }

}
