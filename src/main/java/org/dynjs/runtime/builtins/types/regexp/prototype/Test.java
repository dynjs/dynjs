package org.dynjs.runtime.builtins.types.regexp.prototype;

import org.dynjs.runtime.AbstractNonConstructorFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSFunction;
import org.dynjs.runtime.Types;

public class Test extends AbstractNonConstructorFunction {

    public Test(GlobalObject globalObject) {
        super(globalObject, "string");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        JSFunction execFn = (JSFunction) context.getPrototypeFor("RegExp").get(context, "exec");
        Object result = context.call( execFn, self, args[0] );
        
        return ( result != Types.NULL );
    }

}
