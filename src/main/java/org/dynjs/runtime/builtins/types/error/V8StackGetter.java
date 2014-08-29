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
        return stack;
    }

    @Override
    public void setFileName() {
        this.filename = "<internal>";
    }
    
    @Override
    public void setupDebugContext() {
        setDebugContext("<v8-stack-getter>");
    }

}
