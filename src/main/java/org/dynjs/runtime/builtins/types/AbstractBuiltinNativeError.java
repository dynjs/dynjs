package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

public class AbstractBuiltinNativeError extends AbstractBuiltinType {

    private String name;

    public AbstractBuiltinNativeError(GlobalObject globalObject, final String name) {
        super(globalObject, "message");

        final DynObject proto = new DynObject( globalObject );
        put(null, "prototype", proto, false);
        this.name = name;
    }
    
    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        proto.setClassName("Error");
        proto.put(null, "constructor", this, false);
        proto.put(null, "name", this.name, false);
        proto.put(null, "message", "", false);
        proto.setPrototype(globalObject.getPrototypeFor("Error"));
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {
        JSObject o = null;

        if (self == Types.UNDEFINED) {
            o = context.createError( (String) ((JSObject)get( context, "prototype" )).get(null, "name"), null);
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
    public JSObject createNewObject(ExecutionContext context) {
        DynObject o = new DynObject( context.getGlobalObject() );
        o.setClassName("Error");
        return o;
    }

}
