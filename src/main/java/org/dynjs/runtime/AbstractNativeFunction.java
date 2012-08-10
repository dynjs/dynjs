package org.dynjs.runtime;

import java.util.Collections;
import java.util.List;

import org.dynjs.parser.statement.FunctionDeclaration;
import org.dynjs.parser.statement.VariableDeclarationStatement;

public abstract class AbstractNativeFunction extends AbstractFunction {

    public AbstractNativeFunction(LexicalEnvironment scope, boolean strict, String...formalParameters) {
        super( scope, strict, formalParameters );
    }

    @Override
    public List<FunctionDeclaration> getFunctionDeclarations() {
        return Collections.emptyList();
    }

    @Override
    public List<VariableDeclarationStatement> getVariableDeclarations() {
        return Collections.emptyList();
    }

}
