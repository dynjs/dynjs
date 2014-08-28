package org.dynjs.runtime.builtins.types;

import org.dynjs.runtime.*;
import org.dynjs.runtime.builtins.types.error.ToString;
import org.dynjs.runtime.builtins.types.error.v8.CaptureStackTrace;
import org.dynjs.runtime.builtins.types.error.v8.PrepareStackTrace;

public class BuiltinError extends AbstractBuiltinType {

    public BuiltinError(final GlobalObject globalObject) {
        super(globalObject, "message");

        final JSObject proto = new DynObject(globalObject);
        proto.setClassName("Error");
        setPrototypeProperty(proto);

        // Set these properties on the Error ctor
        defineNonEnumerableProperty(this, "stackTraceLimit", 10);
        defineNonEnumerableProperty(this, "captureStackTrace", new CaptureStackTrace(globalObject));
        defineNonEnumerableProperty(this, "prepareStackTrace", new PrepareStackTrace(globalObject));
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

        // Here we lookup the value of captureStackTrace because it could be
        // set by user code and no longer will we be executing the builtin.
        Object capturer = this.getOwnProperty(context, "captureStackTrace");
        if (capturer instanceof JSFunction) {
            // But uh-oh! It looks like we can't supply any args to a non-native function.
            // We should capturer.call(context, error)
            ((JSFunction)capturer).call(context);
        }
        // Here we should do the same thing for prepareStackTrace as we have
        // done above. Same issues apply
        Object preparer = this.getOwnProperty(context, "prepareStackTrace");
        if (preparer instanceof JSFunction) {
            // But uh-oh! It looks like we can't supply any args to a non-native function.
            // We should preparer.call(context, error, structuredStackTrace)
            ((JSFunction)capturer).call(context);
        }
        // TODO: Setting the stack property should be done by captureStackTrace above
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
