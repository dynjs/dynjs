package org.dynjs.compiler.interpreter;

import org.dynjs.compiler.BasicBlockCompiler;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.interp.InterpretedStatement;

public class InterpretingBasicBlockCompiler implements BasicBlockCompiler {

    @Override
    public BasicBlock compile(ExecutionContext context, String grist, Statement body) {
        return new InterpretedStatement(body, context.isStrict() );
    }

}
