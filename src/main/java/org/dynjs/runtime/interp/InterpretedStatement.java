package org.dynjs.runtime.interp;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.BasicBlock;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;

public class InterpretedStatement implements BasicBlock {

    private Statement block;
    private boolean strict;

    public InterpretedStatement(Statement block, boolean strict) {
        this.block = block;
        this.strict = strict;
    }

    @Override
    public Completion call(ExecutionContext context) {
        InterpretingVisitor visitor = new InterpretingVisitor( context.getBlockManager() );
        this.block.accept(context, visitor, this.strict);
        return (Completion) visitor.pop();
    }

}
