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
        String name = (String) args[0];
        JSFunction function = (JSFunction) args[1];
        DynObject obj = (DynObject) self;

        PropertyDescriptor descriptor = new PropertyDescriptor();
        descriptor.setConfigurable(true);
        if (obj.hasProperty(context, name)) {
            descriptor = (PropertyDescriptor) obj.getOwnProperty(context, name);
        }

        descriptor.setGetter(function);
        obj.defineOwnProperty(context, name, descriptor, false);
        return Types.UNDEFINED;
    }
}
