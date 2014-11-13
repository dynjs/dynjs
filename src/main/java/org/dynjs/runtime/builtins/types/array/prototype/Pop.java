package org.dynjs.runtime.builtins.types.array.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

public class Pop extends AbstractNonConstructorFunction {

    public Pop(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        // 15.4.4.2
        JSObject array = Types.toObject(context, self);
        long len = Types.toUint32(context, array.get(context, "length"));

        if (len == 0) {
            array.put(context, "length", 0L, true);
            return Types.UNDEFINED;
        }

        long index = len - 1;

        Object element = array.get(context, "" + index);
        array.delete(context, "" + index, true);
        array.put(context, "length", index, true);
        return element;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/array/prototype/Pop.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: pop>";
    }

}
