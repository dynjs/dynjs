package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.exception.ThrowException;
import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.parser.ast.VariableDeclarationStatement;

public abstract class AbstractFunction extends DynObject implements JSFunction {

    private Statement body;
    private String[] formalParameters;
    private LexicalEnvironment scope;
    private boolean strict;

    private String debugContext;

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
        Object proto = scope.getGlobalObject().get(null, "Function");
        if (proto != Types.UNDEFINED) {
            setPrototype((JSObject) proto);
        }
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
        if (name.equals("caller") && this.strict) {
            throw new ThrowException(context.createTypeError("may not reference 'caller'"));
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
        if (!(v instanceof JSObject)) {
            return false;
        }

        JSObject proto = getPrototype();

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

    public String getFileName() {
        String name = null;
        if (this.body.getPosition() != null) {
            name = this.body.getPosition().getFileName();
        }
        if (name == null) {
            name = "<eval>";
        }
        return name;
    }

    public void setDebugContext(String debugContext) {
        this.debugContext = debugContext;
    }

    public String getDebugContext() {
        return this.debugContext;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("function(");
        String[] params = getFormalParameters();
        for (int i = 0; i < params.length; ++i) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(params[i]);
        }
        buffer.append("){\n");
        buffer.append(body.toIndentedString("  "));
        buffer.append("}");

        return buffer.toString();
    }
}
