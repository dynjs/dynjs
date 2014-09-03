package org.dynjs.runtime.builtins.types.error;

import org.dynjs.runtime.*;

import java.util.ArrayList;
import java.util.List;

public class V8StackGetter extends AbstractNativeFunction {

    public V8StackGetter(GlobalObject global) {
        super(global);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        final List<StackElement> stack = new ArrayList<>();
        context.collectStackElements(stack);
        return new V8Stack(stack, context, (JSObject) args[0], args[1], args[2]);
    }

    @Override
    public void setFileName() {
        this.filename = "<internal>";
    }
    
    @Override
    public void setupDebugContext() {
        setDebugContext("<v8-stack-getter>");
    }

    class V8Stack {
        private final List<StackElement> stack;
        private final ExecutionContext context;
        private final JSObject error;
        private String stackString;
        private Number limit = 10;
        private Object func;

        V8Stack(List<StackElement> stack, ExecutionContext context, JSObject error, Object limit, Object func) {
            // Ignore the first three elements of the stack since those are internal
            this.stack = stack.subList(2, stack.size());
            this.context = context;
            this.error = error;
            this.func = func;
            if (limit instanceof Number) this.limit = (Number) limit;
        }

        public List<StackElement> getStack() {
            return stack;
        }

        @Override
        public String toString() {
            if (stackString == null) {
                JSObject jsObject = error;
                String errorName = null;
                if (jsObject.hasProperty(context, "name")) {
                    errorName = Types.toString(context, jsObject.get(context, "name"));
                }
                String message = null;
                if (jsObject.hasProperty(context, "message")) {
                    final Object value = jsObject.get(context, "message");
                    if (value != Types.UNDEFINED) {
                        message = Types.toString(context, value);
                    }
                }

                StringBuilder buf = new StringBuilder();
                if (errorName == null) {
                    buf.append("<unknown>");
                } else {
                    buf.append(errorName);
                }
                if (message != null && !message.equals("")) {
                    buf.append(": ").append(message);
                }
                buf.append("\n");

                String topFunction = null;
                if (this.func instanceof JSFunction) {
                    final Object nameProperty = ((JSFunction) this.func).getProperty(null, "name");
                    if (nameProperty != Types.UNDEFINED) {
                        topFunction = (String) ((PropertyDescriptor) nameProperty).getValue();
                    }
                }
                int level = 0;
                for (StackElement each : stack) {
                    if (topFunction == null) {
                        buf.append("  at ").append(each).append("\n");
                    } else {
                        if (topFunction.equals(each.getFunction())) topFunction = null;
                    }
                    if (level++ > limit.intValue()) break;
                }
                stackString = buf.toString();
            }
            return stackString;
        }
    }

}
