package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.Statement;
import org.objectweb.asm.tree.LabelNode;

import java.util.Stack;

public class ContinueStatement extends BaseStatement implements Statement {
    private final Stack<LabelNode> labelStack;

    public ContinueStatement(Stack<LabelNode> labelStack, Tree tree, String id) {
        super(tree);
        this.labelStack = labelStack;
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            go_to(labelStack.peek());
        }};
    }
}
