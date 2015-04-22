package org.dynjs.compiler.bytecode;

import org.dynjs.compiler.CompilationContext;
import org.dynjs.compiler.ProgramCompiler;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.wrapper.JavascriptProgram;

public class BytecodeProgramCompiler implements ProgramCompiler {
    public JSProgram compile(final CompilationContext context, final ProgramTree body, boolean forceStrict) {
        BasicBlock code = context.getCompiler().compileBasicBlock(context, "ProgramBody", body, forceStrict || body.isStrict() );

        return new JavascriptProgram( body.getSource(), context.getBlockManager(), code );
    }
}
