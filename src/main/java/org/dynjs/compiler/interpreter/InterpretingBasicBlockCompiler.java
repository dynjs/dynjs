package org.dynjs.compiler.interpreter;

import org.dynjs.compiler.BasicBlockCompiler;
import org.dynjs.compiler.CompilationContext;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.BlockManager.Entry;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.interp.InterpretedBasicBlock;
import org.dynjs.runtime.interp.InterpretingVisitorFactory;

public class InterpretingBasicBlockCompiler implements BasicBlockCompiler {

    
    private InterpretingVisitorFactory factory;
    public InterpretingBasicBlockCompiler(InterpretingVisitorFactory factory) {
        this.factory = factory;
    }
    
    @Override
    public BasicBlock compile(CompilationContext context, String grist, Statement body, boolean strict) {
        int statementNumber = body.getStatementNumber();

        Entry entry = context.getBlockManager().retrieve(statementNumber);
        BasicBlock code = entry.getCompiled();
        if (code != null) {
            return code;
        }
        
        code = new InterpretedBasicBlock(this.factory, body, strict);
        entry.setCompiled(code);
        return code;
    }

}
