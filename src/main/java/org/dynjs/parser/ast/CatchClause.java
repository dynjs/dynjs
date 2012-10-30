package org.dynjs.parser.ast;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.ExecutionContext;

public class CatchClause extends AbstractStatement {
    private final String identifier;
    private final Statement block;

    public CatchClause(Tree tree, String identifier, Statement block) {
        super(tree);
        this.identifier = identifier;
        this.block = block;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Statement getBlock() {
        return this.block;
    }

    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();
        buf.append(indent).append("catch(").append(this.identifier).append("){\n");
        buf.append(block.toIndentedString(indent + ""));
        buf.append(indent).append("}");
        return buf.toString();
    }
    
    

    @Override
    public String dump(String indent) {
        return super.dump(indent) + this.block.dump( indent + "  " );
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

}
