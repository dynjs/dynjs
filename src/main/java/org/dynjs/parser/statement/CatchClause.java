package org.dynjs.parser.statement;

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

}
