package org.dynjs.compiler;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.ExecutionContext;

public interface BasicBlockCompiler {

    public BasicBlock compile(CompilationContext context, final String grist, final Statement body, boolean strict);

}
