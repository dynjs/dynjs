package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.compiler.JSCompiler;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.objectweb.asm.tree.LabelNode;

public class RelationalExpression extends AbstractBinaryExpression {

    public RelationalExpression(Tree tree, Expression lhs, Expression rhs, String op) {
        super(tree, lhs, rhs, op);
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode returnFalse = new LabelNode();
                LabelNode end = new LabelNode();

                aload(JSCompiler.Arities.EXECUTION_CONTEXT);
                append(getLhs().getCodeBlock());
                append(jsGetValue());
                append(getRhs().getCodeBlock());
                append(jsGetValue());
                // context lhs rhs

                if (getOp().equals(">") || getOp().equals("<=")) {
                    swap();
                    iconst_0();
                    i2b();
                    // y x false
                } else {
                    iconst_1();
                    i2b();
                }

                invokestatic(p(Types.class), "compareRelational", sig(Object.class, ExecutionContext.class, Object.class, Object.class, boolean.class));

                // result
                dup();
                // result result

                if (getOp().equals("<") || getOp().equals(">")) {
                    // result result
                    append(jsPushUndefined());
                    // result result UNDEF
                    if_acmpeq(returnFalse);
                    // result
                    go_to(end);
                } else if (getOp().equals("<=") || getOp().equals(">=")) {
                    // result result
                    append(jsPushUndefined());
                    // result result UNDEF
                    if_acmpeq(returnFalse);
                    // result
                    dup();
                    // result result
                    getstatic(p(Boolean.class), "TRUE", ci(Boolean.class));
                    // result result TRUE
                    if_acmpeq(returnFalse);
                    // result(FALSE)
                    pop();
                    // <empty>
                    getstatic(p(Boolean.class), "TRUE", ci(Boolean.class));
                    // TRUE
                    go_to(end);
                }

                // ----------------------------------------
                // FALSE

                label(returnFalse);
                // result
                pop();
                getstatic(p(Boolean.class), "FALSE", ci(Boolean.class));
                go_to(end);

                // ----------------------------------------
                label(end);
                nop();
            }
        };
    }
}
