package org.dynjs.runtime.builtins.types.object.prototype.rhino;

import org.dynjs.runtime.*;

/**
 * Rhino compatibility support
 * Implements Object.prototype.__lookupSetter__
 */
public class LookupSetter extends AbstractNativeFunction {
    public LookupSetter(GlobalContext globalContext) {
        super(globalContext, "name");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        try {
            String name = (String) args[0];
            DynObject object = (DynObject) self;
            if (object.hasProperty(context, name)) {
                PropertyDescriptor descriptor = (PropertyDescriptor) object.getOwnProperty(context, name);
                if (descriptor.hasSet()) {
                    return descriptor.getSetter();
                }
            }
        } catch (ClassCastException e) {
            // e.g. if no name is provided
        }
        return Types.UNDEFINED;
    }
}
