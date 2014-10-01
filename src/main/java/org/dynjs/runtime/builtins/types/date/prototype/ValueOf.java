package org.dynjs.runtime.builtins.types.date.prototype;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.builtins.types.date.DynDate;

public class ValueOf extends AbstractNativeFunction {
    public ValueOf(GlobalContext globalContext, String... formalParameters) {
        super(globalContext, formalParameters);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return ((DynDate) self).getPrimitiveValue();
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/date/prototype/ValueOf.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: valueOf>";
    }
}
