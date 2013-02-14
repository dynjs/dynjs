package org.dynjs.parser.ast;

import java.util.Collection;

import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.parser.js.Position;
import org.dynjs.runtime.ExecutionContext;

public class CatchClause {
    private final Position position;
    private final String identifier;
    private final Statement block;

    public CatchClause(Position position, String identifier, Statement block) {
        this.position = position;
        this.identifier = identifier;
        this.block = block;
    }
    
    public Position getPosition() {
        return this.position;
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
    
    public String dump(String indent) {
        return this.block.dump( indent + "  " );
    }

    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }

    public Collection<? extends VariableDeclaration> getVariableDeclarations() {
        return this.block.getVariableDeclarations();
    }

}
