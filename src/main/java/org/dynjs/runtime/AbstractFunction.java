package org.dynjs.runtime;

import org.dynjs.exception.TypeError;
import org.dynjs.parser.Statement;

public abstract class AbstractFunction extends DynObject implements JSFunction {

    private static final Statement[] EMPTY_STATEMENT_ARRAY = new Statement[0];
    private Statement[] statements;
    private String[] formalParameters;
    private LexicalEnvironment scope;
    private boolean strict;
    
    public AbstractFunction(final LexicalEnvironment scope, final boolean strict, final String...formalParameters) {
        this( EMPTY_STATEMENT_ARRAY, scope, strict, formalParameters);
    }

    public AbstractFunction(final Statement[] statements, final LexicalEnvironment scope, final boolean strict, final String...formalParameters) {
        this.statements = statements;
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
        defineOwnProperty( null, "length", desc, false );
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
    public Object construct(ExecutionContext context, Object... args) {
        // 13.2.2
        DynObject obj = new DynObject();
        obj.setClassName( "Object" );
        obj.setExtensible( true );
        JSObject proto = getPrototype();
        obj.setPrototype( proto );
        context.call( this, obj, args );
        this.call( context, obj, args );
        return obj;
    }

    

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    @Override
    public Object get(ExecutionContext context, String name) {
        // 15.3.5.4
        if ( this.strict ) {
            throw new TypeError();
        }
        return super.get( context, name );
    }
    
    
    

}
