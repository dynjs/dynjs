package org.dynjs.runtime.builtins.types.number;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PropertyDescriptor;

public class NaN extends AbstractNativeFunction {
    
    public NaN(final GlobalObject globalObject) { 
        super(globalObject);
        this.defineOwnProperty(null, "toFixed", new PropertyDescriptor() {
            {
                set("Value", new ToFixed(globalObject));
            }
        }, false);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return Double.NaN;
    }
}
