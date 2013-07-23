package org.dynjs.runtime;

import java.util.ArrayList;
import java.util.List;

public class StackGetter extends AbstractNativeFunction {

    private String errorName;
    private String message;
    private List<StackElement> stack;

    public StackGetter(GlobalObject globalObject, String errorName, String message, List<StackElement> stack) {
        super(globalObject, false);
        this.errorName = errorName;
        this.message = message;
        this.stack = stack;
    }

    public StackGetter(ExecutionContext context) {
        super(context.getGlobalObject());
        final List<StackElement> stack = new ArrayList<>();
        context.collectStackElements(stack);
        this.stack = stack;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        JSObject jsObject = (JSObject) self;
        if (errorName == null && jsObject.hasProperty(context, "name")) {
            errorName = Types.toString(context, jsObject.get(context, "name"));
        }
        if (message == null && jsObject.hasProperty(context, "message")) {
            message = Types.toString(context, jsObject.get(context, "message"));
        }

        StringBuilder buf = new StringBuilder();
        if (this.errorName == null) {
            buf.append("<unknown>");
        } else {
            buf.append(this.errorName);
        }
        if (this.message != null && !this.message.equals("")) {
            buf.append(": " + this.message);
        }
        buf.append("\n");
        for (StackElement each : stack) {
            buf.append("  at " + each + "\n");
        }
        return buf.toString();
    }

}
