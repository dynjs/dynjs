package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;

public class BitwiseExpression extends AbstractBinaryExpression {

    public BitwiseExpression(Tree tree, Expression lhs, Expression rhs, String op) {
        super( tree, lhs, rhs, op );
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                append( getLhs().getCodeBlock() );
                append( jsGetValue() );
                if (getOp().equals( ">>>" )) {
                    append( jsToUint32() );
                } else {
                    append( jsToInt32() );
                }
                invokevirtual( p( Number.class ), "intValue", sig( int.class ) );

                append( getRhs().getCodeBlock() );
                append( jsGetValue() );
                append( jsToUint32() );
                invokevirtual( p( Number.class ), "intValue", sig( int.class ) );
                // int int

                if (getOp().equals( "<<" )) {
                    ldc( 0x1F );
                    iand();
                    ishl();
                } else if (getOp().equals( ">>" )) {
                    ldc( 0x1F );
                    iand();
                    ishr();
                } else if (getOp().equals( ">>>" )) {
                    ldc( 0x1F );
                    iand();
                    iushr();
                } else if (getOp().equals( "&" )) {
                    iand();
                } else if (getOp().equals( "|" )) {
                    ior();
                } else if (getOp().equals( "^" )) {
                    ixor();
                }
                // int

                append( convertTopToInteger() );
                // Integer
            }
        };
    }

}
