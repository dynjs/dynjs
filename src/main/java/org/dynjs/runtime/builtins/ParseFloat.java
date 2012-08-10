package org.dynjs.runtime.builtins;

import org.dynjs.api.Function;

public class ParseFloat implements Function {
    @Override
    public Object call(Object self, DynThreadContext context, Object... arguments) {
        if (arguments.length == 1 && arguments[0] != null) {
            return Double.parseDouble( arguments[0].toString() );
        }
        return DynThreadContext.UNDEFINED;
    }

    @Override
    public String[] getParameters() {
        return new String[] { "a" };
    }
}
