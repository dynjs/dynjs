package org.dynjs.parser.statement;

import static me.qmx.jitescript.util.CodegenUtils.*;
import me.qmx.jitescript.CodeBlock;

import org.antlr.runtime.tree.Tree;
import org.objectweb.asm.tree.LabelNode;

public class AdditiveExpression extends AbstractBinaryExpression {

    public AdditiveExpression(final Tree tree, final Expression lhs, final Expression rhs, final String op) {
        super( tree, lhs, rhs, op );
    }

    @Override
    public CodeBlock getCodeBlock() {
        if (getOp().equals( "+" )) {
            return getCodeBlockForPlus();
        } else {
            return getCodeBlockForMinus();
        }
    }

    public CodeBlock getCodeBlockForPlus() {
        return new CodeBlock() {
            {
                LabelNode stringConcatByLeft = new LabelNode();
                LabelNode stringConcat = new LabelNode();
                LabelNode end = new LabelNode();

                append( getLhs().getCodeBlock() );
                // ref(lhs)
                append( jsGetValue() );
                // val(lhs)
                aconst_null();
                // val(lhs) null
                append( jsToPrimitive() );
                // val(lhs)
                dup();
                // val(lhs) val(lhs)
                instance_of( p( String.class ) );
                // val(lhs) bool
                iftrue( stringConcatByLeft );

                append( getRhs().getCodeBlock() );
                // val(lhs) ref(rhs)
                append( jsGetValue() );
                // val(lhs) val(rhs)
                aconst_null();
                // val(lhs) val(rhs) null
                append( jsToPrimitive() );
                // val(lhs) val(rhs)
                dup();
                // val(lhs) val(rhs) val(rhs)
                instance_of( p( String.class ) );
                // val(lhs) val(rhs) bool
                iftrue( stringConcat );

                // ----------------------------------------
                // Numbers
                // val(lhs) val(rhs)
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
                dadd();
                // num(total)
                invokestatic( p( Double.class ), "valueOf", sig( Double.class, double.class ) );
                // obj(total)
                go_to( end );

                // ----------------------------------------
                // Strings forced by LHS
                label( stringConcatByLeft );
                // val(lhs)
                append( getRhs().getCodeBlock() );
                // val(lhs) ref(rhs)
                append( jsGetValue() );
                // val(lhs) val(rhs)
                aconst_null();
                // val(lhs) val(rhs) null
                append( jsToPrimitive() );
                // val(lhs) val(rhs);

                // ----------------------------------------
                // Strings
                label( stringConcat );
                // val(lhs) val(rhs)
                checkcast( p(String.class) );
                swap();
                // str(rhs) val(lhs)
                checkcast( p(String.class) );
                // str(rhs) str(lhs)
                swap();
                // str(lhs) str(rhs)
                invokevirtual( p( String.class ), "concat", sig( String.class, String.class ) );
                // obj(concat)

                // ----------------------------------------
                label( end );
                nop();
            }
        };

    }

    public CodeBlock getCodeBlockForMinus() {
        return new CodeBlock() {
            {
                append( getLhs().getCodeBlock() );
                // val(lhs)
                append( getRhs().getCodeBlock() );
                // val(rhs)
                
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
                dsub();
                // num(total)
                invokestatic( p( Double.class ), "valueOf", sig( Double.class, double.class ) );
                // obj(total)

            }
        };

    }

}
