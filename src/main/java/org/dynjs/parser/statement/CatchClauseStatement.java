package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;

public class CatchClauseStatement extends BaseCompilableBlockStatement implements Statement {
    private final String id;

    public CatchClauseStatement(Tree tree, DynThreadContext context, String id, Statement block) {
        super(tree, context, block);
        this.id = id;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return compileBasicBlockIfNecessary( "Catch", id );
    }
}
