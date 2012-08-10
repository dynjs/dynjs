package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;

public class CatchClause extends AbstractStatement {
    private final String identifier;
    private final BlockStatement block;

    public CatchClause(Tree tree, String identifier, BlockStatement block) {
        super( tree );
        this.identifier = identifier;
        this.block = block;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return this.block.getCodeBlock();
    }
}
