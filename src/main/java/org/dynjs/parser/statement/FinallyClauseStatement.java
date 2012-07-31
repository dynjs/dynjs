package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;

public class FinallyClauseStatement extends BaseCompilableBlockStatement implements Statement {

    public FinallyClauseStatement(Tree tree, DynThreadContext context, Statement block) {
        super(tree, context, block);
    }

    @Override
    public CodeBlock getCodeBlock() {
        return compileBasicBlockIfNecessary( "Finally" );
    }


}
