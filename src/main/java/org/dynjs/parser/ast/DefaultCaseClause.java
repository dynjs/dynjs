package org.dynjs.parser.ast;

import org.dynjs.parser.Statement;

public class DefaultCaseClause extends CaseClause{

    public DefaultCaseClause(Statement block) {
        super( null, block );
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();
        buf.append(indent).append("default: \n");
        if (getBlock() != null) {
            buf.append(getBlock().toIndentedString("  " + indent));
        }
        return buf.toString();
    }

}
