package org.dynjs.runtime.builtins.intl;

import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.builtins.types.AbstractBuiltinType;

public class Collator extends AbstractBuiltinType {

    public Collator(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    public void initialize(GlobalContext globalContext, JSObject prototype) {
        
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return null;
    }

}
