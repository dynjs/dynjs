package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.DynArray;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.error.ToString;

public class BuiltinError extends AbstractBuiltinType {

    public BuiltinError(final GlobalObject globalObject) {
        super(globalObject);

        final DynObject proto = new DynObject(globalObject);
        proto.setClassName("Error");
        setPrototypeProperty( proto );

    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        defineNonEnumerableProperty(proto, "constructor", this );
        defineNonEnumerableProperty(proto, "name", "Error" );
        defineNonEnumerableProperty(proto, "message", "" );
        defineNonEnumerableProperty(proto, "toString", new ToString(globalObject) );
    }

    @Override
    public Object call(ExecutionContext context, Object self, final Object... args) {
        JSObject o = null;

        if (self == Types.UNDEFINED) {
            o = context.createError((String) ((JSObject) get(context, "prototype")).get(null, "name"), null);
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
        DynObject o = new DynObject(context.getGlobalObject());
        o.setClassName("Error");
        return o;
    }

}
