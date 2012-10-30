package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.parser.Statement;
import org.dynjs.runtime.ExecutionContext;

public class BreakStatement extends AbstractStatement implements Statement {

    private String target;

    public BreakStatement(Tree tree, String target) {
        super(tree);
        this.target = target;
    }
    
    public String getTarget() {
        return this.target;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                append(breakCompletion(target));
            }
        };
    }

    public String toIndentedString(String indent) {
        return indent + "break" + (target == null ? ";" : target + ";");
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
