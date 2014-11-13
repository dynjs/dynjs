package org.dynjs.runtime.builtins.types.object.prototype.rhino;

import org.dynjs.runtime.*;

/**
 * Rhino compatibility support
 * Implements Object.prototype.__lookupGetter__
 */
public class LookupGetter extends AbstractNativeFunction {

    public LookupGetter(GlobalContext globalContext) {
        super(globalContext, "name");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        try {
            String name = (String) args[0];
            DynObject object = (DynObject) self;
            if (object.hasProperty(context, name)) {
                PropertyDescriptor descriptor = (PropertyDescriptor) object.getOwnProperty(context, name);
                if (descriptor.hasGet()) {
                    return descriptor.getGetter();
                }
            }
        } catch (ClassCastException e) {
            // E.g. if no name is provided
        }
        return Types.UNDEFINED;
    }
}
