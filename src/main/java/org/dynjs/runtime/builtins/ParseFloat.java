package org.dynjs.runtime.builtins;

import java.util.Arrays;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.Types;

public class ParseFloat extends AbstractNativeFunction {

    public ParseFloat(GlobalObject globalObject) {
        super(globalObject, "f");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        Object f = args[0];
        if (f != Types.UNDEFINED) {
            try {
                return Double.parseDouble(f.toString());
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return Types.UNDEFINED;
    }

}
