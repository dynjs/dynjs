package org.dynjs.compiler;

import org.dynjs.parser.ast.BlockStatement;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;

public interface FunctionCompiler {

    public JSFunction compile(final ExecutionContext context, final String identifier, final String[] formalParameters, final BlockStatement body, final boolean strict);

}