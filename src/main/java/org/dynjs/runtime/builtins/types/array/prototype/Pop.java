package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Pop extends AbstractNativeFunction {

    public Pop(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.2
        JSObject array = Types.toObject(context, self);
        int len = Types.toInteger( array.get( context, "length" ));
        
        if ( len == 0 ) {
            array.put(context, "length", 0, true);
            return Types.UNDEFINED;
        }
        
        int index = len - 1;
        
        Object element = array.get(context, "" + index );
        array.delete(context, "" + index, true);
        array.put(context, "length", index, true);
        return element;
    }

}
