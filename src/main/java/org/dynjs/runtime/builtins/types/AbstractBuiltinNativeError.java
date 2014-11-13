package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.builtins.types.error.StackGetter;
import org.dynjs.runtime.Types;

public class AbstractBuiltinNativeError extends AbstractBuiltinType {

    private String name;

    public AbstractBuiltinNativeError(GlobalContext globalContext, final String name) {
        super(globalContext, "message");

        final DynObject proto = new DynObject(globalContext);
        this.defineReadOnlyProperty(globalContext, "prototype", proto);
        this.name = name;
    }

    @Override
    public void initialize(GlobalContext globalContext, JSObject proto) {
        proto.setClassName("Error");
        proto.defineNonEnumerableProperty(globalContext, "constructor", this);
        proto.put(null, "name", this.name, false);
        proto.put(null, "message", "", false);
        proto.setPrototype(globalContext.getPrototypeFor("Error"));
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
            messageDesc.setValue(Types.toString( context, args[0]) );
            o.defineOwnProperty(context, "message", messageDesc, false);
        }

        PropertyDescriptor stackDesc = new PropertyDescriptor();
        stackDesc.setGetter(new StackGetter(context));
        o.defineOwnProperty(context, "stack", stackDesc, false);

        return o;
    }

    @Override
    public JSObject createNewObject(ExecutionContext context) {
        DynObject o = new DynObject(context.getGlobalContext());
        o.setClassName("Error");
        return o;
    }

}
