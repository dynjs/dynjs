package org.dynjs.runtime.wrapper;

import java.util.List;

import org.dynjs.parser.Statement;
import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;
import org.dynjs.runtime.AbstractFunction;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BasicBlockDelegate;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.LexicalEnvironment;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.PropertyDescriptor.Names;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.interp.InterpretedBasicBlock;

public class JavascriptFunction extends AbstractFunction {

    private BasicBlock code;

    public JavascriptFunction(final GlobalObject globalObject, final String identifier, final BasicBlock code, final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        super(globalObject, scope, strict, formalParameters);
        this.code = code;

        final DynObject proto = new DynObject(globalObject);
        PropertyDescriptor constructorDesc = new PropertyDescriptor();
        constructorDesc.set(Names.VALUE, JavascriptFunction.this);
        constructorDesc.set(Names.WRITABLE, true);
        constructorDesc.set(Names.ENUMERABLE, false);
        constructorDesc.set(Names.CONFIGURABLE, true);
        proto.defineOwnProperty(null, "constructor", constructorDesc, false);

        PropertyDescriptor prototypeDesc = new PropertyDescriptor();
        prototypeDesc.set(Names.VALUE, proto);
        prototypeDesc.set(Names.WRITABLE, true);
        prototypeDesc.set(Names.ENUMERABLE, false);
        prototypeDesc.set(Names.CONFIGURABLE, false);
        defineOwnProperty(null, "prototype", prototypeDesc, false);

        PropertyDescriptor nameDesc = new PropertyDescriptor();
        nameDesc.set(Names.VALUE, identifier);
        nameDesc.set(Names.WRITABLE, true);
        nameDesc.set(Names.ENUMERABLE, false);
        nameDesc.set(Names.CONFIGURABLE, true);
        defineOwnProperty(null, "name", nameDesc, false);
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
