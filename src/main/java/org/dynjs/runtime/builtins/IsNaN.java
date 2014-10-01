package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class IsNaN extends AbstractNonConstructorFunction {

    public IsNaN(GlobalContext globalContext) {
        super(globalContext, "text");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Object o = args[0];
        if (o != Types.UNDEFINED) {
            final double doubleValue = Types.toNumber(context, args[0]).doubleValue();
            if (Double.isInfinite(doubleValue)) return false;
            else return Double.isNaN( doubleValue );
        } else {
            return true;
        }
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/IsNaN.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: isNaN>";
    }
}
