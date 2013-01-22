package org.dynjs.runtime.builtins.intl;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.builtins.types.AbstractBuiltinType;

public class Collator extends AbstractBuiltinType {

    public Collator(GlobalObject globalObject) {
        super(globalObject);
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject prototype) {
        
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return null;
    }

}
