package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.parser.statement.DeclareVarStatement;
import org.dynjs.parser.statement.FunctionStatement;

public abstract class AbstractNativeFunction extends AbstractFunction {

    public AbstractNativeFunction(LexicalEnvironment scope, boolean strict, String...formalParameters) {
        super( scope, strict, formalParameters );
    }

    @Override
    public List<FunctionStatement> getFunctionDeclarations() {
        return Collections.emptyList();
    }

    @Override
    public List<DeclareVarStatement> getVariableDeclarations() {
        return Collections.emptyList();
    }

}
