package org.dynjs.runtime.builtins.types.object.prototype.rhino;

import org.dynjs.runtime.*;

/**
 * Rhino compatibility support
 */
public class DefineGetter extends AbstractNativeFunction {

    public DefineGetter(GlobalObject globalObject) {
        super(globalObject, "name", "function");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        PropertyDescriptor descriptor = new PropertyDescriptor();
        descriptor.setGetter((JSFunction) args[1]);
        DynObject obj = (DynObject) self;
        if (obj.getPrototype() != null) {
            System.err.println("DEFINE GETTER SETTING " + args[0] + " to " + args[1]);
            obj.getPrototype().defineOwnProperty(context, (String) args[0], descriptor, false);
        } else {
            obj.defineOwnProperty(context, (String) args[0], descriptor, false);
        }
        return Types.UNDEFINED;
    }
}
