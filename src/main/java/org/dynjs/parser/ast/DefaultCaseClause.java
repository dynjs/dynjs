package org.dynjs.parser.ast;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class DefaultCaseClause extends CaseClause {


    public DefaultCaseClause(Position position, Statement block) {
        super( position, null, block );
    }

    public String toIndentedString(String indent) {
        StringBuilder buf = new StringBuilder();
        buf.append(indent).append("default: \n");
        if (getBlock() != null) {
            buf.append(getBlock().toIndentedString("  " + indent));
        }
        return buf.toString();
    }
    
    public int getSizeMetric() {
        return 3;
    }
    
    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

}
