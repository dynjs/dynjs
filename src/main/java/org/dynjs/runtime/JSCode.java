package org.dynjs.runtime;

import java.util.List;

import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;

public interface JSCode {
    boolean isStrict();
    
    void verify(ExecutionContext context);

    List<FunctionDeclaration> getFunctionDeclarations();

    List<VariableDeclaration> getVariableDeclarations();
}
