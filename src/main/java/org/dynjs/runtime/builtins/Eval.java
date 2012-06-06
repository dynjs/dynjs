package org.dynjs.runtime.builtins;

import org.dynjs.api.Function;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynThreadContext;

public class Eval implements Function {

    @Override
    public Object call(Object self, DynThreadContext context, Object[] arguments) {
        if (arguments.length == 1 && arguments[0] instanceof String) {
            DynJS runtime = context.getRuntime();
            runtime.eval(context, (String) arguments[0]);
        }
        return DynThreadContext.UNDEFINED;
    }

    @Override
    public String[] getArguments() {
        return new String[]{"x"};
    }

}
