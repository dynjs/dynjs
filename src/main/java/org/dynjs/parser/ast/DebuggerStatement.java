package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class DebuggerStatement extends BaseStatement {

    public DebuggerStatement(Position position) {
        super( position );
    }
    
    @Override
    public String toIndentedString(String indent) {
        return indent + "debugger";
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        //visitor.visit( context, this, strict );
    }

}
