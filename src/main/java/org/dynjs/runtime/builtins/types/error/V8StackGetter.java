package org.dynjs.runtime.builtins.types.error;

import org.dynjs.runtime.*;

import java.util.ArrayList;
import java.util.List;

public class V8StackGetter extends AbstractNativeFunction {

    public V8StackGetter(GlobalContext global) {
        super(global);
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        final List<StackElement> stack = new ArrayList<>();
        context.collectStackElements(stack);
        return new V8Stack(args[0], stack, args[1], args[2]);
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
        private final Object func;
        private final Object err;
        private final List<StackElement> stack;
        private Number limit = 10;

        V8Stack(Object err, List<StackElement> stack, Object limit, Object func) {
            this.func = func;
            if (limit instanceof Number) this.limit = (Number) limit;
            this.stack = stack;
            this.err = err;
        }

        public Object[] getStackArray() {
            // Now chop off the top of the stack if there's a top function provided
            int top = 2;
            if (this.func instanceof JSFunction) {
                final JSObject errObject = (JSObject) err;
                final Object topFunction = ((JSFunction) this.func).get(null, "name");
                if (topFunction != Types.UNDEFINED && ((JSObject)err).get(null, "__native") == Types.UNDEFINED) {
                    top = 1;
                    for (StackElement element : stack) {
                        if (topFunction.equals(element.getFunctionName())) break;
                        top++;
                    }
                }
            }
            final int bottom = Math.min(stack.size(), this.limit.intValue() + top);
            top = Math.min(top, bottom);
            return stack.subList(top, bottom).toArray();
        }
    }

}
