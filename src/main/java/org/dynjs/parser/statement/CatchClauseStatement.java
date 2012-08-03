package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;

public class CatchClauseStatement extends BaseStatement implements Statement {
    private final String id;
    private DynThreadContext context;
    private Statement block;

    public CatchClauseStatement(Tree tree, DynThreadContext context, String id, Statement block) {
        super(tree);
        this.context = context;
        this.block = block;
        this.id = id;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlockUtils.compileBasicBlockWithArg( this.context, "Catch", this.block, this.id );
    }
}
