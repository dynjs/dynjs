package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;

public class MultiplicativeExpression extends AbstractBinaryExpression {

    public MultiplicativeExpression(Tree tree, Expression lhs, Expression rhs, String op) {
        super( tree, lhs, rhs, op );
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                append( getLhs().getCodeBlock() );
                // val(lhs)
                
                append( jsGetValue() );
                append( getRhs().getCodeBlock() );
                // val(rhs)
                append( jsGetValue() );
                
                checkcast( p(Number.class) );
                swap();
                checkcast( p(Number.class) );
                // num(rhs) num(lhs);
                invokevirtual( p( Number.class ), "doubleValue", sig( double.class ) );
                // val(rhs) num(lhs)
                dup2_x1();
                pop2();
                // num(lhs) val(rhs)
                invokevirtual( p( Number.class ), "doubleValue", sig( double.class ) );
                // num(lhs) num(rhs)

                if (getOp().equals( "*" )) {
                    dmul();
                } else if (getOp().equals( "/" )) {
                    ddiv();
                } else if (getOp().equals( "%" )) {
                    drem();
                }
                invokestatic( p( Double.class ), "valueOf", sig( Double.class, double.class ) );
                // obj(total)
            }
        };
    }

}
