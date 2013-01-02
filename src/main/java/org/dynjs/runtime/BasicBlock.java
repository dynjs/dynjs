package org.dynjs.runtime;

import java.util.List;

import org.dynjs.parser.ast.FunctionDeclaration;
import org.dynjs.parser.ast.VariableDeclaration;

public interface BasicBlock {

    String getFileName();
    boolean isStrict();
    
    Completion call(ExecutionContext context);
    
    List<VariableDeclaration> getVariableDeclarations();
    List<FunctionDeclaration> getFunctionDeclarations();

}
