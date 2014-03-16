package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.StackGetter;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.builtins.types.error.ToString;

public class BuiltinError extends AbstractBuiltinType {

    public BuiltinError(final GlobalObject globalObject) {
        super(globalObject, "message");

        final JSObject proto = new DynObject(globalObject);
        proto.setClassName("Error");
        setPrototypeProperty(proto);

    }

    @Override
    public void initialize(GlobalObject globalObject, JSObject proto) {
        defineNonEnumerableProperty(proto, "constructor", this);
        defineNonEnumerableProperty(proto, "name", "Error");
        defineNonEnumerableProperty(proto, "message", "");
        defineNonEnumerableProperty(proto, "toString", new ToString(globalObject));
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
            PropertyDescriptor messageDesc = new PropertyDescriptor();
            messageDesc.setValue(Types.toString( context, args[0]));
            o.defineOwnProperty(context, "message", messageDesc, false);
        }

        PropertyDescriptor stackDesc = new PropertyDescriptor();
        stackDesc.setGetter(new StackGetter(context));
        o.defineOwnProperty(context, "stack", stackDesc, false);

        return o;
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        DynObject o = new DynObject(context.getGlobalObject());
        o.setClassName("Error");
        return o;
    }
    
    @Override
    public void setFileName() {
        this.filename = "org/dynjs/runtime/builtins/types/BuiltinError.java";
    }

    @Override
    public void setupDebugContext() {
        this.debugContext = "<native function: Error>";
    }

}
