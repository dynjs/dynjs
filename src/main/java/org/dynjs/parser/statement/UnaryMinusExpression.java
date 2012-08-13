package org.dynjs.parser.statement;

import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;

public class UnaryMinusExpression extends AbstractUnaryOperatorExpression {

    public UnaryMinusExpression(Tree tree, Expression expr) {
        super( tree, expr, "-" );
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                append( getExpr().getCodeBlock() );
                // val
                append( jsToNumber() );
                // num
                iconst_m1();
                // num -1
                imul();
                // -num
            }
        };
    }
}
