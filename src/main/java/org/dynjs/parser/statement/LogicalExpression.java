package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.objectweb.asm.tree.LabelNode;

public class LogicalExpression extends AbstractBinaryExpression {

    public LogicalExpression(Tree tree, Expression lhs, Expression rhs, String op) {
        super( tree, lhs, rhs, op );
    }

    @Override
    public CodeBlock getCodeBlock() {
        return new CodeBlock() {
            {
                LabelNode end = new LabelNode();

                append( getLhs().getCodeBlock() );
                append( jsGetValue() );
                append( jsToBoolean() );
                invokevirtual( p( Boolean.class ), "boolValue", sig( boolean.class ) );
                // bool(lhs)
                dup();
                // bool(lhs) bool(lhs)

                if (getOp().equals( "&&" )) {
                    iffalse( end );
                } else if (getOp().equals( "||" )) {
                    iftrue( end );
                }

                append( getRhs().getCodeBlock() );
                append( jsGetValue() );
                append( jsToBoolean() );
                invokevirtual( p( Boolean.class ), "boolValue", sig( boolean.class ) );
                // bool(lhs) bool(rhs)
                swap();
                // bool(rhs) bool(lhs)
                pop();
                // bool(rhs)

                // ----------------------------------------
                label( end );
                nop();
            }
        };
    }

}
