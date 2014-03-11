package org.dynjs.runtime;

public class ArgGetter extends AbstractNativeFunction {

    private String name;

    public ArgGetter(GlobalObject globalObject, LexicalEnvironment env, String name) {
        super(globalObject, env, false);
        this.name = name;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return getScope().getIdentifierReference(context, this.name, isStrict()).getValue(context);
    }
    
    @Override
    public void setFileName() {
        this.filename = "<internal>";
    }
    
    @Override
    public void setupDebugContext() {
        setDebugContext("<arg-getter>");
    }

}
