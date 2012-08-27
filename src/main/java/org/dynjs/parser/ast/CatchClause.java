package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;

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

    @Override
    public CodeBlock getCodeBlock() {
        return this.block.getCodeBlock();
    }
    
    public String toIndentedString(String indent) {
        StringBuffer buf = new StringBuffer();
        buf.append( indent ).append( "catch(" ).append( this.identifier ).append( "){\n" );
        buf.append( block.toIndentedString(indent + "" ));
        buf.append( indent ).append( "}" );
        return buf.toString();
    }

}
