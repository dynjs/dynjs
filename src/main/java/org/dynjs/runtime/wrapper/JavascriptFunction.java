package org.dynjs.runtime.wrapper;

import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.*;
import org.dynjs.runtime.interp.InterpretedBasicBlock;

public class JavascriptFunction extends AbstractFunction {

    private BasicBlock code;

    public JavascriptFunction(final GlobalContext globalContext, final String identifier, final BasicBlock code, final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        super(globalContext, scope, strict, formalParameters);
        this.code = code;

        final DynObject proto = new DynObject(globalContext);

        proto.defineOwnProperty(null, "constructor",
                PropertyDescriptor.newDataPropertyDescriptor(JavascriptFunction.this, true, true, false), false);
        defineOwnProperty(null, "prototype",
                PropertyDescriptor.newDataPropertyDescriptor(proto, true, false, false), false);
        defineOwnProperty(null, "name",
                PropertyDescriptor.newDataPropertyDescriptor(identifier, true, true, false), false);
    }

    @Override
    public String getFileName() {
        return this.code.getFileName();
    }

    @Override
    public Object call(ExecutionContext context) {
        Completion result = this.code.call(context);
        if (result.type == Completion.Type.RETURN) {
            if (result.value == null) {
                return Types.UNDEFINED;
            }

            return result.value;
        }

        return Types.UNDEFINED;
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return this.code.getFunctionDeclarations();
    }

    @Override
    public List<VariableDeclaration> getVariableDeclarations() {
        return this.code.getVariableDeclarations();
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append("function(");
        String[] params = getFormalParameters();
        for (int i = 0; i < params.length; ++i) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(params[i]);
        }
        buffer.append("){\n");
        Statement statement = null;
        if (code instanceof BasicBlockDelegate && ((BasicBlockDelegate) code).getDelegate() instanceof InterpretedBasicBlock) {
            statement = ((InterpretedBasicBlock) ((BasicBlockDelegate) code).getDelegate()).getBody();
        } else if (code instanceof InterpretedBasicBlock) {
            statement = ((InterpretedBasicBlock) code).getBody();
        }
        if (statement != null) {
            buffer.append(statement.toIndentedString("  "));
        }
        buffer.append("}");

        return buffer.toString();
    }

}
