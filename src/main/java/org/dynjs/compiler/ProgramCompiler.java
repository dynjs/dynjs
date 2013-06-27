package org.dynjs.compiler;

import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.DynamicClassLoader;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.ThreadCompilationManager;
import org.dynjs.runtime.wrapper.JavascriptProgram;

public class ProgramCompiler {

    public JSProgram compile(final ExecutionContext context, final ProgramTree body, boolean forceStrict) {
        DynamicClassLoader cl = new DynamicClassLoader(context.getConfig().getClassLoader());
        ThreadCompilationManager.push(cl);
        try {
            BasicBlock code = context.getCompiler().compileBasicBlock(context, "ProgramBody", body, forceStrict || body.isStrict());
            JavascriptProgram program = new JavascriptProgram(cl, code);
            return program;
        } finally {
            ThreadCompilationManager.pop();
        }

    }

}
