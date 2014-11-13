package org.dynjs.compiler;

import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.JSProgram;

public interface ProgramCompiler {
    public JSProgram compile(final CompilationContext context, final ProgramTree body, boolean forceStrict);
}
