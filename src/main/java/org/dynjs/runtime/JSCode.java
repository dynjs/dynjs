package org.dynjs.runtime;

import java.util.List;

import org.dynjs.parser.statement.DeclareVarStatement;
import org.dynjs.parser.statement.FunctionStatement;

public interface JSCode {
    boolean isStrict();
    List<FunctionStatement> getFunctionDeclarations();
    List<DeclareVarStatement> getVariableDeclarations();
}
