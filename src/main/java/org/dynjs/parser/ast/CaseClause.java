package org.dynjs.parser.ast;

import org.dynjs.parser.Statement;
import org.objectweb.asm.tree.LabelNode;

public class CaseClause {

    private Expression expr;
    private Statement block;
    private LabelNode entranceLabel;
    private LabelNode fallThroughLabel;

    public CaseClause(Expression expr, Statement block) {
        this.expr = expr;
        this.block = block;
        this.entranceLabel = new LabelNode();
        this.fallThroughLabel = new LabelNode();
    }

    public Expression getExpression() {
        return this.expr;
    }

    public Statement getBlock() {
        return this.block;
    }
    
    public LabelNode getEntranceLabel() {
        return this.entranceLabel;
    }
    
    public LabelNode getFallThroughLabel() {
        return this.fallThroughLabel;
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();
        buf.append(indent).append("case ").append(expr.toString()).append(":\n");
        if (this.block != null) {
            buf.append(this.block.toIndentedString("  " + indent));
        }
        return buf.toString();
    }

}
