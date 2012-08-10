package org.dynjs.parser.statement;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.BlockManager;
import org.dynjs.runtime.BlockManager.Entry;

public abstract class AbstractCompilingStatement extends AbstractStatement {

    private BlockManager blockManager;

    public AbstractCompilingStatement(Tree tree, BlockManager blockManager) {
        super( tree );
        this.blockManager = blockManager;
    }

    public BlockManager getBlockManager() {
        return this.blockManager;
    }

    public void stash(Statement block) {
        Entry entry = blockManager.retrieve( block.getStatementNumber() );
        if (entry.statement == null) {
            entry.statement = block;
        }
    }

}
