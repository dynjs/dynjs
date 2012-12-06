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
        StringBuffer buf = new StringBuffer();
        buf.append(indent).append("default: \n");
        if (getBlock() != null) {
            buf.append(getBlock().toIndentedString("  " + indent));
        }
        return buf.toString();
    }
    
    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

}
