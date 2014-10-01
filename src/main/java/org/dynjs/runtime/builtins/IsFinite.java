package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.Types;

public class IsFinite extends AbstractNonConstructorFunction {

    public IsFinite(GlobalContext globalContext) {
        super(globalContext, "text");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Object o = args[0];
        if (o != Types.UNDEFINED) {
            Number n = Types.toNumber(context, o);
            if (Double.isNaN(n.doubleValue()) || Double.isInfinite(n.doubleValue())) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/IsFinite.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: isFinite>";
    }
}
