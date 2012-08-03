package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.CodeBlockUtils;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;

public class FinallyClauseStatement extends BaseStatement implements Statement {

    private DynThreadContext context;
    private Statement block;

    public FinallyClauseStatement(Tree tree, DynThreadContext context, Statement block) {
        super(tree );
        this.context = context;
        this.block = block;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return CodeBlockUtils.compileBasicBlock( this.context, "Finally", this.block );
    }


}
