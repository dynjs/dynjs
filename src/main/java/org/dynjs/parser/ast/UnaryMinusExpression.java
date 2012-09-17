package org.dynjs.parser.ast;

import me.qmx.jitescript.CodeBlock;
import static me.qmx.jitescript.util.CodegenUtils.*;

import org.antlr.runtime.tree.Tree;
import org.objectweb.asm.tree.LabelNode;

public class UnaryMinusExpression extends AbstractUnaryOperatorExpression {

    public UnaryMinusExpression(Tree tree, Expression expr) {
        super(tree, expr, "-");
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode doubleNum = new LabelNode();
                LabelNode zero = new LabelNode();
                LabelNode end = new LabelNode();

                append(getExpr().getCodeBlock());
                // val
                append(jsGetValue());
                // val
                append(jsToNumber());
                // num
                dup();
                // num num
                instance_of(p(Double.class));
                // num bool
                iftrue(doubleNum);
                
                // num
                dup();
                // num num
                invokevirtual(p(Number.class), "longValue", sig(long.class));
                // num long
                ldc( 0L );
                // num long 0L
                lcmp();
                // num bool
                iffalse(zero);
                
                

                // ------------------------------------
                // Integral

                // num(Long)
                invokevirtual(p(Number.class), "longValue", sig(long.class));
                // num(long)

                ldc( -1L );
                // num -1L
                lmul();
                // -num
                invokestatic(p(Long.class), "valueOf", sig(Long.class, long.class));
                // -num(Long)

                go_to(end);

                // ------------------------------------
                // Double

                label(doubleNum);
                // num(Doube)
                invokevirtual(p(Number.class), "doubleValue", sig(double.class));
                // num(double)
                iconst_m1();
                // num -1
                i2d();
                // num -1.0
                dmul();
                // -num
                invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));
                // -num(Double)
                
                go_to(end);

                // ------------------------------------
                
                label( zero );
                // num
                pop();
                ldc( -0.0 );
                invokestatic(p(Double.class), "valueOf", sig(Double.class, double.class));
                

                label(end);
                nop();
            }
        };
    }

    public String toString() {
        return "-" + getExpr();
    }
}
