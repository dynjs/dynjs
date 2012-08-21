package org.dynjs.runtime;

public class ArgSetter extends AbstractNativeFunction {

    private String name;

    public ArgSetter(LexicalEnvironment env, String name) {
        super(env, false, "value");
        this.name = name;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        getScope().getIdentifierReference(context, this.name, isStrict()).putValue(context, args[0]);
        return null;
    }

}
