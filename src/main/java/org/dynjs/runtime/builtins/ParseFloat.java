package org.dynjs.runtime.builtins;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PrimitiveDynObject;
import org.dynjs.runtime.Types;

public class ParseFloat extends AbstractNativeFunction {

    public ParseFloat(GlobalObject globalObject) {
        super(globalObject, "f");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        String text = Types.toString(context, args[0]).trim();
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

}
