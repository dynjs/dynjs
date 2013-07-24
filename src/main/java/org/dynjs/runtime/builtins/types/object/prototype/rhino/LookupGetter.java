package org.dynjs.runtime.builtins.types.object.prototype.rhino;

import org.dynjs.runtime.*;

/**
 * Rhino compatibility support
 */
public class LookupGetter extends AbstractNativeFunction {

    public LookupGetter(GlobalObject globalObject) {
        super(globalObject, "name");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return Types.UNDEFINED;
    }
}
