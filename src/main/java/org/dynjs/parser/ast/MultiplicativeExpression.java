package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.objectweb.asm.tree.LabelNode;

public class MultiplicativeExpression extends AbstractBinaryExpression {

    public MultiplicativeExpression(Tree tree, Expression lhs, Expression rhs, String op) {
        super(tree, lhs, rhs, op);
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode doubleNums = new LabelNode();
                LabelNode end = new LabelNode();

                append(getLhs().getCodeBlock());
                // val(lhs)
                append(jsGetValue());
                append(jsToNumber());
                append(getRhs().getCodeBlock());
                // val(rhs)
                append(jsGetValue());
                append(jsToNumber());

                if (!getOp().equals("/")) {
                    append(ifEitherIsDouble(doubleNums));

                    append(convertTopTwoToPrimitiveLongs());
                    if (getOp().equals("*")) {
                        lmul();
                    } else if (getOp().equals("/")) {
                        ldiv();
                    } else if (getOp().equals("%")) {
                        lrem();
                    }
                    append(convertTopToLong());
                    go_to(end);

                    label(doubleNums);
                }
                append(convertTopTwoToPrimitiveDoubles());

                if (getOp().equals("*")) {
                    dmul();
                } else if (getOp().equals("/")) {
                    ddiv();
                } else if (getOp().equals("%")) {
                    drem();
                }
                append(convertTopToDouble());

                label(end);
                nop();
            }
        };
    }
}
