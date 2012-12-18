package org.dynjs.compiler.interpreter;

import org.dynjs.compiler.ProgramCompiler;
import org.dynjs.parser.ast.ProgramTree;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.JSProgram;
import org.dynjs.runtime.interp.InterpretedProgram;

public class InterpretingProgramCompiler implements ProgramCompiler {

    @Override
    public JSProgram compile(ExecutionContext context, ProgramTree body, boolean forceStrict) {
        return new InterpretedProgram(body, body.isStrict() || forceStrict );
    }

}
