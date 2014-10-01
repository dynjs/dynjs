package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;

public abstract class AbstractNativeFunction extends AbstractFunction {

    protected String filename;

    public AbstractNativeFunction(GlobalContext globalContext, String... formalParameters) {
        super(globalContext, LexicalEnvironment.newObjectEnvironment(globalContext.getObject(), false, null), true, formalParameters);
        setupDebugContext();
        setFileName();
    }

    public AbstractNativeFunction(GlobalContext globalContext, boolean strict, String... formalParameters) {
        super(globalContext, LexicalEnvironment.newObjectEnvironment(globalContext.getObject(), false, null), strict, formalParameters);
        setupDebugContext();
        setFileName();
    }

    public AbstractNativeFunction(final GlobalContext globalContext, final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        super(globalContext, scope, strict, formalParameters);
        setupDebugContext();
        setFileName();
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return Collections.emptyList();
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        return Collections.emptyList();
    }

    @Override
    public Object call(ExecutionContext context) {
        Object self = context.getThisBinding();

        Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
        int numArgs = (int) argsObj.get(context, "length");
        int paramsLen = getFormalParameters().length;

        Object[] args = new Object[numArgs < paramsLen ? paramsLen : numArgs];

        for (int i = 0; i < numArgs; ++i) {
            Object v = argsObj.get(context, "" + i);
            if (v instanceof Reference) {
                if (((Reference) v).isUnresolvableReference()) {
                    v = Types.UNDEFINED;
                } else {
                    v = ((Reference) v).getValue(context);
                }
            }
            args[i] = v;
        }

        for (int i = numArgs; i < paramsLen; ++i) {
            args[i] = Types.UNDEFINED;
        }

        return call(context, self, args);
    }

    public abstract Object call(ExecutionContext context, Object self, Object... args);

    public String getFileName() {
        return this.filename;
    }

    public void setFileName() {
        this.filename = getClass().getName().replace(".", "/") + ".java";
    }

    public void setupDebugContext() {
        this.debugContext = "<native function: " + getClass().getSimpleName() + ">";
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append("function(" );
        String[] params = getFormalParameters();
        for (int i = 0; i < params.length; ++i) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(params[i]);
        }
        buffer.append("){\n");
        buffer.append("  <native code in: " ).append( getClass().getName() ).append( ">\n" );
        buffer.append("}");

        return buffer.toString();
    }

}
