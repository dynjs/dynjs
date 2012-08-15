package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.parser.statement.FunctionDeclaration;
import org.dynjs.parser.statement.VariableDeclaration;

public abstract class AbstractNativeFunction extends AbstractFunction {

    public AbstractNativeFunction(GlobalObject globalObject, String... formalParameters) {
        super( LexicalEnvironment.newObjectEnvironment( globalObject, false, null ), false, formalParameters );
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
        Reference selfRef = context.resolve( "this" );
        Object self = Types.UNDEFINED;

        if (selfRef != null && !selfRef.isUnresolvableReference()) {
            self = selfRef.getValue( context );
        }

        String[] formalParams = getFormalParameters();
        Object[] args = new Object[formalParams.length];
        for (int i = 0; i < formalParams.length; ++i) {
            Reference eachRef = context.resolve( formalParams[i] );
            if (!eachRef.isUnresolvableReference()) {
                args[i] = eachRef.getValue( context );
            } else {
                args[i] = Types.UNDEFINED;
            }
        }

        return call( context, self, args );
    }

    public abstract Object call(ExecutionContext context, Object self, Object... args);

}
