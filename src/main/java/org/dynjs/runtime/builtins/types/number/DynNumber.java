package org.dynjs.runtime.builtins.types.number;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PropertyDescriptor;

public abstract class DynNumber extends AbstractNativeFunction {

    public DynNumber(final GlobalObject globalObject) {
        super(globalObject);
        this.setClassName("Number");
        this.defineOwnProperty(null, "toFixed", new PropertyDescriptor() {
            {
                set("Value", new ToFixed(globalObject));
            }
        }, false);
        this.defineOwnProperty(null, "valueOf", new PropertyDescriptor() {
            {
                set("Value", new ValueOf(globalObject));
            }
        }, false);

    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return this.getPrimitiveValue();
    }

    public abstract Object getPrimitiveValue();
}