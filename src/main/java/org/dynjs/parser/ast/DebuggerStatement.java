package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.Completion;
import org.dynjs.runtime.ExecutionContext;

public class DebuggerStatement extends BaseStatement {

    public DebuggerStatement(Position position) {
        super( position );
    }
    
    @Override
    public String toIndentedString(String indent) {
        return indent + "debugger";
    }
    
    public int getSizeMetric() {
        return 1;
    }

    @Override
    public Object accept(Object context, CodeVisitor visitor, boolean strict) {
        return null;
    }

    public Completion interpret(ExecutionContext context, boolean debug) {
        return Completion.createNormal();
    }
}
