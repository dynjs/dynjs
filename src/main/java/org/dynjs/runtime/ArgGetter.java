package org.dynjs.runtime;

public class ArgGetter extends AbstractNativeFunction {

    private String name;

    public ArgGetter(LexicalEnvironment env, String name) {
        super( env, true );
        this.name = name;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return getScope().getIdentifierReference( context, this.name, isStrict() );
    }

}