package org.dynjs.compiler;

import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;

public interface ProgramCompiler {
    public JSProgram compile(final ExecutionContext context, final ProgramTree body, boolean forceStrict);
}
