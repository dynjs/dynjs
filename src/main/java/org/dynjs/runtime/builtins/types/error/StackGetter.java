package org.dynjs.runtime.builtins.types.error;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.Types;

import java.util.ArrayList;
import java.util.List;

public class StackGetter extends AbstractNativeFunction {

    private List<StackElement> stack;

    public StackGetter(ExecutionContext context) {
        super(context.getGlobalContext());
        final List<StackElement> stack = new ArrayList<>();
        context.collectStackElements(stack);
        this.stack = stack;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        JSObject jsObject = (JSObject) self;
        String errorName = null;
        if (jsObject.hasProperty(context, "name")) {
            errorName = Types.toString(context, jsObject.get(context, "name"));
        }
        String message = null;
        if (jsObject.hasProperty(context, "message")) {
            message = Types.toString(context, jsObject.get(context, "message"));
        }

        StringBuilder buf = new StringBuilder();
        if (errorName == null) {
            buf.append("<unknown>");
        } else {
            buf.append(errorName);
        }
        if (message != null && !message.equals("")) {
            buf.append(": " + message);
        }
        buf.append("\n");
        for (StackElement each : stack) {
            buf.append("  at " + each + "\n");
        }
        return buf.toString();
    }

    @Override
    public void setFileName() {
        this.filename = "<internal>";
    }
    
    @Override
    public void setupDebugContext() {
        setDebugContext("<stack-getter>");
    }

}
