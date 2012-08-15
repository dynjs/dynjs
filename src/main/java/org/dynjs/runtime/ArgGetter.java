package org.dynjs.runtime;

public class ArgGetter extends AbstractNativeFunction {

    private String name;

    public ArgGetter(GlobalObject globalObject, String name) {
        super( globalObject );
        this.name = name;
    }

    @Override
    public Object call(ExecutionContext context, Object self, Object... args) {
        return getScope().getIdentifierReference( context, this.name, isStrict() );
    }

}