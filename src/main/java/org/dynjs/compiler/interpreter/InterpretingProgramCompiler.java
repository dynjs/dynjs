package org.dynjs.compiler.interpreter;

import org.dynjs.compiler.ProgramCompiler;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.interp.InterpretedBasicBlock;
import org.dynjs.runtime.interp.InterpretingVisitorFactory;
import org.dynjs.runtime.wrapper.JavascriptProgram;

public class InterpretingProgramCompiler implements ProgramCompiler {
    private InterpretingVisitorFactory factory;

    public InterpretingProgramCompiler(InterpretingVisitorFactory factory) {
        this.factory = factory;
    }

    public JSProgram compile(final ExecutionContext context, final ProgramTree body, boolean strict) {
        return new JavascriptProgram(new InterpretedBasicBlock(this.factory, body, strict));
    }
}
