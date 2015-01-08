package org.dynjs.runtime.builtins.types.object.prototype.rhino;

import org.dynjs.runtime.*;

/**
 /**
  * Rhino compatibility support
  * Implements Object.prototype.__defineSetter__
  */
public class DefineSetter extends AbstractNativeFunction {
    public DefineSetter(GlobalContext globalContext) {
        super(globalContext, "name", "function");
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        try {
            String name = (String) args[0];
            JSFunction function = (JSFunction) args[1];
            DynObject obj = (DynObject) self;

            PropertyDescriptor descriptor = new PropertyDescriptor();
            descriptor.setConfigurable(true);
            descriptor.setEnumerable(true);
            if (obj.hasProperty(context, name)) {
                descriptor = (PropertyDescriptor) obj.getOwnProperty(context, name);
            }

            descriptor.setSetter(function);
            obj.defineOwnProperty(context, name, descriptor, false);
        } catch (ClassCastException e) {
            // E.g. if no name is provided
        }
        return Types.UNDEFINED;
    }
}
