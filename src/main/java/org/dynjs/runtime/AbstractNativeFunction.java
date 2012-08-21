package org.dynjs.runtime;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dynjs.parser.statement.FunctionDeclaration;
import org.dynjs.parser.statement.VariableDeclaration;

public abstract class AbstractNativeFunction extends AbstractFunction {

    public AbstractNativeFunction(GlobalObject globalObject, String... formalParameters) {
        super(LexicalEnvironment.newObjectEnvironment(globalObject, false, null), false, formalParameters);
    }
    
    public AbstractNativeFunction(final LexicalEnvironment scope, final boolean strict, final String... formalParameters) {
        super( scope, strict, formalParameters);
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
        JSObject self = context.getThisBinding();

        Arguments argsObj = (Arguments) context.resolve("arguments").getValue(context);
        int numArgs = (int) argsObj.get(context, "length");
        int paramsLen = getFormalParameters().length;

        Object[] args = new Object[numArgs < paramsLen ? paramsLen : numArgs ];

        for (int i = 0; i < numArgs; ++i) {
            Object v = argsObj.get(context, "" + i);
            if (v instanceof Reference) {
                if ( ((Reference)v).isUnresolvableReference() ) {
                    v = Types.UNDEFINED;
                } else {
                    v = ((Reference)v).getValue(context);
                }
            }
            args[i] = v;
        }
        
        for ( int i = numArgs ; i < paramsLen; ++i ) {
            args[i] = Types.UNDEFINED;
        }

        return call(context, self, args);
    }

    public abstract Object call(ExecutionContext context, Object self, Object... args);

}
