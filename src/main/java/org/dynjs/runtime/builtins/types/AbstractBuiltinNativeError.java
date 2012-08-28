package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class AbstractBuiltinNativeError extends AbstractNativeFunction {

    public AbstractBuiltinNativeError(GlobalObject globalObject, final String name) {
        super(globalObject, true, "message");

        final DynObject proto = new DynObject();
        proto.setClassName("Error");
        proto.put(null, "constructor", this, false);
        proto.put(null, "name", name, false);
        proto.put(null, "message", "", false);
        proto.setPrototype(globalObject.getPrototypeFor("Error"));
        
        put(null, "prototype", proto, false);
        setPrototype(globalObject.getPrototypeFor("Function"));
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {
        JSObject o = null;

        if (self == Types.UNDEFINED) {
            o = createNewObject();
        } else {
            o = (JSObject) self;
        }

        if (args[0] != Types.UNDEFINED) {
            o.defineOwnProperty(context, "message", new PropertyDescriptor() {
                {
                    set("Value", args[0]);
                }
            }, false);
        }
        return o;
    }

    @Override
    public JSObject createNewObject() {
        DynObject o = new DynObject();
        o.setPrototype((JSObject) get(null, "prototype"));
        o.setClassName("Error");
        return o;
    }

}
