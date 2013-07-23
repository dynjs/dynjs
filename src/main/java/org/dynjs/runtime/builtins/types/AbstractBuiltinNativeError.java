package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.StackGetter;
import org.dynjs.runtime.Types;

public class AbstractBuiltinNativeError extends AbstractBuiltinType {

    private String name;

    public AbstractBuiltinNativeError(GlobalObject globalObject, final String name) {
        super(globalObject, "message");

        final DynObject proto = new DynObject(globalObject);
        this.defineReadOnlyProperty(globalObject, "prototype", proto);
        this.name = name;
    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        proto.setClassName("Error");
        proto.defineNonEnumerableProperty(globalObject, "constructor", this);
        proto.put(null, "name", this.name, false);
        proto.put(null, "message", "", false);
        proto.setPrototype(globalObject.getPrototypeFor("Error"));
    }

    @Override
    public Object call(final ExecutionContext context, Object self, final Object... args) {
        JSObject o = null;

        if (self == Types.UNDEFINED) {
            String errorType = (String) ((JSObject) get(context, "prototype")).get(context, "name");
            o = context.createError(errorType, args[0] == Types.UNDEFINED ? null : Types.toString( context, args[0]) );
        } else {
            o = (JSObject) self;
        }

        if (args[0] != Types.UNDEFINED) {
            o.defineOwnProperty(context, "message", new PropertyDescriptor() {
                {
                    set("Value", Types.toString( context, args[0]) );
                }
            }, false);
        }

        o.defineOwnProperty(context, "stack", new PropertyDescriptor() {
            {
                set("Get", new StackGetter(context));
            }
        }, false);

        return o;
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        DynObject o = new DynObject(context.getGlobalObject());
        o.setClassName("Error");
        return o;
    }

}
