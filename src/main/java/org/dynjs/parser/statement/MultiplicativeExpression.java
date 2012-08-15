package org.dynjs.parser.statement;

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
                append( jsGetValue() );
                append( jsToNumber() );
                append( getRhs().getCodeBlock() );
                append( jsGetValue() );
                append( jsToNumber() );
                // lhs rhs

                if (getOp().equals( "*" )) {
                    dmul();
                } else if (getOp().equals( "/" )) {
                    ddiv();
                } else if (getOp().equals( "%" )) {
                    drem();
                }
            }
        };
    }

}
