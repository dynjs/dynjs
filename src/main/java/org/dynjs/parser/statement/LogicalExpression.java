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
                LabelNode returnTrue = new LabelNode();
                LabelNode returnFalse = new LabelNode();
                LabelNode end = new LabelNode();

                append( getLhs().getCodeBlock() );
                append( jsGetValue() );
                append( jsToBoolean() );
                invokevirtual( p( Boolean.class ), "booleanValue", sig( boolean.class ) );
                // bool(lhs)
                dup();
                // bool(lhs) bool(lhs)

                if (getOp().equals( "&&" )) {
                    iffalse( returnFalse );
                } else if (getOp().equals( "||" )) {
                    iftrue( returnTrue );
                }

                append( getRhs().getCodeBlock() );
                append( jsGetValue() );
                append( jsToBoolean() );
                invokevirtual( p( Boolean.class ), "booleanValue", sig( boolean.class ) );
                // bool(lhs) bool(rhs)
                swap();
                // bool(rhs) bool(lhs)
                pop();
                // bool(rhs)
                dup();
                // bool(rhs) bool(rhs)
                iftrue( returnTrue );
                go_to( returnFalse );
                
                // ----------------------------------------
                // TRUE
                // bool
                label( returnTrue );
                pop();
                getstatic( p(Boolean.class), "TRUE", ci(Boolean.class) ); 
                go_to(end);
                
                // ----------------------------------------
                // FALSE
                label( returnFalse );
                pop();
                getstatic( p(Boolean.class), "FALSE", ci(Boolean.class) ); 

                // ----------------------------------------
                label( end );
                // bool
                
                nop();
            }
        };
    }

}
