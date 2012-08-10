package org.dynjs.parser.statement;

import org.antlr.runtime.tree.Tree;

import me.qmx.jitescript.CodeBlock;

public class RelationalExpression extends AbstractBinaryExpression {

    public RelationalExpression(Tree tree, Expression lhs, Expression rhs, String op) {
        super( tree, lhs, rhs, op );
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {{
            append( getLhs().getCodeBlock() );
            append( jsGetValue() );
            append( getRhs().getCodeBlock() );
            append( jsGetValue() );
            // lhs rhs
            
            if ( getOp().equals( "<") || getOp().equals( "<=" )  ) {
                compare( true );
                // result
            } else {
                compare( false );
                // result
            }
        }};
    }
    
    public CodeBlock compare(boolean leftFirst) {
        return null;
    }

}
