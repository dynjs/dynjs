package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;

public class CatchClauseStatement extends BaseStatement implements Statement {
    private final String id;
    private Statement block;

    public CatchClauseStatement(Tree tree, String id, Statement block) {
        super(tree);
        this.id = id;
        this.block = block;
    }

    @Override
    public CodeBlock getCodeBlock() {
        //return compileBasicBlockIfNecessary( "Catch", id );
        return null;
    }
}
