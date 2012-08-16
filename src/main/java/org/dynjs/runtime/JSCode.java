package org.dynjs.runtime;

import java.util.List;

import org.dynjs.parser.statement.FunctionDeclaration;
import org.dynjs.parser.statement.VariableDeclaration;

public interface JSCode {
    boolean isStrict();

    List<FunctionDeclaration> getFunctionDeclarations();

    List<VariableDeclaration> getVariableDeclarations();
}
