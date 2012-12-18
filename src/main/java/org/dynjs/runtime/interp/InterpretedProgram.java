package org.dynjs.runtime.interp;

import org.dynjs.parser.Statement;
import org.dynjs.runtime.AbstractProgram;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;

public class InterpretedProgram extends AbstractProgram {
    
    public InterpretedProgram(Statement block, boolean strict) {
        super( block, strict );
    }

    @Override
    public Completion execute(ExecutionContext context) {
        InterpretingVisitor visitor = new InterpretingVisitor();
        getBlock().accept(context, visitor, isStrict() );
        return (Completion) visitor.pop();
    }

}
