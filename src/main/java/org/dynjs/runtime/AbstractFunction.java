package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.exception.TypeError;
import org.dynjs.parser.Statement;
import org.dynjs.parser.statement.BlockStatement;
import org.dynjs.parser.statement.FunctionDeclaration;
import org.dynjs.parser.statement.VariableDeclaration;
import org.dynjs.parser.statement.VariableDeclarationStatement;

public abstract class AbstractFunction extends DynObject implements JSFunction {

    private Statement body;
    private String[] formalParameters;
    private LexicalEnvironment scope;
    private boolean strict;

    public AbstractFunction(final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        this(null, scope, strict, formalParameters);
    }

    public AbstractFunction(final Statement body, final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        this.body = body;
        this.formalParameters = formalParameters;
        this.scope = scope;
        this.strict = strict;
        setClassName("Function");
        PropertyDescriptor desc = new PropertyDescriptor() {
            {
                set("Value", formalParameters.length);
                set("Writable", false);
                set("Configurable", false);
                set("Enumerable", false);
            }
        };
        defineOwnProperty(null, "length", desc, false);
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

    protected void setFormalParamters(String[] formalParameters) {
        this.formalParameters = formalParameters;
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    @Override
    public Object get(ExecutionContext context, String name) {
        // 15.3.5.4
        if (this.strict) {
            throw new TypeError();
        }
        return super.get(context, name);
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        if (this.body instanceof BlockStatement) {
            return ((BlockStatement) this.body).getFunctionDeclarations();
        }

        if (this.body instanceof FunctionDeclaration) {
            return Collections.singletonList((FunctionDeclaration) this.body);
        }
        return Collections.emptyList();
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        if (this.body instanceof BlockStatement) {
            return ((BlockStatement) this.body).getVariableDeclarations();
        }

        if (this.body instanceof VariableDeclarationStatement) {
            return ((VariableDeclarationStatement) this.body).getVariableDeclarations();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean hasInstance(Object v) {
        System.err.println( this + "#hasInstance: " + v);
        if (!(v instanceof JSObject)) {
            return false;
        }

        JSObject proto = getPrototype();
        
        System.err.println( "myProto: " + proto );

        if (proto == null) {
            return false;
        }

        while (true) {
            v = ((JSObject) v).getPrototype();
            if (v == null) {
                return false;
            }
            if (v == proto) {
                return true;
            }
        }
    }
    
    @Override
    public JSObject createNewObject() {
        DynObject o = new DynObject();
        o.setPrototype(getPrototype());
        return o;
    }
}
