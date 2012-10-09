package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.objectweb.asm.tree.LabelNode;

public class LogicalExpression extends AbstractBinaryExpression {

    public LogicalExpression(Tree tree, Expression lhs, Expression rhs, String op) {
        super(tree, lhs, rhs, op);
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode end = new LabelNode();

                append(getLhs().getCodeBlock());
                append(jsGetValue());
                dup();
                // val(lhs) val(lhs)
                append(jsToBoolean());
                // val(lhs) bool(lhs)
                invokevirtual(p(Boolean.class), "booleanValue", sig(boolean.class));
                // val(lhs) bool(lhs)

                if (getOp().equals("&&")) {
                    iffalse(end);
                } else if (getOp().equals("||")) {
                    iftrue(end);
                }
                pop();
                
                // <empty>

                append(getRhs().getCodeBlock());
                // val(rhs)
                append(jsGetValue());
                // val(rhs)
                go_to(end);

                // ----------------------------------------
                label(end);
                // val

                nop();
            }
        };
    }

}
