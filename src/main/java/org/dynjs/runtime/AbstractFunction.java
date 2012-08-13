package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.exception.TypeError;
import org.dynjs.parser.Statement;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.parser.statement.FunctionDeclaration;
import org.dynjs.parser.statement.VariableDeclaration;

public abstract class AbstractFunction extends DynObject implements JSFunction {

    private static final Statement[] EMPTY_STATEMENT_ARRAY = new Statement[0];
    private BlockStatement body;
    private String[] formalParameters;
    private LexicalEnvironment scope;
    private boolean strict;

    public AbstractFunction(final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        this( null, scope, strict, formalParameters );
    }

    public AbstractFunction(final BlockStatement body, final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        this.body = body;
        this.formalParameters = formalParameters;
        this.scope = scope;
        this.strict = strict;
        setClassName( "Function" );
        PropertyDescriptor desc = new PropertyDescriptor() {
            {
                set( "Value", formalParameters.length );
                set( "Writable", false );
                set( "Configurable", false );
                set( "Enumerable", false );
            }
        };
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

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    @Override
    public Object get(ExecutionContext context, String name) {
        // 15.3.5.4
        if (this.strict) {
            throw new TypeError();
        }
        return super.get( context, name );
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        if (this.body == null) {
            return Collections.emptyList();
        }
        return this.body.getFunctionDeclarations();
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        if ( this.body == null ) {
            return Collections.emptyList();
        }
        
        return this.body.getVariableDeclarations();
    }

}
