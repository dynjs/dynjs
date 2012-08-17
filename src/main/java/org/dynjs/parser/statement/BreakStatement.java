package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;

public class BreakStatement extends AbstractStatement implements Statement {

    private String target;

    public BreakStatement(Tree tree, String target) {
        super(tree);
        this.target = target;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                append(breakCompletion(target));
            }
        };
    }
}
