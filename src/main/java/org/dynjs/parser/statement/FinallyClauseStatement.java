package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.DynThreadContext;

public class FinallyClauseStatement extends BaseStatement implements Statement {

    private Statement block;

    public FinallyClauseStatement(Tree tree, Statement block) {
        super(tree);
        this.block = block;
    }

    @Override
    public CodeBlock getCodeBlock() {
        //return compileBasicBlockIfNecessary( "Finally" );
        return null;
    }


}
