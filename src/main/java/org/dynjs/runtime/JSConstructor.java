package org.dynjs.runtime;


public class JSConstructor extends AbstractFunction {

    private JSFunction function;

    public JSConstructor(JSFunction function) {
        super( null, false );
        this.function = function;
    }
    
    public JSFunction getFunction() {
        return this.function;
    }

    @Override
    public Object call(ExecutionContext context) {
        return null;
    }
}