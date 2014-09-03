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
        return new V8Stack(stack, args[0], args[1]);
    }

    @Override
    public void setFileName() {
        this.filename = "<internal>";
    }
    
    @Override
    public void setupDebugContext() {
        setDebugContext("<v8-stack-getter>");
    }

    public class V8Stack {
        private final List<StackElement> stack;
        private Number limit = 10;
        private Object func;

        V8Stack(List<StackElement> stack, Object limit, Object func) {
            this.func = func;
            if (limit instanceof Number) this.limit = (Number) limit;

            // Now chop off the top of the stack if there's a top function provided
            int top = 2;
            if (this.func instanceof JSFunction) {
                final Object nameProperty = ((JSFunction) this.func).getProperty(null, "name");
                if (nameProperty != Types.UNDEFINED) {
                    String topFunction = (String) ((PropertyDescriptor) nameProperty).getValue();
                    top = 1;
                    for (StackElement element : stack) {
                        if (topFunction.equals(element.getFunctionName())) break;
                        top++;
                    }
                }
            }
            this.stack = stack.subList(top, Math.min(stack.size(), this.limit.intValue() + top));
        }

        public Object[] getStackArray() {
            return stack.toArray();
        }
    }

}
