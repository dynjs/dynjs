package org.dynjs.parser.ast;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.dynjs.parser.CodeVisitor;
import org.dynjs.runtime.ExecutionContext;
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
                LabelNode returnNaN = new LabelNode();
                LabelNode end = new LabelNode();

                append(getLhs().getCodeBlock());
                // val(lhs)
                append(jsGetValue());
                append(jsToNumber());
                append(getRhs().getCodeBlock());
                // val(rhs)
                append(jsGetValue());
                append(jsToNumber());
                // val(lhs) val(rhs)
                
                append( ifEitherIsNaN(returnNaN) );
                
                if ( getOp().equals( "%" ) ) {
                    append( ifTopIsZero(returnNaN) );
                }

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
                
                go_to(end);
                
                label( returnNaN );
                pop();
                pop();
                getstatic(p(Double.class), "NaN", ci(double.class));
                invokestatic( p(Double.class), "valueOf", sig(Double.class, double.class));

                label(end);
                nop();
            }
        };
    }

    @Override
    public void accept(ExecutionContext context, CodeVisitor visitor, boolean strict) {
        visitor.visit( context, this, strict );
    }
}
