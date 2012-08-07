package org.dynjs.runtime;

import org.dynjs.exception.TypeError;

public abstract class AbstractFunction extends DynObject implements JSFunction {

    private String[] formalParameters;
    private LexicalEnvironment scope;
    private boolean strict;

    public AbstractFunction(final LexicalEnvironment scope, final boolean strict, final String...formalParameters) {
        this.formalParameters = formalParameters;
        this.scope = scope;
        this.strict = strict;
        setClassName( "Function" );
        PropertyDescriptor desc = new PropertyDescriptor() {{
            set( "Value", formalParameters.length );
            set( "Writable", false );
            set( "Configurable", false );
            set( "Enumerable", false );
        }};
        defineOwnProperty( "length", desc, false );
    }
    
    public LexicalEnvironment getScope() {
        return this.scope;
    }
    
    public boolean isStrict() {
        return this.strict;
    }
    
    @Override
    public String[] getFormalParameters() {
        return this.formalParameters;
    }

    @Override
    public Object call(JSObject self, String... args) {
        // TODO Auto-generated method stub
        return null;
    }
    
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    @Override
    public Object get(String name) {
        // 15.3.5.4
        if ( this.strict ) {
            throw new TypeError();
        }
        return super.get( name );
    }
    
    
    

}
