package org.dynjs.compiler;

import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.wrapper.JavascriptProgram;

public class ProgramCompiler {

    public JSProgram compile(final ExecutionContext context, final ProgramTree body, boolean forceStrict) {
        BasicBlock code = context.getCompiler().compileBasicBlock(context, "ProgramBody", body, forceStrict || body.isStrict() );
        JavascriptProgram program = new JavascriptProgram( code );
        return program;
        
    }

}
