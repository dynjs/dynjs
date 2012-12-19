package org.dynjs.parser.ast;

import java.util.Collections;
import java.util.List;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;
import org.objectweb.asm.tree.LabelNode;

public class CaseClause {

    private Position position;
    private Expression expr;
    private Statement block;
    private LabelNode entranceLabel;
    private LabelNode fallThroughLabel;

    public CaseClause(Position position, Expression expr, Statement block) {
        this.position = position;
        this.expr = expr;
        this.block = block;
        this.entranceLabel = new LabelNode();
        this.fallThroughLabel = new LabelNode();
    }

    public Position getPosition() {
        return this.position;
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
    
    public int getSizeMetric() {
        return this.expr.getSizeMetric() + 3;
    }
    
    public List<VariableDeclaration> getVariableDeclarations() {
        if ( this.block == null ) {
            return Collections.emptyList();
        }
        
        return this.block.getVariableDeclarations();
    }
    
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
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
