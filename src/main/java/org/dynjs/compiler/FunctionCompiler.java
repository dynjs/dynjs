package org.dynjs.compiler;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSFunction;

public interface FunctionCompiler {

    public JSFunction compile(final CompilationContext context, final String identifier, final String[] formalParameters, final Statement body, final boolean strict);
}
