package org.dynjs.runtime;

import java.util.List;

import org.dynjs.parser.statement.VariableDeclarationStatement;
import org.dynjs.parser.statement.FunctionDeclaration;

public interface JSCode {
    boolean isStrict();
    List<FunctionDeclaration> getFunctionDeclarations();
    List<VariableDeclarationStatement> getVariableDeclarations();
}
